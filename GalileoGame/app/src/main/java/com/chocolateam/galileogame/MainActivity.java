package com.chocolateam.galileogame;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.galfins.gnss_compare.GNSSCompareInitFragment;
import com.galfins.gnss_compare.StartGNSSFragment;

public class MainActivity extends AppCompatActivity implements GNSSCompareInitFragment.OnFinishedListener{

    private boolean mLocationPermissionGranted = false;
    private StartGNSSFragment startedFragment;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int LOCATION_REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        requestPermissionAndSetupFragments(this);

        while ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            requestPermissionAndSetupFragments(this);
        }

        findViewById(R.id.GameButton).setEnabled(false);
        findViewById(R.id.GameButton).setAlpha(0.6f);
        findViewById(R.id.spaceshipButton).setEnabled(false);
        findViewById(R.id.spaceshipButton).setAlpha(0.6f);
        findViewById(R.id.MapButton).setEnabled(false);
        findViewById(R.id.MapButton).setAlpha(0.6f);

        checkLocationAndMobileDataEnabled();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        startedFragment = new StartGNSSFragment();
        fragmentTransaction.add(android.R.id.content, startedFragment).commit();
    }

    @Override
    protected void onResume() {
        // Start the blank fragment initiating Gal/Gps PVT on app start
        super.onResume();
        if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            // FragmentManager fragmentManager = getSupportFragmentManager();
            // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // PvtFragment pvtFrag = new PvtFragment();
            // fragmentTransaction.add(android.R.id.content, pvtFrag).commit();
//            Log.e("uvodny text", String.valueOf(PvtFragment.getUserLatitudeDegrees()));
        }


    }

    public void goToGame(View view) {
        Intent intent = new Intent(this, com.chocolateam.galileomap.MapWithGameActivity.class);
        intent.putExtra("location_permit", mLocationPermissionGranted);
        if (checkLocationAndMobileDataEnabled()) {
            startActivity(intent);
        }
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, com.chocolateam.galileomap.MapsOnlyActivity.class);
        intent.putExtra("location_permit", mLocationPermissionGranted);
        if (checkLocationAndMobileDataEnabled()){
            startActivity(intent);
        }
    }

    public void goToSpaceship(View view) {
        Intent intent = new Intent(this, com.chocolateam.galileospaceship.SpaceshipViewActivity.class);
        if (checkLocationAndMobileDataEnabled()) {
            startActivity(intent);
        }
    }

    /**
    public void goToPVT(View view) {
        Intent intent = new Intent(this, PvtActivity.class);
        startActivity(intent);
    }
     **/

    public void goToDesc(View view) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        checkLocationAndMobileDataEnabled();
        startActivity(intent);
    }

    private boolean hasPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Permissions granted at install time.
            return true;
        }
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissionAndSetupFragments(final Activity activity) {
        if (hasPermissions(activity)) {
            return;
        } else {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, LOCATION_REQUEST_ID);
        }
    }

    @Override
    public void onFragmentReady() {
        findViewById(R.id.GameButton).setEnabled(true);
        findViewById(R.id.GameButton).setAlpha(1.0f);
        findViewById(R.id.spaceshipButton).setEnabled(true);
        findViewById(R.id.spaceshipButton).setAlpha(1.0f);
        findViewById(R.id.MapButton).setEnabled(true);
        findViewById(R.id.MapButton).setAlpha(1.0f);
    }

    /**
     * Checks if Mobile data and Location Services are enabled and displays an Alert dialog box
     * warning the user that they are required.
     */
    public boolean checkLocationAndMobileDataEnabled() {
        final Context context = getApplicationContext();
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean gps_enabled = false;
        boolean network_enabled = false;
        boolean mobileDataEnabled = false;
        NetworkInfo activeNetwork = null;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch(Exception ex) {}

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            mobileDataEnabled = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }

        if(!(gps_enabled && network_enabled && mobileDataEnabled)) {
            // notify user
            Log.e("CHECK 1", "Services not enabled ");
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(context.getResources().getString(R.string.services_not_enabled));
            dialog.setNeutralButton(context.getString(R.string.Ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // do Nothing
                }
            });
            dialog.show();
            return false;
        }
        else {
            Log.e("CHECK 2", "All services enabled ");
            return true;
        }
    }
}

