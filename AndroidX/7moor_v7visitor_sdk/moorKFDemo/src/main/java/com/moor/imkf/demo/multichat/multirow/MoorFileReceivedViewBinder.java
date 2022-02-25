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
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorFileFormatUtils;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.MoorCircleProgressView;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.utils.MoorUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/9/21
 *     @desc   : 收到的文件消息类型
 *     @version: 1.0
 * </pre>
 */
public class MoorFileReceivedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorFileReceivedViewBinder.ViewHolder> {

    private final MoorOptions options;

    public MoorFileReceivedViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_file_received, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
        ViewGroup.LayoutParams layoutParams = holder.rlFileRootView.getLayoutParams();
        layoutParams.width = (int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.7);
        holder.rlFileRootView.setLayoutParams(layoutParams);

        holder.setData(item);
    }

    class ViewHolder extends MoorBaseReceivedHolder {

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
            tvFileSize.setText(item.getFileSize());

            int fileIcon = MoorFileFormatUtils.getFileIcon(item.getContent());
            ivFileIcon.setImageResource(fileIcon);


            chatContentFileProgress.setProgress(item.getFileProgress());
            if (item.getFileProgress() >= 100) {
                chatContentFileProgress.setVisibility(View.GONE);
            }


            final String url;
            if (TextUtils.isEmpty(item.getFileLocalPath())) {
                url = item.getContent().startsWith("http") ? item.getContent() : MoorUrlManager.BASE_IM_URL + item.getContent();

                tvFileSize.setText(item.getFileSize() + " / " + rlFileRootView.getContext().getString(R.string.moor_file_download));
                chatContentFileProgress.setVisibility(View.VISIBLE);
            } else {
                url = item.getFileLocalPath();
                tvFileSize.setText(item.getFileSize() + " / " + rlFileRootView.getContext().getString(R.string.moor_file_look));
                chatContentFileProgress.setVisibility(View.GONE);
            }
            rlFileRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_FILE_CLICK.setObj(url, getAdapterPosition()));
                }
            });
        }
    }


}
