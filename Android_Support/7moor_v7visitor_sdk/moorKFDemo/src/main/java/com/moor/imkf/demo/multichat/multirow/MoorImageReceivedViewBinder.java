package com.moor.imkf.demo.multichat.multirow;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.multichat.MoorStartImagePreview;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorImageUtil;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorsdk.bean.MoorImageInfoBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.db.MoorMsgDao;
import com.moor.imkf.moorsdk.listener.IMoorImageLoaderListener;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.io.File;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 收到的图片消息类型
 *     @version: 1.0
 * </pre>
 */
public class MoorImageReceivedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorImageReceivedViewBinder.ViewHolder> {


    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_imge_received, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull final ViewHolder holder, @NonNull MoorMsgBean item) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.chatContentIv.getLayoutParams();
        if (item.getImageHeight() != 0 && item.getImageWidth() != 0) {
            layoutParams.width = item.getImageWidth();
            layoutParams.height = item.getImageHeight();
        } else {
            layoutParams.width = MoorPixelUtil.dp2px(160f);
            layoutParams.height = MoorPixelUtil.dp2px(160f);
        }
        holder.chatContentIv.setLayoutParams(layoutParams);
        if (!item.getContent().startsWith("http")) {
            item.setContent(MoorUrlManager.BASE_IM_URL + item.getContent());
        }
        holder.chatContentIv.setTag(R.id.moor_list_image_tag, item.getContent());
        List<MoorMsgBean> items = getItems();
        holder.setData(item, items);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item, @NonNull List<Object> payloads) {

    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {
        MoorManager.getInstance().clearImage(holder.chatContentIv);
    }


    static class ViewHolder extends MoorBaseReceivedHolder {

        ImageView chatContentIv;
        LinearLayout ll_image_received;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatContentIv = (ImageView) itemView.findViewById(R.id.chat_content_iv);
            ll_image_received = (LinearLayout) itemView.findViewById(R.id.ll_image_received);
        }

        void setData(@NonNull final MoorMsgBean item, final List<MoorMsgBean> items) {
            if (MoorImageUtil.isGifImageWithMime(item.getContent(), item.getContent())) {
                MoorManager.getInstance().loadGifImage(item.getContent(), chatContentIv);
            } else {
                MoorManager.getInstance().loadImage(item.getContent(), 0, 0, new IMoorImageLoaderListener() {
                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {
                                if (item.getContent() == chatContentIv.getTag(R.id.moor_list_image_tag)) {
                                    chatContentIv.setBackgroundDrawable(placeholder);
                                }
                            }

                            @Override
                            public void onLoadComplete(@NonNull Bitmap bitmap) {
                                if (item.getContent() == chatContentIv.getTag(R.id.moor_list_image_tag)) {
                                    chatContentIv.setBackgroundDrawable(null);
                                }
                                MoorImageInfoBean imageInfoBean = MoorImageUtil.resizeImageView(bitmap.getWidth(), bitmap.getHeight());

                                if (item.getImageHeight() != imageInfoBean.getLayoutH() || item.getImageWidth() != imageInfoBean.getLayoutW()) {
                                    //保存宽高
                                    item.setImageWidth(imageInfoBean.getLayoutW());
                                    item.setImageHeight(imageInfoBean.getLayoutH());
                                    MoorMsgDao.getInstance().updateMoorMsgBeanToDao(item);

                                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) chatContentIv.getLayoutParams();
                                    layoutParams.width = imageInfoBean.getLayoutW();
                                    layoutParams.height = imageInfoBean.getLayoutH();
                                    chatContentIv.setLayoutParams(layoutParams);
                                }
//                            Drawable drawable = new BitmapDrawable(MoorUtils.getApp().getResources(), bitmap);
//                            chatContentIv.setImageDrawable(drawable);
                                Bitmap imageBitmap = ThumbnailUtils.extractThumbnail(bitmap, imageInfoBean.getLayoutW(), imageInfoBean.getLayoutH());
//                            Drawable drawable = new BitmapDrawable(MoorUtils.getApp().getResources(), imageBitmap);
                                if (item.getContent() == chatContentIv.getTag(R.id.moor_list_image_tag)) {
                                    chatContentIv.setImageBitmap(imageBitmap);
                                }
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                if (item.getContent() == chatContentIv.getTag(R.id.moor_list_image_tag)) {
                                    chatContentIv.setImageDrawable(errorDrawable);
                                }
                            }


                            @Override
                            public void onResourceReady(@NonNull File resource) {

                            }
                        }
                );
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
