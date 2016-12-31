package stijnkerckhove.forklift_remote.fragments;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import stijnkerckhove.forklift_remote.BluetoothController;
import stijnkerckhove.forklift_remote.DialogController;
import stijnkerckhove.forklift_remote.MainActivity;
import stijnkerckhove.forklift_remote.ManageConnectionTask;
import stijnkerckhove.forklift_remote.R;
import stijnkerckhove.forklift_remote.exceptions.FailedBluetoothConnectionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LinkBluetoothDeviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LinkBluetoothDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinkBluetoothDeviceFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView pairedDevicesListView;

    @BindView(R.id.discoveredDevicesProgressbar)
    ProgressBar discoveredDevicesProgressbar;

    @BindView(R.id.DiscoveredDevicesTextView)
    TextView discoveredDevicesTextView;

    @BindView(R.id.discoveredDevicesListView)
    ListView discoveredDevicesListView;

    @BindView(R.id.searchDevicesButton)
    Button searchDevicesButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private BluetoothController bluetoothController;
    private ArrayAdapter mArrayAdapter;
    private ArrayAdapter discoveredDevicesAdapter;
    private MainActivity mainActivity;


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                bluetoothController.addDiscoveredDevice(device);
                discoveredDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    private Handler handler;
    private Runnable r;

    public LinkBluetoothDeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LinkBluetoothDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LinkBluetoothDeviceFragment newInstance(String param1, String param2) {
        LinkBluetoothDeviceFragment fragment = new LinkBluetoothDeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mainActivity = (MainActivity) getActivity();
        bluetoothController = mainActivity.getBluetoothController();

        mainActivity.getNavigationView().getMenu().getItem(1).setChecked(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_link_bluetooth_device, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        //Stop Bluetooth device discovery after x seconds
        handler = new Handler();
        r = new Runnable() {
            public void run() {
                bluetoothController.stopDiscovery();
                searchDevicesButton.setVisibility(View.VISIBLE);
                discoveredDevicesProgressbar.setVisibility(View.GONE);
            }
        };

        // If there are paired devices
        if (bluetoothController.getPairedDevices().size() > 0) {
            mArrayAdapter = new ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1);
            discoveredDevicesAdapter = new ArrayAdapter(mainActivity, android.R.layout.simple_list_item_1);

            // Loop through paired devices
            for (BluetoothDevice device : bluetoothController.getPairedDevices()) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }

            pairedDevicesListView = (ListView) mainActivity.findViewById(R.id.pairedDevicesListView);
            pairedDevicesListView.setAdapter(mArrayAdapter);
        }

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        searchDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discoveredDevicesAdapter.clear();
                bluetoothController.clearDiscoveredDevices();
                view.setVisibility(View.GONE);
                discoveredDevicesListView.setAdapter(discoveredDevicesAdapter);
                discoveredDevicesTextView.setVisibility(View.VISIBLE);
                discoveredDevicesListView.setVisibility(View.VISIBLE);
                discoveredDevicesProgressbar.setVisibility(View.VISIBLE);
                bluetoothController.startDiscovery();
                handler.postDelayed(r, 10000);
            }
        });

        discoveredDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothController.connect(bluetoothController.getDiscoveredDevice(i));
            }
        });

        pairedDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothController.connect(bluetoothController.getPairedDevice(i));
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
