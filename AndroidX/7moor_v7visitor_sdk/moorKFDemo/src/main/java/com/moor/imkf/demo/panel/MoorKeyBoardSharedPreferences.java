package com.moor.imkf.demo.panel;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/19/21
 *     @desc   : 用于保存键盘高度
 *     @version: 1.0
 * </pre>
 */
public class MoorKeyBoardSharedPreferences {

    private static final String FILE_NAME = "keyboard.common";

    private static final String KEY_KEYBOARD_HEIGHT = "sp.key.keyboard.height";
    private static final String KEY_SCREEN_HEIGHT = "sp.key.screen.height";

    private static volatile SharedPreferences sp;

    public static boolean saveKeyBoardHeight(final Context context, final int keyboardHeight) {
        return with(context).edit()
                .putInt(KEY_KEYBOARD_HEIGHT, keyboardHeight)
                .commit();
    }

    public static boolean saveScreenHeight(final Context context, final int screenHeight) {
        return with(context).edit()
                .putInt(KEY_SCREEN_HEIGHT, screenHeight)
                .commit();
    }

    private static SharedPreferences with(final Context context) {
        if (sp == null) {
            synchronized (MoorKeyBoardSharedPreferences.class) {
                if (sp == null) {
                    sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                }
            }
        }

        return sp;
    }

    public static int getKeyBoardHeight(final Context context, final int defaultHeight) {
        return with(context).getInt(KEY_KEYBOARD_HEIGHT, defaultHeight);
    }

    public static int getScreenHeight(final Context context, final int defaultHeight) {
        return with(context).getInt(KEY_SCREEN_HEIGHT, defaultHeight);
    }

}
