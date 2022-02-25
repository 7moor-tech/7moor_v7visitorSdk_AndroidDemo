package com.moor.imkf.demo.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/18/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorMimeTypeParser {


    public static final String TAG_MIMETYPES = "MimeTypes";
    public static final String TAG_TYPE = "type";

    public static final String ATTR_EXTENSION = "extension";
    public static final String ATTR_MIMETYPE = "mimetype";
    public static final String ATTR_ICON = "icon";

    private XmlPullParser mXpp;
    private MoorMimeTypes mMimeTypes;
    private final Resources resources;
    private final String packagename;

    public MoorMimeTypeParser(Context ctx, String packagename) throws NameNotFoundException {
        this.packagename = packagename;
        resources = ctx.getPackageManager().getResourcesForApplication(packagename);
    }


    public MoorMimeTypes fromXmlResource(XmlResourceParser in)
            throws XmlPullParserException, IOException {
        mXpp = in;
        return parse();
    }

    public MoorMimeTypes parse()
            throws XmlPullParserException, IOException {

        mMimeTypes = new MoorMimeTypes();

        int eventType = mXpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tag = mXpp.getName();

            if (eventType == XmlPullParser.START_TAG) {
                if (tag.equals(TAG_MIMETYPES)) {

                } else if (tag.equals(TAG_TYPE)) {
                    addMimeTypeStart();
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (tag.equals(TAG_MIMETYPES)) {

                }
            }

            eventType = mXpp.next();
        }

        return mMimeTypes;
    }

    private void addMimeTypeStart() {
        String extension = mXpp.getAttributeValue(null, ATTR_EXTENSION);
        String mimetype = mXpp.getAttributeValue(null, ATTR_MIMETYPE);
        String icon = mXpp.getAttributeValue(null, ATTR_ICON);

        if (icon != null) {
            int id = resources.getIdentifier(icon.substring(1), null, packagename);
            if (id > 0) {
                mMimeTypes.put(extension, mimetype, id);
                return;
            }
        }

        mMimeTypes.put(extension, mimetype);
    }

}
