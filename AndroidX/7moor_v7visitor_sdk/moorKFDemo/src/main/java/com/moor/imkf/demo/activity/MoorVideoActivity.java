package com.moor.imkf.demo.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.utils.MoorStatusBarUtil;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorsdk.constants.MoorPathConstants;
import com.moor.imkf.moorsdk.listener.IMoorFileDownLoadListener;
import com.moor.imkf.moorsdk.utils.MoorLogUtils;
import com.moor.imkf.moorsdk.utils.MoorSdkFileUtils;
import com.moor.imkf.moorsdk.utils.MoorSdkVersionUtil;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.io.File;
import java.util.UUID;

import okhttp3.Response;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/8/21
 *     @desc   : 视频播放页面
 *     @version: 1.0
 * </pre>
 */
public class MoorVideoActivity extends AppCompatActivity {

    private VideoView ykf_videoview;
    private ImageView iv_closevideo;
    private String video_url;
    private RelativeLayout rl_video_progress;
    private String dirStr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moor_videoactivity);
        MoorStatusBarUtil.setTranslucentStatus(this);
        ykf_videoview=findViewById(R.id.ykf_videoview);
        iv_closevideo=findViewById(R.id.iv_closevideo);
        rl_video_progress=findViewById(R.id.rl_video_progress);

        video_url=getIntent().getStringExtra(MoorDemoConstants.MOOR_VIDEO_PATH);

        if (MoorSdkVersionUtil.over_29()) {
            assert MoorUtils.getApp() != null;
            dirStr = MoorUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "moor";

        } else {
            dirStr = Environment.getExternalStorageDirectory() + File.separator + "cc/downloadfile/";
        }
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "7moor_video" + UUID.randomUUID());

        if (file.exists()) {
            file.delete();
        }

        if (video_url.startsWith("http")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //下载附件
                    Response fileResponse;
                    try {
                        fileResponse = MoorHttpUtils
                                .get()
                                .url(video_url)
                                .build()
                                .execute();
                        MoorSdkFileUtils.saveFile(fileResponse
                                , MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_DOWNLOAD_FILE)
                                , "moor_video.mp4", new IMoorFileDownLoadListener() {
                                    @Override
                                    public void onSuccess(File file) {
                                        String filePath = file.getAbsolutePath();
                                        initVideo(filePath);
                                    }

                                    @Override
                                    public void onFailed() {
                                        MoorLogUtils.e("error_file_download");

                                    }

                                    @Override
                                    public void onProgress(int progress) {
                                        MoorLogUtils.d(progress);


                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        iv_closevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initVideo(String  url) {
        Uri uri = Uri.parse( url );

        ykf_videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        //视频显示出第一帧隐藏 progress
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                            rl_video_progress.setVisibility(View.GONE);
                        }

                        return true;
                    }
                });
            }

        });

        //播放完成回调
        ykf_videoview.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
        ykf_videoview.setVideoURI(uri);
        //设置视频控制器
        MediaController mediaController=new MediaController(this);
        ykf_videoview.setMediaController(mediaController);
        mediaController.setMediaPlayer(ykf_videoview);


        //开始播放视频
        ykf_videoview.start();
        ykf_videoview.requestFocus();

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            ykf_videoview.start();

        }
    }

    @Override
    protected void onDestroy() {
        if(ykf_videoview.isPlaying()){
            ykf_videoview.stopPlayback();
        }
        super.onDestroy();
    }

 }
