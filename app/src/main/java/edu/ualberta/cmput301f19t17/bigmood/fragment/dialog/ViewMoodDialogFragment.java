package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class ViewMoodDialogFragment extends DialogFragment {

    // Interface for the listener. We will implement this in the class that sets the onClickListener.
    public interface OnClickListener {
        void onDeletePressed();
        void onEditPressed();
    }

    // Default constructor. We set a default listener to avoid a crash if for some reason it is not overridden. This should not happen in the code, but it is here as a good measure.
    public ViewMoodDialogFragment() {
    }

    // This is the proper function to use in order to make a new ViewMoodDialogFragment. This creates a new instance with a Mood object and an index.
    public static ViewMoodDialogFragment newInstance(Mood mood) {

        // Define new Bundle for storing arguments
        Bundle args = new Bundle();

        // Put arguments in Bundle
        // TODO: 2019-10-25 Uncomment when mood implements parcelable
//        args.putParcelable(Mood.TAG_MOOD_OBJECT, mood);

        // Create new stock fragment and set arguments
        ViewMoodDialogFragment fragment = new ViewMoodDialogFragment();
        fragment.setArguments(args);

        return fragment;

    }

    // This gets called when the dialog is drawn on the screen. We have to populate a bunch of values using the mood we get from the bundle.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Declare the Mood and the int from the bundle.
        final Mood mood;

        // Get arguments from the bundle
        Bundle args = this.getArguments();

        // Try to get both a mood and the integer.
        mood = (Mood) args.getParcelable(Mood.TAG_MOOD_OBJECT);

        // This will only trigger if someone creates a new ViewMoodDialogFragment without using the static method. In that case we cannot draw this Dialog if we don't have the information to display it.
        if (mood == null)
            throw new RuntimeException("mood object must have values associated with them, please use ViewMoodDialogFragment.newInstance();");

        // Define LayoutInflater for the body of the dialog.
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_define_mood, null);

        // Find all views and inflate them with the info
        // TODO: 2019-10-25 INFLATE VIEWS

        // Set all Date, time, and mood details
        // TODO: 2019-10-25 POPULATE INFO

        return this.buildDialog(view);

    }

    protected Dialog buildDialog(View view) {

        // Define new AlertDialog.Builder so we can display a custom dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        // Build and return the dialog. We set the onClick listeners for each button to point to a different method in our interface which take different parameters
        return builder
                .setView(view)
                .setTitle(this.getString(R.string.title_dialog_view_mood))
                .create();

    }




}
