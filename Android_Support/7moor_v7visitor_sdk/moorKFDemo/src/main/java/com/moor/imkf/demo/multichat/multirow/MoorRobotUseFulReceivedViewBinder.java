package com.moor.imkf.demo.multichat.multirow;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorFlowTextAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.multichat.MoorTextParseUtil;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.db.MoorInfoDao;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/8/21
 *     @desc   : 有无帮助类型
 *     @version: 1.0
 * </pre>
 */
public class MoorRobotUseFulReceivedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorRobotUseFulReceivedViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorRobotUseFulReceivedViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_robot_useful_received, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull final ViewHolder holder, @NonNull final MoorMsgBean item) {
        holder.creatView();
        holder.tvFeedBack.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.65));
        holder.getParent().chattingTvName.post(new Runnable() {

            @Override
            public void run() {
                int width = holder.getParent().chattingTvName.getWidth();
                holder.viewUseful.setMinWidth(width);
            }
        });
        if (options != null) {
            String leftMsgBgColor = options.getLeftMsgBgColor();
            if (!TextUtils.isEmpty(leftMsgBgColor)) {
                holder.slUsefulFlowText.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
                holder.slUsefulFlow.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
                holder.slUsefulFeedBack.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
            }
        }
        List<View> views = MoorTextParseUtil.getInstance().parseFlow(item);
        holder.llFlowText.removeAllViews();
        if (views.size() > 0) {
            holder.slUsefulFlowText.setVisibility(View.VISIBLE);
            boolean hasImage = false;
            for (View view : views) {
                if (view instanceof ImageView) {
                    hasImage = true;
                }
                holder.llFlowText.addView(view);
            }
            if (hasImage) {
                holder.llFlowText.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.llFlowText.setMinimumWidth(MoorPixelUtil.dp2px(200f));
                    }
                });
            }

        } else {
            holder.slUsefulFlowText.setVisibility(View.GONE);
        }

        String flowTips = item.getFlowTips();
        if (!TextUtils.isEmpty(flowTips)) {
            Spanned spanned;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                spanned = Html.fromHtml(flowTips, Html.FROM_HTML_MODE_COMPACT);
            } else {
                spanned = Html.fromHtml(flowTips);
            }
            CharSequence charSequence = MoorTextParseUtil.trimTrailingWhitespace(spanned);

            holder.tvFlowTips.setVisibility(View.VISIBLE);

            holder.tvFlowTips.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
            holder.tvFlowTips.getPaint().setStrokeWidth(0.7f);
            holder.tvFlowTips.setText(charSequence);
        } else {
            holder.tvFlowTips.setVisibility(View.GONE);
        }
        List<MoorFlowListBean> flowList = new Gson().fromJson(item.getFlowlistJson(), new TypeToken<List<MoorFlowListBean>>() {
        }.getType());
        if (flowList != null && flowList.size() > 0) {
            holder.viewUseful.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.65));
            holder.viewUseful.setMinWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.5));

            holder.tvFlowTips.setMaxWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.65));
            holder.tvFlowTips.setMinWidth((int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.5));
            holder.rlRobotFlowlist.setVisibility(View.VISIBLE);
            MoorFlowTextAdapter adapter = new MoorFlowTextAdapter(flowList);
            adapter.setOnItemClickListener(new MoorFlowTextAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, MoorFlowListBean flowBean) {
                    getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_FLOW_LIST_TEXT.setObj(flowBean));
                }
            });
            holder.rlRobotFlowlist.setAdapter(adapter);
        } else {
            holder.rlRobotFlowlist.setVisibility(View.GONE);
        }
        if (holder.tvFlowTips.getVisibility() == View.GONE && holder.rlRobotFlowlist.getVisibility() == View.GONE) {
            holder.slUsefulFlow.setVisibility(View.GONE);
            MoorShadowLayout.LayoutParams layoutParams = (MoorShadowLayout.LayoutParams) holder.llFlowText.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            holder.llFlowText.setLayoutParams(layoutParams);

        } else {
            holder.slUsefulFlow.setVisibility(View.VISIBLE);
            MoorShadowLayout.LayoutParams layoutParams = (MoorShadowLayout.LayoutParams) holder.llFlowText.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, MoorPixelUtil.dp2px(10f));
            holder.llFlowText.setLayoutParams(layoutParams);
        }

        if (holder.getParentView() != null) {
            holder.getParentView().removeAllViews();
            holder.getParentView().addView(holder.root);
        }
        if (!MoorInfoDao.getInstance().queryInfo().getSessionId().equals(item.getSessionId())) {
            //会话id不同或者是历史消息：隐藏点赞点踩
            holder.llUseful.setVisibility(View.GONE);
            holder.tvFeedBack.setVisibility(View.GONE);
        } else {
            holder.llUseful.setVisibility(View.VISIBLE);
            String robotFeedBack = item.getRobotFeedBack();
            if (MoorDemoConstants.MOOR_ROBOT_USEFUL.equals(robotFeedBack)) {
                holder.ivUseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_uesful_selected));
                holder.ivUnuseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_unuesful));
                holder.ivUseful.setClickable(false);
                holder.ivUnuseful.setClickable(false);
                holder.tvFeedBack.setText(item.getFingerUp());
                holder.tvFeedBack.setVisibility(View.VISIBLE);
            } else if (MoorDemoConstants.MOOR_ROBOT_UNUSEFUL.equals(robotFeedBack)) {
                holder.ivUseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_uesful));
                holder.ivUnuseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_unuesful_selected));
                holder.ivUseful.setClickable(false);
                holder.ivUnuseful.setClickable(false);
                holder.tvFeedBack.setText(item.getFingerDown());
                holder.tvFeedBack.setVisibility(View.VISIBLE);
            } else {
                holder.ivUseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_uesful));
                holder.ivUnuseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_unuesful));
                holder.ivUseful.setClickable(true);
                holder.ivUnuseful.setClickable(true);
                holder.tvFeedBack.setVisibility(View.GONE);
                holder.ivUseful.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBinderClickListener().onClick(holder.ivUnuseful, item, MoorEnumChatItemClickType.TYPE_USEFUL.setObj(true));
                    }
                });
                holder.ivUnuseful.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBinderClickListener().onClick(holder.ivUnuseful, item, MoorEnumChatItemClickType.TYPE_USEFUL.setObj(false));

                    }
                });
            }
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull final MoorMsgBean item, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (MoorDemoConstants.MOOR_PAYLOAD_USEFUL.equals(payloads.get(0))) {
                if (!MoorInfoDao.getInstance().queryInfo().getSessionId().equals(item.getSessionId())) {
                    //会话id不同或者是历史消息：隐藏点赞点踩
                    holder.llUseful.setVisibility(View.GONE);
                    holder.tvFeedBack.setVisibility(View.GONE);
                } else {
                    holder.llUseful.setVisibility(View.VISIBLE);
                    String robotFeedBack = item.getRobotFeedBack();
                    if (MoorDemoConstants.MOOR_ROBOT_USEFUL.equals(robotFeedBack)) {
                        holder.ivUseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_uesful_selected));
                        holder.ivUnuseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_unuesful));
                        holder.ivUseful.setClickable(false);
                        holder.ivUnuseful.setClickable(false);
                        holder.tvFeedBack.setText(item.getFingerUp());
                        holder.tvFeedBack.setVisibility(View.VISIBLE);
                    } else if (MoorDemoConstants.MOOR_ROBOT_UNUSEFUL.equals(robotFeedBack)) {
                        holder.ivUseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_uesful));
                        holder.ivUnuseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_unuesful_selected));
                        holder.ivUseful.setClickable(false);
                        holder.ivUnuseful.setClickable(false);
                        holder.tvFeedBack.setText(item.getFingerDown());
                        holder.tvFeedBack.setVisibility(View.VISIBLE);
                    } else {
                        holder.ivUseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_uesful));
                        holder.ivUnuseful.setImageDrawable(holder.llUsefulRoot.getContext().getResources().getDrawable(R.drawable.moor_unuesful));
                        holder.ivUseful.setClickable(true);
                        holder.ivUnuseful.setClickable(true);
                        holder.tvFeedBack.setVisibility(View.GONE);
                    }
                }
            } else if (MoorDemoConstants.MOOR_PAYLOAD_HIDE.equals(payloads.get(0))) {
                //会话id不同或者是历史消息：隐藏点赞点踩
                holder.llUseful.setVisibility(View.GONE);
                holder.tvFeedBack.setVisibility(View.GONE);

            }

        }
    }

    static class ViewHolder extends MoorBaseReceivedHolder {

        LinearLayout llUsefulRoot;
        LinearLayout llBottomContentMargin;
        MoorShadowLayout slUsefulFeedBack;
        TextView tvFeedBack;
        LinearLayout llUseful;
        LinearLayout llFlowText;
        RecyclerView rlRobotFlowlist;
        ImageView ivUseful;
        ImageView ivUnuseful;
        TextView tvFlowTips;
        TextView viewUseful;
        MoorShadowLayout slUsefulFlowText;
        MoorShadowLayout slUsefulFlow;
        View root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.llUsefulRoot = (LinearLayout) itemView.findViewById(R.id.ll_useful_root);
        }

        public LinearLayout getParentView() {
            MoorBaseReceivedViewBinder.ViewHolder parent = getParent();
            if (parent.llBottomContentMargin != null) {
                llBottomContentMargin = parent.llBottomContentMargin;
            }
            return llBottomContentMargin;
        }

        public void creatView() {
            root = LayoutInflater.from(llUsefulRoot.getContext()).inflate(R.layout.moor_layout_useful, llBottomContentMargin, false);

            slUsefulFeedBack = root.findViewById(R.id.sl_useful_feed_back);
            tvFeedBack = root.findViewById(R.id.tv_feed_back);
            llUseful = root.findViewById(R.id.ll_useful);
            llFlowText = root.findViewById(R.id.ll_flow_list_text);
            rlRobotFlowlist = root.findViewById(R.id.rl_robot_useful);
            ivUseful = root.findViewById(R.id.iv_useful);
            ivUnuseful = root.findViewById(R.id.iv_unuseful);
            tvFlowTips = root.findViewById(R.id.tv_flow_tips);
            viewUseful = root.findViewById(R.id.view_useful);
            slUsefulFlowText = root.findViewById(R.id.sl_useful_flow_text);
            slUsefulFlow = root.findViewById(R.id.sl_useful_flow);
        }
    }


}
