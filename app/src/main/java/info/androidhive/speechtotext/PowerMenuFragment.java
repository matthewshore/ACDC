package info.androidhive.speechtotext;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Becky on 2/12/2015.
 *
 * TODO: Add user feedback for on/off
 *       pictures?
 *       colors?
 *
 */
public class PowerMenuFragment extends Fragment {
    private LinearLayout powerLayout;
    private Button onButton1;
    private Button onButton2;
    private Button offButton1;
    private Button offButton2;
    private static String TAG = "PowerMenuFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_power, container, false);
        onButton1 = (Button) currentView.findViewById(R.id.power_on_1_button);
        onButton2 = (Button) currentView.findViewById(R.id.power_on_2_button);
        offButton1 = (Button) currentView.findViewById(R.id.power_off_1_button);
        offButton2 = (Button) currentView.findViewById(R.id.power_off_2_button);
        onButton1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("power 0");
                Log.d(TAG, "Sending BT message");
            }
        });
        offButton1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("power 1");
                Log.d(TAG, "Sending BT message");
            }
        });
        onButton2.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("power 2");
                Log.d(TAG, "Sending BT message");
            }
        });
        offButton2.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("power 3");
                Log.d(TAG, "Sending BT message");
            }
        });
        return currentView;
    }
}
