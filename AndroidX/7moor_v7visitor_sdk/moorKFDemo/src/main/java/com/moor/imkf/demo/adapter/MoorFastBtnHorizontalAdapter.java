package com.moor.imkf.demo.adapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorFastBtnBean;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.ArrayList;


public class MoorFastBtnHorizontalAdapter extends RecyclerView.Adapter<MoorFastBtnHorizontalAdapter.FastViewHolder> {
    ArrayList<MoorFastBtnBean> datas;

    public MoorFastBtnHorizontalAdapter(ArrayList<MoorFastBtnBean> datas) {
        this.datas = datas;
    }


    @Override
    public FastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_fast_btn, parent, false);
        return new FastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FastViewHolder holder, final int position) {
        holder.tv_fast_text.setText(datas.get(position).getShowName());
        holder.sl_fast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(holder.sl_fast_btn, datas.get(holder.getAdapterPosition()));
                }
            }
        });

        MoorManager.getInstance().loadImage(datas.get(position).getImgUrl(),holder.iv_fast_btn, MoorPixelUtil.dp2px(60f),MoorPixelUtil.dp2px(60f));

        if(!TextUtils.isEmpty(datas.get(position).getBgColor())){
            holder.sl_fast_btn.setLayoutBackground(Color.parseColor(datas.get(position).getBgColor()));
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    static class FastViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fast_text;
        MoorShadowLayout sl_fast_btn;
        ImageView iv_fast_btn;

        public FastViewHolder(View itemView) {
            super(itemView);
            sl_fast_btn = itemView.findViewById(R.id.sl_fast_btn);
            tv_fast_text = itemView.findViewById(R.id.tv_fast_text);
            iv_fast_btn=itemView.findViewById(R.id.iv_fast_btn);
        }
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnItemClick(View v, MoorFastBtnBean fastBtnBean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
