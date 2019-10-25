package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class ViewUserMoodDialogFragment extends ViewMoodDialogFragment {

    // Define new listener
    private @NonNull OnClickListener listener;

    public ViewUserMoodDialogFragment() {

        this.listener = new OnClickListener() {
            @Override
            public void onDeletePressed() {
                this.logError();
            }

            @Override
            public void onEditPressed() {
                this.logError();
            }

            private void logError() {
                Log.e(HomeActivity.LOG_TAG, "ViewUserMoodDialogFragment.OnClickListener is NOT IMPLEMENTED");
            }
        };

    }

    public static ViewUserMoodDialogFragment newInstance(Mood mood) {

        // Define new Bundle for storing arguments
        Bundle args = new Bundle();

        // Put arguments in Bundle
        // TODO: 2019-10-25 Uncomment when mood implements parcelable
//        args.putParcelable(Mood.TAG_MOOD_OBJECT, mood);

        // Create new stock fragment and set arguments
        ViewUserMoodDialogFragment fragment = new ViewUserMoodDialogFragment();
        fragment.setArguments(args);

        return fragment;

    }

    // Set the OnClickListener. If this is not called the default listener will be the default as specified above.
    public void setOnClickListener(@NonNull OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected Dialog buildDialog(View view) {

        // Define new AlertDialog.Builder so we can display a custom dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        // Build and return the dialog. We set the onClick listeners for each button to point to a different method in our interface which take different parameters
        return builder
                .setView(view)
                .setTitle(this.getString(R.string.title_dialog_view_mood))
                .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(HomeActivity.LOG_TAG, "EDIT button clicked");
                        ViewUserMoodDialogFragment.this.listener.onEditPressed();

                    }
                })
                .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(HomeActivity.LOG_TAG, "DELETE button clicked");
                        ViewUserMoodDialogFragment.this.listener.onDeletePressed();

                    }
                })
                .create();
    }
}
