package com.onlylemi.android.capture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.onlylemi.android.sense.SensorActivity;

public class PreviewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PreviewSurface.ColorListener {

    private final static String TAG = "PreviewActivity:";

    private PreviewSurface previewSurface;
    private ColorCrosshairView crosshairView;

    private String ipname = ""; // ip address

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_preview);

        sp = getSharedPreferences("sp", 0);
        ipname = sp.getString("ipname", "");

        // camera view
        previewSurface = (PreviewSurface) findViewById(R.id.preview_surface);
        // color id view
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
        } else if (id == R.id.nav_set_sense) {
            Intent intent = new Intent().setClass(PreviewActivity.this, SensorActivity.class);
            intent.putExtra("ipname", ipname);
            startActivity(intent);
        }
        return true;
    }

    /**
     * set ip
     */
    private void setIdDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set IP To Login Server");
        LinearLayout loginForm = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_setip,
                null);
        builder.setView(loginForm);
        final EditText ipnameEdit = (EditText) loginForm.findViewById(R.id.ipname);
        ipnameEdit.setText(ipname);
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ipname = ipnameEdit.getText().toString().trim();
                sp.edit().putString("ipname", ipname).commit();

                previewSurface.setIP(ipnameEdit.getText().toString().trim());
                Log.i(TAG, "ipname:" + ipnameEdit.getText().toString());

                Toast.makeText(PreviewActivity.this, "connected server : " + ipnameEdit.getText()
                                .toString(),
                        Toast.LENGTH_SHORT).show();


            }
        });
        builder.create().show();
    }

    @Override
    public void onColor(final int color) {
        crosshairView.setColor(color);
    }

}
