package info.androidhive.speechtotext;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Becky on 3/13/2015.
 */
public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    public BluetoothDeviceAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.bluetooth_adapter_item, null);
        }
        BluetoothDevice bluetoothDevice = getItem(position);

        TextView deviceName = (TextView)v.findViewById(R.id.device_name);
        TextView deviceAddress = (TextView)v.findViewById(R.id.device_address);

            if(deviceName != null) {
                deviceName.setText(bluetoothDevice.getName());
                Log.d("BluetoothDeviceAdapter", "assigned device name");
            }
            else{
                Log.d("BluetoothDeviceAdapter", "could not assign device name");
            }
            if(deviceAddress != null) {
                deviceAddress.setText(bluetoothDevice.getAddress());
            }
        return v;
    }
}
