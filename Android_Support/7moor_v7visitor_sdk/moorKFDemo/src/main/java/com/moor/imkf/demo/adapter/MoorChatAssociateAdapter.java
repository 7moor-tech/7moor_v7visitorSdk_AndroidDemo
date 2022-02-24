package com.moor.imkf.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorRegexUtils;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 5/24/21
 *     @desc   : 聊天页面 关键词联想 适配器
 *     @version: 1.0
 * </pre>
 */
public class MoorChatAssociateAdapter extends RecyclerView.Adapter<MoorChatAssociateAdapter.ChatTagViewHolder> {
    private List<String> datas;
    /**
     * 当前文本框的数据
     */
    private final String matcherKey;
    private final Context mContext;

    public MoorChatAssociateAdapter(Context context, String key, List<String> datas) {
        this.datas = datas;
        this.matcherKey = key;
        this.mContext = context;
    }

    public void refreshData(List<String> datas) {
        this.datas.clear();
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public ChatTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_associate, parent, false);
        return new ChatTagViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ChatTagViewHolder holder, final int position) {
        if (TextUtils.isEmpty(matcherKey)) {
            holder.tvFlowItem.setText(datas.get(position));
        } else {
            holder.tvFlowItem.setText(MoorRegexUtils.matchSearchText(mContext.getResources().getColor(R.color.moor_color_f8624f), datas.get(position), matcherKey));
        }
        holder.tvFlowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(datas.get(holder.getAdapterPosition()));
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

        public ChatTagViewHolder(View itemView) {
            super(itemView);
            tvFlowItem = itemView.findViewById(R.id.tv_asscociate);
        }
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String flowBean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
