package com.moor.imkf.demo.utils.permission;

import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.moor.imkf.demo.utils.permission.request.PermissionBuilder;
import com.moor.imkf.demo.utils.permission.request.RequestBackgroundLocationPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MoorPermissionCollection {

    private static final String TAG = "PermissionCollection";

    private FragmentActivity activity;

    private Fragment fragment;

    public MoorPermissionCollection(FragmentActivity activity) {
        this.activity = activity;
    }

    public MoorPermissionCollection(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * All permissions that you want to request.
     * @param permissions A vararg param to pass permissions.
     * @return PermissionBuilder itself.
     */
    public PermissionBuilder permissions(String... permissions)  {
        return permissions(new ArrayList<>(Arrays.asList(permissions)));
    }

    /**
     * All permissions that you want to request.
     * @param permissions A vararg param to pass permissions.
     * @return PermissionBuilder itself.
     */
    public PermissionBuilder permissions(List<String> permissions)  {
        Set<String> permissionSet = new HashSet<>(permissions);
        boolean requireBackgroundLocationPermission = false;
        Set<String> permissionsWontRequest = new HashSet<>();
        if (permissionSet.contains(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)) {
            int osVersion = Build.VERSION.SDK_INT;
            int targetSdkVersion;
            if (fragment != null && fragment.getContext() != null) {
                targetSdkVersion = fragment.getContext().getApplicationInfo().targetSdkVersion;
            } else {
                targetSdkVersion = activity.getApplicationInfo().targetSdkVersion;
            }
            if (osVersion >= 30 && targetSdkVersion >= 30) {
                requireBackgroundLocationPermission = true;
                permissionSet.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION);
            } else if (osVersion < 29) {
                // If app runs under Android Q, there's no ACCESS_BACKGROUND_LOCATION permissions.
                // We remove it from request list, but will append it to the request callback as denied permission.
                permissionSet.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION);
                permissionsWontRequest.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION);
            }
        }
        return new PermissionBuilder(activity, fragment, permissionSet, requireBackgroundLocationPermission, permissionsWontRequest);
    }

}
