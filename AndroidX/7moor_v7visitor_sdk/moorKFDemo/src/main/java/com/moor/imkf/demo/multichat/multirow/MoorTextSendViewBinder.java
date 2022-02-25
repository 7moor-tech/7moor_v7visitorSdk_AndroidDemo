package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import android.text.Spannable;
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
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
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
        if (MoorEmojiBitmapUtil.getMoorEmotions().size() > 0) {
            Spannable emotionSpannable = MoorEmojiSpanBuilder.buildEmotionSpannable(item.getContent());
            holder.chatContentTv.setText(emotionSpannable);
        } else {
            holder.chatContentTv.setText(item.getContent());
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
