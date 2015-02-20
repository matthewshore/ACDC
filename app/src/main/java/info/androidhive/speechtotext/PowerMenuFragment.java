package info.androidhive.speechtotext;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Becky on 2/12/2015.
 *
 * TODO: Add button functionality
 */
public class PowerMenuFragment extends Fragment {
    private LinearLayout powerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_power, container, false);
    }
}
