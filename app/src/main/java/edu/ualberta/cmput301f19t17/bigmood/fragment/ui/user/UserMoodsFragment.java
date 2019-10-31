package edu.ualberta.cmput301f19t17.bigmood.fragment.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.adapter.MoodAdapter;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.DefineMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.fragment.dialog.ViewUserMoodDialogFragment;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class UserMoodsFragment extends Fragment {
    private ArrayList<Mood> moodList;
    private ArrayAdapter<Mood> moodAdapter;

    private UserMoodsViewModel userMoodsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userMoodsViewModel = ViewModelProviders.of(this).get(UserMoodsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_user_moods, container, false);

//        final TextView textView = root.findViewById(R.id.text_user_moods);

//        userMoodsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);

        ListView moodListView = root.findViewById(R.id.mood_list);

        moodList = new ArrayList<>();
        moodAdapter = new MoodAdapter(root.getContext(), R.layout.mood_item, moodList);
        moodListView.setAdapter(moodAdapter);

        //TODO Cameron 10-26-2019 remove canned data
        final Mood mockMood1 = new Mood("2019-07-25", "12:24", "Sad");
        final Mood mockMood2 = new Mood("2019-10-20", "11:11", "Happy");
        final Mood mockMood3 = new Mood("2019-10-21", "12:12", "Anger");
        final Mood mockMood4 = new Mood("2019-10-22", "13:13", "Disgust");
        final Mood mockMood5 = new Mood("2019-10-23", "14:14", "Fear");
        final Mood mockMood6 = new Mood("2019-10-24", "15:15", "Surprise");
        mockMood5.setSituation("Alone");
        mockMood5.setReason("Just watched a horror film");
        moodList.add(mockMood1);
        moodList.add(mockMood2);
        moodList.add(mockMood3);
        moodList.add(mockMood4);
        moodList.add(mockMood5);
        moodList.add(mockMood6);
        moodAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DefineMoodDialogFragment fragment = DefineMoodDialogFragment.newInstance();
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

                        DefineMoodDialogFragment defineFragment =
                                DefineMoodDialogFragment.newInstance(moodList.get(finalIndex));
                        defineFragment.show(getFragmentManager(), "DEFINE_MOOD_FRAGMENT_EDIT");

                    }
                });

                fragment.show(getFragmentManager(), "VIEW_MOOD_FRAGMENT");
            }
        });


        return root;
    }
}