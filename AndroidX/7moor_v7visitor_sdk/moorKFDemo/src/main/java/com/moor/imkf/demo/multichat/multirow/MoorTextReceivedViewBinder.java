package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
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
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
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

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item, @NonNull List<Object> payloads) {

    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {

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
                SpannableStringBuilder parseText = MoorTextParseUtil.getInstance().parseText(item);

                if(parseText!=null&&!TextUtils.isEmpty(parseText.toString())){
                    chatContentTv.setText(parseText);
                }else {
                    chatContentTv.setText(item.getContent());
                }

                chatContentTv.setMovementMethod(LinkMovementMethod.getInstance());
            }

        }
    }


}
