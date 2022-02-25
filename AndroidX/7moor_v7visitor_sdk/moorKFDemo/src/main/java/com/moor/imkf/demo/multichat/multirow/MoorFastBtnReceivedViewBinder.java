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
import com.moor.imkf.demo.adapter.MoorFastBtnHorizontalAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.MoorSpaceItemDecoration;
import com.moor.imkf.moorsdk.bean.MoorFastBtnBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;

import java.util.ArrayList;

public class MoorFastBtnReceivedViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorFastBtnReceivedViewBinder.ViewHolder> {
    private final MoorOptions options;

    public MoorFastBtnReceivedViewBinder(MoorOptions options) {
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

        ArrayList<MoorFastBtnBean> fastList = new Gson().fromJson(item.getContent(), new TypeToken<ArrayList<MoorFastBtnBean>>() {
        }.getType());


        MoorFastBtnHorizontalAdapter adapter = new MoorFastBtnHorizontalAdapter(fastList);

        adapter.setOnItemClickListener(new MoorFastBtnHorizontalAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, MoorFastBtnBean fastBtnBean) {
                getBinderClickListener().onClick(v, item, MoorEnumChatItemClickType.TYPE_FAST_BTN_LIST.setObj(fastBtnBean));
            }
        });


        RecyclerView rlRobotFlowlist = new RecyclerView(holder.ll_fast.getContext());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0,0,-MoorPixelUtil.dp2px(5f),0);
//        rlRobotFlowlist.setLayoutParams(params);
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
