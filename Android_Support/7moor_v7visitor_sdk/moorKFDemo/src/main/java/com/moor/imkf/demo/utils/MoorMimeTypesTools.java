package com.moor.imkf.demo.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.bean.MoorMimeTypes;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/18/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorMimeTypesTools {

    private static boolean hasLoadMimeType = false;

    public static String getMimeType(Context context, String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            fileName = fileName.toLowerCase();

            MoorMimeTypes mimeTypes = getMimeTypes(context);
            String extension = MoorFileFormatUtils.getFormatName(fileName);
            return mimeTypes.getMimeType(extension);
        }

        return null;
    }

    private static MoorMimeTypes getMimeTypes(Context context) {
        return loadMimeTypes(context);
    }

    private static MoorMimeTypes loadMimeTypes(Context context) {
        MoorMimeTypeParser parser = null;
        XmlResourceParser xmlResourceParser = null;
        if (!hasLoadMimeType) {
            try {
                parser = new MoorMimeTypeParser(context, context.getPackageName());
                xmlResourceParser = context.getResources().getXml(R.xml.moor_mimetypes);

                return parser.fromXmlResource(xmlResourceParser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            hasLoadMimeType = true;
        }

        return null;
    }
}
