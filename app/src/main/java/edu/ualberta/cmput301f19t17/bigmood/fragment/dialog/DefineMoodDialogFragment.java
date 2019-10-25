package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;


public class DefineMoodDialogFragment extends DialogFragment {

    private Toolbar toolbar;

    public DefineMoodDialogFragment() {
    }

    /**
     * This method creates a new instance of a DefineMoodDialog for the purposes of adding a Mood. Because we have no Mood to prepopulate we don't have to specify one.
     * @return A new instance of a DefineMoodDialogFragment
     */
    public static DefineMoodDialogFragment newInstance() {

        // Create new stock fragment and set arguments
        return new DefineMoodDialogFragment();

    }


    /**
     * This method creates a new instance of a DefineMoodDialog for the purposes of editing a Mood. Because we have a Mood to prepopulate we must specify it in here so it can be added to the fragment's arguments.
     * @param mood The mood to edit
     * @return A new instance of a DefineMoodDialogFragment
     */
    public static DefineMoodDialogFragment newInstance(Mood mood) {

        // Define new Bundle for storing arguments
        Bundle args = new Bundle();

        // Put arguments in Bundle
        // TODO: 2019-10-25 Uncomment when mood implements parcelable
//        args.putParcelable(Mood.TAG_MOOD_OBJECT, mood);

        // Create new stock fragment and set arguments
        DefineMoodDialogFragment fragment = new DefineMoodDialogFragment();
        fragment.setArguments(args);

        return fragment;

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
     * of the on*()methods, this is the second. After the dialog has been started we want to inflate the dialog. This is where we inflate all the views and populate all the fields.
     * @param inflater view inflater service
     * @param container container that the inflater is housed in
     * @param savedInstanceState a bundle that holds the state of the fragment
     * @return we return the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Set inflater view
        View view = inflater.inflate(R.layout.fragment_define_mood, container, false);

        // Bind toolbar XML to view
        this.toolbar = view.findViewById(R.id.toolbar_define_fragment);

        // Inflate textview
        TextView textView = (TextView) view.findViewById(R.id.textview_fragment);
        textView.setText("This is inside the fragment");

        // Return view that has been created
        return view;
    }

    /**
     * of the on*()methods, this is the third. This is executed when the view is created. Here we set onClickListeners, etc.
     * @param view the view that was created and inflated
     * @param savedInstanceState a bundle that holds the state of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(HomeActivity.LOG_TAG, "Close button clicked");
                DefineMoodDialogFragment.this.dismiss();

            }
        });

        this.toolbar.setTitle("Fragment");
        this.toolbar.inflateMenu(R.menu.define_mood);
        this.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_save) {
                    Log.d(HomeActivity.LOG_TAG, "SAVE clicked");

                    // Handle save here

                    DefineMoodDialogFragment.this.dismiss();

                    return true;

                } else {

                    return false;

                }

            }
        });

    }

    /**
     * of the on*()methods, this is the fourth. We set the width and height of the view and also set its animations.
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
