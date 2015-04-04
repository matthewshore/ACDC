package info.androidhive.speechtotext;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Becky on 2/20/2015.
 */
public class BluetoothHandler {
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static BluetoothAdapter bluetoothAdapter;
    // TODO: get server address by searching, not hard coding
    private static String serverAddress = "";
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private Handler BTHandler = new Handler();

    public BluetoothHandler(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void connect(BluetoothDevice bluetoothDevice) {
        Log.d("BluetoothHandler", "Connecting to: " + bluetoothDevice.getName() + ":" + bluetoothDevice.getAddress());
        serverAddress = bluetoothDevice.getAddress();
        openSocket();
    }

    public static void cancelDiscovery() {
        bluetoothAdapter.cancelDiscovery();
    }

    public void openSocket() {
        if (serverAddress != "") {
            Log.d("BluetoothHandler", "Connecting...");
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(serverAddress);
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
            } catch (IOException e) {
                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeSocket() {
        if (outputStream != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        byte[] messageBuffer = message.getBytes();
        try {
            outputStream.write(messageBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean beginDiscovery() {
        return bluetoothAdapter.startDiscovery();
    }
}
