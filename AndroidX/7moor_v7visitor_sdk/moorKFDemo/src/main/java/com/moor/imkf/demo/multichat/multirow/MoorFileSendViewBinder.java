package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.base.MoorBaseSendHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseSendViewBinder;
import com.moor.imkf.demo.utils.MoorFileFormatUtils;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.MoorCircleProgressView;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.constants.MoorChatMsgType;
import com.moor.imkf.moorsdk.utils.MoorUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/9/21
 *     @desc   : 发送的文件消息类型
 *     @version: 1.0
 * </pre>
 */
public class MoorFileSendViewBinder extends MoorBaseSendViewBinder<MoorMsgBean, MoorFileSendViewBinder.ViewHolder> {

    private final MoorOptions options;

    public MoorFileSendViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseSendHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_file_send, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {

        ViewGroup.LayoutParams layoutParams = holder.rlFileRootView.getLayoutParams();
        layoutParams.width = (int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.7);
        holder.rlFileRootView.setLayoutParams(layoutParams);

        holder.setData(item);

    }

    class ViewHolder extends MoorBaseSendHolder {

        RelativeLayout rlFileRootView;
        TextView tvFileTitle;
        TextView tvFileSize;
        ImageView ivFileIcon;
        MoorCircleProgressView chatContentFileProgress;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlFileRootView = itemView.findViewById(R.id.rl_file_root_view);
            tvFileTitle = itemView.findViewById(R.id.tv_file_title);
            tvFileSize = itemView.findViewById(R.id.tv_file_size);
            ivFileIcon = itemView.findViewById(R.id.iv_file_icon);
            chatContentFileProgress = itemView.findViewById(R.id.chat_content_file_progress);
        }

        void setData(@NonNull final MoorMsgBean item) {

            tvFileTitle.setText(item.getFileName());
            tvFileSize.setText(item.getFileSize() + " / " + rlFileRootView.getContext().getString(R.string.moor_file_sending));

            final int fileIcon = MoorFileFormatUtils.getFileIcon(item.getContent());
            ivFileIcon.setImageResource(fileIcon);


            chatContentFileProgress.setProgress(item.getFileProgress());
            if (item.getFileProgress() >= 100) {
                tvFileSize.setText(item.getFileSize() + " / " + rlFileRootView.getContext().getString(R.string.moor_file_sendok));
                chatContentFileProgress.setVisibility(View.GONE);
            } else {
                tvFileSize.setText(item.getFileSize() + " / " + rlFileRootView.getContext().getString(R.string.moor_file_sending));
                chatContentFileProgress.setVisibility(View.VISIBLE);
            }


            String url = "";
            if (!item.getStatus().equals(MoorChatMsgType.MSG_TYPE_STATUS_SENDING)) {
                if (TextUtils.isEmpty(item.getFileLocalPath())) {
                    url = item.getContent();
                    tvFileSize.setText(item.getFileSize() + " / " + rlFileRootView.getContext().getString(R.string.moor_file_download));
                    chatContentFileProgress.setVisibility(View.VISIBLE);
                } else {
                    url = item.getFileLocalPath();
                    tvFileSize.setText(item.getFileSize() + " / " + rlFileRootView.getContext().getString(R.string.moor_file_look));
                    chatContentFileProgress.setVisibility(View.GONE);
                }
            }


            final String finalUrl = url;
            rlFileRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_FILE_CLICK.setObj(finalUrl, getAdapterPosition()));
                }
            });
        }
    }


}
