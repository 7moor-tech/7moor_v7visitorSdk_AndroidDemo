package com.moor.imkf.demo.multichat.multirow;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.multichat.base.MoorBaseSendHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseSendViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorMediaPlayTools;
import com.moor.imkf.demo.utils.MoorMp3Util;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.utils.MoorViewUtil;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/8/21
 *     @desc   : 发送的语音消息类型
 *     @version: 1.0
 * </pre>
 */
public class MoorVoiceSendViewBinder extends MoorBaseSendViewBinder<MoorMsgBean, MoorVoiceSendViewBinder.ViewHolder> {
    MoorMediaPlayTools instance = MoorMediaPlayTools.getInstance();

    private final MoorOptions options;

    public MoorVoiceSendViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseSendHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_voice_send, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
        if (options != null) {
            String rightMsgBgColor = options.getRightMsgBgColor();
            if (!TextUtils.isEmpty(rightMsgBgColor)) {
                holder.slVoiceSend.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, rightMsgBgColor));
            }
            String rightMsgTextColor = options.getRightMsgTextColor();
            if (!TextUtils.isEmpty(rightMsgBgColor)) {
                holder.tvItemVoiceLongTime.setTextColor(MoorColorUtils.getColorWithAlpha(1f, rightMsgTextColor));
            }

        }
        String url;
        if (TextUtils.isEmpty(item.getFileLocalPath())) {
            url = item.getContent();
        } else {
            url = item.getFileLocalPath();
        }
        holder.tvItemVoiceLongTime.setText(MoorMp3Util.getMp3TimeForFile(url) + "''");

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.rlVoiceLayout.getLayoutParams();
        params.width = MoorPixelUtil.dp2px(MoorViewUtil.getVoiceLongWitdth(MoorMp3Util.getMp3TimeForFile(item.getFileLocalPath())));
        holder.rlVoiceLayout.setLayoutParams(params);
        holder.setData(item);
    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {

    }


    class ViewHolder extends MoorBaseSendHolder {
        TextView tvItemVoiceLongTime;
        ImageView ivSendplayAnim;
        RelativeLayout rlVoiceLayout;
        AnimationDrawable animationDrawable;
        MoorShadowLayout slVoiceSend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemVoiceLongTime = itemView.findViewById(R.id.tv_item_voice_longTime);
            ivSendplayAnim = itemView.findViewById(R.id.iv_sendplay_anim);
            rlVoiceLayout = itemView.findViewById(R.id.rl_voice_layout);
            slVoiceSend = itemView.findViewById(R.id.sl_voice_send);
        }

        void setData(@NonNull final MoorMsgBean item) {

            if (MoorMediaPlayTools.PlayPosition == getAdapterPosition()) {
                animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(ivSendplayAnim.getContext(), R.drawable.moor_voice_send_play_anim);
                ivSendplayAnim.setImageDrawable(animationDrawable);
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            } else {
                if (animationDrawable != null) {
                    animationDrawable.stop();
                    animationDrawable = null;
                }
                ivSendplayAnim.setImageDrawable(null);
                ivSendplayAnim.setImageResource(R.drawable.moor_voice_send_play3);
            }

            instance.setOnVoicePlayCompletionListener(new MoorMediaPlayTools.OnVoicePlayCompletionListener() {
                @Override
                public void onVoicePlayCompletion(int playPosition) {
                    if (playPosition != -1) {
                        MoorMediaPlayTools.PlayPosition = -1;
                        getAdapter().notifyItemChanged(playPosition);
                    }
                }

                @Override
                public void onCancel(int playPosition) {
                    if (playPosition != -1) {
                        MoorMediaPlayTools.PlayPosition = -1;
                        getAdapter().notifyItemChanged(playPosition);
                    }
                }
            });
            slVoiceSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MoorMediaPlayTools.PlayPosition != -1) {
                        getAdapter().notifyItemChanged(MoorMediaPlayTools.PlayPosition);
                    }
                    if (instance.isPlaying()) {
                        instance.stop();
                    }
                    if (MoorMediaPlayTools.PlayPosition == getAdapterPosition()) {
                        MoorMediaPlayTools.PlayPosition = -1;
                        getAdapter().notifyItemChanged(getAdapterPosition());
                        return;
                    }
                    MoorMediaPlayTools.PlayPosition = getAdapterPosition();
                    instance.playVoice(item.getFileLocalPath(), false);
                    getAdapter().notifyItemChanged(MoorMediaPlayTools.PlayPosition);
                }
            });

        }
    }
}
