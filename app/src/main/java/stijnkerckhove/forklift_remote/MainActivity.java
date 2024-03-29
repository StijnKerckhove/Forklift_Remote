package stijnkerckhove.forklift_remote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import stijnkerckhove.forklift_remote.fragments.FragmentFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentFactory fragmentFactory;
    private BluetoothController bluetoothController;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        fragmentFactory = new FragmentFactory(this);
        bluetoothController = new BluetoothController(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        // Device does not support Bluetooth
        bluetoothController.checkBluetoothSupport();

        if (bluetoothController.checkBluetoothEnabled()) {
            pairDevice();
        } else {
            bluetoothController.enableBluetooth();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Do something when bluetooth gets enabled
        if (requestCode == BluetoothController.REQUEST_ENABLE_BT) {
            if (resultCode != -1) {
                Log.w("Bluetooth not enabled", BLUETOOTH_SERVICE);
            } else {
                pairDevice();
            }
        }


    }

    public void pairDevice() {
        fragmentFactory.navigateToLinkBluetoothDeviceFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_controller) {
            fragmentFactory.navigateToControllerFragment();

        } else if (id == R.id.nav_connect) {
            fragmentFactory.navigateToLinkBluetoothDeviceFragment();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public BluetoothController getBluetoothController() {
        return bluetoothController;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }


}
