package com.onlylemi.android.sense;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.onlylemi.android.capture.R;

import java.util.List;

/**
 * Created by only乐秘 on 2015-12-29.
 */
public class SensorListAdapter extends BaseAdapter {

    public final static String TAG = "SensorListAdapter";
    public List<SensorInfo> list;

    private SensorActivity context;

    public SensorListAdapter(SensorActivity context, List<SensorInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.sensor_item, null);

            holder.sensorImageView = (ImageView) view.findViewById(R.id.senso_image);
            holder.sensorNameView = (TextView) view.findViewById(R.id.sensor_name);
            holder.sensorSwitch = (Switch) view.findViewById(R.id.sensor_switch);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.sensorNameView.setText(list.get(i).getName());
        holder.sensorImageView.setImageResource(list.get(i).getImageId());

        final int index = i;
        holder.sensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener
                () {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(context, list.get(index).getName() + " ON", Toast
                            .LENGTH_SHORT)
                            .show();
                    context.registerListener(context, SensorActivity.TYPE_SENSORS[index]);
                } else {
                    Toast.makeText(context, list.get(index).getName() + " OFF", Toast
                            .LENGTH_SHORT)
                            .show();
                    context.unRegisterListener(context, SensorActivity.TYPE_SENSORS[index]);
                }
            }
        });

        return view;
    }

    class ViewHolder {

        private TextView sensorNameView;
        private ImageView sensorImageView;
        private Switch sensorSwitch;

    }
}
