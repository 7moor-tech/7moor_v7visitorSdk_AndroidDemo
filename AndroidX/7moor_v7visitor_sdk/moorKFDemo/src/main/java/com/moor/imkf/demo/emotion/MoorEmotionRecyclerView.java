package com.moor.imkf.demo.emotion;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.listener.IMoorEmojiClickListener;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.demo.view.shadowlayout.MoorShadowLayout;
import com.moor.imkf.moorsdk.bean.MoorEmotion;
import com.moor.imkf.moorsdk.manager.MoorManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   : 表情列表
 *     @version: 1.0
 * </pre>
 */
public class MoorEmotionRecyclerView extends RecyclerView {

    /**
     * 列
     */
    private static int sNumColumns = 0;
    private static int sNumRows = 0;
    private static int sPadding = 0;
    private static int sEmotionSize = 0;
    private final EditText mEditText;
    private static int sPaddingBottom;
    private IMoorEmojiClickListener emojiClickListener;

    public static int calSizeForContainEmotion(int width, int height) {
        sPadding = MoorPixelUtil.dp2px(5f);
        sEmotionSize = MoorPixelUtil.dp2px(30f);
        sPaddingBottom = MoorPixelUtil.dp2px(70f);
        sNumColumns = (width - sPadding - sPadding) / (sEmotionSize + sPadding + sPadding);
        if (sNumColumns > 8) {
            sNumColumns = 8;
        }
        sNumRows = (height - sPadding) / (sEmotionSize + sPadding + sPadding);
        return sNumColumns * sNumRows;
    }

    public MoorEmotionRecyclerView(Context context, EditText editText) {
        super(context);
        this.mEditText = editText;
    }

    public void buildEmotions(final List<MoorEmotion> data, final IMoorEmojiClickListener listener) {
        setVerticalScrollBarEnabled(false);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), sNumColumns);
        setLayoutManager(layoutManager);
        setPadding(sPadding, sPadding, sPadding, sPadding + sPaddingBottom);
        setClipToPadding(false);
        setAdapter(new EmotionAdapter(getContext(), data));
        emojiClickListener = listener;
//        setOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                try {
//                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//                    if ((lastVisibleItemPosition + 1) % sNumColumns == 0 || (lastVisibleItemPosition + 2) % sNumColumns == 0) {
//
//                        View view;
//                        if ((lastVisibleItemPosition + 1) % sNumColumns == 0) {
//                            view = layoutManager.findViewByPosition(lastVisibleItemPosition);
//                            if (view != null) {
//                                view.setAlpha(0);
//                                view.setClickable(false);
//                            }
//                        }
//                        view = layoutManager.findViewByPosition(lastVisibleItemPosition - 1);
//                        if (view != null) {
//                            view.setAlpha(0);
//                            view.setClickable(false);
//                        }
//                        view = layoutManager.findViewByPosition(lastVisibleItemPosition - 2);
//                        if (view != null) {
//                            view.setAlpha(0);
//                            view.setClickable(false);
//                        }
//                        view = layoutManager.findViewByPosition(lastVisibleItemPosition + sNumColumns);
//                        if (view != null) {
//                            view.setAlpha(0);
//                            view.setClickable(false);
//                        }
//                        view = layoutManager.findViewByPosition(lastVisibleItemPosition + sNumColumns - 1);
//                        if (view != null) {
//                            view.setAlpha(0);
//                            view.setClickable(false);
//                        }
//                        view = layoutManager.findViewByPosition(lastVisibleItemPosition + sNumColumns - 2);
//                        if (view != null) {
//                            view.setAlpha(0);
//                            view.setClickable(false);
//                        }
//                    }
//                    int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
//                    if ((lastCompletelyVisibleItemPosition + 1) % sNumColumns == 0 || (lastCompletelyVisibleItemPosition + 2) % sNumColumns == 0) {
//                        View view;
//                        if ((lastCompletelyVisibleItemPosition + 1) % sNumColumns == 0) {
//                            view = layoutManager.findViewByPosition(lastCompletelyVisibleItemPosition);
//                            if (view != null) {
//                                view.setAlpha(1);
//                                view.setClickable(true);
//                            }
//                        }
//                        view = layoutManager.findViewByPosition(lastCompletelyVisibleItemPosition - 1);
//                        if (view != null) {
//                            view.setAlpha(1);
//                            view.setClickable(true);
//                        }
//                        view = layoutManager.findViewByPosition(lastCompletelyVisibleItemPosition - 2);
//                        if (view != null) {
//                            view.setAlpha(1);
//                            view.setClickable(true);
//                        }
//                        view = layoutManager.findViewByPosition(lastCompletelyVisibleItemPosition - sNumColumns);
//                        if (view != null) {
//                            view.setAlpha(1);
//                            view.setClickable(true);
//                        }
//                        view = layoutManager.findViewByPosition(lastCompletelyVisibleItemPosition - sNumColumns - 1);
//                        if (view != null) {
//                            view.setAlpha(1);
//                            view.setClickable(true);
//                        }
//                        view = layoutManager.findViewByPosition(lastCompletelyVisibleItemPosition - sNumColumns - 2);
//                        if (view != null) {
//                            view.setAlpha(1);
//                            view.setClickable(true);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    public class EmotionAdapter extends RecyclerView.Adapter<EmotionHolder> {

        public List<MoorEmotion> mMoorEmotions;
        private final Context mContext;

        public EmotionAdapter(Context context, List<MoorEmotion> moorEmotions) {
            if (moorEmotions == null) {
                moorEmotions = new ArrayList<>();
            }
            mMoorEmotions = moorEmotions;
            mContext = context;
        }


        @NonNull
        @Override
        public EmotionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            EmotionHolder holder = new EmotionHolder(LayoutInflater.from(mContext).inflate(R.layout.moor_item_emotion, viewGroup, false));
            return holder;

        }

        @Override
        public void onBindViewHolder(@NonNull final EmotionHolder holder, int i) {
            MoorShadowLayout.LayoutParams layoutParams = (MoorShadowLayout.LayoutParams) holder.ivItemEmoji.getLayoutParams();
            layoutParams.width = sEmotionSize;
            layoutParams.height = sEmotionSize;
            holder.ll_item_emoji.setPadding(sPadding, sPadding, sPadding, sPadding);
            holder.ivItemEmoji.setLayoutParams(layoutParams);

            File file = new File(mMoorEmotions.get(holder.getAdapterPosition()).getFilePath());
            MoorManager.getInstance().loadImage(file.toURI().toString(), holder.ivItemEmoji, sEmotionSize, sEmotionSize);

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoorEmotion moorEmotion = mMoorEmotions.get(holder.getAdapterPosition());
                    int start = mEditText.getSelectionStart();
                    Editable editable = mEditText.getEditableText();
                    Spannable emotionSpannable = MoorEmojiSpanBuilder.buildEmotionSpannable(moorEmotion.getCode());
                    editable.insert(start, emotionSpannable);
                    if (emojiClickListener != null) {
                        emojiClickListener.onItemClick();
                    }
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mMoorEmotions.size();
        }

    }

    private static class EmotionHolder extends RecyclerView.ViewHolder {

        private final ImageView ivItemEmoji;
        private final MoorShadowLayout ll_item_emoji;

        public EmotionHolder(@NonNull View itemView) {
            super(itemView);
            ll_item_emoji = itemView.findViewById(R.id.ll_item_emoji);
            ivItemEmoji = itemView.findViewById(R.id.iv_item_emoji);
        }
    }
}
