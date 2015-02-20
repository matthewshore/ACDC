package info.androidhive.speechtotext;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * TODO: Create TV and Bed fragments
 */

public class MainActivity extends Activity {

    /**
     * Menu buttons
     */
    private Button tvMenuButton;
    private Button fanMenuButton;
    private Button bedMenuButton;
    private Button powerMenuButton;

    /**
     * Variables related to fragment management
     */
    private static FragmentManager fragmentManager;
    private PowerMenuFragment powerMenuFragment;
    private FanMenuFragment fanMenuFragment;
    private static Fragment currentFragment;

    /**
     * Variables related to interacting with service
     */
    private boolean mIsBound = false;
    private Messenger mServiceMessenger;
    private CommandHandler commandHandler;
    private ServiceConnection mConnection = new ServiceConnection() {

        /**
         * Purpose:     Handle connection to service, start continuous voice rec
         * Params:      className - Class name of the service that is created
         *              service - pointer to service
         * Returns:     Nothing
         *
         * @param className Class name of the service that is created
         * @param service pointer to service
         */
        public void onServiceConnected(ComponentName className, IBinder service) {
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

        /**
         * Purpose:     Handle disconnection from service
         * Params:      className - Class name of service that is disconnected
         * Returns:     Nothing
         *
         * @param className Class name of service that is disconnected
         */
        public void onServiceDisconnected(ComponentName className) {
            mServiceMessenger = null;
        }
    };

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        initFragments();
        initButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // bind VoiceRecService, start service
        if (!mIsBound) {
            doBindService();
            Log.d(TAG, "bound service");
        }

        // register for messages broadcast by VoiceRecService
        commandHandler = new CommandHandler();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VoiceRecService.COMMAND_ACTION);
        registerReceiver(commandHandler, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // unbind VoiceRecService, stop service
        if (mIsBound) {
            doUnbindService();
            Log.d(TAG, "unbound service");
        }

        // unregister for messages
        unregisterReceiver(commandHandler);
    }

    /**
     * Purpose:     Initializes all menu fragments
     * Params:      None
     * Returns:     Nothing
     *
     * TODO: need to add TVMenuFragment and BedMenuFragment
     */
    private void initFragments() {
        fragmentManager = getFragmentManager();
        powerMenuFragment = new PowerMenuFragment();
        fanMenuFragment = new FanMenuFragment();

        fragmentManager.beginTransaction().add(R.id.fragment, powerMenuFragment).commit();
        fragmentManager.beginTransaction().detach(powerMenuFragment).add(R.id.fragment, fanMenuFragment).commit();

        currentFragment = fanMenuFragment;
    }

    /**
     * Purpose:     Changes fragments, to be used when one of the menu buttons is pressed
     * Params:      fragment - menu fragment to be displayed
     * Returns:     Nothing
     * @param fragment menu fragment to be displayed
     */
    private void changeFragment(Fragment fragment) {
        fragmentManager.beginTransaction().detach(currentFragment).attach(fragment).commit();
        currentFragment = fragment;
    }

    /**
     * Purpose:     Initializes menu buttons
     * Params:      None
     * Returns:     Nothing
     * TODO: Add OnClickListeners for bed and TV menu buttons
     */
    private void initButtons() {
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
    }

    /**
     * Purpose:     Used to connect to service
     * Params:      None
     * Returns:     Nothing
     */
    private void doBindService() {
        bindService(new Intent(MainActivity.this,
                VoiceRecService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /**
     * Purpose:     Used to disconnect from service
     * Params:      None
     * Returns:     Nothing
     */
    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    /**
     * Purpose:     Generic method to create a toast with given message and duration
     * Params:      message - text that should be in toast
     *              duration - length of toast, use Toast.LENGTH_SHORT, Toast.LENGTH_LONG, etc.
     * @param message text that should be in toast
     * @param duration length of toast, use Toast.LENGTH_SHORT, Toast.LENGTH_LONG, etc.
     */
    public void toaster(String message, int duration)
    {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    /**
     * Purpose:     This class is used to receive and deal with messages sent from the VoiceRecService
     */
    private class CommandHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> commands = intent.getStringArrayListExtra("COMMAND");
            Log.d(TAG, "got commands: " + String.valueOf(commands));
            toaster("got commands: " + String.valueOf(commands), Toast.LENGTH_SHORT);

            // Voice service only seems to work again once commands are received if you "restart" the service using
            // a cancel message then a start listening message
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
