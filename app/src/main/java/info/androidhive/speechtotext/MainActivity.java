package info.androidhive.speechtotext;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognizerIntent;
import android.util.Log;
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

    private boolean mIsBound = false;
    private VoiceRecService voiceRecService;

    private CommandHandler commandHandler;

    private static final String TAG = "MainActivity";

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

        voiceRecService = new VoiceRecService();

    }

    private void initFragments() {
        fragmentManager = getFragmentManager();
        powerMenuFragment = new PowerMenuFragment();
        fanMenuFragment = new FanMenuFragment();

        fragmentManager.beginTransaction().add(R.id.fragment, powerMenuFragment).commit();
        fragmentManager.beginTransaction().detach(powerMenuFragment).add(R.id.fragment, fanMenuFragment).commit();

        currentFragment = fanMenuFragment;
    }

    private void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction().detach(currentFragment).attach(fragment).commit();
        currentFragment = fragment;
    }

    /**
     * Showing google speech input dialog
     */
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
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result.get(0).toString().equals("volume up")) {
                        txtSpeechInput.setText("You got it! Turning volume up.");
                    } else if (result.get(0).toString().equals("fan on")) {
                        txtSpeechInput.setText("You got it! Turning fan on.");
                    } else txtSpeechInput.setText("Not Quite! Got:\n" +
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

    private VoiceRecService mBoundService;
    private Messenger mServiceMessenger;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            //mBoundService = ((VoiceRecService.VoiceRecBinder) service).getService();

            Log.d(TAG, "onServiceConnected"); //$NON-NLS-1$

            mServiceMessenger = new Messenger(service);

            Message msg = new Message();
            msg.what = VoiceRecService.MSG_RECOGNIZER_START_LISTENING;

            try {
                mServiceMessenger.send(msg);
                //voiceRecService.mServerMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(MainActivity.this,
                VoiceRecService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mIsBound) {
            doBindService();
            Log.d(TAG, "bound service");
        }
        commandHandler = new CommandHandler();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VoiceRecService.COMMAND_ACTION);
        registerReceiver(commandHandler, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBound) {
            doUnbindService();
            Log.d(TAG, "unbound service");
        }
    }

    private class CommandHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> commands = intent.getStringArrayListExtra("COMMAND");
            Log.d(TAG, "got commands: " + String.valueOf(commands));
            Toast.makeText(context, "got commands: " + String.valueOf(commands), Toast.LENGTH_SHORT).show();

            Message msg = new Message();
            msg.what = VoiceRecService.MSG_RECOGNIZER_CANCEL;
            try {
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Message msg2 = new Message();
            msg2.what = VoiceRecService.MSG_RECOGNIZER_START_LISTENING;

            try {
                mServiceMessenger.send(msg2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }
}
