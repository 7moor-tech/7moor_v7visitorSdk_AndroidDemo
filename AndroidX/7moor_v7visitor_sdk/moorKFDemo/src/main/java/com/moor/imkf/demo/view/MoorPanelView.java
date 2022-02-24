package com.moor.imkf.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.listener.IMoorPanelOnClickListener;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.moorsdk.bean.MoorPanelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/19/21
 *     @desc   : 更多面板GridView
 *     @version: 1.0
 * </pre>
 */
public class MoorPanelView extends GridView {

    private static final int S_NUM_COLUMNS = 4;
    private static final int S_VERTICAL_SPACING = MoorPixelUtil.dp2px(10f);
    private List<MoorPanelBean> beanList;
    private PanelAdapter adapter;

    public MoorPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoorPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public MoorPanelView(Context context) {
        super(context);
    }

    public void updatePanel(List<MoorPanelBean> data) {
        this.beanList = data;
        adapter.notifyDataSetChanged();

    }

    public void buildPanels(final IMoorPanelOnClickListener listener, final List<MoorPanelBean> data) {
        setVerticalScrollBarEnabled(false);
        setNumColumns(S_NUM_COLUMNS);
        setVerticalSpacing(S_VERTICAL_SPACING);
        setPadding(0, 0, 0, 0);
        setClipToPadding(false);
        beanList = data;
        adapter = new PanelAdapter(getContext(), beanList);
        setAdapter(adapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onPanelClickListener(data.get(position));
                }
            }
        });
    }

    public static class PanelAdapter extends BaseAdapter {

        public List<MoorPanelBean> moorPanelBeans;
        private final Context mContext;

        public PanelAdapter(Context context, List<MoorPanelBean> panelBeans) {
            if (panelBeans == null) {
                panelBeans = new ArrayList<>();
            }
            moorPanelBeans = panelBeans;
            mContext = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.moor_panel_item_layout, parent, false);
            }
            ImageView ivPanelPic = view.findViewById(R.id.iv_panel_pic);
            TextView tvPanelTitle = view.findViewById(R.id.tv_panel_title);
            tvPanelTitle.setText(moorPanelBeans.get(position).getTitle());
            ivPanelPic.setImageDrawable(moorPanelBeans.get(position).getResDrawable());

            return view;
        }

        @Override
        public int getCount() {
            return moorPanelBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return moorPanelBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }
}
