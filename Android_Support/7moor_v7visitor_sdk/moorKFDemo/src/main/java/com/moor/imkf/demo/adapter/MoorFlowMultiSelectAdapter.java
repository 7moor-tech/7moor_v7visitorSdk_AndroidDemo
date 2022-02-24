package com.moor.imkf.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorInfoBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.db.MoorInfoDao;
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
public class MoorFlowMultiSelectAdapter extends RecyclerView.Adapter<MoorFlowMultiSelectAdapter.MoorTagViewMoreHolder> {
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_MORE = 1;
    private final Context mContext;
    private final MoorMsgBean msgBean;
    private List<MoorFlowListBean> evaluationList = new ArrayList<>();
    private final Set<MoorFlowListBean> evaluationSet = new LinkedHashSet<>();
    private final int type;
    private int selectedPosition = -1;
    private OnSelectedChangeListener onSelectedChangeListener;
    private final ArrayList<MoorFlowListBean> selectList = new ArrayList<>();
    private final MoorInfoBean infoBean;

    public MoorFlowMultiSelectAdapter(Context mActivity, final MoorMsgBean item, List<MoorFlowListBean> evaluationList, int type) {
        this.mContext = mActivity;
        this.evaluationList = evaluationList;
        this.type = type;
        this.msgBean = item;
        infoBean = MoorInfoDao.getInstance().queryInfo();
    }

    @Override
    public MoorTagViewMoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoorTagViewMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.moor_item_flow_multi, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final MoorTagViewMoreHolder holder, final int position) {
        final MoorFlowListBean bean = evaluationList.get(position);
        holder.tvTitle.setText(bean.getButton());

        if (bean.isSelected()) {
            evaluationSet.add(bean);
        }
        MoorOptions options = MoorManager.getInstance().getOptions();
        holder.slTag.setStrokeColorTrue(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
        holder.slTag.setTextColor_true(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
        holder.slTag.setSelected(bean.isSelected());

        //单选
        if (type == TYPE_SINGLE) {
            holder.tvTitle.setTag(position);
            evaluationSet.clear();
            evaluationSet.add(bean);
            if (infoBean.getSessionId().equals(msgBean.getSessionId())) {
                holder.slTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //拿到正确的position
                        int rightPosition = (int) holder.tvTitle.getTag();
                        MoorFlowListBean bean = evaluationList.get(rightPosition);
                        //点击的是选中的
                        if (bean.isSelected()) {
                            bean.setSelected(false);
                            notifyItemChanged(rightPosition, bean);
                            selectedPosition = -1;
                        } else {
                            //点击的如果是没有选中的
                            if (selectedPosition != -1) {
                                //说明肯定是有选中的
                                MoorFlowListBean option = evaluationList.get(selectedPosition);
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
            }

        } else {
            //多选
            if (infoBean.getSessionId().equals(msgBean.getSessionId())) {
                holder.slTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.isSelected()) {
                            for (Iterator<MoorFlowListBean> it = evaluationSet.iterator(); it.hasNext(); ) {
                                MoorFlowListBean value = it.next();
                                if (value.getText().equals(bean.getText())) {
                                    it.remove();
                                }
                            }
                        } else {
                            evaluationSet.add(bean);
                        }
                        bean.setSelected(!bean.isSelected());
                        notifyItemChanged(position);

                        if (evaluationSet.size() > 0) {
                            selectList.clear();
                            selectList.addAll(evaluationSet);
                            if (onSelectedChangeListener != null) {
                                onSelectedChangeListener.getSelectedTagList(selectList);
                            }
                        } else {
                            selectList.clear();
                            clearSelected();
                            if (onSelectedChangeListener != null) {
                                onSelectedChangeListener.getSelectedTagList(selectList);
                            }
                        }
                    }
                });
            }

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
        void getSelectedTagList(List<MoorFlowListBean> evaluationList);
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    public void clearSelected() {
        if (evaluationList.size() > 0) {
            for (MoorFlowListBean option : evaluationList) {
                option.setSelected(false);
            }
        }
    }

}
