package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorFlowSingleVerticalAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.MoorTextParseUtil;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.pagergrid.MoorPagerGridLayoutManager;
import com.moor.imkf.demo.view.pagergrid.MoorPagerGridSnapHelper;
import com.moor.imkf.demo.view.pagergrid.MoorPointBottomView;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;

import java.util.List;

/**
 * `
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : flowList:单选:  一列竖向
 *     @version: 1.0
 * </pre>
 */
public class MoorFlowListSingleVerticalViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorFlowListSingleVerticalViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorFlowListSingleVerticalViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_flowlist_single_vertical, parent, false);
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
        }
        holder.llFlowText.setVisibility(views.size() == 0 ? View.GONE : View.VISIBLE);
        holder.getParent().chattingTvName.post(new Runnable() {

            @Override
            public void run() {
                int width = holder.getParent().chattingTvName.getWidth();
                holder.llFlowText.setMinimumWidth(width);
            }
        });
        final List<MoorFlowListBean> flowList = new Gson().fromJson(item.getFlowlistJson(), new TypeToken<List<MoorFlowListBean>>() {
        }.getType());
        MoorFlowSingleVerticalAdapter adapter = new MoorFlowSingleVerticalAdapter(flowList);
        adapter.setOnItemClickListener(new MoorFlowSingleVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, MoorFlowListBean flowBean) {
                getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_FLOW_LIST.setObj(flowBean));
            }
        });

        int size = 4;
        if (flowList.size() < 4) {
            size = flowList.size();
        }

        int rows = size;
        int columns = 1;
        int overSize = rows * columns;
        MoorPagerGridLayoutManager layoutManager = new MoorPagerGridLayoutManager(
                rows, columns, MoorPagerGridLayoutManager.HORIZONTAL);
        holder.rlRobotFlowlist.setLayoutManager(layoutManager);

        MoorPagerGridSnapHelper pageSnapHelper = new MoorPagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(holder.rlRobotFlowlist);
        holder.rlRobotFlowlist.setAdapter(adapter);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(holder.rlRobotFlowlist.getLayoutParams());
        params.height = MoorPixelUtil.dp2px(45f * size);
        holder.rlRobotFlowlist.setLayoutParams(params);

        holder.pbvFlow.setFillColor(MoorColorUtils.getColorWithAlpha(1f,options.getSdkMainThemeColor()));
        int flowSize = flowList.size() / overSize;
        if (flowList.size() % overSize > 0) {
            flowSize = flowSize + 1;
        }
        holder.pbvFlow.initData(flowSize, 0);
        holder.pbvFlow.setVisibility(flowSize == 1 ? View.GONE : View.VISIBLE);
        layoutManager.setPageListener(new MoorPagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                holder.pbvFlow.setCurrentPage(pageIndex);
            }
        });
    }

    static class ViewHolder extends MoorBaseReceivedHolder {

        LinearLayout llFlowText;
        LinearLayout llFlow;
        MoorPointBottomView pbvFlow;
        RecyclerView rlRobotFlowlist;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pbvFlow = (MoorPointBottomView) itemView.findViewById(R.id.pbv_flow);
            this.llFlow = (LinearLayout) itemView.findViewById(R.id.ll_flow);
            this.llFlowText = (LinearLayout) itemView.findViewById(R.id.ll_flow_list_text);
            this.rlRobotFlowlist = (RecyclerView) itemView.findViewById(R.id.rl_robot_flowlist);
        }
    }


}
