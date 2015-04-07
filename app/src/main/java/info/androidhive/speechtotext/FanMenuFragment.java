package info.androidhive.speechtotext;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Becky on 2/12/2015.
 * TODO: Add button functionality
 */
public class FanMenuFragment extends Fragment {

    private Button powerButton;
    private Button hotterButton;
    private Button colderButton;
    private Button higherButton;
    private Button lowerButton;
    private Button rotateButton;
    private static String TAG = "FanMenuFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View currentView = inflater.inflate(R.layout.fragment_fan, container, false);
        //create buttons
        powerButton = (Button) currentView.findViewById(R.id.fan_power);
        hotterButton = (Button) currentView.findViewById(R.id.fan_hotter);
        colderButton = (Button) currentView.findViewById(R.id.fan_colder);
        higherButton = (Button) currentView.findViewById(R.id.fan_higher);
        lowerButton = (Button) currentView.findViewById(R.id.fan_lower);
        rotateButton = (Button) currentView.findViewById(R.id.fan_rotate);
        //set listeners for buttons
        powerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("fan 0");
                Log.d(TAG, "Sending BT message");
            }
        });
        hotterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("fan 1");
                Log.d(TAG, "Sending BT message");
            }
        });
        colderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("fan 2");
                Log.d(TAG, "Sending BT message");
            }
        });
        lowerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("fan 3");
                Log.d(TAG, "Sending BT message");
            }
        });
        higherButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("fan 4");
                Log.d(TAG, "Sending BT message");
            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("fan 5");
                Log.d(TAG, "Sending BT message");
            }
        });


        

        return currentView;
    }
}
