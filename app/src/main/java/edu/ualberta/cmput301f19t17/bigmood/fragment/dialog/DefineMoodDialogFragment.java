package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodSpinnerAdapter;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;
import edu.ualberta.cmput301f19t17.bigmood.model.SocialSituation;

import static android.app.Activity.RESULT_OK;

/**
 * DefineMoodDialogFragment is used to create a new mood, or edit a currently existing mood
 */
public class DefineMoodDialogFragment extends DialogFragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private Toolbar toolbar;

    private OnButtonPressListener listener;
    private Mood moodToEdit = null;

    private Spinner stateSpinner;
    private Spinner situationSpinner;
    private Spinner dateSpinner;
    private Spinner timeSpinner;
    private TextInputLayout reasonInputLayout;

    private ImageView imageView;

    private MoodSpinnerAdapter moodSpinnerAdapter;
    ArrayList<EmotionalState> moodSpinnerArrayList;

    /**
     * This is an interface contained by this class to define the method for the save action. A class can either implement this or define it as a new anonymous class
     */
    public interface OnButtonPressListener {
        void onSavePressed(Mood moodToSave);
    }

    /**
     * This is the default constructor for the dialog. newInstance() methods. Technically a user of this class should not use this constructor. If it happens, the Dialog will not error, but will spawn as in a state of adding a mood
     */
    public DefineMoodDialogFragment() {

        this.listener = new OnButtonPressListener() {
            @Override
            public void onSavePressed(Mood moodToSave) {

                throw new UnsupportedOperationException("DefineMoodDialogFragment.OnButtonPressListener is NOT IMPLEMENTED. use setOnButtonPressListener() to set one.");

            }
        };

    }

    /**
     * This method creates a new instance of a DefineMoodDialog for the purposes of adding a Mood. Because we have no Mood to prepopulate we don't have to specify one.
     * @return A new instance of a DefineMoodDialogFragment
     */
    public static DefineMoodDialogFragment newInstance() {

        // Create new stock fragment. We don't have to set any arguments
        return new DefineMoodDialogFragment();

    }

    /**
     * This method creates a new instance of a DefineMoodDialog for the purposes of editing a Mood.
     * Because we have a Mood to prepopulate we must specify it in here so it can be added to the fragment's arguments.
     * @param mood The mood to edit
     * @return A new instance of a DefineMoodDialogFragment
     */
    public static DefineMoodDialogFragment newInstance(Mood mood) {

        // Define new Bundle for storing arguments
        Bundle args = new Bundle();

        // Put arguments in Bundle
        args.putParcelable(Mood.TAG_MOOD_OBJECT, mood);

        // Create new stock fragment and set arguments
        DefineMoodDialogFragment fragment = new DefineMoodDialogFragment();
        fragment.setArguments(args);

        return fragment;

    }

    /**
     * This method sets the OnButtonPressListener for the save action.
     * @param listener This is the listener that will be set for this fragment.
     */
    public void setOnButtonPressListener(OnButtonPressListener listener) {
        this.listener = listener;
    }

    /**
     * of the on*()methods, this is the first. When we first want to create the dialog we set the theme to the fullscreen theme so that the edges match the parent. Here we also check for the existence of a mood in the arguments bundle and set it to our instance variable.
     * @param savedInstanceState a bundle that holds the state of the fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        // Get the arguments bundle. This will be NULL if the fragment was constructed without a mood to "edit".
        Bundle args = this.getArguments();

        // Check if the arguments are null.
        if (args != null) {

            // Get mood. If we have arguments we probably have a mood object but we check just in case.
            Mood mood = args.getParcelable(Mood.TAG_MOOD_OBJECT);

            // If a Mood object is not received, this object was not created using the newInstance() methods. We throw an exception if this is the case.
            if (mood != null)
                this.moodToEdit = mood;
            else
                throw new IllegalStateException("Something went wrong with creating the view. Received an argument bundle but not a proper Mood. Did you use the newInstance() methods?");

        }

    }

    /**
     * of the on*()methods, this is the second. After the dialog has been started we want to inflate the dialog.
     * This is where we inflate all the views and *if applicable* populate all the fields.
     * @param inflater           View inflater service
     * @param container          Container that the inflater is housed in
     * @param savedInstanceState A bundle that holds the state of the fragment
     * @return                   Returns the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Set inflater view
        View view = inflater.inflate(R.layout.dialog_define_mood, container, false);

        // Bind toolbar XML to view
        this.toolbar = view.findViewById(R.id.toolbar_define_fragment);

        // Find and bind elements
        initList();
        this.stateSpinner = view.findViewById(R.id.spinner_state);
        this.moodSpinnerAdapter = new MoodSpinnerAdapter(this.getContext(), 0, moodSpinnerArrayList);
        this.stateSpinner.setAdapter(this.moodSpinnerAdapter);

        this.situationSpinner = view.findViewById(R.id.situation_spinner);
        this.reasonInputLayout = view.findViewById(R.id.text_input_reason);

        this.imageView = view.findViewById(R.id.image);

        this.dateSpinner = view.findViewById(R.id.spinner_date);
        this.timeSpinner = view.findViewById(R.id.spinner_time);

        // Return view that has been created
        return view;

    }

    /**
     * of the on*()methods, this is the third. This is executed when the view is created. Here we set onClickListeners, etc. This is where we will actually error check all the views and
     * @param view               The view that was created and inflated
     * @param savedInstanceState A bundle that holds the state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Inflate Menu resource onto the toolbar
        this.toolbar.inflateMenu(R.menu.define_mood);

        // Set the Listener for the close button in the toolbar
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(HomeActivity.LOG_TAG, "Close button clicked");
                DefineMoodDialogFragment.this.dismiss();
            }
        });

        // TODO 2019-11-03 Cameron removed since i don't believe there is a way to set a preset for the spinner programmatically, which is necessary for setting it with an ArrayAdapter.
        // TODO 2019-11-03 Cameron create custom ArrayAdapter to include the mood pictograms
        // set up the spinner with the emotional states
        final ArrayAdapter<EmotionalState> stateAdapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                EmotionalState.values()
        );
        stateAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        this.stateSpinner.setAdapter(stateAdapter);

        // set up the spinner with the social situations
        final ArrayAdapter<SocialSituation> situationAdapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                SocialSituation.values()
        );
        situationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        this.situationSpinner.setAdapter(situationAdapter);

        final Calendar calendar;

        // Here we populate values in the fragment if we have a mood and set the appropriate title.
        if (this.moodToEdit != null) {

            // Set title
            this.toolbar.setTitle(getString(R.string.title_dialog_edit_mood));

            // Set the calendar object since we have it in the mood object.
            calendar = this.moodToEdit.getDatetime();

            // Populate state
            int statePosition = stateAdapter.getPosition(this.moodToEdit.getState());
            this.stateSpinner.setSelection(statePosition);

            // Populate
            if (this.moodToEdit.getSituation() != null) {
                int situationPosition = situationAdapter.getPosition(this.moodToEdit.getSituation());
                this.situationSpinner.setSelection(situationPosition);
            }

            if (! this.moodToEdit.getReason().equals(""))
                this.reasonInputLayout.getEditText().setText(this.moodToEdit.getReason());

            //TODO add location and image

        } else {

            // Set Title
            this.toolbar.setTitle(getString(R.string.title_dialog_add_mood));

            // Set calendar to a new calendar since it's a new mood
            calendar = Calendar.getInstance();

        }

        // Set text of date spinner
        dateSpinner.setAdapter(
                new ArrayAdapter<String>(
                        this.getContext(),
                        android.R.layout.simple_spinner_item,
                        Collections.singletonList(new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(calendar.getTime()))
                ));
        dateSpinner.setEnabled(false);

        // Set text of time spinner
        timeSpinner.setAdapter(
                new ArrayAdapter<String>(
                        this.getContext(),
                        android.R.layout.simple_spinner_item,
                        Collections.singletonList(new SimpleDateFormat("HH:mm", Locale.CANADA).format(calendar.getTime()))
                ));
        timeSpinner.setEnabled(false);

        // TODO: 2019-11-06 Ranajay: Disabled for now since going to take a photo results in a crash

//        // add click listener to the image to pick picture from gallery or camera
//        this.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String title = "Open Photo";
//                CharSequence[] itemlist ={"Take a Photo",
//                        "Pick from Gallery"};
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle(title);
//                builder.setItems(itemlist, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:// Take Photo
//                                // Do Take Photo task here
//                                dispatchTakePictureIntent();
//                                break;
//                            case 1:// Choose Existing Photo
//                                // Do Pick Photo task here
//                                dispatchPickImageIntent();
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.setCancelable(true);
//                alert.show();
//            }
//        });

        // Set the OnMenuItemClickListener for the one menu option we have, which is SAVE. Just for extendability we check if the ID matches.
        // This is where the core of the input validation will happen -- that is when the user tries to press Save.
        this.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_save) {

                    // Get emotional state and social situation
                    EmotionalState emotionalState = stateAdapter.getItem(stateSpinner.getSelectedItemPosition());
                    SocialSituation socialSituation = situationAdapter.getItem(situationSpinner.getSelectedItemPosition());

                    // Get reason
                    String reason = DefineMoodDialogFragment.this.reasonInputLayout
                            .getEditText()
                            .getText()
                            .toString()
                            .trim();

                    // Get the max length and max word count tha the reason is allowed to be
                    int maxLength = DefineMoodDialogFragment.this.getContext().getResources().getInteger(R.integer.max_length_reason);
                    int maxWordCount = DefineMoodDialogFragment.this.getContext().getResources().getInteger(R.integer.max_word_count_reason);

                    // This controls the logic for the max reason length. We display error messages depending on what is violated. For checking word count, we split the string by any space character (\s) one or more times (+). This makes sure that any number sf spaces is counted as one delimiter.
                    if (reason.length() > maxLength) {

                        // Set error and return, since this is not valid.
                        DefineMoodDialogFragment.this.reasonInputLayout.setError(DefineMoodDialogFragment.this.getString(R.string.error_reason_too_long));
                        return false;

                    } else if (reason.split("\\s+").length > maxWordCount) {

                        // Set error and return, since this is not valid.
                        DefineMoodDialogFragment.this.reasonInputLayout.setError(DefineMoodDialogFragment.this.getString(R.string.error_reason_word_count));
                        return false;

                    } else {

                        // Technically has no effect because the dialog is immediately dismissed but for completeness we do this.
                        DefineMoodDialogFragment.this.reasonInputLayout.setError(null);

                    }

                    // TODO add image, location - canned for now

                    // Declare mood. Can be initialized as an "old" mood (with firestoreId) or a "new" mood (without firestoreId).
                    Mood mood;

                    // If we have an old mood, pass the firestoreId along.
                    if (DefineMoodDialogFragment.this.moodToEdit != null)
                        mood = new Mood(
                                DefineMoodDialogFragment.this.moodToEdit.getFirestoreId(),
                                emotionalState,
                                calendar,
                                socialSituation,
                                reason,
                                new GeoPoint(32.32, 142.22),
                                null
                        );

                    // If we don't have an old mood, we have to create a brand new one, without the firestoreId.
                    else
                        mood = new Mood(
                                emotionalState,
                                calendar,
                                socialSituation,
                                reason,
                                new GeoPoint(32.32, 142.22),
                                null
                        );

                    // Invoke the callback method with the mood and dismiss the fragment
                    DefineMoodDialogFragment.this.listener.onSavePressed(mood);
                    DefineMoodDialogFragment.this.dismiss();
                    return true;

                }  // End if statement on R.id.action_save

                // Base case
                return false;

            }
        });



    }

    /**
     * of the on*()methods, this is the fourth. We set the width and height of the view and also set its animation.
     */
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchPickImageIntent() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (i.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_PICK_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    public void initList() {
        moodSpinnerArrayList = new ArrayList<>();
        moodSpinnerArrayList.add(EmotionalState.HAPPINESS);
        moodSpinnerArrayList.add(EmotionalState.SADNESS);
        moodSpinnerArrayList.add(EmotionalState.ANGER);
        moodSpinnerArrayList.add(EmotionalState.DISGUST);
        moodSpinnerArrayList.add(EmotionalState.FEAR);
        moodSpinnerArrayList.add(EmotionalState.SURPRISE);
    }

}
