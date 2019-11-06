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

import java.util.ArrayList;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodAdapter;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.DefineMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewUserMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class UserMoodsFragment extends Fragment {
    private ArrayList<Mood> moodList;
    private ArrayAdapter<Mood> moodAdapter;

    private UserMoodsViewModel userMoodsViewModel;

    private View menuItemFilter;

    private PopupMenu menu;

    private EmotionalState filter = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userMoodsViewModel = ViewModelProviders.of(this).get(UserMoodsViewModel.class);
        final AppPreferences appPreferences = AppPreferences.getInstance();

        // Enable options menu
        this.setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_user_moods, container, false);

        final ListView moodListView = root.findViewById(R.id.mood_list);
        moodList = new ArrayList<>();
        moodAdapter = new MoodAdapter(root.getContext(), R.layout.mood_item, moodList);
        moodListView.setAdapter(moodAdapter);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DefineMoodDialogFragment fragment = DefineMoodDialogFragment.newInstance();
                fragment.setOnButtonPressListener(
                        new DefineMoodDialogFragment.OnButtonPressListener() {
                            @Override
                            public void onSavePressed(final Mood mood) {
                                Log.d("Save Pressed", "Adding Mood");
                                //TODO dont update local list directly
                                if (appPreferences.getCurrentUser() != null) {
                                    appPreferences.getRepository().createMood(appPreferences.getCurrentUser(), mood)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            moodList.add(mood);
                                            moodAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("SAVING", "FAILED TO SAVE TO FIRESTORE");
                                        }
                                    });

                                }
                                else
                                    throw new IllegalStateException("USER IS NULL");

                            }
                        });
                fragment.show(getFragmentManager(), "DEFINE_MOOD_FRAGMENT_ADD");

            }
        });


        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ViewUserMoodDialogFragment fragment = ViewUserMoodDialogFragment.newInstance(moodList.get(i));
                final int finalIndex = i;
                fragment.setOnButtonPressListener(new ViewUserMoodDialogFragment.OnButtonPressListener() {
                    @Override
                    public void onDeletePressed() {
                        if (appPreferences.getCurrentUser() != null) {
                            appPreferences.getRepository().deleteMood(
                                    appPreferences.getCurrentUser(), moodList.get(finalIndex))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //TODO dont update local list directly
                                    moodList.remove(finalIndex);
                                    moodAdapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("DELETING", "FAILED TO DELETE FROM FIRESTORE");

                                }
                            });

                        }
                        else
                            throw new IllegalStateException("USER IS NULL");

                        Toast.makeText(getContext(), "The Mood was deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onEditPressed() {
                        Mood moodToEdit = moodList.get(finalIndex);

                        DefineMoodDialogFragment defineFragment = DefineMoodDialogFragment.newInstance(moodToEdit);
                        defineFragment.setOnButtonPressListener(
                                        new DefineMoodDialogFragment.OnButtonPressListener() {
                                    @Override
                                    public void onSavePressed(Mood mood) {
                                        // TODO Cameron Oct 28, 2019 add location and image
                                        if (appPreferences.getCurrentUser() != null)
                                            appPreferences.getRepository().updateMood(
                                                    appPreferences.getCurrentUser(), mood)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("EDITING", "EDIT SUCCESSFUL");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e("EDITING", "FAILED TO EDIT MOOD IN FIRESTORE");
                                                        }
                                                    });
                                        else
                                            throw new IllegalStateException("USER IS NULL");
                                    }
                                });
                        defineFragment.show(getFragmentManager(), "DEFINE_MOOD_FRAGMENT_EDIT");

                    }
                });

                fragment.show(getFragmentManager(), "VIEW_MOOD_FRAGMENT");
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

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
                        if (item.getItemId() == R.id.filter_none)
                            UserMoodsFragment.this.filter = null;
                        else
                            UserMoodsFragment.this.filter = EmotionalState.findByStateCode(item.getItemId());
                            moodAdapter.getFilter().filter(item.getTitle());
                            moodAdapter.notifyDataSetChanged();

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
}
