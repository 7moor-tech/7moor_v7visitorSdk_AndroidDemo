package com.moor.imkf.demo.multichat.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.multitype.MoorItemViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorDateUtil;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.MoorRoundImageView;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.constants.MoorChatMsgType;
import com.moor.imkf.moorsdk.db.MoorOptionsDao;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 多类型消息基类
 *     @version: 1.0
 * </pre>
 */
public abstract class MoorBaseReceivedViewBinder
        <T extends MoorMsgBean, VH extends MoorBaseReceivedHolder>
        extends MoorItemViewBinder<MoorMsgBean, MoorBaseReceivedViewBinder.ViewHolder> {

    /**
     * onCreateContentViewHolder
     *
     * @param inflater
     * @param parent
     * @return
     */
    protected abstract MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    /**
     * onBindContentViewHolder
     *
     * @param holder
     * @param item
     */
    protected abstract void onBindContentViewHolder(@NonNull VH holder, @NonNull T item);

    protected void onBindContentViewHolder(@NonNull VH holder, @NonNull T item, @NonNull List<Object> payloads) {
    }

    protected void onBaseViewRecycled(@NonNull VH holder) {
    }

    @Override
    protected void onViewRecycled(@NonNull MoorBaseReceivedViewBinder.ViewHolder holder) {
        super.onViewRecycled(holder);
        MoorManager.getInstance().clearImage(holder.ivChatAvatar);
        onBaseViewRecycled((VH) holder.subViewHolder);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_base_received, parent, false);
        MoorBaseReceivedHolder subViewHolder = onCreateContentViewHolder(inflater, parent);
        return new ViewHolder(root, subViewHolder, this);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
        MoorOptions options = (MoorOptions) MoorManager.getInstance().getOptions();
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

        //坐席名称
        if (!TextUtils.isEmpty(item.getUserName())) {
//            holder.chattingTvName.setVisibility(View.VISIBLE);
            holder.chattingTvName.setText(item.getUserName());
        } else {
//            holder.chattingTvName.setVisibility(View.GONE);
            holder.chattingTvName.setText(MoorUtils.getApp().getResources().getString(R.string.moor_system));
        }
        //消息气泡颜色
        if (options != null && (MoorChatMsgType.MSG_TYPE_TEXT.equals(item.getContentType())
                || MoorChatMsgType.MSG_TYPE_FLOW_LIST_SINGLE_ONE_HORIZONTAL.equals(item.getContentType())
                || MoorChatMsgType.MSG_TYPE_FLOW_LIST_SINGLE_ONE_VERTICAL.equals(item.getContentType())
                || MoorChatMsgType.MSG_TYPE_FLOW_LIST_MULTI_SELECT.equals(item.getContentType())
                || MoorChatMsgType.MSG_TYPE_FLOW_LIST_TWO.equals(item.getContentType()))) {
            String leftMsgBgColor = options.getLeftMsgBgColor();
            if (!TextUtils.isEmpty(leftMsgBgColor)) {
                holder.chattingContentLayout.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
            }

        }

        //是否需要显示头像
        boolean showHeadPortrait = true;
        if (options != null) {
            showHeadPortrait = options.isShowHeadPortrait();
        }
        if (showHeadPortrait) {
            String userId = item.getLocalUserType();
            if ("system".equals(userId)) {
                String sysMsgHeadImgUrl = MoorOptionsDao.getInstance().queryOptions().getSysMsgHeadImgUrl();
                if (!TextUtils.isEmpty(sysMsgHeadImgUrl)) {
                    MoorManager.getInstance().loadImage(MoorUrlManager.BASE_IM_URL + "/" + sysMsgHeadImgUrl, holder.ivChatAvatar, MoorPixelUtil.dp2px(45f), MoorPixelUtil.dp2px(45f));
                } else {
                    holder.ivChatAvatar.setImageResource(R.drawable.moor_head_default_system);
                }
            } else {
                holder.ivChatAvatar.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(item.getUserHeadImg())) {
                    if (item.getUserHeadImg().startsWith("http")) {
                        MoorManager.getInstance().loadImage(item.getUserHeadImg(), holder.ivChatAvatar, MoorPixelUtil.dp2px(45f), MoorPixelUtil.dp2px(45f));
                    } else if ("1".equals(item.getUserHeadImg())) {
                        //显示机器人默认头像
                        holder.ivChatAvatar.setImageResource(R.drawable.moor_head_default_robot);
                    } else {
                        MoorManager.getInstance().loadImage(MoorUrlManager.BASE_COMMON_URL + "/" + item.getUserHeadImg(), holder.ivChatAvatar, MoorPixelUtil.dp2px(45f), MoorPixelUtil.dp2px(45f));
                    }
                } else {
                    //显示人工默认头像
                    holder.ivChatAvatar.setImageResource(R.drawable.moor_head_default_local);
                }
            }
        } else {
            holder.ivChatAvatar.setVisibility(View.GONE);
        }

        //是否是圆形头像
        String msgHeadImgType = "rectangle";
        //默认圆角度数
        int headPortraitRadius = 5;

        if (options != null) {
            msgHeadImgType = options.getMsgHeadImgType();
            headPortraitRadius = options.getHeadPortraitRadius();
        }

        if ("circular".equals(msgHeadImgType)) {
            holder.ivChatAvatar.setType(0);
        } else {
            holder.ivChatAvatar.setType(1);
            holder.ivChatAvatar.setCornerRadius(headPortraitRadius);
        }

        onBindContentViewHolder((VH) holder.subViewHolder, (T) item);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, item);
        } else {
            onBindContentViewHolder((VH) holder.subViewHolder, (T) item, payloads);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public MoorShadowLayout chattingContentLayout;
        public MoorRoundImageView ivChatAvatar;
        public TextView chattingTimeTv;
        public TextView chattingTvName;
        public MoorBaseReceivedHolder subViewHolder;
        public LinearLayout llUseful;
        public LinearLayout llBottomContentMatch;
        public LinearLayout llBottomContentMargin;
        public LinearLayout llBottomContentMulti;
        public ImageView ivUseful;
        public ImageView ivUnuseful;

        ViewHolder(View itemView, MoorBaseReceivedHolder subViewHolder, MoorBaseReceivedViewBinder binder) {
            super(itemView);
            chattingContentLayout = (MoorShadowLayout) findViewById(R.id.chatting_content_layout);
            chattingTimeTv = (TextView) findViewById(R.id.chatting_time_tv);
            chattingTvName = (TextView) findViewById(R.id.chatting_tv_name);
            ivChatAvatar = (MoorRoundImageView) findViewById(R.id.iv_chat_avatar);
            llUseful = (LinearLayout) findViewById(R.id.ll_useful);
            ivUseful = (ImageView) findViewById(R.id.iv_useful);
            ivUnuseful = (ImageView) findViewById(R.id.iv_unuseful);
            llBottomContentMatch = (LinearLayout) findViewById(R.id.ll_bottom_content_match);
            llBottomContentMargin = (LinearLayout) findViewById(R.id.ll_bottom_content_margin);
            llBottomContentMulti = (LinearLayout) findViewById(R.id.ll_bottom_content_multi);
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

}
