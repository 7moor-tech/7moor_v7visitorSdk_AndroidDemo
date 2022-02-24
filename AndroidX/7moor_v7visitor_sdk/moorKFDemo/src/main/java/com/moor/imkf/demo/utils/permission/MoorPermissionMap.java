package com.moor.imkf.demo.utils.permission;

import android.Manifest;
import android.annotation.TargetApi;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorPermissionMap {
    @TargetApi(29)
    @NonNull
    public static Map<String, String> getPermissionMapOnQ() {
        Map<String, String> map = new HashMap<>();
        map.put(Manifest.permission.CAMERA, Manifest.permission_group.CAMERA);
        map.put(Manifest.permission.RECORD_AUDIO, Manifest.permission_group.MICROPHONE);
        map.put(Manifest.permission.READ_PHONE_STATE, Manifest.permission_group.PHONE);
        map.put(Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission_group.PHONE);
        map.put(Manifest.permission.CALL_PHONE, Manifest.permission_group.PHONE);
        map.put(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission_group.PHONE);
        map.put(Manifest.permission.ADD_VOICEMAIL, Manifest.permission_group.PHONE);
        map.put(Manifest.permission.USE_SIP, Manifest.permission_group.PHONE);
        map.put(Manifest.permission.ACCEPT_HANDOVER, Manifest.permission_group.PHONE);
        map.put(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
        map.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
        map.put(Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission_group.STORAGE);

        return map;
    }
}
