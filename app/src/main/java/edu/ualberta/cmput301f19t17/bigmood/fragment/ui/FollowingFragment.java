package edu.ualberta.cmput301f19t17.bigmood.fragment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodAdapter;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MoodsListener;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.MapDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

/**
 * FollowingFragment houses the logic for viewing the moods of users that the logged in user follows.
 */
public class FollowingFragment extends Fragment {

    private AppPreferences appPreferences;

    private ArrayList<Mood> moodList;
    private MoodAdapter moodAdapter;

    private ListenerRegistration listenerRegistration;

    private View menuItemFilter = null;
    private PopupMenu menu = null;

    /**
     * of the on*()methods, this is the second. After the dialog has been started we want to inflate the dialog.
     * This is where we inflate all the views and *if applicable* populate all the fields.
     * @param inflater           View inflater service
     * @param container          Container that the inflater is housed in
     * @param savedInstanceState A bundle that holds the state of the fragment
     * @return                   Returns the inflated view
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_following, container, false);

        // Enable options menu
        this.setHasOptionsMenu(true);

        this.appPreferences = AppPreferences.getInstance();

        // Initialize a new ArrayList
        this.moodList = new ArrayList<>();

        this.moodAdapter = new MoodAdapter(root.getContext(), R.layout.list_item_mood, moodList);
        moodAdapter.notifyDataSetChanged();

        //set up a reference to the listview we will be populating
        ListView moodListView = root.findViewById(R.id.following_mood_list);
        moodListView.setAdapter(moodAdapter);

        // Set up the MoodsListener to listen to updates in FireStore
        this.listenerRegistration = this.appPreferences
                .getRepository()
                .getFollowingMoods(
                        this.appPreferences.getCurrentUser(),
                        new MoodsListener() {

                            @Override
                            public void onUpdate(List<Mood> moodList) {

                                FollowingFragment.this.moodList.clear();
                                FollowingFragment.this.moodList.addAll(moodList);
                                FollowingFragment.this.moodAdapter.notifyDataSetChanged();

                                // This refresh the filter with the updated data
                                FollowingFragment.this.moodAdapter.applyFilter(menuItemFilter, menu);

                            }
                        });

        // set the onItemClickListener for the mood list items. This will be called anytime a mood is clicked on in the lis
        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                // Create dialog and set the button press listener for delete and edit
                ViewMoodDialogFragment viewUserFragment = ViewMoodDialogFragment.newInstance(moodAdapter.getItem(i));
                // Show the view Dialog
                viewUserFragment.show(getFragmentManager(), "FRAGMENT_VIEW_MOOD");
            }
        });

        return root;
    }

    /**
     * We need to unbind the ListenerRegistration so that updates do not occur in the background, so we have to make sure we do that upon exit only.
     */
    @Override
    public void onDestroyView() {

        this.listenerRegistration.remove();

        super.onDestroyView();

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

        if (item.getItemId() == R.id.action_maps_following) {
            Toast.makeText(this.getContext(), "Display User Maps", Toast.LENGTH_SHORT).show();
            MapDialogFragment mapDialogFragment = new MapDialogFragment(moodAdapter);
            mapDialogFragment.show(getFragmentManager(), "FRAGMENT_VIEW_USER_MAP");
        }
        return super.onOptionsItemSelected(item);

    }

}
