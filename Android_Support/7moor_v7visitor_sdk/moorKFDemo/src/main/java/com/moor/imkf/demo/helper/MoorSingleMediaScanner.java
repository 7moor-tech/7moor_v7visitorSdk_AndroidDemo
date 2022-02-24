package com.moor.imkf.demo.helper;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/4/21
 *     @desc   : 媒体扫描监听
 *     @version: 1.0
 * </pre>
 */
public class MoorSingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private final MediaScannerConnection mMs;
    private final String path;
    private final ScanListener listener;

    public MoorSingleMediaScanner(Context context, String path, ScanListener l) {
        this.path = path;
        this.listener = l;
        this.mMs = new MediaScannerConnection(context, this);
        this.mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(path, null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
        if (listener != null) {
            listener.onScanFinish();
        }
    }

    public interface ScanListener {
        void onScanFinish();
    }
}