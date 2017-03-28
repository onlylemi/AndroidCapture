package com.onlylemi.android;

import android.content.Intent;
import android.os.Bundle;

import com.onlylemi.android.capture.PreviewActivity;
import com.onlylemi.android.capture.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGrantedAllPermission()) {
            startPreview();
        }
    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[]{
                "android.permission.INTERNET",
                "android.permission.CAMERA"
        };
    }

    @Override
    protected String getDialogTipsPart() {
        return "CAMERA";
    }

    @Override
    protected void permissionGrantedSuccess() {
        super.permissionGrantedSuccess();
        startPreview();
    }

    private void startPreview() {
        Intent intent = new Intent();
        intent.setClass(this, PreviewActivity.class);
        startActivity(intent);
        finish();
    }
}
