package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodAdapter;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

/**
 * MapDialogFragment holds the MapView that is used to select the user's location when they choose to add an image to
 * their mood. This is currently not implemented yet.
 * */
public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private MoodAdapter moodAdapter;
    private Toolbar toolbar;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";


    /**
     * of the on*()methods, this is the first. When we first want to create the dialog we set the theme to the fullscreen theme so that the edges match the parent. Here we also check for the existence of a mood in the arguments bundle and set it to our instance variable.
     * @param savedInstanceState a bundle that holds the state of the fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        // TODO: 11/15 Tri, the idea is to pass in the moodAdapter, get the location of every mood in it => put the marker on the map
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_view_user_map, null, false);


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        this.toolbar = view.findViewById(R.id.toolbar_view_map);
        this.mapView = view.findViewById(R.id.map_view);
        this.mapView.onCreate(mapViewBundle);
        this.mapView.getMapAsync(this);

        return view;


    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(HomeActivity.LOG_TAG, "Close button clicked");
                MapDialogFragment.this.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = this.getDialog();

        if (dialog != null) {

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);

        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMapView) {
        googleMap = googleMapView;
        googleMap.setMinZoomPreference(15);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        googleMap.addMarker(new MarkerOptions().position(ny)
                .title("Marker in NY")
                .snippet("Test marker!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    public void makeMoodMaker(GoogleMap googleMap, Mood mood) {
        GeoPoint geoPoint = mood.getLocation();
        LatLng moodLocation = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
        Calendar calendar = mood.getDatetime();
        String moodDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(calendar.getTime());
        String moodTime = new SimpleDateFormat("HH:mm", Locale.CANADA).format(calendar.getTime());
        String moodReason = mood.getReason();

        int moodState = mood.getState().getStateCode();
        switch (moodState) {
            case 0:
                googleMap.addMarker(new MarkerOptions().position(moodLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        .title(moodDate)
                        .snippet(moodTime + " " + moodReason));
                break;
            case 1:
                googleMap.addMarker(new MarkerOptions().position(moodLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title(moodDate)
                        .snippet(moodTime + " " + moodReason));
                break;
            case 2:
                googleMap.addMarker(new MarkerOptions().position(moodLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(moodDate)
                        .snippet(moodTime + " " + moodReason));
                break;
            case 3:
                googleMap.addMarker(new MarkerOptions().position(moodLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title(moodDate)
                        .snippet(moodTime + " " + moodReason));
                break;
            case 4:
                googleMap.addMarker(new MarkerOptions().position(moodLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(moodDate)
                        .snippet(moodTime + " " + moodReason));
                break;
            case 5:
                googleMap.addMarker(new MarkerOptions().position(moodLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                        .title(moodDate)
                        .snippet(moodTime + " " + moodReason));
                break;
        }

    }

}
