package com.moor.imkf.demo.utils.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorPermissionX {

    /**
     * Init MoorPermissionX to make everything prepare to work.
     *
     * @param activity An instance of FragmentActivity
     * @return PermissionCollection instance.
     */
    public static MoorPermissionCollection init(FragmentActivity activity) {
        return new MoorPermissionCollection(activity);
    }

    /**
     * Init MoorPermissionX to make everything prepare to work.
     *
     * @param fragment An instance of Fragment
     * @return PermissionCollection instance.
     */
    public static MoorPermissionCollection init(Fragment fragment) {
        return new MoorPermissionCollection(fragment);
    }

    /**
     *  A helper function to check a permission is granted or not.
     *
     *  @param context Any context, will not be retained.
     *  @param permission Specific permission name to check. e.g. [android.Manifest.permission.CAMERA].
     *  @return True if this permission is granted, False otherwise.
     */
    public static boolean isGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
