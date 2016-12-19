package stijnkerckhove.forklift_remote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import stijnkerckhove.forklift_remote.exceptions.FailedBluetoothConnectionException;
import stijnkerckhove.forklift_remote.fragments.FragmentFactory;

/**
 * Created by User on 17/12/2016.
 */

public class ConnectTask extends AsyncTask<Void, Void, Boolean> {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothController bluetoothController;
    private Context context;
    private ProgressDialog dialog;

    private BluetoothAdapter mBluetoothAdapter;
    private DialogController dialogController;

    public ConnectTask(BluetoothDevice device, Context context, BluetoothController controller) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mmDevice = device;
        this.context = context;
        dialogController = new DialogController(context);
        bluetoothController = controller;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
        } catch (IOException e) {
        }
        mmSocket = tmp;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "", "Trying to connect to device....", true);
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if (!bool) {
            dialogController.showUnableToConnectDialog();
        }
        else {
            bluetoothController.setBluetoothSocket(mmSocket);
            FragmentFactory fragmentFactory = new FragmentFactory((AppCompatActivity) context);
            fragmentFactory.navigateToControllerFragment();
        }
        dialog.dismiss();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out

            try {
                mmSocket.close();
            } catch (IOException closeException) {
            }

            return false;
        }

        return true;
    }

    public BluetoothSocket getBluetoothSocket() {
        return mmSocket;
    }
}
