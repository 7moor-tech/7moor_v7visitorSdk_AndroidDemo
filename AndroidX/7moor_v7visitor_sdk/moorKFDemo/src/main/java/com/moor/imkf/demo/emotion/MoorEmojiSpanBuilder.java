package com.moor.imkf.demo.emotion;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;

import com.moor.imkf.demo.utils.MoorPixelUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorEmojiSpanBuilder {

    private static final String PATTERN = ":[^:]+:";
    private static final Pattern S_PATTERN_EMOTION = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);

    public static SpannableString buildEmotionSpannable(String text) {
        Matcher matcherEmotion = S_PATTERN_EMOTION.matcher(text);
        SpannableString spannableString = new SpannableString(text);
        while (matcherEmotion.find()) {
            String key = matcherEmotion.group();
            String imgRes = MoorEmotions.getFilePathByCode(key);
            if (!TextUtils.isEmpty(imgRes)) {
                int start = matcherEmotion.start();
                Drawable drawable = Drawable.createFromPath(imgRes);
                drawable.setBounds(0, 0, MoorPixelUtil.dp2px(24.5f), MoorPixelUtil.dp2px(24.5f));
                MoorCenterImageSpan span = new MoorCenterImageSpan(drawable);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }
}
