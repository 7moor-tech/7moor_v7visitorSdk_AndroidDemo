package com.moor.imkf.demo.utils;

import android.webkit.MimeTypeMap;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/18/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorMimeTypes {

    private final Map<String, String> mMimeTypes;
    private final Map<String, Integer> mIcons;

    public MoorMimeTypes() {
        mMimeTypes = new HashMap<>();
        mIcons = new HashMap<>();
    }

    public void put(String type, String extension, int icon) {
        put(type, extension);
        mIcons.put(extension, icon);
    }

    public void put(String type, String extension) {
        extension = extension.toLowerCase();
        mMimeTypes.put(type, extension);
    }

    public String getMimeType(String extension) {


        if (extension.length() > 0) {
            String webkitMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

            if (webkitMimeType != null) {
                return webkitMimeType;
            }
        }
        extension = extension.toLowerCase();

        String mimeType = mMimeTypes.get(extension);

        if (mimeType == null) {
            mimeType = "*/*";
        }
        return mimeType;
    }


}
