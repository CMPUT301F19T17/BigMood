package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;


public class DefineMoodDialogFragment extends DialogFragment {

    private Toolbar toolbar;
    private OnButtonPressListener listener;

    /**
     * This is an interface contained by this class to define the method for the save action. A class can either implement this or define it as a new anonymous class
     */
    public interface OnButtonPressListener {
        void onSavePressed(Mood mood);
    }

    /**
     * This is the default constructor for the dialog. newInstance() methods. Technically a user of this class should not use this constructor. If it happens, the Dialog will not error, but will spawn as in a state of adding a mood
     */
    public DefineMoodDialogFragment() {

        this.listener = new OnButtonPressListener() {
            @Override
            public void onSavePressed(Mood mood) {

                Log.e(HomeActivity.LOG_TAG, "DefineMoodDialogFragment.OnButtonPressListener is NOT IMPLEMENTED");

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
     * of the on*()methods, this is the first. When we first want to create the dialog we set the theme to the fullscreen theme so that the edges match the parent.
     * @param savedInstanceState a bundle that holds the state of the fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

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

        // Example of inflating the fields

        // This will be NULL if the fragment was constructed without a mood to "edit". So therefore we need to check this when populating in all the views.
        Bundle args = this.getArguments();

        // We have to check if the arguments were provided since in the less verbose newInstance() we don't set arguments for the purposes of adding a new mood.
        if (args != null) {

            // Get mood. If we have arguments we probably have a mood object but we check just in case.
            Mood mood = args.getParcelable(Mood.TAG_MOOD_OBJECT);

            // If a Mood object is not received, this object was not created using the newInstance() methods. Instead of crashing, we log a message
            if (mood != null) {

            } else {

                Log.e(HomeActivity.LOG_TAG, "DefineMoodFragment was not provided args, did you use the newInstance() methods?");

            }

        }

        // Return view that has been created
        return view;
    }

    /**
     * of the on*()methods, this is the third. This is executed when the view is created. Here we set onClickListeners, etc. This is where we will actually error check all the views and
     * @param view               The view that was created and inflated
     * @param savedInstanceState A bundle that holds the state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Set the title depending on what state the fragment is in. As in onCreateView, we have to check if the arguments exist and if the mood object exists to avoid a potential error.
        if (this.getArguments() != null) {

            if (this.getArguments().getParcelable(Mood.TAG_MOOD_OBJECT) != null)
                this.toolbar.setTitle(getString(R.string.title_dialog_edit_mood));

        } else {

            this.toolbar.setTitle(getString(R.string.title_dialog_add_mood));

        }

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

        final View finalView = view;
        // Set the OnMenuItemClickListener for the one menu option we have, which is SAVE. Just for extendability we check if the ID matches.
        // This is where the core of the input validation will happen -- that is when the user tries to press Save.
        this.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_save) {
                    //save was pressed

                    Spinner stateSpinner = finalView.findViewById(R.id.state_spinner);

                    //TODO Cameron Oct30 2019 Research to see if there is a better way to ensure the
                    // user did not leave the spinner in the first position, and fix if available
                    if (stateSpinner.getSelectedItemPosition()==0) {
                        Toast.makeText(DefineMoodDialogFragment.this.getContext(),
                                "You must enter an emotional state!", Toast.LENGTH_SHORT).show();

                        Log.e("SPINNER ERROR", "The State Spinner was left empty");
                    }
                    else {
                        //get date and time
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM", Locale.CANADA);

                        Date dateTime = new Date();
                        String dateString = dateFormat.format(dateTime);
                        String timeString = timeFormat.format(dateTime);

                        Spinner situationSpinner = finalView.findViewById(R.id.situation_spinner);
                        EditText reasonEditText = finalView.findViewById(R.id.reason_edit_text);
                        // we have everything that is required to create a mood
                        Mood mood = new Mood(dateString, timeString, stateSpinner.getSelectedItem().toString());

                        //if any of the data is filled in, we update the mood to fill in more information
                        if (situationSpinner.getSelectedItemPosition()!=0) {
                            mood.setSituation(situationSpinner.getSelectedItem().toString());
                        }
                        if (reasonEditText.getText().toString().equals("")) {
                            mood.setReason(reasonEditText.getText().toString());
                        }
                        //TODO add image, location

                        // add the mood and dismiss the fragment
                        DefineMoodDialogFragment.this.listener.onSavePressed(mood);
                        DefineMoodDialogFragment.this.dismiss();
                    }
                    return true;

                }

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
}
