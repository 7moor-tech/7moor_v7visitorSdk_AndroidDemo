package com.moor.imkf.demo.view.permissiondialog;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.permission.MoorPermissionMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/29/21
 *     @desc   : 申请权限被拒绝时的弹框提示
 *     @version: 1.0
 * </pre>
 */
public class MoorPermissionDialog extends RationaleDialog {

    private Button positiveBtn;
    private Button negativeBtn;
    private TextView messageText;
    private LinearLayout permissionsLayout;
    private final List<String> permissions;
    private final Context context;
    private final String message;
    private final String positiveText;
    private final String negativeText;

    public MoorPermissionDialog(@NonNull Context context, List<String> permissions, String message, String positiveText, String negativeText) {
        super(context, R.style.moor_PermissionXDefaultDialog);
        this.context = context;
        this.permissions = permissions;
        this.message = message;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moor_layout_permission_default_dialog);
        messageText = findViewById(R.id.messageText);
        permissionsLayout = findViewById(R.id.permissionsLayout);
        positiveBtn = findViewById(R.id.positiveBtn);
        negativeBtn = findViewById(R.id.negativeBtn);
        setupText();
        buildPermissionsLayout();
        setupWindow();
    }


    @NonNull
    @Override
    public View getPositiveButton() {
        return positiveBtn;
    }

    @Nullable
    @Override
    public View getNegativeButton() {
        return negativeBtn;
    }

    @NonNull
    @Override
    public List<String> getPermissionsToRequest() {
        return permissions;
    }

    private void setupText() {
        messageText.setText(message);
        positiveBtn.setText(positiveText);
        negativeBtn.setText(negativeText);
    }

    private void buildPermissionsLayout() {
        Set<String> groupSet = new HashSet<>();
        int currentVersion = Build.VERSION.SDK_INT;
        String permissionGroup = "";
        for (String permission : permissions) {
            if (currentVersion >= Build.VERSION_CODES.Q) {
                permissionGroup = MoorPermissionMap.getPermissionMapOnQ().get(permission);
            }
            else {
                try {
                    PermissionInfo permissionInfo = context.getPackageManager().getPermissionInfo(permission, 0);
                    permissionGroup = permissionInfo.group;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(permissionGroup) && !groupSet.contains(permissionGroup)) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.moor_item_dialog_permission, null);
                ImageView permissionIcon = inflate.findViewById(R.id.permissionIcon);
                TextView permissionText = inflate.findViewById(R.id.permissionText);
                try {
                    permissionText.setText(context.getString(context.getPackageManager().getPermissionGroupInfo(permissionGroup, 0).labelRes));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    permissionIcon.setImageResource(context.getPackageManager().getPermissionGroupInfo(permissionGroup, 0).icon);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                View rootView = inflate.getRootView();
                permissionsLayout.addView(rootView);
                groupSet.add(permissionGroup);
            }
        }
    }

    private void setupWindow() {

        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        if (width < height) {
            attributes.width = (int) (width * 0.86);
        } else {
            attributes.width = (int) (width * 0.6);
        }
    }


}
