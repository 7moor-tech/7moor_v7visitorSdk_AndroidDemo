package com.moor.imkf.demo.utils.permission;

import android.Manifest;

import java.util.HashMap;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 3/25/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorPermissionConstants {
    private static volatile MoorPermissionConstants instance;
    private static final HashMap<String, String> perMap = new HashMap<>();
    public static final String STORE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;

    private MoorPermissionConstants() {
        perMap.put(STORE, "存储");
        perMap.put(CAMERA, "相机");
        perMap.put(RECORD_AUDIO, "麦克风");
    }

    public static MoorPermissionConstants getInstance() {
        return MoorPermissionConstants.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MoorPermissionConstants sInstance = new MoorPermissionConstants();
    }

    public String getPermissionName(String permission) {
        return perMap.get(permission);
    }
}
