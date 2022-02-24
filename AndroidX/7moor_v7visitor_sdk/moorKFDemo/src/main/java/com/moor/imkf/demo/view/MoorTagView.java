package com.moor.imkf.demo.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.adapter.MoorTagSelectAdapter;
import com.moor.imkf.demo.bean.MoorEvaluation;
import com.moor.imkf.demo.view.flexbox.FlexboxLayoutManager;
import com.moor.imkf.demo.view.flexbox.JustifyContent;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/5/15
 *     @desc   : 评价弹框中的标签
 *     @version: 1.0
 * </pre>
 */
public class MoorTagView extends LinearLayout {

    private RecyclerView rvTagName;
    private Context mContext;
    private MoorTagSelectAdapter moorSelectAdapter;

    public MoorTagView(Context mContext) {
        super(mContext);
    }

    public MoorTagView(Context mContext, @Nullable AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;
        LayoutInflater.from(mContext).inflate(R.layout.moor_tag_view, this);
        rvTagName = findViewById(R.id.rv_tagName);
    }

    public void initTagView(List<MoorEvaluation> optionList, int type,MoorTagSelectAdapter.OnSelectedChangeListener onSelectedChangeListener) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        rvTagName.setLayoutManager(layoutManager);

        moorSelectAdapter = new MoorTagSelectAdapter(mContext, optionList, type);
        rvTagName.setAdapter(moorSelectAdapter);
        moorSelectAdapter.setOnSelectedChangeListener(onSelectedChangeListener);
    }


}
