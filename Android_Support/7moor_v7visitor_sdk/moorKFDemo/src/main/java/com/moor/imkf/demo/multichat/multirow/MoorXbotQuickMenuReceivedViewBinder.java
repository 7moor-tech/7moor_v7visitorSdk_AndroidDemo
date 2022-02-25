package com.moor.imkf.demo.multichat.multirow;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorXbotQuickMenuHorizontalAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.MoorSpaceItemDecoration;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.bean.MoorQuickMenuBean;

import java.util.ArrayList;

public class MoorXbotQuickMenuReceivedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorXbotQuickMenuReceivedViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorXbotQuickMenuReceivedViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_fastbtn_recevied, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull final MoorMsgBean item) {
        MoorBaseReceivedViewBinder.ViewHolder parent_holder = holder.getParent();
        gone_baseView(parent_holder);

        ArrayList<MoorQuickMenuBean> fastList = new Gson().fromJson(item.getQuickMenu(), new TypeToken<ArrayList<MoorQuickMenuBean>>() {
        }.getType());


        MoorXbotQuickMenuHorizontalAdapter adapter = new MoorXbotQuickMenuHorizontalAdapter(fastList);

        adapter.setOnItemClickListener(new MoorXbotQuickMenuHorizontalAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, MoorQuickMenuBean quickMenuBean) {
                getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_XBOT_QUICKMENU_CLICK.setObj(quickMenuBean));
            }
        });

        RecyclerView rlRobotFlowlist = new RecyclerView(holder.ll_fast.getContext());
        rlRobotFlowlist.setPadding(0,0,MoorPixelUtil.dp2px(5f),0);
        rlRobotFlowlist.setLayoutManager(new LinearLayoutManager(holder.ll_fast.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rlRobotFlowlist.addItemDecoration(new MoorSpaceItemDecoration(MoorPixelUtil.dp2px(12f), 0, 0, 0));
        rlRobotFlowlist.setAdapter(adapter);

        if (holder.getParent().llBottomContentMatch != null) {
            holder.getParent().llBottomContentMatch.removeAllViews();
            holder.getParent().llBottomContentMatch.addView(rlRobotFlowlist);
        }
    }

    /*隐藏左侧头像，名称等ui */
    private void gone_baseView(MoorBaseReceivedViewBinder.ViewHolder parent_holder) {
        if (parent_holder != null) {
            if (parent_holder.ivChatAvatar != null) {
                parent_holder.ivChatAvatar.setVisibility(View.GONE);
            }
            if (parent_holder.chattingTvName != null) {
                parent_holder.chattingTvName.setVisibility(View.GONE);
            }
        }
    }

    static class ViewHolder extends MoorBaseReceivedHolder {

        LinearLayout ll_fast;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_fast = itemView.findViewById(R.id.ll_fast);
        }
    }
}
