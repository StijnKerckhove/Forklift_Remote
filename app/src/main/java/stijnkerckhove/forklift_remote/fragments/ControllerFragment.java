package stijnkerckhove.forklift_remote.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import stijnkerckhove.forklift_remote.MainActivity;
import stijnkerckhove.forklift_remote.ManageConnectionTask;
import stijnkerckhove.forklift_remote.R;

enum CONNECTIONSTATUS {
    CONNECTED,
    DISCONNECTED
}

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ControllerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControllerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.driveForwardButton)
    ImageButton driveForwardButton;
    @BindView(R.id.driveBackwardButton)
    ImageButton driveBackwardButton;
    @BindView(R.id.turnLeftButton)
    ImageButton turnLeftButton;
    @BindView(R.id.turnRightButton)
    ImageButton turnRightButton;
    @BindView(R.id.liftUpButton)
    ImageButton liftUpButon;
    @BindView(R.id.liftDownButton)
    ImageButton liftDownButton;
    @BindView(R.id.tiltFowardButton)
    ImageButton tiltForwardButton;
    @BindView(R.id.tiltBackwardButton)
    ImageButton tiltBackwardButton;
    @BindView(R.id.deviceStatusTextview)
    TextView deviceStatusTextview;
    @BindView(R.id.deviceStatusImageView)
    ImageView deviceImageView;

    private OnFragmentInteractionListener mListener;
    private MainActivity mainActivity;
    private ManageConnectionTask manageConnectionTask;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;

    public ControllerFragment() {
        // Required empty public constructor

        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                switch (action){
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        changeDeviceConnectionStatus(CONNECTIONSTATUS.CONNECTED);
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        changeDeviceConnectionStatus(CONNECTIONSTATUS.DISCONNECTED);
                        mainActivity.getBluetoothController().disconnectSocket();
                        break;
                }
            }
        };
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControllerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControllerFragment newInstance(String param1, String param2) {
        ControllerFragment fragment = new ControllerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        mainActivity.registerReceiver(broadcastReceiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        mainActivity.unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        mainActivity.getNavigationView().getMenu().getItem(0).setChecked(true);
        BluetoothSocket bluetoothSocket = mainActivity.getBluetoothController().getBluetoothSocket();

        if(bluetoothSocket != null && bluetoothSocket.isConnected()) {
            manageConnectionTask = new ManageConnectionTask(bluetoothSocket, getContext());

            changeDeviceConnectionStatus(CONNECTIONSTATUS.CONNECTED);

            driveForwardButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("driveforward".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("drivestop".getBytes());
                            break;
                    }
                    return false;
                }
            });

            driveBackwardButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("drivebackward".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("drivestop".getBytes());
                            break;
                    }
                    return false;
                }
            });

            liftUpButon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("liftup".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("liftstop".getBytes());
                            break;
                    }
                    return false;
                }
            });

            liftDownButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("liftdown".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("liftstop".getBytes());
                            break;
                    }
                    return false;
                }
            });

            turnLeftButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("steerleft".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("steerstop".getBytes());
                            break;
                    }
                    return false;
                }
            });

            turnRightButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("steerright".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("steerstop".getBytes());
                            break;
                    }
                    return false;
                }
            });

            tiltForwardButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("tiltforward".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("tiltstop".getBytes());
                            break;
                    }
                    return false;
                }
            });

            tiltBackwardButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            manageConnectionTask.write("tiltbackward".getBytes());
                            break;
                        case MotionEvent.ACTION_UP:
                            manageConnectionTask.write("tiltstop".getBytes());
                            break;
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_controller, container, false);
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

    public void changeDeviceConnectionStatus(CONNECTIONSTATUS status) {
        if(status == CONNECTIONSTATUS.CONNECTED) {
            deviceStatusTextview.setTextColor(ContextCompat.getColor(mainActivity, R.color.green));
            deviceStatusTextview.setText(R.string.deviceConnected);
            deviceImageView.setImageResource(R.drawable.ic_info_green_20dp);
        }
        else {
            deviceStatusTextview.setTextColor(ContextCompat.getColor(mainActivity, R.color.red));
            deviceStatusTextview.setText(R.string.deviceDisconnected);
            deviceImageView.setImageResource(R.drawable.ic_info_red_20dp);
        }
    }
}
