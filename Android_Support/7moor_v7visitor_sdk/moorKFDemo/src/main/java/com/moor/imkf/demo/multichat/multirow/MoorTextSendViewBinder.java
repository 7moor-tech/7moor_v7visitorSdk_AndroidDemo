package com.moor.imkf.demo.multichat.multirow;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.emotion.MoorEmojiSpanBuilder;
import com.moor.imkf.demo.multichat.base.MoorBaseSendHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseSendViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorEmojiBitmapUtil;
import com.moor.imkf.demo.utils.MoorRegexUtils;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.db.MoorInfoDao;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 发送的文本消息类型
 *     @version: 1.0
 * </pre>
 */
public class MoorTextSendViewBinder extends MoorBaseSendViewBinder<MoorMsgBean, MoorTextSendViewBinder.ViewHolder> {

    private final MoorOptions options;

    public MoorTextSendViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseSendHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_text_send, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull final MoorMsgBean item) {
        if (options != null) {
            String rightMsgBgColor = options.getRightMsgBgColor();
            if (!TextUtils.isEmpty(rightMsgBgColor)) {
                holder.slContentSend.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, rightMsgBgColor));
            }
            String rightMsgTextColor = options.getRightMsgTextColor();
            if (!TextUtils.isEmpty(rightMsgTextColor)) {
                holder.chatContentTv.setTextColor(MoorColorUtils.getColorWithAlpha(1f, rightMsgTextColor));
            }

        }
        holder.chatContentTv.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.7));
        SpannableString emotionSpannable;
        if (MoorEmojiBitmapUtil.getMoorEmotions().size() > 0) {
            emotionSpannable = MoorEmojiSpanBuilder.buildEmotionSpannable(item.getContent());
        } else {
            emotionSpannable = new SpannableString(item.getContent());
        }


        //开启访客关键词，并且关键词列表不为空
        if (MoorManager.getInstance().isVisitorFocusWordsFlag() && MoorManager.getInstance().getVisitorFocusWords() != null) {
            //从MoorManager获取访客关键词列表，如果长度异常，从数据库中重新解析一个
            if (MoorManager.getInstance().getVisitorFocusWords().size() <= 0) {
                if (TextUtils.isEmpty(MoorInfoDao.getInstance().queryInfo().getVisitorFocusWords())) {
                    MoorManager.getInstance().setVisitorFocusWords(MoorInfoDao.getInstance().queryInfo().getVisitorFocusWords());
                }
            }
            int color = MoorUtils.getApp().getResources().getColor(R.color.moor_color_151515);

            if (options != null) {
                String rightMsgTextColor = options.getRightMsgTextColor();
                if (!TextUtils.isEmpty(rightMsgTextColor)) {
                    color = MoorColorUtils.getColorWithAlpha(1f, rightMsgTextColor);
                }
            }

            if (!TextUtils.isEmpty(MoorManager.getInstance().getVisitorFocusWordsColor())) {
                try {
                    color = Color.parseColor(MoorManager.getInstance().getVisitorFocusWordsColor());
                } catch (Exception e) {
                    color = MoorUtils.getApp().getResources().getColor(R.color.moor_color_151515);
                }
            }
            //关键字匹配
            holder.chatContentTv.setText(MoorRegexUtils.matchSearchText(color, emotionSpannable, MoorManager.getInstance().getVisitorFocusWords()));
        } else {
            holder.chatContentTv.setText(emotionSpannable);
        }


    }

    static class ViewHolder extends MoorBaseSendHolder {
        TextView chatContentTv;
        MoorShadowLayout slContentSend;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.chatContentTv = (TextView) itemView.findViewById(R.id.chat_content_tv);
            this.slContentSend = (MoorShadowLayout) itemView.findViewById(R.id.sl_content_send);
        }
    }
}
