package com.moor.imkf.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 5/26/21
 *     @desc   : FlowList 文本
 *     @version: 1.0
 * </pre>
 */
public class MoorFlowTextAdapter extends RecyclerView.Adapter<MoorFlowTextAdapter.FlowListTextHolder> {
    List<MoorFlowListBean> list;

    public MoorFlowTextAdapter(List<MoorFlowListBean> list) {
        this.list = list;
    }

    @Override
    public FlowListTextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_flow_text, parent, false);
        return new FlowListTextHolder(v);
    }

    @Override
    public void onBindViewHolder(final FlowListTextHolder holder, final int position) {
        String text = holder.getAdapterPosition() + 1 + ". " + list.get(holder.getAdapterPosition()).getButton();
        holder.tvFlowText.setText(text);
        SpannableString spannableString = new SpannableString(text);
        MoorOptions options = (MoorOptions) MoorManager.getInstance().getOptions();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(MoorColorUtils.getColorWithAlpha(1f,options.getSdkMainThemeColor()));
        spannableString.setSpan(colorSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tvFlowText.setText(spannableString);
        holder.tvFlowText.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvFlowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.tvFlowText, list.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class FlowListTextHolder extends RecyclerView.ViewHolder {
        TextView tvFlowText;

        public FlowListTextHolder(View itemView) {
            super(itemView);
            tvFlowText = itemView.findViewById(R.id.tv_flow_text);
        }
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View v, MoorFlowListBean flowBean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
