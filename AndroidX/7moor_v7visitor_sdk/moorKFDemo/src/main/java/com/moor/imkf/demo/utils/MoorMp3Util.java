package com.moor.imkf.demo.utils;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.moor.imkf.lib.utils.MoorLogUtils;

import java.io.IOException;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/8/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorMp3Util {

    /**
     * 获取存储中mp3文件的时长
     *
     * @param filePath mp3文件路径.
     * @return 时长 s
     */
    public static int getMp3TimeForFile(String filePath) {
        int mediaPlayerDuration = 1;
        if (TextUtils.isEmpty(filePath)) {
            return mediaPlayerDuration;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayerDuration = mediaPlayer.getDuration() / 1000;
        } catch (IOException ioException) {
            MoorLogUtils.e("getMp3TimeForFile()" + ioException.getMessage());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        if (mediaPlayerDuration == 0) {
            mediaPlayerDuration = 1;
        }
        return mediaPlayerDuration;
    }
}
