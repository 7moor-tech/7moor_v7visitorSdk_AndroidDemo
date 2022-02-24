package com.moor.imkf.demo.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorBottomBtnBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 5/26/21
 *     @desc   : 聊天页面底部的横向滚动推荐条
 *     @version: 1.0
 * </pre>
 */
public class MoorChatBottomLabelsAdapter extends RecyclerView.Adapter<MoorChatBottomLabelsAdapter.ChatTagViewHolder> {
    List<MoorBottomBtnBean> datas;
    MoorOptions options;

    public MoorChatBottomLabelsAdapter(List<MoorBottomBtnBean> datas) {
        this.datas = datas;
        options = (MoorOptions) MoorManager.getInstance().getOptions();
        if (options == null) {
            options = new MoorOptions();
        }
    }

    public void refreshData(List<MoorBottomBtnBean> datas) {
        this.datas.clear();
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public ChatTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_chat_tag_label, parent, false);
        return new ChatTagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChatTagViewHolder holder, final int position) {
        holder.slTag.setStrokeColor(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
        holder.tvFlowItem.setText(datas.get(position).getButton());
        holder.slTag.setOnClickListener(new View.OnClickListener() {
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
        MoorShadowLayout slTag;

        public ChatTagViewHolder(View itemView) {
            super(itemView);
            slTag = itemView.findViewById(R.id.sl_tag);
            tvFlowItem = itemView.findViewById(R.id.tv_flowItem);
        }
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(MoorBottomBtnBean flowBean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
