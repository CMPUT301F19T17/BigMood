package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodAdapter;
import edu.ualberta.cmput301f19t17.bigmood.database.Repository;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.DefineMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewUserMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class UserMoodsFragment extends Fragment {
    private ArrayList<Mood> moodList;
    private ArrayAdapter<Mood> moodAdapter;

    private UserMoodsViewModel userMoodsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userMoodsViewModel = ViewModelProviders.of(this).get(UserMoodsViewModel.class);

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
                            public void onSavePressed(Mood mood) {
                                //TODO call database method for saving
                                Log.d("Save Pressed", "Adding Mood");
                                moodList.add(mood);
                                moodAdapter.notifyDataSetChanged();
                                Repository repository = Repository.getInstance();
                                repository.createMood(repository.getUser(), mood);
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

                        moodList.remove(finalIndex);
                        moodAdapter.notifyDataSetChanged();

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
                                        //  Call database method for editing
                                        Repository repository = Repository.getInstance();
                                        repository.createMood(repository.getUser(), mood);
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
}