package edu.ualberta.cmput301f19t17.bigmood.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * This class is used to help set the location of a mood in MapFragment
 */
public class LocationHelper implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int PERMISSION_LOCATION_REQUEST_CODE = 199;
    private static final long INTERVAL = 60 * 1000;
    private static final long FASTEST_INTERVAL = 60 * 1000;

    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private LocationRequestUpdatesListener listener;
    private boolean isInitialized = false;

    /**
     * This Constructor sets the Context for the LocationHelper
     * @param context the context we are in
     */
    public LocationHelper(Context context) {
        this.context = context;
    }

    /**
     * This method sets the listener for the LocationHelper, which updates anytime there is a change in the moods in firestore
     * @param listener the listener for the LocationHelper
     */
    public void setLocationUpdatesListener(LocationRequestUpdatesListener listener) {
        this.listener = listener;
    }

    /**
     * This method calls initLocationAPIs()
     */
    public void init() {
        initLocationAPIs();
    }

    /**
     * This method initializes the Google Map APIs if they havent been initialized yet.
     */
    private void initLocationAPIs() {

        if (!isInitialized) {

            isInitialized = true;

            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            createLocationRequest();

            mGoogleApiClient.connect();
        }

    }

    /**
     * This method makes sure we have the permissions required to get the location, and if so it requests updates to the user's location
     * from the Google Map client
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * This method prevents the app from getting updates to the user's location from the Google Map client
     */
    public void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * This method creates a request to get the user's location
     */
    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }

    /**
     * This method runs when we are connected to the google map client
     * @param bundle the data from google maps
     */
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    /**
     * This method runs when the user's location connection has been suspended, meaning we
     * cant get any updates to their location
     * @param i the index of the connection
     */
    @Override
    public void onConnectionSuspended(int i) {
    }

    /**
     * This method is called when the user's location changes
     * @param location the new location
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        listener.onLocationChanged(location);
    }

    /**
     * This method is called when the connection to google maps cannot be made
     * @param connectionResult the result containing information about why the connection could not be made
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    /**
     * An inner interface for the updates to the LocationRequest
     */
    public interface LocationRequestUpdatesListener {
        /**
         * This method is called when the user's location changes
         * @param location the new location
         */
        void onLocationChanged(Location location);
    }


}

