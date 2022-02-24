package com.moor.imkf.demo.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MoorXbotTitleTabAdapter extends PagerAdapter {

    private Context context;

    private List<View> pages;
    private List<String> tittles;

    public MoorXbotTitleTabAdapter(Context context, List<View> pages, List<String> tittles) {
        this.context = context;
        this.pages = pages;
        this.tittles = tittles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tittles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position < pages.size()) {
            View page = pages.get(position);
            container.addView(page);
            return page;
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
