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
 * Created by Matt on 2/24/2015.
 */
public class TVMenuFragment extends Fragment {

    private Button powerButton;
    private Button channelUpButton;
    private Button channelDownButton;
    private Button volumeUpButton;
    private Button volumeDownButton;
    private static String TAG = "TVMenuFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment
        View currentView = inflater.inflate(R.layout.fragment_tv, container, false);
        //create buttons
        powerButton = (Button) currentView.findViewById(R.id.tv_power);
        channelUpButton = (Button) currentView.findViewById(R.id.tv_channel_up);
        channelDownButton = (Button) currentView.findViewById(R.id.tv_channel_down);
        volumeUpButton = (Button) currentView.findViewById(R.id.tv_volume_up);
        volumeDownButton = (Button) currentView.findViewById(R.id.tv_volume_down);
        //set listeners for buttons
        powerButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("tv 0");
                Log.d(TAG, "Sending BT message");
            }
        });

        channelUpButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("tv 3");
                Log.d(TAG, "Sending BT message");
            }
        });
        channelDownButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("tv 4");
                Log.d(TAG, "Sending BT message");
            }
        });
        volumeUpButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("tv 1");
                Log.d(TAG, "Sending BT message");
            }
        });
        volumeDownButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("tv 2");
                Log.d(TAG, "Sending BT message");
            }
        });
        return currentView;
    }

}
