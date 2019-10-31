package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class ViewMoodDialogFragment extends DialogFragment {

    private Toolbar toolbar;

    /**
     * This is the default constructor for the dialog. newInstance() methods. Technically a user of this class should not use this constructor. If it happens, the class will eventually log a message when spawned.
     */
    public ViewMoodDialogFragment() {
    }

    /**
     * This method creates a new instance of a ViewMoodDialogFragment for the purposes of viewing a Mood. This method should be used instead of the base constructor as the mood must get put into the arguments Bundle of the Fragment.
     * @param mood The Mood to be viewed
     * @return     An instance of ViewMoodDialogFragment for the using class to show.
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
     * This method serves the purpose of building a proper dialog for this fragment. onCreateDialog() must return a dialog and this is where that occurs. The intention is that any subclasses will be able to override this method to implement their own dialogs.
     * @param view This is the view that the Dialog has to attach to. We presume this has already been created.
     * @return     Fully built Dialog
     */
    protected Dialog buildDialog(View view) {

        // Define new AlertDialog.Builder so we can display a custom dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        // Build and return the dialog. We set the onClick listeners for each button to point to a different method in our interface which take different parameters
        return builder
                .setView(view)
                .create();

    }

    /**
     * This gets called when the dialog is drawn on the screen. We have to populate a bunch of values using the mood we get from the arguments Bundle.
     * @param savedInstanceState A bundle with the saved state.
     * @return                   An instance of a Dialog after we've created it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Get arguments from the bundle. This can be null if the user did not use the newInstance() methods.
        Bundle args = this.getArguments();

        // If null, raise exception
        if (args == null)
            throw new RuntimeException("ViewMoodDialogFragment was not properly provided arguments, did you use the newInstance() Methods?");

        // Get Mood. This can be null if the user did not use the newInstance() methods.
        Mood mood = args.getParcelable(Mood.TAG_MOOD_OBJECT);

        // If null, raise exception
        if (mood == null)
            throw new RuntimeException("ViewMoodDialogFragment was not provided a Mood object in the arguments bundle, did you use the newInstance() methods?");

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

        // Find all views and inflate them with the info
        // TODO: 2019-10-25 INFLATE VIEWS
        TextView textView = view.findViewById(R.id.debug_textview_mood);

        // Set all Date, time, and mood details
        // TODO: 2019-10-25 POPULATE INFO
        textView.setText(mood.getDate());

        return this.buildDialog(view);

    }

}
