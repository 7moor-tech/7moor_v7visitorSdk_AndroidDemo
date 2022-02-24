package com.moor.imkf.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;

import java.util.List;

//分组常见问题查看更多 适配器
public class MoorChatTagQuestionMoreAdapter extends RecyclerView.Adapter<MoorChatTagQuestionMoreAdapter.ChatTagViewHolder> {
    List<String> datas;

    public MoorChatTagQuestionMoreAdapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public ChatTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_question_more, parent, false);
        return new ChatTagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChatTagViewHolder holder, final int position) {
        holder.tvFlowItem.setText(datas.get(position));
        holder.tvFlowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(datas.get(holder.getAdapterPosition()));
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
            tvFlowItem = itemView.findViewById(R.id.tv_question);
        }
    }

    private onItemClickListener mListener;

    public interface onItemClickListener {
        void OnItemClick(String s);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }
}
