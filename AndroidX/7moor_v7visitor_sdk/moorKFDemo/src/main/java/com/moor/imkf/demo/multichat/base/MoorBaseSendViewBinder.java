package com.moor.imkf.demo.multichat.base;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multitype.MoorItemViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorDateUtil;
import com.moor.imkf.demo.utils.MoorDrawableUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.MoorRoundImageView;
import com.moor.imkf.moorsdk.bean.MoorInfoBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.constants.MoorChatMsgType;
import com.moor.imkf.moorsdk.db.MoorInfoDao;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 发送消息类型基类
 *     @version: 1.0
 * </pre>
 */
public abstract class MoorBaseSendViewBinder
        <T extends MoorMsgBean, VH extends MoorBaseSendHolder>
        extends MoorItemViewBinder<MoorMsgBean, MoorBaseSendViewBinder.ViewHolder> {


    /**
     * onCreateContentViewHolder
     *
     * @param inflater
     * @param parent
     * @return
     */
    protected abstract MoorBaseSendHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    /**
     * onBindContentViewHolder
     *
     * @param holder
     * @param item
     */
    protected abstract void onBindContentViewHolder(@NonNull VH holder, @NonNull T item);

    protected void onBaseViewRecycled(@NonNull VH holder) {
    }

    protected GradientDrawable statusDrawable;

    protected MoorInfoBean infoBean;

    @Override
    protected void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        onBaseViewRecycled((VH) holder.subViewHolder);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_base_send, parent, false);
        MoorBaseSendHolder subViewHolder = onCreateContentViewHolder(inflater, parent);
        return new ViewHolder(root, subViewHolder, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final MoorMsgBean item) {
        MoorOptions options = (MoorOptions) MoorManager.getInstance().getOptions();

        if (infoBean == null) {
            infoBean = MoorInfoDao.getInstance().queryInfo();
        }

        switch (item.getStatus()) {
            case MoorChatMsgType.MSG_TYPE_STATUS_SENDING:
                holder.chattingUploadingPb.setVisibility(View.VISIBLE);
                holder.uploadState.setVisibility(View.GONE);
                holder.iv_msg_read_status.setVisibility(View.GONE);
                break;
            case MoorChatMsgType.MSG_TYPE_STATUS_SUCCESS:
                holder.chattingUploadingPb.setVisibility(View.GONE);
                holder.uploadState.setVisibility(View.GONE);
                //设置已读未读
                setReadStatus(holder, item, options);
                break;
            case MoorChatMsgType.MSG_TYPE_STATUS_FAILURE:
                holder.chattingUploadingPb.setVisibility(View.GONE);
                holder.uploadState.setVisibility(View.VISIBLE);
                holder.iv_msg_read_status.setVisibility(View.GONE);
                holder.uploadState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_RESEND_MSG);
                    }
                });
                break;
            default:
                break;
        }
        //显示时间
        boolean showTimer = false;
        int position = holder.getAdapterPosition();
        if (position == 0) {
            showTimer = true;
        }

        if (position != 0) {
            MoorMsgBean previousItem = (MoorMsgBean) getAdapter().getItems().get(position - 1);
            if ((item.getCreateTimestamp() - previousItem.getCreateTimestamp() >= 180000L)) {
                showTimer = true;
            }
        }
        if (showTimer) {
            holder.chattingTimeTv.setVisibility(View.VISIBLE);
            holder.chattingTimeTv.setText(MoorDateUtil.getDateString(item.getCreateTimestamp(), MoorDateUtil.SHOW_TYPE_CALL_LOG).trim());
        } else {
            holder.chattingTimeTv.setVisibility(View.GONE);
        }


        //是否需要显示头像
        boolean showHeadPortrait = true;
        //是否是圆形头像
        String msgHeadImgType = "rectangle";
        //默认圆角度数
        int headPortraitRadius = 5;

        if (options != null) {
            showHeadPortrait = options.isShowHeadPortrait();
            msgHeadImgType = options.getMsgHeadImgType();
            headPortraitRadius = options.getHeadPortraitRadius();
        }


        if (showHeadPortrait) {
            holder.chattingAvatarIv.setVisibility(View.VISIBLE);
        } else {
            holder.chattingAvatarIv.setVisibility(View.GONE);
        }

        if ("circular".equals(msgHeadImgType)) {
            holder.chattingAvatarIv.setType(0);
        } else {
            holder.chattingAvatarIv.setType(1);
            holder.chattingAvatarIv.setCornerRadius(headPortraitRadius);
        }

        //设置访客头像
        if (MoorManager.getInstance().getMoorConfiguration() != null) {
            if (!TextUtils.isEmpty(MoorManager.getInstance().getMoorConfiguration().userHeadImg)) {
                MoorManager.getInstance().loadImage(MoorManager.getInstance().getMoorConfiguration().userHeadImg,
                        holder.chattingAvatarIv, MoorPixelUtil.dp2px(45f), MoorPixelUtil.dp2px(45f));
            }
        }


        onBindContentViewHolder((VH) holder.subViewHolder, (T) item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView chattingTimeTv;
        FrameLayout chattingContentLayout;
        ImageView uploadState;
        MoorRoundImageView chattingAvatarIv;
        ProgressBar chattingUploadingPb;
        MoorBaseSendHolder subViewHolder;
        ImageView iv_msg_read_status;

        ViewHolder(View itemView, MoorBaseSendHolder subViewHolder, MoorBaseSendViewBinder binder) {
            super(itemView);

            chattingContentLayout = (FrameLayout) findViewById(R.id.chatting_content_layout);
            uploadState = (ImageView) findViewById(R.id.chatting_state_iv);
            chattingUploadingPb = (ProgressBar) findViewById(R.id.chatting_uploading_pb);
            chattingAvatarIv = (MoorRoundImageView) findViewById(R.id.iv_chat_avatar);
            chattingTimeTv = (TextView) findViewById(R.id.chatting_time_tv);
            iv_msg_read_status = (ImageView) findViewById(R.id.iv_msg_read_status);
            chattingContentLayout.addView(subViewHolder.itemView);
            this.subViewHolder = subViewHolder;
            this.subViewHolder.viewHolder = this;
        }

        private View findViewById(int resId) {
            return itemView.findViewById(resId);
        }

    }

    public List<MoorMsgBean> getItems() {
        return (List<MoorMsgBean>) getAdapter().getItems();
    }


    private void setReadStatus(ViewHolder holder, MoorMsgBean item, MoorOptions options) {
        if (infoBean != null) {
            if (infoBean.isAgentReadMessage()) {
                //打开已读未读开关
                holder.iv_msg_read_status.setVisibility(View.VISIBLE);
                if (!item.isDealCustomerMsg()) {
                    String color = options.getRightMsgBgColor();
                    if (!TextUtils.isEmpty(color)) {
                        statusDrawable = MoorDrawableUtils.getCircleShape(4, 0,
                                MoorColorUtils.getColorWithAlpha(1f, color), Color.TRANSPARENT);
                    } else {
                        statusDrawable = MoorDrawableUtils.getCircleShape(4, 0,
                                MoorColorUtils.getColorWithAlpha(1f, MoorUtils.getApp().getResources().getColor(R.color.moor_color_0081ff)), Color.TRANSPARENT);
                    }
                    holder.iv_msg_read_status.setBackground(statusDrawable);
                } else {
                    holder.iv_msg_read_status.setBackground(MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_icon_read_status));
                }
            } else {
                holder.iv_msg_read_status.setVisibility(View.GONE);
            }
        } else {
            holder.iv_msg_read_status.setVisibility(View.GONE);
        }
    }
}
