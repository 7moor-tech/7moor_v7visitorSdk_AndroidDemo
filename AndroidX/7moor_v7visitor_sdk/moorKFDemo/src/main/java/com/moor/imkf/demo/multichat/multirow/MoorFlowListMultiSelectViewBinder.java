package com.moor.imkf.demo.multichat.multirow;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorFlowMultiSelectAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.MoorTextParseUtil;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.demo.view.MoorChildRecyclerView;
import com.moor.imkf.demo.view.flexbox.FlexboxLayoutManager;
import com.moor.imkf.demo.view.flexbox.JustifyContent;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorFlowListBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * `
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : FlowList 多选
 *     @version: 1.0
 * </pre>
 */
public class MoorFlowListMultiSelectViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorFlowListMultiSelectViewBinder.ViewHolder> {
    private final MoorOptions options;
    private List<MoorFlowListBean> selectFlowList = new ArrayList<>();

    public MoorFlowListMultiSelectViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_flowlist_multi, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull final ViewHolder holder, @NonNull final MoorMsgBean item) {
        List<View> views = MoorTextParseUtil.getInstance().parseFlow(item);
        holder.llFlowText.removeAllViews();
        if (views.size() > 0) {
            for (View view : views) {
                holder.llFlowText.addView(view);
            }
        }
        holder.msl_flow_multi_bg.setVisibility(views.size() == 0 ? View.GONE : View.VISIBLE);
        if (options != null) {
            String leftMsgBgColor = options.getLeftMsgBgColor();
            if (!TextUtils.isEmpty(leftMsgBgColor)) {
                holder.msl_flow_multi_bg.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
            }
        }
        holder.getParent().chattingTvName.post(new Runnable() {

            @Override
            public void run() {
                int width = holder.getParent().chattingTvName.getWidth();
                holder.llFlowText.setMinimumWidth(width);
            }
        });
        List<MoorFlowListBean> flowList = new Gson().fromJson(item.getFlowlistJson(), new TypeToken<List<MoorFlowListBean>>() {
        }.getType());
        final MoorFlowMultiSelectAdapter adapter = new MoorFlowMultiSelectAdapter(holder.llFlowText.getContext(), item, flowList, MoorFlowMultiSelectAdapter.TYPE_MORE);

        View root = LayoutInflater.from(holder.llFlowText.getContext()).inflate(R.layout.moor_layout_flow_multi, holder.llBottomContentMulti, false);

        final MoorShadowLayout mslFlowMultiBg = root.findViewById(R.id.msl_flow_multi_bg);
        final MoorChildRecyclerView rvFlowMulti = root.findViewById(R.id.rv_flow_multi);
        final MoorShadowLayout mslFlowMulti = root.findViewById(R.id.msl_flow_multi);
        if (options != null) {
            String leftMsgBgColor = options.getLeftMsgBgColor();
            mslFlowMulti.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
            mslFlowMulti.setLayoutBackgroundTrue(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
            if (!TextUtils.isEmpty(leftMsgBgColor)) {
                mslFlowMultiBg.setLayoutBackground(MoorColorUtils.getColorWithAlpha(1f, leftMsgBgColor));
            }
        }

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rvFlowMulti.getLayoutParams();

        layoutParams.setMargins(0, MoorPixelUtil.dp2px(10f), MoorPixelUtil.dp2px(10f), 0);
        if (flowList.size() == 1) {
            layoutParams.height = MoorPixelUtil.dp2px(70f);
        } else if (flowList.size() == 2) {
            if (flowList.get(0).getText().length() + flowList.get(1).getText().length() > 8) {
                layoutParams.height = MoorPixelUtil.dp2px(170f);
            } else {
                layoutParams.height = MoorPixelUtil.dp2px(70f);
            }
        } else if (flowList.size() == 3) {
            if (flowList.get(0).getText().length() + flowList.get(1).getText().length() + flowList.get(2).getText().length() > 8) {
                layoutParams.height = MoorPixelUtil.dp2px(170f);
            } else {
                layoutParams.height = MoorPixelUtil.dp2px(70f);
            }
        } else {

            layoutParams.height = MoorPixelUtil.dp2px(170f);
        }
        layoutParams.width = (int) (MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) * 0.65);
        rvFlowMulti.setLayoutParams(layoutParams);


        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(holder.llFlowText.getContext());
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        rvFlowMulti.setLayoutManager(layoutManager);

        adapter.setOnSelectedChangeListener(new MoorFlowMultiSelectAdapter.OnSelectedChangeListener() {
            @Override
            public void getSelectedTagList(List<MoorFlowListBean> evaluationList) {
                selectFlowList = evaluationList;
            }
        });
        if (!item.isHasSendMulti()) {
            mslFlowMulti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBinderClickListener().onClick(mslFlowMulti, item, MoorEnumChatItemClickType.TYPE_FLOW_LIST_MULTI.setObj(selectFlowList));
                }
            });
        }

        rvFlowMulti.setAdapter(adapter);
        if (holder.getParentView() != null) {
            holder.getParentView().removeAllViews();
            holder.getParentView().addView(root);
        }
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item, @NonNull List<Object> payloads) {

    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {

    }

    static class ViewHolder extends MoorBaseReceivedHolder {

        LinearLayout llFlowText;
        MoorShadowLayout msl_flow_multi_bg;
        LinearLayout llBottomContentMulti;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.msl_flow_multi_bg = (MoorShadowLayout) itemView.findViewById(R.id.msl_flow_multi_bg);
            this.llFlowText = (LinearLayout) itemView.findViewById(R.id.ll_flow_list_text);
        }

        public LinearLayout getParentView() {
            MoorBaseReceivedViewBinder.ViewHolder parent = getParent();
            if (parent.llBottomContentMulti != null) {
                llBottomContentMulti = parent.llBottomContentMulti;
            }
            return llBottomContentMulti;
        }
    }
}
