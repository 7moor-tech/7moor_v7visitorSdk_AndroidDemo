package com.moor.imkf.demo.view.audiobutton;

import android.content.Context;
import android.media.AudioRecord;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.utils.MoorMediaPlayTools;
import com.moor.imkf.moorsdk.constants.MoorPathConstants;
import com.moor.imkf.mp3recorder.MP3Recorder;

import java.lang.ref.WeakReference;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/2/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorAudioRecorderButton extends AppCompatButton implements MoorAudioManager.AudioStateListener {

    private static final int DISTANCE_Y_CANCEL = 50;

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private int mCurrentState = STATE_NORMAL;
    private boolean isRecording = false;

    private MoorRecoderDialogManager mDialogManager;

    private MoorAudioManager mAudioManager;

    protected float mTime;

    protected int leftTime;
    private boolean is_send = true;

    private boolean mReady;

    private static final int MSG_RECORDER_PREPARE = 0x11;
    private static final int MSG_VOICE_CHANGE = 0x12;
    private static final int MSG_DIALOG_DISMISS = 0x13;
    private static final int MSG_TIME_LEFT_TEN = 0x14;
    private static final int MSG_TIME_LEFT_EXCEED_ALARM = 0x15;

    private RecorderFinishListener listener;

    private final Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            is_send = true;
            while (isRecording()) {
                if(handler==null){
                    break;
                }
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    if (is_send) {
                        if ((MoorDemoConstants.VOICE_RECOERD_LONG_MAX - Math.round(mTime + 0.5f)) == 10) {
                            leftTime = 10;
                            handler.sendEmptyMessage(MSG_TIME_LEFT_TEN);
                            is_send = false;
                        }
                    }
                    handler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private AudioHandler handler;
    private AudioRecord audioRecord;

    public interface RecorderFinishListener {
        void onRecordFinished(float mTime, String filePath, String pcmFilePath);
    }

    public MoorAudioRecorderButton(Context context) {
        this(context, null);
    }

    public MoorAudioRecorderButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoorAudioRecorderButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDialogManager = new MoorRecoderDialogManager(context);
        String dir = MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_RECORDAUDIO_FILE);
        handler = new AudioHandler(this);
        mAudioManager = MoorAudioManager.getInstance(dir);
        mAudioManager.setAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mReady = true;
                MoorMediaPlayTools.getInstance().recycle();
                if (mAudioManager != null) {

                    mAudioManager.prepareAudio();
                    MP3Recorder mp3Recorder = mAudioManager.getMp3Recorder();
                    if (mp3Recorder != null) {
                        audioRecord = mp3Recorder.getmAudioRecord();
                    }
                }
                return false;
            }
        });
    }


    public void setRecordFinishListener(RecorderFinishListener listener) {
        this.listener = listener;
    }

    public void cancelListener() {
        if (mAudioManager != null) {
            mAudioManager.setAudioStateListener(null);
        }
    }


    private static class AudioHandler extends Handler {
        private final WeakReference<MoorAudioRecorderButton> mContext;


        AudioHandler(MoorAudioRecorderButton mContext) {
            this.mContext = new WeakReference<>(mContext);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MoorAudioRecorderButton moorAudioRecorderButton = mContext.get();
            moorAudioRecorderButton.dealMsg(msg);
        }
    }

    private void dealMsg(Message msg) {
        switch (msg.what) {
            case MSG_RECORDER_PREPARE:
                mDialogManager.showDialog();
                isRecording = true;
                new Thread(mGetVoiceLevelRunnable).start();
                break;
            case MSG_VOICE_CHANGE:
                mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                break;
            case MSG_DIALOG_DISMISS:
            case MSG_TIME_LEFT_EXCEED_ALARM:
                mDialogManager.dismissDialog();
                break;
            case MSG_TIME_LEFT_TEN:

                if (leftTime >= 0) {
                    mDialogManager.secondLeft(leftTime);
                    Message message = handler.obtainMessage();
                    message.what = MSG_TIME_LEFT_TEN;
                    handler.sendMessageDelayed(message, 1000);
                    leftTime--;
                } else {
                    mDialogManager.exceedTime();
                    handler.sendMessageDelayed(handler.obtainMessage(MSG_TIME_LEFT_EXCEED_ALARM), 1000);
                    if (listener != null) {
                        listener.onRecordFinished(mTime, mAudioManager.getCurrentFilePath(), mAudioManager.getPCMFilePath());
                    }
                    mAudioManager.release();
                    reset();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);

                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording()) {
                    if (wanttocancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }

                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording() || mTime < 0.9) {
                    if (mDialogManager != null) {
                        mDialogManager.tooShort();
                    }
                    if (mAudioManager != null) {
                        mAudioManager.cancel();
                    }
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1000);
                } else if (mCurrentState == STATE_RECORDING) {
                    if (mDialogManager != null) {
                        mDialogManager.dismissDialog();
                    }

                    if (listener != null) {
                        listener.onRecordFinished(mTime, mAudioManager.getCurrentFilePath(), mAudioManager.getPCMFilePath());
                    }
                    if (mAudioManager != null) {
                        mAudioManager.release();
                    }
                    handler.removeMessages(MSG_TIME_LEFT_TEN);
                    handler.removeMessages(MSG_TIME_LEFT_EXCEED_ALARM);
                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                    if (mDialogManager != null) {
                        mDialogManager.dismissDialog();
                    }
                    if (mAudioManager != null) {
                        mAudioManager.cancel();
                    }
                    handler.removeMessages(MSG_TIME_LEFT_TEN);
                    handler.removeMessages(MSG_TIME_LEFT_EXCEED_ALARM);
                }

                reset();
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 恢复标志位
     */
    private void reset() {
        isRecording = false;
        mReady = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wanttocancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        return y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL;
    }

    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.moor_chat_input_bg);
                    setText(R.string.moor_press_record);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.moor_btn_recorder_press);
                    setText(R.string.moor_release_end);
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.moor_btn_recorder_press);
                    setText(R.string.moor_release_cancel);
                    mDialogManager.wantToCancel();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void wellPrepared() {
        handler.sendEmptyMessage(MSG_RECORDER_PREPARE);
    }

    public void recycle() {
        cancelListener();
        if (mAudioManager != null) {
            mAudioManager = null;
        }
        if (audioRecord != null) {
            audioRecord = null;
        }
        if (mDialogManager != null) {
            mDialogManager.recycle();
            mDialogManager = null;
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public boolean isRecording() {
        return isRecording && (audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING);
    }
}
