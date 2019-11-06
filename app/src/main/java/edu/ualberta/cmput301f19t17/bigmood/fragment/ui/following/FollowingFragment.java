package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.following;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import edu.ualberta.cmput301f19t17.bigmood.R;

public class FollowingFragment extends Fragment {

    private FollowingViewModel followingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.followingViewModel = ViewModelProviders.of(this).get(FollowingViewModel.class);

        View root = inflater.inflate(R.layout.fragment_following, container, false);

        // Enable options menu
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

        inflater.inflate(R.menu.fragment_following, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * This method gets called when a menu item in the toolbar is clicked. We only have one item here so we only check one
     * @param item The menu item that was selected. This value must never be null.
     * @return     Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_maps_following)
            Toast.makeText(this.getContext(), "Display Maps", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);

    }


}