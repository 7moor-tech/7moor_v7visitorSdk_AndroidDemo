/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-09-18 23:47:01
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.moor.imkf.demo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorDrawableUtils;
import com.moor.imkf.demo.utils.MoorRegexUtils;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.bean.MoorOrderInfoBean;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.List;

/**
 * @author R-D
 * @Description 完整物流进度的adapter
 * @date 2020-01-06
 */
public class MoorLogisticsProgressListAdapter extends RecyclerView.Adapter<MoorLogisticsProgressListAdapter.LogisticsProgressHolder> {
    public List<MoorOrderInfoBean> data;
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL = 0x0001;
    private static final int TYPE_BOTTOM = 0x0002;
    private Context context;
    private MoorOptions moorOptions;
    private GradientDrawable startDrawable, stopDrawable;
    /*
    区分是哪里展示的数据；true：点击查看完整物流；false：会话中的物流进度
     */
    private boolean isFormMoreProgress;

    public MoorLogisticsProgressListAdapter(List<MoorOrderInfoBean> list, boolean isFormMoreProgress) {
        this.data = list;
        this.isFormMoreProgress = isFormMoreProgress;
        moorOptions = MoorManager.getInstance().getOptions();
        if (!TextUtils.isEmpty(moorOptions.getSdkMainThemeColor())) {
            startDrawable = MoorDrawableUtils.getCircleShape(0, 0, Color.TRANSPARENT, MoorColorUtils.getColorWithAlpha(0.5f, moorOptions.getSdkMainThemeColor()));
            stopDrawable = MoorDrawableUtils.getCircleShape(0, 0, Color.TRANSPARENT, Color.parseColor(moorOptions.getSdkMainThemeColor()));
        }
    }

    @NonNull
    @Override
    public LogisticsProgressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moor_item_logistics_progress, parent, false);
        return new LogisticsProgressHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LogisticsProgressHolder holder, final int position) {
        if (data != null && data.size() > 0) {
            if (getItemViewType(position) == TYPE_TOP) {
                // 第一行上面的竖线不显示
                holder.view_topline.setVisibility(View.INVISIBLE);
                holder.view_bottomline.setVisibility(View.VISIBLE);
                //数据个数是1时，显示浅色，否则显示主题色
                if (startDrawable != null && stopDrawable != null) {
                    holder.view_dot.setBackgroundDrawable(data.size() == 1 ? startDrawable : stopDrawable);
                } else {
                    holder.view_dot.setBackgroundResource(data.size() == 1 ? R.drawable.moor_timelline_dot_bottom : R.drawable.moor_timelline_dot_first);
                }
            } else if (getItemViewType(position) == TYPE_NORMAL) {
                holder.view_topline.setVisibility(View.VISIBLE);
                holder.view_bottomline.setVisibility(View.VISIBLE);
                holder.view_dot.setBackgroundResource(R.drawable.moor_timelline_dot_normal);
            } else {
                if (isFormMoreProgress) {
                    holder.view_bottomline.setVisibility(View.INVISIBLE);
                    holder.view_dot.setBackgroundDrawable(startDrawable);
                } else {
                    //最后一行，下面的竖线不显示
                    if (data.size() > 3) {
                        holder.view_bottomline.setVisibility(View.VISIBLE);
                        holder.view_dot.setBackgroundResource(R.drawable.moor_timelline_dot_normal);

                    } else {
                        holder.view_bottomline.setVisibility(View.INVISIBLE);
                    }
                }

            }

            final MoorOrderInfoBean progressBean = data.get(position);
            holder.tvAcceptTime.setText(progressBean.getDesc());
            SpannableStringBuilder builder = new SpannableStringBuilder(progressBean.getTitle());

            holder.tvAcceptStation.setText(MoorRegexUtils.setPhoneNum(builder, moorOptions.getSdkMainThemeColor()));

        }

    }

    @Override
    public int getItemCount() {
        if (isFormMoreProgress) {
            return data.size();
        } else {
            if (data.size() > 3) {
                return 3;
            } else {
                return data.size();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        } else if (position == data.size() - 1)
            return TYPE_BOTTOM;
        else {
            return TYPE_NORMAL;
        }
    }

    public class LogisticsProgressHolder extends RecyclerView.ViewHolder {
        public TextView tvAcceptStation;
        public View view_topline;
        public View view_bottomline;
        public TextView tvAcceptTime;
        public View view_dot;

        public LogisticsProgressHolder(View itemView) {
            super(itemView);
            tvAcceptStation = (TextView) itemView.findViewById(R.id.tvAcceptStation);
            view_topline = itemView.findViewById(R.id.view_topline);
            view_bottomline = itemView.findViewById(R.id.view_bottomline);
            tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
            view_dot = itemView.findViewById(R.id.view_dot);
        }
    }

}
