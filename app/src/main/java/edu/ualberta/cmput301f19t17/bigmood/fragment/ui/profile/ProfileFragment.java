package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;

/**
 * ProfileFragment is used to view the current user's profile.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private AppPreferences appPreferences;

    /**
     * of the on*()methods, this is the second. After the dialog has been started we want to inflate the dialog.
     * This is where we inflate all the views and *if applicable* populate all the fields.
     * @param inflater           View inflater service
     * @param container          Container that the inflater is housed in
     * @param savedInstanceState A bundle that holds the state of the fragment
     * @return                   Returns the inflated view
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        this.appPreferences = AppPreferences.getInstance();

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        this.setHasOptionsMenu(true);


        return root;
    }

    /**
     * This method gets called when the fragment needs to assemble menu options.
     * @param menu     The options menu in which you place your items.
     * @param inflater The menu inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        // Inflate the menu
        inflater.inflate(R.menu.fragment_profile, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * This method gets called when a menu item in the toolbar is clicked. We only have one item here so we only check one
     * @param item The menu item that was selected. This value must never be null.
     * @return     Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_sign_out) {

            // If the sign out button is pressed, clear the current user and call finish() on the underlying activity.
            this.appPreferences.setCurrentUser(null);
            this.getActivity().finish();
            return true;

        } else {

            return super.onOptionsItemSelected(item);

        }

    }
}