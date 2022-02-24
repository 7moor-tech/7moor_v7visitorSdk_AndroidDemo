package com.moor.imkf.demo.emotion;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.moor.imkf.demo.listener.IMoorEmojiClickListener;
import com.moor.imkf.moorsdk.bean.MoorEmotion;

import java.util.ArrayList;
import java.util.List;
/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorEmotionPagerView extends ViewPager {

    private int currentWidth = -1;
    private int currentHeight = -1;
    private Adapter mAdapter;

    public MoorEmotionPagerView(@NonNull Context context) {
        this(context, null);
    }

    public MoorEmotionPagerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void buildEmotionViews(final EditText editText, List<MoorEmotion> data, int width, int height, IMoorEmojiClickListener listener) {
        if (data == null || data.isEmpty() || editText == null) {
            return;
        }
        if (currentWidth == width && currentHeight == height) {
            return;
        }
        currentWidth = width;
        currentHeight = height;
        int emotionViewContainSize = MoorEmotionRecyclerView.calSizeForContainEmotion(currentWidth, currentHeight);
        if (emotionViewContainSize == 0) {
            return;
        }

        int pagerCount = data.size() / emotionViewContainSize;
//        pagerCount += (data.size() % emotionViewContainSize == 0) ? 0 : 1;
        pagerCount = 1;
        int index = 0;
        List<MoorEmotionRecyclerView> emotionViews = new ArrayList<>();
        for (int i = 0; i < pagerCount; i++) {
            MoorEmotionRecyclerView emotionView = new MoorEmotionRecyclerView(getContext(), editText);
            int end = (i + 1) * emotionViewContainSize;
            if (end > data.size()) {
                end = data.size();
            }
//            emotionView.buildEmotions(data.subList(index, end));
            emotionView.buildEmotions(data,listener);
            emotionViews.add(emotionView);
            index = end;
        }
        mAdapter = new Adapter(emotionViews);
        setAdapter(mAdapter);
//        addOnPageChangeListener(new OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                indicatorView.setSelection(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    public static class Adapter extends PagerAdapter {

        private final List<MoorEmotionRecyclerView> mList;

        public Adapter(List<MoorEmotionRecyclerView> mList) {
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    }
}
