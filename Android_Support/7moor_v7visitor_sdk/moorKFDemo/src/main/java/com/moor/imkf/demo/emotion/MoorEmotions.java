package com.moor.imkf.demo.emotion;

import com.moor.imkf.demo.utils.MoorEmojiBitmapUtil;
import com.moor.imkf.moorsdk.bean.MoorEmotion;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorEmotions {
    public static String getFilePathByCode(String emotionName) {
        List<MoorEmotion> emotions = MoorEmojiBitmapUtil.getMoorEmotions();
        for (MoorEmotion emotion : emotions) {
            if (emotion.getCode().equals(emotionName)) {
                return emotion.getFilePath();
            }
        }
        return "";
    }
}
