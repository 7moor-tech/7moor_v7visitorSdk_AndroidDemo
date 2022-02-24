package com.moor.imkf.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.multirow.MoorXbotTabQuestionViewBinder;

import java.util.ArrayList;
/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/10/21
 *     @desc   : 常见问题-分组式列表
 *     @version: 1.0
 * </pre>
 */
public class MoorXbotTabQuestionAdapter extends RecyclerView.Adapter<MoorXbotTabQuestionAdapter.TagViewHolder> {
    private final ArrayList<String> datas;
    private final MoorXbotTabQuestionViewBinder viewBinder;

    public MoorXbotTabQuestionAdapter(ArrayList<String> datas,  MoorXbotTabQuestionViewBinder viewBinder) {
        this.datas = datas;
        this.viewBinder = viewBinder;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_chat_tab_question_item, parent, false);
        return new TagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TagViewHolder holder, final int position) {
        holder.tvTextView.setText(datas.get(position));
        holder.tvTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewBinder != null && viewBinder.getBinderClickListener() != null) {
                    viewBinder.getBinderClickListener().onClick(v, null, MoorEnumChatItemClickType.TYPE_XBOT_TABQUESTION_ITEM.setObj(datas.get(holder.getAdapterPosition())));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tvTextView;

        public TagViewHolder(View itemView) {
            super(itemView);
            tvTextView = itemView.findViewById(R.id.tv_question);
        }
    }

}
