package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorFlowTextAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.MoorTextParseUtil;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;

import java.util.List;

/**
 * `
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : FlowList 文本
 *     @version: 1.0
 * </pre>
 */
public class MoorFlowListTextViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorFlowListTextViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorFlowListTextViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_flowlist_text, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull final ViewHolder holder, @NonNull final MoorMsgBean item) {
        List<View> views = MoorTextParseUtil.getInstance().parseFlow(item);
        holder.llFlowText.removeAllViews();
        if (views.size() > 0) {
            for (View view : views) {
                holder.llFlowText.addView(view);
            }
        } else {
        }
        holder.sl_flow_list_text.setVisibility(views.size() == 0 ? View.GONE : View.VISIBLE);
        holder.getParent().chattingTvName.post(new Runnable() {

            @Override
            public void run() {
                int width = holder.getParent().chattingTvName.getWidth();
                holder.llFlowTips.setMinimumWidth(width);
            }
        });
        if (options != null) {
            String leftMsgBgColor = options.getLeftMsgBgColor();
            if (!TextUtils.isEmpty(leftMsgBgColor)) {
                holder.sl_flow_list_text.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
                holder.sl_flow_tips.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
                holder.sl_flow_list.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
            }
        }
        String flowTips = item.getFlowTips();
        if (!TextUtils.isEmpty(flowTips)) {
            StringBuilder builder = new StringBuilder();
            builder.append(flowTips);
            List<View> flowViews = MoorTextParseUtil.getInstance().parseFlowText(builder);
            holder.llFlowTips.removeAllViews();
            if (flowViews.size() > 0) {
                holder.llFlowTips.setVisibility(View.VISIBLE);
                for (View view : flowViews) {
                    holder.llFlowTips.addView(view);
                }
            } else {
                holder.llFlowTips.setVisibility(View.GONE);
            }
        } else {
            holder.llFlowTips.setVisibility(View.GONE);
        }
        List<MoorFlowListBean> flowList = new Gson().fromJson(item.getFlowlistJson(), new TypeToken<List<MoorFlowListBean>>() {
        }.getType());
        if (flowList != null && flowList.size() > 0) {
            holder.sl_flow_list.setVisibility(View.VISIBLE);

            MoorFlowTextAdapter adapter = new MoorFlowTextAdapter(flowList);
            adapter.setOnItemClickListener(new MoorFlowTextAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, MoorFlowListBean flowBean) {
                    getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_FLOW_LIST_TEXT.setObj(flowBean));
                }
            });
            holder.rlRobotFlowlist.setAdapter(adapter);

        } else {
            holder.sl_flow_list.setVisibility(View.GONE);
        }
        if (holder.llFlowTips.getVisibility() == View.GONE && holder.sl_flow_list.getVisibility() == View.GONE) {
            holder.sl_flow_tips.setVisibility(View.GONE);
        } else {
            holder.sl_flow_tips.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item, @NonNull List<Object> payloads) {

    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {

    }

    static class ViewHolder extends MoorBaseReceivedHolder {

        LinearLayout llFlowText;
        LinearLayout llFlowTips;
        RecyclerView rlRobotFlowlist;
        MoorShadowLayout sl_flow_tips;
        MoorShadowLayout sl_flow_list_text;
        MoorShadowLayout sl_flow_list;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.llFlowText = (LinearLayout) itemView.findViewById(R.id.ll_flow_list_text);
            this.llFlowTips = (LinearLayout) itemView.findViewById(R.id.ll_flow_tips);
            this.rlRobotFlowlist = (RecyclerView) itemView.findViewById(R.id.rl_robot_flowlist);
            this.sl_flow_list_text = (MoorShadowLayout) itemView.findViewById(R.id.sl_flow_list_text);
            this.sl_flow_tips = (MoorShadowLayout) itemView.findViewById(R.id.sl_flow_tips);
            this.sl_flow_list = (MoorShadowLayout) itemView.findViewById(R.id.sl_flow_list);
        }
    }


}
