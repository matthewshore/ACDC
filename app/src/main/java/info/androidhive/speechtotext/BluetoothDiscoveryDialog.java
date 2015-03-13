package info.androidhive.speechtotext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Becky on 3/12/2015.
 */
public class BluetoothDiscoveryDialog extends DialogFragment {

    private ArrayList<String> displayList;
    private ListView listView;
    private AdapterView.OnItemClickListener onItemClickListener;
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        listView = (ListView)inflater.inflate(R.layout.list_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.bluetooth_dialog_title);
        builder.setView(listView);
        builder.setCancelable(true);
        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(bluetoothDeviceAdapter);
        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MainActivity.connect((BluetoothDevice) adapterView.getItemAtPosition(position));
                BluetoothHandler.cancelDiscovery();
                dismiss();
            }
        };
        listView.setOnItemClickListener(onItemClickListener);
        return builder.create();
    }

    public void addDevice(BluetoothDevice device){

        if (bluetoothDeviceAdapter.getPosition(device) == -1)
            bluetoothDeviceAdapter.add(device);
    }
}
