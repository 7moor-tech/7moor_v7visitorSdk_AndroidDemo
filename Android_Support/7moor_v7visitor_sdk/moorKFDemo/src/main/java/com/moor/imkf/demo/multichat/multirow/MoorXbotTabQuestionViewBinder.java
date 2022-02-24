package com.moor.imkf.demo.multichat.multirow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorXbotTabQuestionAdapter;
import com.moor.imkf.demo.adapter.MoorXbotTitleTabAdapter;
import com.moor.imkf.demo.bean.MoorEnumChatItemClickType;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedHolder;
import com.moor.imkf.demo.multichat.base.MoorBaseReceivedViewBinder;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.utils.MoorScreenUtils;
import com.moor.imkf.moorsdk.bean.MoorCommonQuesBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.manager.MoorManager;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/10/21
 *     @desc   : xbot分组常见问题样式
 *     @version: 1.0
 * </pre>
 */
public class MoorXbotTabQuestionViewBinder extends MoorBaseReceivedViewBinder<MoorMsgBean, MoorXbotTabQuestionViewBinder.ViewHolder> {

    private final MoorOptions options;

    public MoorXbotTabQuestionViewBinder(MoorOptions options) {
        this.options = options;
    }

    @Override
    protected MoorBaseReceivedHolder onCreateContentViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.moor_chat_row_tabquestion_recevied, parent, false);
        return new MoorXbotTabQuestionViewBinder.ViewHolder(root);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item) {
        MoorBaseReceivedViewBinder.ViewHolder parentHolder = holder.getParent();
        if (options != null) {
            if(TextUtils.isEmpty(item.getCommonQuestionsImg())){
                //没有配置 logo图片，使用系统主题默认图和背景
                holder.ivTabqus.setBackgroundColor(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
                holder.ivTabqus.setImageResource(R.drawable.moor_icon_tabquestion);
            }else{
                //配置 logo图片，加载图片
                MoorManager.getInstance().loadImage(item.getCommonQuestionsImg(),holder.ivTabqus);
            }
            holder.tabLayout.setSelectedTabIndicatorColor(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
            holder.tabLayout.setTabTextColors(MoorUtils.getApp().getResources().getColor(R.color.moor_color_333333), MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
            holder.tvSeemore.setTextColor(MoorColorUtils.getColorWithAlpha(1f, options.getSdkMainThemeColor()));
        }
        goneBaseView(parentHolder);
        holder.setData(item);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull ViewHolder holder, @NonNull MoorMsgBean item, @NonNull List<Object> payloads) {

    }

    @Override
    protected void onBaseViewRecycled(@NonNull ViewHolder holder) {

    }

    /**
     * 隐藏左侧头像，名称等ui
     */
    private void goneBaseView(MoorBaseReceivedViewBinder.ViewHolder parentHolder) {
        if (parentHolder != null) {
            if (parentHolder.ivChatAvatar != null) {
                parentHolder.ivChatAvatar.setVisibility(View.GONE);
            }
            if (parentHolder.chattingTvName != null) {
                parentHolder.chattingTvName.setVisibility(View.GONE);
            }
        }
    }


    class ViewHolder extends MoorBaseReceivedHolder {
        TabLayout tabLayout;
        ViewPager viewPager;
        TextView tvSeemore;
        LinearLayout llTabquestion;
        ImageView ivTabqus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tabLayout = itemView.findViewById(R.id.tb_question);
            viewPager = itemView.findViewById(R.id.vp_tabquestion);
            tvSeemore = itemView.findViewById(R.id.tv_seemore);
            llTabquestion = itemView.findViewById(R.id.ll_tabquestion);
            ivTabqus = itemView.findViewById(R.id.iv_tabqus);
        }


        public void setData(MoorMsgBean item) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llTabquestion.getLayoutParams();
            params.width = MoorScreenUtils.getScreenWidthOrHeight(MoorUtils.getApp()) - ((int) MoorUtils.getApp().getResources().getDimension(R.dimen.moor_left_margin_10)) * 2;
            llTabquestion.setLayoutParams(params);
            final Context context = MoorUtils.getApp();
            if (!TextUtils.isEmpty(item.getCommonQuestionsGroupJson())) {
                if (viewPager.getAdapter() != null) {
                    return;
                }
                Type token = new TypeToken<ArrayList<MoorCommonQuesBean>>() {
                }.getType();
                final ArrayList<MoorCommonQuesBean> list = new Gson().fromJson(item.getCommonQuestionsGroupJson(), token);
                if (list != null) {
                    if (list.size() > 0) {
                        final HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                        final ArrayList<String> tittles = new ArrayList<String>();
                        ArrayList<View> views = new ArrayList<View>();
                        for (int i = 0; i < list.size(); i++) {
                            map.put(list.get(i).getName(), list.get(i).getList());
                            tittles.add(list.get(i).getName());
                        }

                        for (int i = 0; i < tittles.size(); i++) {
                            tabLayout.addTab(tabLayout.newTab());
                            View view = View.inflate(context, R.layout.moor_chat_tab_question_fragment, null);
                            RecyclerView recyclerView = view.findViewById(R.id.reclcle_question);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setNestedScrollingEnabled(false);
                            MoorXbotTabQuestionAdapter adapter = new MoorXbotTabQuestionAdapter(map.get(tittles.get(i)), MoorXbotTabQuestionViewBinder.this);
                            recyclerView.setAdapter(adapter);
                            views.add(view);
                        }

                        MoorXbotTitleTabAdapter adapter = new MoorXbotTitleTabAdapter(context, views, tittles);

                        viewPager.setAdapter(adapter);

                        tabLayout.setupWithViewPager(viewPager);

                        viewPager.setCurrentItem(0);


                        if (map.get(tittles.get(0)) != null) {
                            if (map.get(tittles.get(0)).size() > 5) {
                                tvSeemore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getBinderClickListener().onClick(v, null, MoorEnumChatItemClickType.TYPE_XBOT_TABQUESTION_MORE.setObj(tittles.get(0), map.get(tittles.get(0))));
                                    }
                                });

                                tvSeemore.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                                layoutParams.height = MoorPixelUtil.dp2px(45 * 5 + 10);
                                viewPager.setLayoutParams(layoutParams);
                            } else {
                                tvSeemore.setVisibility(View.GONE);
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                                layoutParams.height = MoorPixelUtil.dp2px(45 * map.get(tittles.get(0)).size());
                                viewPager.setLayoutParams(layoutParams);
                            }
                        } else {
                            tvSeemore.setVisibility(View.GONE);
                        }
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int i, float v, int i1) {

                            }

                            @Override
                            public void onPageSelected(final int i) {
                                if (i < tittles.size()) {
                                    if (map.get(tittles.get(i)) != null) {
                                        int count = map.get(tittles.get(i)).size();
                                        if (count > 5) {
                                            tvSeemore.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    getBinderClickListener().onClick(v, null, MoorEnumChatItemClickType.TYPE_XBOT_TABQUESTION_MORE.setObj(tittles.get(i), map.get(tittles.get(i))));
                                                }
                                            });
                                            tvSeemore.setVisibility(View.VISIBLE);
                                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                                            layoutParams.height = MoorPixelUtil.dp2px(45 * 5 + 10);
                                            viewPager.setLayoutParams(layoutParams);
                                        } else {
                                            tvSeemore.setVisibility(View.GONE);
                                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                                            layoutParams.height = MoorPixelUtil.dp2px(45 * count);
                                            viewPager.setLayoutParams(layoutParams);

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int i) {

                            }
                        });


                    }
                }

            }
        }
    }
}
