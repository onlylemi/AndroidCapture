package com.onlylemi.android.capture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PreviewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PreviewSurface.ColorListener {

    private final static String TAG = "PreviewActivity:";

    private PreviewSurface previewSurface;
    private ColorCrosshairView crosshairView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_preview);


        previewSurface = (PreviewSurface) findViewById(R.id.preview_surface);

        crosshairView = (ColorCrosshairView) findViewById(R.id.crosshair);
        crosshairView.setVisibility(View.GONE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_setip) {
            setIdDialog();
        } else if (id == R.id.nav_set_colorid) {
            previewSurface.setColorListener(this);
            crosshairView.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_preview) {
            previewSurface.setColorListener(null);
            crosshairView.setVisibility(View.GONE);
        }
        return true;
    }

    /**
     * set ip
     */
    private void setIdDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set IP To Login Server");
        LinearLayout loginForm = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_setip, null);
        builder.setView(loginForm);
        final EditText ipname = (EditText) loginForm.findViewById(R.id.ipname);
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                previewSurface.setIP(ipname.getText().toString().trim());
                Log.i(TAG, "ipname:" + ipname.getText().toString());

                Toast.makeText(PreviewActivity.this, "服务器地址：" + ipname.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    @Override
    public void onColor(final int color) {
        crosshairView.setColor(color);
    }

}
