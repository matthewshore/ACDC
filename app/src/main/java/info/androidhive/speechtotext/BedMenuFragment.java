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
public class BedMenuFragment extends Fragment {

    private Button kneeUp;
    private Button kneeDown;
    private static String TAG = "BedMenuFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View currentView = inflater.inflate(R.layout.fragment_bed, container, false);
        //create buttons
        kneeDown = (Button) currentView.findViewById(R.id.bed_knee_down);
        kneeUp = (Button) currentView.findViewById(R.id.bed_knee_up);

        //set listeners for buttons
        kneeDown.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("bed 0");
                Log.d(TAG, "Sending BT message");
            }
        });
        kneeUp.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity.bluetoothHandler.sendMessage("bed 1");
                Log.d(TAG, "Sending BT message");
            }
        });

        return inflater.inflate(R.layout.fragment_bed, container, false);
    }
}
