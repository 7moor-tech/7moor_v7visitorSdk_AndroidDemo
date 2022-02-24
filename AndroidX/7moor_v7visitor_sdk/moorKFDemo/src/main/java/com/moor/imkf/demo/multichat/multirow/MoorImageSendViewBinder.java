package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.multichat.MoorLoadImageUtil;
import com.moor.imkf.demo.multichat.MoorStartImagePreview;
import com.moor.imkf.demo.multichat.base.MoorBaseSendHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseSendViewBinder;
import com.moor.imkf.demo.utils.MoorImageUtil;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 发送的图片消息类型
 *     @version: 1.0
 * </pre>
 */
public class MoorImageSendViewBinder extends MoorBaseSendViewBinder<MoorMsgBean, MoorImageSendViewBinder.ViewHolder> {

    @Override
    protected MoorBaseSendHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_image_send, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
        if (item.getImageHeight() != 0 && item.getImageWidth() != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.chatContentIv.getLayoutParams();
            layoutParams.width = item.getImageWidth();
            layoutParams.height = item.getImageHeight();
            holder.chatContentIv.setLayoutParams(layoutParams);
        }

        String url;
        if (TextUtils.isEmpty(item.getFileLocalPath())) {
            url = item.getContent();
        } else {
            url = item.getFileLocalPath();
        }

        holder.chatContentIv.setTag(R.id.moor_list_image_tag, url);
        List<MoorMsgBean> items = getItems();
        holder.setData(item, items);

    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {
        MoorManager.getInstance().clearImage(holder.chatContentIv);
    }


    static class ViewHolder extends MoorBaseSendHolder {

        ImageView chatContentIv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatContentIv = itemView.findViewById(R.id.chat_content_iv);
        }

        void setData(@NonNull final MoorMsgBean item, final List<MoorMsgBean> items) {
            if (MoorImageUtil.isGifImageWithMime(item.getContent(), item.getContent())) {
                MoorManager.getInstance().loadGifImage(item.getContent(), chatContentIv);
            } else {
                MoorLoadImageUtil.loadImage(item, chatContentIv);
//                int[] imageWidthHeight = MoorImageUtil.getImageWidthHeight(item.getContent());
//                MoorImageInfoBean imageInfoBean = MoorImageUtil.resizeImageView(imageWidthHeight[0], imageWidthHeight[1]);
//
//                if (item.getImage_height() != imageInfoBean.getLayoutH() || item.getImage_width() != imageInfoBean.getLayoutW()) {
//                    //保存宽高
//                    item.setImage_width(imageInfoBean.getLayoutW());
//                    item.setImage_height(imageInfoBean.getLayoutH());
//                    MoorMsgDao.getInstance().updateMoorMsgBeanToDao(item);
//
//                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) chatContentIv.getLayoutParams();
//                    layoutParams.width = imageInfoBean.getLayoutW();
//                    layoutParams.height = imageInfoBean.getLayoutH();
//                    chatContentIv.setLayoutParams(layoutParams);
//                }
//
//                if (item.getContent() == chatContentIv.getTag(R.id.moor_list_image_tag)) {
//                    IMoorImageLoaderListener listener = new IMoorImageLoaderListener() {
//                        @Override
//                        public void onLoadStarted(@Nullable Drawable placeholder) {
//
//                        }
//
//                        @Override
//                        public void onLoadComplete(@NonNull Bitmap bitmap) {
//                            Bitmap imageBitmap = ThumbnailUtils.extractThumbnail(bitmap, imageInfoBean.getLayoutW(), imageInfoBean.getLayoutH());
//                            Drawable drawable = new BitmapDrawable(MoorUtils.getApp().getResources(), imageBitmap);
//                            if (item.getContent() == chatContentIv.getTag(R.id.moor_list_image_tag)) {
//                                chatContentIv.setImageDrawable(drawable);
//                            }
//                        }
//
//                        @Override
//                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
//
//                        }
//
//                        @Override
//                        public void onResourceReady(@NonNull File resource) {
//
//                        }
//                    };
//                    if (imageInfoBean.getType() == MoorImageType.HORIZONTAL) {
//                        MoorManager.getInstance().loadImage(item.getContent(), imageInfoBean.getRealSize(), imageInfoBean.getLayoutH(), listener);
//                    } else if (imageInfoBean.getType() == MoorImageType.VERTICAL) {
//                        MoorManager.getInstance().loadImage(item.getContent(), imageInfoBean.getLayoutW(), imageInfoBean.getRealSize(), listener);
//                    } else if (imageInfoBean.getType() == MoorImageType.COMMON) {
//                        MoorManager.getInstance().loadImage(item.getContent(), imageInfoBean.getLayoutW(), imageInfoBean.getLayoutH(), listener);
//                    }
//
//
//                }
            }
            chatContentIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), MoorImageViewerActivity.class);
//                    intent.putExtra("imagePath", item.getContent());
//                    v.getContext().startActivity(intent);

                    MoorStartImagePreview.startPreview(v.getContext(), item, items);
                }
            });
        }
    }
}
