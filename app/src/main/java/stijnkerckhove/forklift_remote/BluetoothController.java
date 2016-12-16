package stijnkerckhove.forklift_remote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Stijn on 14/12/2016.
 */

public class BluetoothController {
    private AppCompatActivity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BluetoothDevice> discoveredDevices;

    public static final int REQUEST_ENABLE_BT = 100;

    public BluetoothController(AppCompatActivity activity) {
        this.activity = activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        discoveredDevices = new ArrayList<>();
    }

    //Check device for Bluetooth support
    public void checkBluetoothSupport() {
        if (mBluetoothAdapter == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(R.string.bluetooth_not_supported)
                    .setTitle(R.string.bluetooth_not_supported_dialog_title)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    //Check if Bluetooth is enabled
    public boolean checkBluetoothEnabled() {

        //// TODO: 12/12/2016 Listen for Bluetooth state broadcast
        return mBluetoothAdapter.isEnabled();

    }

    // Ask to enable Bluetooth if not enabled
    public void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    public void startDiscovery() {
        mBluetoothAdapter.startDiscovery();
    };

    public void stopDiscovery() {
        mBluetoothAdapter.cancelDiscovery();
    }

    public void addDiscoveredDevice(BluetoothDevice bluetoothDevice) {
        discoveredDevices.add(bluetoothDevice);
    }

    public BluetoothDevice getDiscoveredDevice(int i) {
        return discoveredDevices.get(i);
    }

    public void clearDiscoveredDevices() {
        discoveredDevices.clear();
    }

}
