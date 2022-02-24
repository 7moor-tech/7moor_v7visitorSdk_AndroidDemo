package com.moor.imkf.demo.view.audiobutton;


import com.moor.imkf.mp3recorder.MP3Recorder;

import java.io.File;
import java.util.UUID;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/2/21
 *     @desc   : 录音工具类
 *     @version: 1.0
 * </pre>
 */
public class MoorAudioManager {

    private String mDir;
    private String mCurrentFilePath;
    private String mPCMFilePath;

    private boolean isPrepared;

    private volatile static MoorAudioManager instance;

    private AudioStateListener listener;

    MP3Recorder mp3Recorder;

    public interface AudioStateListener {
        void wellPrepared();
    }

    public void setAudioStateListener(AudioStateListener listener) {
        this.listener = listener;
    }

    private MoorAudioManager() {
    }

    private MoorAudioManager(String dir) {
        this.mDir = dir;
    }

    public static MoorAudioManager getInstance(String dir) {
        if (instance == null) {
            synchronized (MoorAudioManager.class) {
                instance = new MoorAudioManager(dir);
            }
        }
        return instance;
    }

    public void prepareAudio() {
        try {
            isPrepared = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();

            }

            String fileName = generateFileName();
            File file = new File(dir, fileName);
            mCurrentFilePath = file.getAbsolutePath();

            File pcmFile = new File(dir, generatePCMFileName());
            mPCMFilePath = pcmFile.getAbsolutePath();

            mp3Recorder = new MP3Recorder(file, pcmFile);
            mp3Recorder.start();

            if (listener != null) {
                listener.wellPrepared();
            }
            isPrepared = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {

        return UUID.randomUUID().toString() + ".mp3";
    }

    private String generatePCMFileName() {

        return UUID.randomUUID().toString() + ".pcm";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared && mp3Recorder != null) {
            int volume = mp3Recorder.getVolume();
            int level = 1;
//            volume = maxLevel * mp3Recorder.getVolume() * mp3Recorder.getVolume() / 5000 + 1;
//            MoorLogUtils.e("mp3Recorder.getVolume()"+mp3Recorder.getVolume()+"--"+volume);

            if (volume < 52) {
                level = 1;
            } else if (volume < 57) {
                level = 2;
            } else if (volume < 62) {
                level = 3;
            } else if (volume < 65) {
                level = 4;
            } else if (volume < 70) {
                level = 5;
            } else if (volume < 76) {
                level = 6;
            } else if (volume < 84) {
                level = 7;
            }
            return level;
        }
        return 1;
    }

    public void release() {
        if (mp3Recorder != null) {
            mp3Recorder.stop();
            mp3Recorder = null;
        }
    }

    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
        if (mPCMFilePath != null) {
            File file = new File(mPCMFilePath);
            file.delete();
            mPCMFilePath = null;
        }
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

    public String getPCMFilePath() {
        return mPCMFilePath;
    }

    public MP3Recorder getMp3Recorder() {
        return mp3Recorder;
    }
}
