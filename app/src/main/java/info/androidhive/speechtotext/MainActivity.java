package info.androidhive.speechtotext;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView txtSpeechInput;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;

    private Button tvMenuButton;
    private Button fanMenuButton;
    private Button bedMenuButton;
    private Button powerMenuButton;

    private PowerMenuFragment powerMenuFragment;
    private FanMenuFragment fanMenuFragment;

    private static FragmentManager fragmentManager;
    private static Fragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        initFragments();



		txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        tvMenuButton = (Button) findViewById(R.id.tv_menu_button);
        fanMenuButton = (Button) findViewById(R.id.fan_menu_button);
        bedMenuButton = (Button) findViewById(R.id.bed_menu_button);
        powerMenuButton = (Button) findViewById(R.id.power_menu_button);

        fanMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(fanMenuFragment);
            }
        });
        powerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(powerMenuFragment);
            }
        });

        // hide the action bar
		getActionBar().hide();

		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				promptSpeechInput();
			}
		});

	}

    private void initFragments(){
        fragmentManager = getFragmentManager();
        powerMenuFragment = new PowerMenuFragment();
        fanMenuFragment = new FanMenuFragment();

        fragmentManager.beginTransaction().add(R.id.fragment, powerMenuFragment).commit();
        fragmentManager.beginTransaction().detach(powerMenuFragment).add(R.id.fragment, fanMenuFragment).commit();

        currentFragment = fanMenuFragment;
    }

    private void changeFragment(Fragment fragment){
        fragmentManager.beginTransaction().detach(currentFragment).attach(fragment).commit();
        currentFragment = fragment;
    }

	/**
	 * Showing google speech input dialog
	 * */
	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				if (result.get(0).toString().equals("volume up")){
                    txtSpeechInput.setText("You got it! Turning volume up.");
                }
                else if (result.get(0).toString().equals("fan on")) {
                    txtSpeechInput.setText("You got it! Turning fan on.");
                }
                else txtSpeechInput.setText("Not Quite! Got:\n" +
                            result);
			}
			break;
		}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
