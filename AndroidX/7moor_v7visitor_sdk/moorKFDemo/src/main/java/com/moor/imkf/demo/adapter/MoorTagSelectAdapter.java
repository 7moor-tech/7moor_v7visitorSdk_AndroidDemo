package com.moor.imkf.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.bean.MoorEvaluation;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/05/15
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorTagSelectAdapter extends RecyclerView.Adapter<MoorTagSelectAdapter.MoorTagViewMoreHolder> {
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_MORE = 1;
    private final Context mContext;
    private List<MoorEvaluation> evaluationList = new ArrayList<>();
    private final Set<MoorEvaluation> evaluationSet = new LinkedHashSet<>();
    private final int type;
    private int selectedPosition = -1;
    private OnSelectedChangeListener onSelectedChangeListener;
    private ArrayList<MoorEvaluation> selectList = new ArrayList<MoorEvaluation>();
    private MoorOptions optionsUI;

    public MoorTagSelectAdapter(Context mActivity, List<MoorEvaluation> evaluationList, int type) {
        this.mContext = mActivity;
        this.evaluationList = evaluationList;
        this.type = type;
        optionsUI = (MoorOptions) MoorManager.getInstance().getOptions();
    }

    @Override
    public MoorTagViewMoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoorTagViewMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.moor_item_flowlayout, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final MoorTagViewMoreHolder holder, final int position) {
        final MoorEvaluation option = evaluationList.get(position);
        holder.tvTitle.setText(option.name);

        if (option.isSelected) {
            evaluationSet.add(option);
        }
        holder.slTag.setSelected(option.isSelected);

        holder.slTag.setTextColor_true(MoorColorUtils.getColorWithAlpha(1f,optionsUI.getSdkMainThemeColor()));
        holder.slTag.setLayoutBackgroundTrue(MoorColorUtils.getColorWithAlpha(0.1f, optionsUI.getSdkMainThemeColor()));

        //单选
        if (type == TYPE_SINGLE) {
            holder.tvTitle.setTag(position);
            evaluationSet.clear();
            evaluationSet.add(option);

            holder.slTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //拿到正确的position
                    int rightPosition = (int) holder.tvTitle.getTag();
                    MoorEvaluation bean = evaluationList.get(rightPosition);
                    //点击的是选中的
                    if (bean.isSelected) {
                        bean.setSelected(false);
                        notifyItemChanged(rightPosition, bean);
                        selectedPosition = -1;
                    } else {
                        //点击的如果是没有选中的
                        if (selectedPosition != -1) {
                            //说明肯定是有选中的
                            MoorEvaluation option = evaluationList.get(selectedPosition);
                            option.setSelected(false);
                            notifyItemChanged(selectedPosition);//同时将之前选中的变成非选中

                        }
                        selectedPosition = rightPosition;//记录将要选中的位置
                        bean.setSelected(true);
                        evaluationSet.clear();
                        evaluationSet.add(bean);
                        notifyItemChanged(rightPosition);//更新新选中的状态
                        evaluationList.clear();
                        evaluationList.addAll(evaluationSet);
                    }
                    if (onSelectedChangeListener != null) {
                        onSelectedChangeListener.getSelectedTagList(evaluationList);
                    }
                }
            });
        } else {
            //多选
            holder.slTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (option.isSelected()) {
                        for (Iterator<MoorEvaluation> it = evaluationSet.iterator(); it.hasNext(); ) {
                            MoorEvaluation value = it.next();
                            if (value.name.equals(option.name)) {
                                it.remove();
                            }
                        }
                    } else {
                        evaluationSet.add(option);
                    }
                    option.setSelected(!option.isSelected());
                    notifyItemChanged(position);

                    if (evaluationSet.size() > 0) {
                        selectList.clear();
                        selectList.addAll(evaluationSet);
                        if (onSelectedChangeListener != null) {
                            onSelectedChangeListener.getSelectedTagList(selectList);
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return evaluationList == null ? 0 : evaluationList.size();
    }


    public static class MoorTagViewMoreHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        MoorShadowLayout slTag;

        public MoorTagViewMoreHolder(View inflate) {
            super(inflate);
            slTag = inflate.findViewById(R.id.sl_tag);
            tvTitle = inflate.findViewById(R.id.tv_title);

        }
    }

    /**
     * 选择标签监听
     */
    public interface OnSelectedChangeListener {
        /**
         * 获取选择的标签列表
         *
         * @param evaluationList
         */
        void getSelectedTagList(List<MoorEvaluation> evaluationList);
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    public void clearSelected() {
        if (evaluationList.size() > 0) {
            for (MoorEvaluation option : evaluationList) {
                option.isSelected = false;
            }
        }
    }

}
