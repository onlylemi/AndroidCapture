package com.onlylemi.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.onlylemi.android.capture.R;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseActivity
 *
 * @author qijianbin
 * @time 2017/3/19
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 202; //权限请求码
    private boolean isNeedCheckPermission = true; //判断是否需要检测，防止无限弹框申请权限

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedCheckPermission) {
            checkAllNeedPermissions();
        }
    }

    protected void checkAllNeedPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        List<String> needRequestPermissionList = getDeniedPermissions(getNeedPermissions());
        if (needRequestPermissionList != null && !needRequestPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, needRequestPermissionList.toArray(
                    new String[needRequestPermissionList.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


    protected boolean isGrantedAllPermission() {
        List<String> needRequestPermissionList = getDeniedPermissions(getNeedPermissions());
        return needRequestPermissionList.size() == 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] paramArrayOfInt) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!verifyPermissions(paramArrayOfInt)) {
                permissionGrantedFail();
                showTipsDialog();
                isNeedCheckPermission = false;
            } else {
                permissionGrantedSuccess();
            }
        }
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    protected void showTipsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setMessage("Current application miss " + getDialogTipsPart()
                + " permission, so this feature is not available. If you would go on, "
                + "please click the button of [OK] and go to setting center to permit!")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    protected String getDialogTipsPart() {
        return "必要";
    }

    protected String[] getNeedPermissions() {
        return null;
    }

    protected void permissionGrantedSuccess() {

    }

    protected void permissionGrantedFail() {

    }
}
