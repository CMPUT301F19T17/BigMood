package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.ImageProgressListener;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

import static edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity.LOG_TAG;

/**
 * ViewMoodDialogFragment is used to view a mood in general. It defines the logic of viewing a mood.
 */
public class ViewMoodDialogFragment extends DialogFragment {

    Mood moodToView;
    private Toolbar toolbar;
    private MapView mapView;
    private GoogleMap googleMap;

    private ImageView photoImageView;

    private TextView placeHolderNoLocation;
    private TextView buttonGetImage;

    private ProgressBar progressBarGetImage;

    private boolean getImageButtonEnabled = true;

    /**
     * This is the default constructor for the dialog. newInstance() methods. Technically a user of this class should not use this constructor. If it happens, the class will eventually log a message when spawned.
     */
    public ViewMoodDialogFragment() {
    }

    /**
     * This method creates a new instance of a ViewMoodDialogFragment for the purposes of viewing a Mood. This method should be used instead of the base constructor as the mood must get put into the arguments Bundle of the Fragment.
     *
     * @param mood The Mood to be viewed
     * @return An instance of ViewMoodDialogFragment for the using class to show.
     */
    public static ViewMoodDialogFragment newInstance(Mood mood) {

        // Define new Bundle for storing arguments
        Bundle args = new Bundle();

        // Put Mood object in Bundle
        args.putParcelable(Mood.TAG_MOOD_OBJECT, mood);

        // Create new stock fragment and set arguments
        ViewMoodDialogFragment fragment = new ViewMoodDialogFragment();
        fragment.setArguments(args);

        return fragment;

    }

    /**
     * This gets called when the dialog is drawn on the screen. We have to populate a bunch of values using the mood we get from the arguments Bundle.
     *
     * @param savedInstanceState A bundle with the saved state.
     * @return An instance of a Dialog after we've created it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Used to allow downloading of large data. https://stackoverflow.com/questions/22395417/error-strictmodeandroidblockguardpolicy-onnetwork
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Get arguments from the bundle. This can be null if the user did not use the newInstance() methods.
        Bundle args = this.getArguments();

        // If null, raise exception
        if (args == null)
            throw new IllegalArgumentException("ViewMoodDialogFragment was not properly provided arguments, did you use the newInstance() method?");

        // Get Mood. This can be null if the user did not use the newInstance() methods.
        this.moodToView = args.getParcelable(Mood.TAG_MOOD_OBJECT);

        // If null, raise exception
        if (this.moodToView == null)
            throw new IllegalArgumentException("ViewMoodDialogFragment was not provided a Mood object in the arguments bundle, did you use the newInstance() method?");

        // At this point we should be in the point in execution where we have a mood to view, so we have to populate all the fields.

        // Define LayoutInflater for the body of the dialog.
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_view_mood, null);

        // Bind toolbar XML to view
        this.toolbar = view.findViewById(R.id.toolbar_view_mood);

        // Set title for toolbar
        this.toolbar.setTitle(this.getString(R.string.title_dialog_view_mood));

        // Dismiss dialog when close button is clicked.
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewMoodDialogFragment.this.dismiss();
            }
        });

        this.mapView = view.findViewById(R.id.mapview_location);

        this.placeHolderNoLocation = view.findViewById(R.id.label_no_location);
        this.buttonGetImage = view.findViewById(R.id.textview_button_get_image);

        this.progressBarGetImage = view.findViewById(R.id.progressbar_image);

        // Find all views and inflate them with the info
        TextView stateTextView = view.findViewById(R.id.textview_placeholder_state);
        ImageView emoteImageView = view.findViewById(R.id.image_view_placeholder_emote);
        TextView dateTextView = view.findViewById(R.id.textview_placeholder_date);
        TextView timeTextView = view.findViewById(R.id.textview_placeholder_time);
        TextView situationTextView = view.findViewById(R.id.textview_placeholder_situation);
        TextView reasonTextView = view.findViewById(R.id.textview_placeholder_reason);

        this.photoImageView = view.findViewById(R.id.imageview_image);

        // Set state to the nice name defined by the enumeration
        stateTextView.setText(this.moodToView.getState().toString());

        // Set emoticon based on enum
        Resources res = this.getContext().getResources();
        Drawable emoticon = res.getDrawable(this.moodToView.getState().getDrawableId());
        emoteImageView.setImageDrawable(emoticon);
        emoteImageView.setTag(this.moodToView.getState().getDrawableId());

        // Set date and time
        Calendar calendar = this.moodToView.getDatetime();
        dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(calendar.getTime()));
        timeTextView.setText(new SimpleDateFormat("HH:mm", Locale.CANADA).format(calendar.getTime()));

        // If the mood has a social situation, add it. If not, let it take the default value in the resource layout.
        if (this.moodToView.getSituation() != null)
            situationTextView.setText(this.moodToView.getSituation().toString());

        // If the mood has a state, add it. If not, let it take the default value in the resource layout.
        if (!this.moodToView.getReason().equals(""))
            reasonTextView.setText(this.moodToView.getReason());

        // If a photograph is provided, add it. Else, let it take the default value in the resource layout.
        if (this.moodToView.getImageId() != null) {

            this.buttonGetImage.setEnabled(true);
            this.buttonGetImage.setText(R.string.label_get_image);
            this.buttonGetImage.setTypeface(Typeface.DEFAULT_BOLD);

            this.buttonGetImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (! ViewMoodDialogFragment.this.getImageButtonEnabled) {

                        Toast.makeText(
                                ViewMoodDialogFragment.this.getContext(),
                                "Please wait until the current upload finishes.",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;

                    }

                    ViewMoodDialogFragment.this.getImageButtonEnabled = false;

                    ViewMoodDialogFragment.this.progressBarGetImage.setVisibility(View.VISIBLE);
                    ViewMoodDialogFragment.this.progressBarGetImage.setProgress(25);

                    AppPreferences preferences = AppPreferences.getInstance();

                    preferences.getRepository()
                            .downloadImage(

                                    ViewMoodDialogFragment.this.moodToView.getImageId(),

                                    new OnSuccessListener<Bitmap>() {
                                        @Override
                                        public void onSuccess(Bitmap bitmap) {

                                            ViewMoodDialogFragment.this.photoImageView.setImageBitmap(bitmap);
                                            ViewMoodDialogFragment.this.photoImageView.setVisibility(View.VISIBLE);

                                            ViewMoodDialogFragment.this.buttonGetImage.setVisibility(View.INVISIBLE);
                                            ViewMoodDialogFragment.this.buttonGetImage.setEnabled(false);

                                            // Hide progress bar on a delay
                                            Handler handler = new Handler();
                                            handler.postDelayed(

                                                    new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            ViewMoodDialogFragment.this.progressBarGetImage.setVisibility(View.INVISIBLE);

                                                        }
                                                    },

                                                    1000

                                            );

                                        }
                                    },

                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(
                                                    ViewMoodDialogFragment.this.getContext(),
                                                    R.string.toast_error_image_download,
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            ViewMoodDialogFragment.this.progressBarGetImage.setProgress(0);

                                            ViewMoodDialogFragment.this.getImageButtonEnabled = true;

                                            // Hide progress bar on a delay
                                            Handler handler = new Handler();
                                            handler.postDelayed(

                                                    new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            ViewMoodDialogFragment.this.progressBarGetImage.setVisibility(View.INVISIBLE);

                                                        }
                                                    },

                                                    1000

                                            );

                                        }
                                    },

                                    new ImageProgressListener() {
                                        @Override
                                        public void onProgress(int progress) {

                                            Log.d(LOG_TAG, "PROGRESS (VMDF): " + progress);

                                            ViewMoodDialogFragment.this.progressBarGetImage.setProgress(progress);

                                        }
                                    }


                            );



                }
            });

        }



        // If a location is provided, add it. Else, let it take the default value in the resource layout.
        if (this.moodToView.getLocation() != null) {

            // needed to get the map to display immediately
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    ViewMoodDialogFragment.this.googleMap = googleMap;
                    ViewMoodDialogFragment.this.googleMap.getUiSettings().setAllGesturesEnabled(false);

                    placeHolderNoLocation.setVisibility(View.INVISIBLE);
                    mapView.setVisibility(View.VISIBLE);

                    LatLng currentLatLng = new LatLng(
                            moodToView.getLocation().getLatitude(),
                            moodToView.getLocation().getLongitude()
                    );

                    addMarkerAtLocation(currentLatLng);

                }
            });

        }

        return this.buildDialog(view);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void addMarkerAtLocation(LatLng latLng) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    /**
     * This method serves the purpose of building a proper dialog for this fragment. onCreateDialog() must return a dialog and this is where that occurs. The intention is that any subclasses will be able to override this method to implement their own dialogs.
     *
     * @param view This is the view that the Dialog has to attach to. We presume this has already been created.
     * @return Fully built Dialog
     */
    protected Dialog buildDialog(View view) {

        // Define new AlertDialog.Builder so we can display a custom dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        // Build and return the dialog. We set the onClick listeners for each button to point to a different method in our interface which take different parameters
        return builder
                .setView(view)
                .create();

    }

}
