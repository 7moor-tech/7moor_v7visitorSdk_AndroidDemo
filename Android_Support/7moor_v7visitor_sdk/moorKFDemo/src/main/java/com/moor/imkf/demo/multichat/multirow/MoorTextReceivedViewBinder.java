package com.moor.imkf.demo.multichat.multirow;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.multichat.MoorTextParseUtil;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorRegexUtils;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.db.MoorInfoDao;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 收到的文本消息类型
 *     @version: 1.0
 * </pre>
 */
public class MoorTextReceivedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorTextReceivedViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorTextReceivedViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_text_received, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
        if (options != null) {
            String leftMsgBgColor = options.getLeftMsgBgColor();
            if (!TextUtils.isEmpty(leftMsgBgColor)) {
                holder.slContentReceived.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
            }
            String leftMsgTextColor = options.getLeftMsgTextColor();
            if (!TextUtils.isEmpty(leftMsgTextColor)) {
                holder.chatContentTv.setTextColor(MoorColorUtils.getColorWithAlpha(1f, leftMsgTextColor));
            }
        }
        holder.setData(item);
    }

    static class ViewHolder extends MoorBaseReceivedHolder {

        TextView chatContentTv;
        LinearLayout llFlowText;
        MoorShadowLayout slContentReceived;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.llFlowText = itemView.findViewById(R.id.ll_flow_list_text);
            this.chatContentTv = itemView.findViewById(R.id.chat_content_tv);
            this.slContentReceived = itemView.findViewById(R.id.sl_content_received);
        }

        void setData(@NonNull MoorMsgBean item) {
            getParent().chattingTvName.post(new Runnable() {

                @Override
                public void run() {
                    int width = getParent().chattingTvName.getWidth();
                    chatContentTv.setMinWidth(width);
                }
            });
            chatContentTv.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.7));
            if (item.isShowHtml()) {
                chatContentTv.setVisibility(View.GONE);
                llFlowText.setVisibility(View.VISIBLE);
                List<View> views = MoorTextParseUtil.getInstance().parseHtmlText(item);
                llFlowText.removeAllViews();
                if (views.size() > 0) {
                    for (View view : views) {
                        llFlowText.addView(view);
                    }
                }
            } else {
                chatContentTv.setVisibility(View.VISIBLE);
                llFlowText.setVisibility(View.GONE);

                //开启坐席敏感词，并且关键词列表不为空
                if (MoorManager.getInstance().isAgentSensitiveWordsFlag() && MoorManager.getInstance().getAgentSensitiveWords() != null) {
                    //从MoorManager获取坐席关键词列表，如果长度异常，从数据库中重新解析一个
                    if (MoorManager.getInstance().getAgentSensitiveWords().size() <= 0) {
                        if (TextUtils.isEmpty(MoorInfoDao.getInstance().queryInfo().getAgentSensitiveWords())) {
                            MoorManager.getInstance().setAgentSensitiveWords(MoorInfoDao.getInstance().queryInfo().getAgentSensitiveWords());
                        }
                    }
                    //敏感词匹配
                    item.setContent(MoorRegexUtils.matchAgentSensitiveWords(item.getContent(), MoorManager.getInstance().getAgentSensitiveWords()));
                }

                //标准解析
                SpannableStringBuilder parseText = MoorTextParseUtil.getInstance().parseText(item);
                if (parseText == null || TextUtils.isEmpty(parseText.toString())) {
                    parseText = new SpannableStringBuilder(item.getContent());
                }


                //开启坐席关键词，并且关键词列表不为空
                if (MoorManager.getInstance().isAgentFocusWordsFlag() && MoorManager.getInstance().getAgentFocusWords() != null) {
                    //从MoorManager获取坐席关键词列表，如果长度异常，从数据库中重新解析一个
                    if (MoorManager.getInstance().getAgentFocusWords().size() <= 0) {
                        if (TextUtils.isEmpty(MoorInfoDao.getInstance().queryInfo().getAgentFocusWords())) {
                            MoorManager.getInstance().setAgentFocusWords(MoorInfoDao.getInstance().queryInfo().getAgentFocusWords());
                        }
                    }
                    int color = MoorUtils.getApp().getResources().getColor(R.color.moor_color_151515);
                    MoorOptions options = MoorManager.getInstance().getOptions();
                    if (options != null) {
                        String leftMsgTextColor = options.getLeftMsgTextColor();
                        if (!TextUtils.isEmpty(leftMsgTextColor)) {
                            color = MoorColorUtils.getColorWithAlpha(1f, leftMsgTextColor);
                        }
                    }

                    if (!TextUtils.isEmpty(MoorManager.getInstance().getAgentFocusWordsColor())) {
                        try {
                            color = Color.parseColor(MoorManager.getInstance().getAgentFocusWordsColor());
                        } catch (Exception e) {
                            color = MoorUtils.getApp().getResources().getColor(R.color.moor_color_151515);
                        }
                    }
                    //关键字匹配
                    parseText = new SpannableStringBuilder(MoorRegexUtils.matchSearchText(color, parseText, MoorManager.getInstance().getAgentFocusWords()));
                }

                chatContentTv.setText(parseText);

                chatContentTv.setMovementMethod(LinkMovementMethod.getInstance());
            }

        }
    }


}
