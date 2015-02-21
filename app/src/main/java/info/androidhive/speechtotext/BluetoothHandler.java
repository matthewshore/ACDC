package info.androidhive.speechtotext;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Becky on 2/20/2015.
 */
public class BluetoothHandler {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // TODO: get server address by searching, not hard coding
    private static String serverAddress = "00:16:A4:09:3A:71";

    public BluetoothHandler(BluetoothAdapter bluetoothAdapter){
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void openSocket() {
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
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
