package com.moor.imkf.demo.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/7/21
 *     @desc   : flowList:单选:  一列横向
 *     @version: 1.0
 * </pre>
 */
public class MoorFlowSingleHorizontalAdapter extends RecyclerView.Adapter<MoorFlowSingleHorizontalAdapter.ChatTagViewHolder> {
    List<MoorFlowListBean> datas;

    public MoorFlowSingleHorizontalAdapter(List<MoorFlowListBean> datas) {
        this.datas = datas;
    }


    @Override
    public ChatTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_chat_tag_label, parent, false);
        return new ChatTagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChatTagViewHolder holder, final int position) {
        holder.tvFlowItem.setText(datas.get(position).getButton());
        holder.slTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(holder.slTag, datas.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    static class ChatTagViewHolder extends RecyclerView.ViewHolder {
        TextView tvFlowItem;
        MoorShadowLayout slTag;

        public ChatTagViewHolder(View itemView) {
            super(itemView);
            slTag = itemView.findViewById(R.id.sl_tag);
            tvFlowItem = itemView.findViewById(R.id.tv_flowItem);
        }
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnItemClick(View v, MoorFlowListBean flowBean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
