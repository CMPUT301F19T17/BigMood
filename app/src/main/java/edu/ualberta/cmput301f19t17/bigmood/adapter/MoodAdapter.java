package edu.ualberta.cmput301f19t17.bigmood.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class MoodAdapter extends ArrayAdapter<Mood> {

    public MoodAdapter(@NonNull Context context, int resource, @NonNull Mood[] objects) {
        super(context, resource, objects);
    }

}
