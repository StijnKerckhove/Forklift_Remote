package stijnkerckhove.forklift_remote.fragments;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import stijnkerckhove.forklift_remote.R;

/**
 * Created by Stijn on 14/12/2016.
 */

public class FragmentFactory {
    private AppCompatActivity activity;
    private FragmentManager fragmentManager;

    public FragmentFactory(AppCompatActivity activity) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();

    }

    public void navigateToControllerFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, new ControllerFragment());
        fragmentTransaction.commit();
    }

    public void navigateToLinkBluetoothDeviceFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, new LinkBluetoothDeviceFragment());
        fragmentTransaction.commit();
    }
}
