package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorFlowSingleHorizontalAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.MoorTextParseUtil;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.MoorSpaceItemDecoration;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;

import java.util.List;

/**
 * `
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : flowList:单选:  一列横向
 *     @version: 1.0
 * </pre>
 */
public class MoorFlowListSingleHorizontalViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorFlowListSingleHorizontalViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorFlowListSingleHorizontalViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_flowlist_single_horizontal, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull final ViewHolder holder, @NonNull final MoorMsgBean item) {
        List<View> views = MoorTextParseUtil.getInstance().parseFlow(item);
        holder.llFlowText.removeAllViews();
        if (views.size()>0) {
            for (View view : views) {
                holder.llFlowText.addView(view);
            }
        }
        holder.getParent().chattingTvName.post(new Runnable() {

            @Override
            public void run() {
                int width = holder.getParent().chattingTvName.getWidth();
                holder.llFlowText.setMinimumWidth(width);
            }
        });
        List<MoorFlowListBean> flowList = new Gson().fromJson(item.getFlowlistJson(), new TypeToken<List<MoorFlowListBean>>() {
        }.getType());
        MoorFlowSingleHorizontalAdapter adapter = new MoorFlowSingleHorizontalAdapter(flowList);
        adapter.setOnItemClickListener(new MoorFlowSingleHorizontalAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, MoorFlowListBean flowBean) {
                getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_FLOW_LIST.setObj(flowBean));
            }
        });
        RecyclerView rlRobotFlowlist = new RecyclerView(holder.llFlowText.getContext());
        rlRobotFlowlist.setPadding(0, MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f));
        rlRobotFlowlist.setLayoutManager(new LinearLayoutManager(holder.llFlowText.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rlRobotFlowlist.addItemDecoration(new MoorSpaceItemDecoration(0, 0, MoorPixelUtil.dp2px(10), 0));
        rlRobotFlowlist.setAdapter(adapter);
        if (holder.getParentView() != null) {
            holder.getParentView().removeAllViews();
            holder.getParentView().addView(rlRobotFlowlist);
        }
    }

    static class ViewHolder extends MoorBaseReceivedHolder {

        LinearLayout llFlowText;
        LinearLayout llBottomContentMatch;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.llFlowText = (LinearLayout) itemView.findViewById(R.id.ll_flow_list_text);

        }

        public LinearLayout getParentView() {
            MoorBaseReceivedViewBinder.ViewHolder parent = getParent();
            if (parent.llBottomContentMatch != null) {
                llBottomContentMatch = parent.llBottomContentMatch;
            }
            return llBottomContentMatch;
        }
    }


}
