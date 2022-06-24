package com.moor.imkf.demo.multichat;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.activity.MoorVideoActivity;
import com.moor.imkf.demo.bean.MoorSendMsgEvent;
import com.moor.imkf.demo.bean.MoorTransferAgentEvent;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.emotion.MoorEmojiSpanBuilder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorEmojiBitmapUtil;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.utils.MoorRegexUtils;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.MoorCommonBottomDialog;
import com.moor.imkf.demo.view.imageviewer.MoorImagePreview;
import com.moor.imkf.lib.jsoup.Jsoup;
import com.moor.imkf.lib.jsoup.nodes.Document;
import com.moor.imkf.lib.jsoup.nodes.Element;
import com.moor.imkf.lib.jsoup.select.Elements;
import com.moor.imkf.lib.utils.MoorLogUtils;
import com.moor.imkf.moorsdk.bean.MoorImageInfoBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.db.MoorOptionsDao;
import com.moor.imkf.moorsdk.manager.MoorActivityHolder;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorEventBusUtil;
import com.moor.imkf.moorsdk.utils.MoorUtils;
import com.moor.imkf.moorsdk.utils.toast.MoorToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/06/08
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorTextParseUtil {

    private Spannable spannable;
    private int moorDefaultThemeColor;

    private MoorTextParseUtil() {
        MoorOptions options = MoorManager.getInstance().getOptions();
        if (options != null) {
            moorDefaultThemeColor = MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor());
        }
        MoorEventBusUtil.registerEventBus(this);
    }

    @Subscribe
    public void handleTransferAgent(MoorTransferAgentEvent agentEvent) {

    }

    public static MoorTextParseUtil getInstance() {
        return MoorTextParseUtil.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MoorTextParseUtil sInstance = new MoorTextParseUtil();
    }

    public void clear() {
        MoorEventBusUtil.unregisterEventBus(this);
    }

    /**
     * 解析 isShowHtml 类型
     *
     * @param msgBean
     * @return
     */
    public List<View> parseHtmlText(MoorMsgBean msgBean) {
        List<View> allView = new ArrayList<>();
        String content = msgBean.getContent();

        final List<String> data = getImgStr(content);
        String str = content.replaceAll("<(img|IMG|video|VIDEO)(.*?)(/>|></img>|></video>|>)", "---");
        String[] strings = str.split("---");
        if (strings.length == 0 && data.size() > 0) {
            View view = showImageOrVideo(data.get(0));
            allView.add(view);
        } else {
            List<View> views = initImgandText(msgBean, strings, data);

            if (views.size() > 0) {
                allView.addAll(views);
            }
        }

        return allView;
    }

    /**
     * 解析文本
     *
     * @param msgBean
     */
    public SpannableStringBuilder parseText(MoorMsgBean msgBean) {
        String content = msgBean.getContent();
        content = content.replaceAll("<7moorbr/>", "\n");
        String actionMsg = setA_String(content);

        if (MoorEmojiBitmapUtil.getMoorEmotions().size() > 0) {
            spannable = MoorEmojiSpanBuilder.buildEmotionSpannable(actionMsg);
        }

        if (spannable == null) {
            spannable = new SpannableString(actionMsg);
        }

        SpannableStringBuilder contentSpannable = new SpannableStringBuilder(spannable);

        dealATag(contentSpannable);

        dealHttpUrl(contentSpannable);

        dealPhoneNum(contentSpannable);
        return contentSpannable;
    }

    /**
     * @param message
     * @return
     */
    public List<View> parseFlow(final MoorMsgBean message) {
        String flowType = message.getFlowType();
        String flowStyle = message.getFlowStyle();

        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(message.getAnswer())) {
            builder.append(message.getAnswer());
        }
        //文本列表形式，不解析flowTips，在viewbinder里自己解析
        if ("button".equals(flowType) && !"2".equals(flowStyle)) {
            if (!TextUtils.isEmpty(message.getFlowTips())) {
                builder.append(message.getFlowTips());
            }
        }

        if (TextUtils.isEmpty(message.getAnswer()) && TextUtils.isEmpty(message.getFlowTips()) && !TextUtils.isEmpty(message.getContent())) {
            builder.append(message.getContent());
        }
        if (TextUtils.isEmpty(builder)) {
            return new ArrayList<>();
        }

        List<View> views = parseFlowText(builder);
        return views;
    }

    /**
     * 解析flowList类型文本
     *
     * @param builder
     * @return
     */
    public List<View> parseFlowText(StringBuilder builder) {
        String text = builder.toString();
        List<String> data = getImgStr(text);
        String content = text.replaceAll("<(img|IMG|video|VIDEO)(.*?)(/>|></img>|></video>|>)", "---");
        String[] strings = content.split("---");
        List<View> views = new ArrayList<>();
        if (strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                TextView tv1 = new TextView(MoorActivityHolder.getCurrentActivity());
                String leftMsgTextColor = MoorOptionsDao.getInstance().queryOptions().getLeftMsgTextColor();
                if (TextUtils.isEmpty(leftMsgTextColor)) {
                    tv1.setTextColor(MoorUtils.getApp().getResources().getColor(R.color.moor_color_151515));
                } else {
                    tv1.setTextColor(MoorColorUtils.getColorWithAlpha(1f, leftMsgTextColor));
                }

                tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv1.setMinHeight(MoorPixelUtil.dp2px(45f));
                tv1.setLineSpacing(0, 1.1f);
                tv1.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.65));
//            tv1.setMinWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.5));
                tv1.setGravity(Gravity.CENTER_VERTICAL);
                tv1.setPadding(MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f));
//            tv1.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
//            tv1.getPaint().setStrokeWidth(0.7f);

                if (text.contains("</a>") && (!text.contains("1："))) {

                    String str = setA_String(strings[i]);
                    Spanned string;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        string = Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT);
                    } else {
                        string = Html.fromHtml(str);
                    }

                    Spanned string_o = new SpannableStringBuilder(trimTrailingWhitespace(string));
                    SpannableStringBuilder spannableString = new SpannableStringBuilder(string_o);
                    spannableString = getClickableHtml(spannableString);

                    //匹配a标签 自定义 点击️
                    dealATag(spannableString);

                    //手机号
                    dealPhoneNum(spannableString);

                    tv1.setAutoLinkMask(Linkify.ALL);
                    tv1.setText(spannableString);
                    tv1.setLinkTextColor(moorDefaultThemeColor);
                    tv1.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    List<AString> list = setAString(strings[i]);
                    setNoImgView(tv1, strings[i], list);
                }

                if (tv1.getText().length() > 0) {
                    views.add(tv1);
                }

                if (data.size() > i) {
                    View view = showImageOrVideo(data.get(i));
                    if (view != null) {
                        views.add(view);
                    }
                }


            }
        }

        return views;
    }

    /**
     * 校验电话号码
     *
     * @param spannableString
     */
    private void dealPhoneNum(SpannableStringBuilder spannableString) {
        String regex = MoorRegexUtils.isPhoneRegexp();
        Pattern patten3 = Pattern.
                compile(regex,
                        Pattern.CASE_INSENSITIVE);
        Matcher matcher3 = patten3.matcher(spannableString);
        while (matcher3.find()) {
            String number = matcher3.group();
            int end = matcher3.start() + number.length();
            NumClickSpan clickSpan = new NumClickSpan(number);
            spannableString.setSpan(clickSpan, matcher3.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(moorDefaultThemeColor);
            spannableString.setSpan(colorSpan, matcher3.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    private void dealHttpUrl(SpannableStringBuilder contentSpannable) {
        Pattern patten2 = Pattern.
                compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(((http[s]{0,1}|ftp)://|)((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)", Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = patten2.matcher(contentSpannable);
        while (matcher2.find()) {

            String number = matcher2.group();
            int end = matcher2.start() + number.length();
            HttpClickSpan clickSpan = new HttpClickSpan(number);
            contentSpannable.setSpan(clickSpan, matcher2.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(moorDefaultThemeColor);
            contentSpannable.setSpan(colorSpan, matcher2.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 匹配a标签 自定义 点击️
     *
     * @param contentSpannable
     */
    private void dealATag(SpannableStringBuilder contentSpannable) {
        Pattern patten1 = Pattern.compile("\\[([^\\]]*)\\]\\(([^\\)]*)\\)", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = patten1.matcher(contentSpannable);
        while (matcher1.find()) {
            String m7Data = matcher1.group(1);
            String m7Name = matcher1.group(2);
            if (checkURL(m7Name)) {
                contentSpannable.replace(matcher1.start(), matcher1.end(), m7Data);
                AClickApan clickSpan = new AClickApan(m7Name);
                contentSpannable.setSpan(clickSpan, matcher1.start(), matcher1.start() + m7Data.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(moorDefaultThemeColor);
                contentSpannable.setSpan(colorSpan, matcher1.start(), matcher1.start() + m7Data.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                matcher1 = patten1.matcher(contentSpannable);
            }
        }
    }


    private void setNoImgView(TextView tv, String message, List<AString> list) {

        String msg = message.replaceAll("\\n", "\n");

        for (int i = 0; i < list.size(); i++) {
            AString aString = list.get(i);

            Document doc = Jsoup.parse(aString.a);
            Elements chatElements = doc.getElementsByTag("a");
            if (chatElements != null) {
                if (chatElements.size() > 0) {
                    for (int p = 0; p < chatElements.size(); p++) {
                        if (chatElements.get(p) != null) {
                            Element chatElement = chatElements.get(p);
                            //m7_action为 transferAgent 响应转人工
                            aString.action = chatElement.attr("m7_action");
                            if (!TextUtils.isEmpty(aString.action)) {
                                if ("robotTransferAgent".equals(aString.action) | "transferAgent".equals(aString.action)) {
                                    aString.peerid = chatElement.attr("m7_data");//m7_action为 transferAgent 响应转人工，m7_data 取出技能组id
                                    msg = msg.replace(aString.a,
                                            "[" + aString.content + "](moor_moor_m7_actionrobotTransferAgent.m7_data:" + aString.peerid + ".com)");
                                    aString.content = "------------___---------------";
                                } else if ("xbot-quick-question".equals(aString.action)) {
                                    //m7_action为 xbot-quick-question 是xbot提问内容，m7_data中是要发送的文案
                                    msg = msg.replace(aString.a,
                                            "[" + aString.content + "](moor_moor_m7_actionXbotQuickQuestionData.m7_data:" + aString.peerid + ".com)");
                                    aString.content = "------------___---------------";
                                }
                            }

                            //data-phone href 并且值为tel: 开头 有值为电话号
                            if (TextUtils.isEmpty(aString.action)) {
                                aString.action = chatElement.attr("href");
                                if (!TextUtils.isEmpty(aString.action)) {
                                    if (aString.action.startsWith("tel:")) {
                                        msg = msg.replace(aString.a,
                                                "[" + aString.content + "](moor_moor_m7_actiondata-phone-href.m7-data-tel:" + aString.content + ".com)");
                                        aString.content = "------------___---------------";
                                    } else {
                                        msg = msg.replaceAll(aString.a, aString.content);
                                    }
                                } else {
                                    msg = msg.replaceAll(aString.a, aString.content);
                                }
                            }
                        } else {

                        }
                    }
                }
            }
        }

        msg = msg.replaceAll("<p>", "");

        msg = msg.replaceAll("</p>", "\n");

        msg = msg.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        msg = msg.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        msg = msg.replaceAll("\\<.*?>", "");


        if (!message.endsWith("\n")) {
            msg = trimTrailingWhitespace(msg).toString();
        }

//        Spanned spanned;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            spanned = Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT);
//        } else {
//            spanned = Html.fromHtml(msg);
//        }
//        CharSequence charSequence = trimTrailingWhitespace(spanned);

        SpannableStringBuilder spannableString = new SpannableStringBuilder(msg);
        Pattern patten = Pattern.compile("\\d+[：].*+\\n", Pattern.CASE_INSENSITIVE);
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String number = matcher.group();
            int end = matcher.start() + number.length();
            RobotClickSpan clickSpan = new RobotClickSpan(number);
            spannableString.setSpan(clickSpan, matcher.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(moorDefaultThemeColor);
            spannableString.setSpan(colorSpan, matcher.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }


        dealHttpUrl(spannableString);


        for (int i = 0; i < list.size(); i++) {
            AString aString = list.get(i);
            Pattern patten3 = Pattern.compile(aString.content, Pattern.CASE_INSENSITIVE);
            Matcher matcher3 = patten3.matcher(spannableString);
            while (matcher3.find()) {
                String number = matcher3.group();
                int end = matcher3.start() + number.length();
                HttpClickSpan clickSpan = new HttpClickSpan(aString.url);
                spannableString.setSpan(clickSpan, matcher3.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(moorDefaultThemeColor);
                spannableString.setSpan(colorSpan, matcher3.start(), end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }


        dealPhoneNum(spannableString);


        dealATag(spannableString);
        tv.setText(spannableString);
        tv.setLinkTextColor(moorDefaultThemeColor);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static CharSequence trimTrailingWhitespace(CharSequence source) {
        if (source == null) {
            return "";
        }

        int i = source.length();

        // loop back to the first non-whitespace character
        while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i + 1);
    }

    public static class RobotClickSpan extends ClickableSpan {
        String msg;

        public RobotClickSpan(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View view) {
            String msgStr = "";
            try {
                msgStr = msg.split("：", 2)[1].trim();

            } catch (Exception e) {
            }

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(true);
        }
    }

    public static class NumClickSpan extends ClickableSpan {
        String msg;

        public NumClickSpan(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View widget) {
            openPhoneDialog(msg);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setUnderlineText(true);
        }
    }

    /**
     * 打电话弹框
     *
     * @param msg
     */
    public static void openPhoneDialog(String msg) {
        final String num = MoorRegexUtils.regexNumber(msg);
        final MoorCommonBottomDialog dialog = MoorCommonBottomDialog.init(num + " " + "可能是一个电话号码,你可以"
                , "呼叫", "复制");
        dialog.setListener(new MoorCommonBottomDialog.OnClickListener() {
            @Override
            public void onClickPositive() {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MoorActivityHolder.getCurrentActivity().startActivity(intent);
                dialog.close();
            }

            @Override
            public void onClickNegative() {
                ClipboardManager cm = (ClipboardManager) MoorActivityHolder.getCurrentActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(num);
                MoorToastUtils.showShort("已复制");
                dialog.close();
            }
        });
        dialog.show(((FragmentActivity) MoorActivityHolder.getCurrentActivity()).getSupportFragmentManager(), "");
    }

    public static class AClickApan extends ClickableSpan {
        String msg;

        public AClickApan(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View widget) {
            String action_str = msg + "";
            if (!TextUtils.isEmpty(action_str)) {
                if (action_str.startsWith("moor_moor_m7_actionrobotTransferAgent.m7_data:")) {
                    //第一种情况 转人工
                    //moor_moor_m7_actionrobotTransferAgent.m7_data:"+技能组id+".com
                    //以 moor_moor_m7_actionrobotTransferAgent.m7_data: 为开头 取技能组id 转人工。
                    action_str = action_str.replace("moor_moor_m7_actionrobotTransferAgent.m7_data:", "");
                    action_str = action_str.replace(".com", "");
//                    TransferAgent transferAgent = new TransferAgent();
//                    transferAgent.peerid = action_str;
//                    EventBus.getDefault().post(transferAgent);
                    MoorLogUtils.i(action_str);
                    MoorEventBusUtil.post(new MoorTransferAgentEvent(action_str));
                }
                if (action_str.startsWith("moor_moor_m7_actiondata-phone-href.m7-data-tel:")) {
                    //第一种情况 电话号
                    //moor_moor_m7_actiondata-phone-href.m7-data-tel:电话号.com
                    //以 moor_moor_m7_actiondata-phone-href.m7-data-tel: 为开头 电话号 弹窗。
                    action_str = action_str.replace("moor_moor_m7_actiondata-phone-href.m7-data-tel:", "");
                    action_str = action_str.replace(".com", "");
//                    NumClickBottomSheetDialog dialog = NumClickBottomSheetDialog.instance(RegexUtils.regexNumber(action_str));
//                    dialog.show(((ChatActivity) context).getSupportFragmentManager(), "");
                    openPhoneDialog(action_str);
                }

                if (action_str.startsWith("moor_moor_m7_actiondata-phone-href.m7-data-tel:")) {
                    //第三种情况 发送xbot问题
                    //moor_moor_m7_actionXbotQuickQuestionData.m7_data:"+问题+".com
                    //以 ActionXbotQuickQuestion 开头取出中间文案作为消息发送
                    action_str = action_str.replace("moor_moor_m7_actiondata-phone-href.m7-data-tel:", "");
                    action_str = action_str.replace(".com", "");
                    if (!TextUtils.isEmpty(action_str)) {
                        MoorEventBusUtil.post(new MoorSendMsgEvent(action_str));
                    }
                }


            }


        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setUnderlineText(true);
        }
    }

    public static class HttpClickSpan extends ClickableSpan {
        String msg;

        public HttpClickSpan(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View widget) {
            try {
                if (!msg.startsWith("http")) {
                    msg = "http://" + msg;
                } else {
                    if (msg.startsWith("http://tel:")) {
                        String tel = msg.replaceAll("http://tel:", "");
//                        NumClickBottomSheetDialog dialog = NumClickBottomSheetDialog.instance(RegexUtils.regexNumber(tel));
//                        dialog.show(((ChatActivity) context).getSupportFragmentManager(), "");
                        openPhoneDialog(tel);
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(msg);
                    intent.setData(content_url);
                    MoorActivityHolder.getCurrentActivity().startActivity(intent);
                }
            } catch (Exception e) {
                Toast.makeText(MoorActivityHolder.getCurrentActivity(), "链接不正确！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setUnderlineText(true);
        }
    }

    public static class NoUnderLineSpan extends UnderlineSpan {

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    private static List<String> getImgStr(String htmlStr) {
        List<String> pics = new ArrayList<String>();
        String regEx_img = "(<img.*src\\s*=\\s*(.*?)[^>]*?>|<p><video.*src\\s*=\\s*(.*?)[^>]*?</video></p>)";
        Pattern p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />或<video数据
            String img = m_image.group();
            Matcher m = Pattern.compile("(<img.*?src|<img.*?SRC|<video.*?src)=(\"|\')(.*?)(\"|\')").matcher(img);
            while (m.find()) {
                String g = m.group();
                if (g.startsWith("<video")) {
                    //如果是
                    pics.add("<video" + m.group(3));
                } else {
                    pics.add(m.group(3));
                }

            }
        }
        return pics;
    }

    public static class AString {
        public String a;
        public String content;
        public String url;
        public String action;
        public String peerid;
    }

    private List<AString> setAString(String message) {

        SpannableString spannableString = new SpannableString(message);

        List<AString> list = new ArrayList<>();

        Pattern patten = Pattern.compile("<a[^>]*>([^<]*)</a>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            AString atring = new AString();
            //a标签
            atring.a = matcher.group();
            //文字
            atring.content = matcher.group(1);
            Matcher m = Pattern.compile("(href|HREF)=(\"|\')(.*?)(\"|\')").matcher(matcher.group());
            //链接
            while (m.find()) {
                atring.url = m.group(3);
            }
            list.add(atring);

        }
        return list;
    }


    private List<View> initImgandText(final MoorMsgBean message, String[] strings, List<String> data) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            TextView tv1 = new TextView(MoorActivityHolder.getCurrentActivity());
            tv1.setTextColor(MoorUtils.getApp().getResources().getColor(R.color.moor_color_151515));
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv1.setMinHeight(MoorPixelUtil.dp2px(45f));
            tv1.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.65));
//            tv1.setMinWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.5));
            tv1.setPadding(MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f));
            tv1.setGravity(Gravity.CENTER_VERTICAL);
            if (message.getContent().contains("</a>") && (!message.getContent().contains("1："))) {

                String actionmsg = setA_String(strings[i]);

                Spanned string;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    string = Html.fromHtml(actionmsg, Html.FROM_HTML_MODE_COMPACT);
                } else {
                    string = Html.fromHtml(actionmsg);
                }

                Spanned string_o = new SpannableStringBuilder(trimTrailingWhitespace(string));

                SpannableStringBuilder spannableString = new SpannableStringBuilder(string_o);

                spannableString = getClickableHtml(spannableString);
                //匹配a标签 自定义 点击️
                dealATag(spannableString);
                //匹配电话
                dealPhoneNum(spannableString);

                tv1.setAutoLinkMask(Linkify.ALL);
                tv1.setText(spannableString);
                tv1.setLinkTextColor(moorDefaultThemeColor);
                tv1.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                List<AString> list = setAString(strings[i]);
                setNoImgView(tv1, strings[i], list);
            }
            views.add(tv1);

            if (data.size() > i) {
                View view = showImageOrVideo(data.get(i));
                views.add(view);
            }
        }
        return views;

    }


    //解析A标签，替换我们要的字符串类型
    private String setA_String(String message) {
        SpannableString spannableString = new SpannableString(message);
        Pattern patten = Pattern.compile("<a[^>]*>([^<]*)</a>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            AString atring = new AString();
            //a标签
            atring.a = matcher.group();
            //文字
            atring.content = matcher.group(1);

            Document doc = Jsoup.parse(atring.a);
            Elements chatElements = doc.getElementsByTag("a");

            if (chatElements != null) {
                if (chatElements.size() > 0) {
                    for (int i = 0; i < chatElements.size(); i++) {
                        if (chatElements.get(i) != null) {
                            Element chatElement = chatElements.get(i);
                            //m7_action为 transferAgent 响应转人工
                            atring.action = chatElement.attr("m7_action");
                            if (!TextUtils.isEmpty(atring.action)) {
                                if ("robotTransferAgent".equals(atring.action) | "transferAgent".equals(atring.action)) {
                                    atring.peerid = chatElement.attr("m7_data");//m7_action为 transferAgent 响应转人工，m7_data 取出技能组id
                                    message = message.replace(atring.a,
                                            "[" + atring.content + "](moor_moor_m7_actionrobotTransferAgent.m7_data:" + atring.peerid + ".com)");
                                }
                            }

                            //data-phone href 并且值为tel: 开头 有值为电话号
                            if (TextUtils.isEmpty(atring.action)) {
                                atring.action = chatElement.attr("href");
                                if (!TextUtils.isEmpty(atring.action)) {
                                    if (atring.action.startsWith("tel:")) {
                                        message = message.replace(atring.a,
                                                "[" + atring.content + "](moor_moor_m7_actiondata-phone-href.m7-data-tel:" + atring.content + ".com)");
                                    }
                                }
                            }
                        } else {

                        }
                    }

                }
            }
        }
        return message;
    }


    /**
     * 展示图片
     */
    private View showImageOrVideo(final String imageurl) {
        if (imageurl.startsWith("<video")) {
            return addVideo(imageurl);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    MoorPixelUtil.dp2px(200f),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            final ImageView iv = new ImageView(MoorActivityHolder.getCurrentActivity());
            iv.setAdjustViewBounds(true);
//            iv.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.55));
//            iv.setMaxHeight(MoorPixelUtil.dp2px(200f));

//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f));
//            params.width = MoorPixelUtil.dp2px(200f);
//            params.height = MoorPixelUtil.dp2px(200f);
            iv.setLayoutParams(params);
            MoorManager.getInstance().loadImage(imageurl, iv);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoorImageInfoBean bean = new MoorImageInfoBean();
                    bean.setFrom("in").setPath(imageurl);
                    MoorImagePreview.getInstance()
                            .setContext(MoorActivityHolder.getCurrentActivity())
                            .setIndex(0)
                            .setImage(bean)
                            .start();
                }
            });


            return iv;
        }

    }

    /**
     * 添加视频view
     *
     * @param videourl
     * @return
     */
    private View addVideo(final String videourl) {
        View view = View.inflate(MoorActivityHolder.getCurrentActivity(), R.layout.moor_text_video, null);
        ImageView thumb_one = view.findViewById(R.id.iv_textrx_video);

        final String video_url = videourl.replace("<video", "");
        MoorManager.getInstance().loadImage(video_url, thumb_one, MoorPixelUtil.dp2px(200f), MoorPixelUtil.dp2px(150f));
        thumb_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MoorActivityHolder.getCurrentActivity(), MoorVideoActivity.class);
                intent.putExtra(MoorDemoConstants.MOOR_VIDEO_PATH, video_url);
                MoorActivityHolder.getCurrentActivity().startActivity(intent);
            }
        });


        return view;
    }


    private boolean checkURL(String url) {

        return url.startsWith("moor_moor_m7_action");

    }

    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        HttpClickSpan clickableSpan = new HttpClickSpan(urlSpan.getURL());
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(moorDefaultThemeColor);
        clickableHtmlBuilder.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private SpannableStringBuilder getClickableHtml(Spanned spannedHtml) {
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
    }
}
