package com.onlylemi.android.sense;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.onlylemi.android.capture.R;
import com.onlylemi.android.capture.SendDataThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private final static String TAG = "SensorActivity";

    public final static int[] TYPE_SENSORS = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_LIGHT, Sensor
            .TYPE_ORIENTATION, Sensor.TYPE_PROXIMITY, Sensor.TYPE_TEMPERATURE, Sensor
            .TYPE_PRESSURE, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_MAGNETIC_FIELD};

    private ListView sensorListView = null;
    private SensorListAdapter sensorListAdapter = null;
    private List<SensorInfo> sensorList;

    private SensorManager sensorManager = null;

    private String ipname = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Intent intent = getIntent();
        ipname = intent.getStringExtra("ipname");

        Log.i(TAG, "ipname:" + ipname);

        sensorListView = (ListView) findViewById(R.id.sensor_list_view);

        // loading list data
        sensorList = loadSensorInfoDate();

        // set listadapter to sensorlist
        sensorListAdapter = new SensorListAdapter(this, sensorList);
        sensorListView.setAdapter(sensorListAdapter);

        Log.i(TAG, "sensorListAdapter init");

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerAllSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * register phone all Sensor
     */
    private void registerAllSensor() {
        // register accelerometer sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        // register light sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI);
        // register orientation sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        // register proximity sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_UI);
        // register temperature sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_TEMPERATURE), SensorManager.SENSOR_DELAY_UI);
        // register pressure sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_PRESSURE), SensorManager.SENSOR_DELAY_UI);
        // register gyroscope sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);
        // register magnetic_field sense
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor
                .TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * load sensor data: name image
     *
     * @return sensor list
     */
    private List<SensorInfo> loadSensorInfoDate() {
        List<SensorInfo> sensorList = new ArrayList<>();

        // get sensor names
        String[] sensorNames = getResources().getStringArray(R.array.sensor_names);
        // get sensor image resources
        TypedArray sensorImages = getResources().obtainTypedArray(R.array.sensor_image);

        SensorInfo sensorInfo = null;
        for (int i = 0; i < sensorNames.length; i++) {
            sensorInfo = new SensorInfo();
            sensorInfo.setName(sensorNames[i]);
            sensorInfo.setImageId(sensorImages.getResourceId(i, 0));

            sensorList.add(sensorInfo);
        }
        return sensorList;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        JSONObject json = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                jsonArray.put("TYPE_ACCELEROMETER");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            case Sensor.TYPE_LIGHT:
                jsonArray.put("TYPE_LIGHT");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            case Sensor.TYPE_ORIENTATION:
                jsonArray.put("TYPE_ORIENTATION");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            case Sensor.TYPE_PROXIMITY:
                jsonArray.put("TYPE_PROXIMITY");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            case Sensor.TYPE_TEMPERATURE:
                jsonArray.put("TYPE_TEMPERATURE");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            case Sensor.TYPE_PRESSURE:
                jsonArray.put("TYPE_PRESSURE");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            case Sensor.TYPE_GYROSCOPE:
                jsonArray.put("TYPE_GYROSCOPE");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                jsonArray.put("TYPE_MAGNETIC_FIELD");
                jsonArray.put(addJsonObject(sensorEvent));
                break;
            default:
                break;
        }

        try {
            json.put("ANDROID_SENSOR", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "qian" + json.toString());
        byte[] byteBuffer = new byte[1024];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.toString().getBytes());
        ByteArrayOutputStream outputStrem = new ByteArrayOutputStream();
        try {
            int amount;
            while ((amount = inputStream.read(byteBuffer)) != -1) {
                outputStrem.write(byteBuffer, 0, amount);
            }
            inputStream.close();
            outputStrem.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "hou1" + new String(outputStrem.toByteArray()));
        Log.i(TAG, "hou2" + outputStrem.toByteArray().toString());

        // send json data to server
        Thread th = new SendDataThread(outputStrem, ipname);
        th.start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * @param sensorEvent
     * @return
     */
    private JSONObject addJsonObject(SensorEvent sensorEvent) {
        JSONObject jo1 = new JSONObject();
        try {
            jo1.put("value0", sensorEvent.values[0]);
            jo1.put("value1", sensorEvent.values[1]);
            jo1.put("value2", sensorEvent.values[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo1;
    }

    /**
     * register sensor listener
     *
     * @param listener
     * @param type
     */
    public void registerListener(SensorEventListener listener, int type) {
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(type),
                SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * unregister sensor listener
     *
     * @param listener
     * @param type
     */
    public void unRegisterListener(SensorEventListener listener, int type) {
        sensorManager.unregisterListener(listener, sensorManager.getDefaultSensor(type));
    }
}
