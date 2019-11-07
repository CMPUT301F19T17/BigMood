package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.activity.HomeActivity;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodAdapter;
import edu.ualberta.cmput301f19t17.bigmood.database.listener.MoodsListener;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.DefineMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewUserMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class UserMoodsFragment extends Fragment {

    private UserMoodsViewModel userMoodsViewModel;
    private AppPreferences appPreferences;

    private ArrayList<Mood> moodList;
    private ArrayAdapter<Mood> moodAdapter;

    private EmotionalState filter = null;

    private View menuItemFilter;
    private PopupMenu menu;

    private ListenerRegistration listenerRegistration;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_moods, container, false);

        // Enable options menu
        this.setHasOptionsMenu(true);

        // Set ViewModel and App Preferences
        this.userMoodsViewModel = ViewModelProviders.of(this).get(UserMoodsViewModel.class);
        this.appPreferences = AppPreferences.getInstance();

        // Initialize a new ArrayList
        this.moodList = new ArrayList<>();
        this.moodAdapter = new MoodAdapter(root.getContext(), R.layout.mood_item, moodList);

        final ListView moodListView = root.findViewById(R.id.mood_list);
        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);

        moodListView.setAdapter(moodAdapter);

        this.listenerRegistration = this.appPreferences
                .getRepository()
                .getUserMoods(
                        this.appPreferences.getCurrentUser(),
                        new MoodsListener() {
                            @Override
                            public void onUpdate(List<Mood> moodList) {

                                UserMoodsFragment.this.moodList.clear();
                                UserMoodsFragment.this.moodList.addAll(moodList);
                                UserMoodsFragment.this.moodAdapter.notifyDataSetChanged();

                            }
                        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DefineMoodDialogFragment addMoodFragment = DefineMoodDialogFragment.newInstance();
                addMoodFragment.setOnButtonPressListener(
                        new DefineMoodDialogFragment.OnButtonPressListener() {
                            @Override
                            public void onSavePressed(Mood moodToSave) {

                                // Create the mood using the repository.

                                UserMoodsFragment.this.appPreferences.getRepository()
                                        .createMood(UserMoodsFragment.this.appPreferences.getCurrentUser(), moodToSave)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                // Show UI feedback if deletion failed
                                                Toast.makeText(UserMoodsFragment.this.getContext(), "Failed to add Mood. Please try again.", Toast.LENGTH_SHORT).show();
                                                Log.e(HomeActivity.LOG_TAG, "Mood failed to save (add) with exception: " + e.toString());

                                            }
                                        });
                                moodList.add(0,moodToSave);
                                applyFilter();

                            }

                        });  // End setOnButtonPressListener

                addMoodFragment.show(getFragmentManager(), "FRAGMENT_DEFINE_MOOD_ADD");

            }

        }); // End setOnClickListener


        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                // Create dialog and set the button press listener for delete and edit
                ViewUserMoodDialogFragment viewUserFragment = ViewUserMoodDialogFragment.newInstance(moodAdapter.getItem(i));
                viewUserFragment.setOnButtonPressListener(new ViewUserMoodDialogFragment.OnButtonPressListener() {
                    @Override
                    public void onDeletePressed(Mood moodToDelete) {

                        // If the user happens to be null, throw an error
                        if (UserMoodsFragment.this.appPreferences.getCurrentUser() == null)
                            throw new IllegalStateException("The current user is null, this should not happen. Did the user log in correctly?");

                        // Use the repository to delete the mood.
                        UserMoodsFragment.this.appPreferences.getRepository()
                                .deleteMood( UserMoodsFragment.this.appPreferences.getCurrentUser(), moodToDelete)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        // Show UI feedback if deletion failed
                                        Toast.makeText(UserMoodsFragment.this.getContext(), "Failed to delete Mood. Please try again.", Toast.LENGTH_SHORT).show();
                                        Log.e(HomeActivity.LOG_TAG, "Mood failed to delete with exception: " + e.toString());

                                    }
                                });
                        moodList.remove(moodToDelete);
                        applyFilter();

                    }  // End onDeletePressed

                    @Override
                    public void onEditPressed(final Mood moodToEdit) {

                        // If the user happens to be null, throw an error
                        if (UserMoodsFragment.this.appPreferences.getCurrentUser() == null)
                            throw new IllegalStateException("The current user is null, this should not happen. Did the user log in correctly?");

                        // Define a dialog fragment in the edit mode and set the listener for the save button.
                        DefineMoodDialogFragment editMoodFragment = DefineMoodDialogFragment.newInstance(moodToEdit);
                        editMoodFragment.setOnButtonPressListener(
                                new DefineMoodDialogFragment.OnButtonPressListener() {
                                    @Override
                                    public void onSavePressed(Mood moodToSave) {

                                        // Update the mood using the repository.
                                        UserMoodsFragment.this.appPreferences.getRepository()
                                                .updateMood(UserMoodsFragment.this.appPreferences.getCurrentUser(), moodToSave)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        // Show UI feedback if deletion failed
                                                        Toast.makeText(UserMoodsFragment.this.getContext(), "Failed to save Mood. Please try again.", Toast.LENGTH_SHORT).show();
                                                        Log.e(HomeActivity.LOG_TAG, "Mood failed to save (edit) with exception: " + e.toString());

                                                    }
                                                });
                                        moodList.set(position, moodToEdit);
                                        applyFilter();

                                    }
                                });

                        // Show the edit fragment
                        editMoodFragment.show(getFragmentManager(), "FRAGMENT_DEFINE_MOOD_EDIT");

                    }  // End onEditPressed

                });

                // Show the view Dialog
                viewUserFragment.show(getFragmentManager(), "FRAGMENT_VIEW_MOOD");

            }
        }); // End setOnItemClickListener

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

        inflater.inflate(R.menu.fragment_user_moods, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * This method gets called when a menu item in the toolbar is clicked. We only have one item here so we only check one
     * @param item The menu item that was selected. This value must never be null.
     * @return     Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_filter) {

            // If the menuItemFilter is uninitialized, then find it
            if (this.menuItemFilter == null)
                this.menuItemFilter = (View) this.getActivity().findViewById(R.id.action_filter);

            // If the menu is uninitialized, inflate it only once to save on performance
            if (this.menu == null) {

                this.menu = new PopupMenu(this.getContext(), this.menuItemFilter);
                this.menu.getMenuInflater().inflate(R.menu.filter_states, menu.getMenu());

                // Add all emotional states to the menu
                for (EmotionalState state : EmotionalState.values())
                    this.menu.getMenu().add(R.id.group_filter, state.getStateCode(), Menu.NONE, state.toString());
                // Set the checkable state of the group
                this.menu.getMenu().setGroupCheckable(R.id.group_filter, true, true);
                // Set the onclick listener
                this.menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        // Once we click an item, we have to set the appropriate filter. In the case of the none item, we select that, and for every other action we set it to the correct emotional state. Keep in mind that we set the item id for each emotional state menu item to exactly the statecode, so it is easy to reverse match it here.
                        if (item.getItemId() == R.id.filter_none){
                            UserMoodsFragment.this.filter = null;
                            // Show the full list
                            moodAdapter.getFilter().filter("None");
                        }
                        else {
                            // Filter the list based on the selected item's title
                            UserMoodsFragment.this.filter = EmotionalState.findByStateCode(item.getItemId());
                            moodAdapter.getFilter().filter(filter.toString());
                        }
                        // For any menu item click we set the checked state to true and return true.
                        item.setChecked(true);
                        return true;

                    }
                });

            }  // end of menu initialization
            // We now have a complete menu but in order to render it properly we need to set the item that is selected to checked. We iterate through every state and if it matches with the current filter, set its checked state to true.
            for (EmotionalState state : EmotionalState.values()) {
                MenuItem menuItem = this.menu.getMenu().findItem(state.getStateCode());

                if (this.filter == state)
                    menuItem.setChecked(true);

            }

            // If the filter happens to be null, that means that there is no filter, so we set the checked state of the "None" item.
            if (this.filter == null)
                this.menu.getMenu().findItem(R.id.filter_none).setChecked(true);

            // Show the menu
            menu.show();

        } else if (item.getItemId() == R.id.action_maps_user) {

            Toast.makeText(this.getContext(), "Display User Maps", Toast.LENGTH_SHORT).show();

        }

//        return super.onOptionsItemSelected(item);
        return true;

    }

    public void applyFilter() {
        for (int i = 0; i < this.menu.getMenu().size(); i++) {
            if (this.menu.getMenu().getItem(i).isChecked()) {

                Toast.makeText(this.getContext(), this.menu.getMenu().getItem(i).getTitle().toString(), Toast.LENGTH_SHORT).show();
                moodAdapter.getFilter().filter(this.menu.getMenu().getItem(i).getTitle().toString());

                moodAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
