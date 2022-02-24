package com.moor.imkf.demo.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 5/26/21
 *     @desc   : flowList:单选:  两列
 *     @version: 1.0
 * </pre>
 */
public class MoorFlowTwoAdapter extends RecyclerView.Adapter<MoorFlowTwoAdapter.FlowListSingleVerticalHolder> {
    List<MoorFlowListBean> list;

    public MoorFlowTwoAdapter(List<MoorFlowListBean> list) {
        this.list = list;
    }

    @Override
    public FlowListSingleVerticalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_flow_two, parent, false);
        return new FlowListSingleVerticalHolder(v);
    }

    @Override
    public void onBindViewHolder(final FlowListSingleVerticalHolder holder, final int position) {
        holder.tvFlowText.setText(list.get(position).getButton());
        MoorOptions options = (MoorOptions) MoorManager.getInstance().getOptions();
        holder.slSingleVer.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f,options.getSdkMainThemeColor()));
        holder.slSingleVer.setLayoutBackgroundTrue(MoorColorUtils.getColorWithAlpha(1f,options.getSdkMainThemeColor()));

        holder.slSingleVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.slSingleVer, list.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class FlowListSingleVerticalHolder extends RecyclerView.ViewHolder {
        TextView tvFlowText;
        MoorShadowLayout slSingleVer;


        public FlowListSingleVerticalHolder(View itemView) {
            super(itemView);
            slSingleVer = itemView.findViewById(R.id.sl_single_ver);
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
