package edu.ualberta.cmput301f19t17.bigmood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.model.EmotionalState;

public class MoodSpinnerAdapter extends ArrayAdapter<EmotionalState> {


    public MoodSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<EmotionalState> arrayList) {
        super(context, resource, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mood_spinner_item,parent,false);
        }
        ImageView emoticonImageView = convertView.findViewById(R.id.mood_spinner_item_emoticon);
        TextView stateTextView = convertView.findViewById(R.id.mood_spinner_item_state);

        EmotionalState emotionalState = getItem(position);


        emoticonImageView.setImageResource(emotionalState.getDrawableId());
        stateTextView.setText(emotionalState.toString());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mood_spinner_item,parent,false);
        }
        ImageView emoticonImageView = convertView.findViewById(R.id.mood_spinner_item_emoticon);
        TextView stateTextView = convertView.findViewById(R.id.mood_spinner_item_state);

        EmotionalState emotionalState = getItem(position);


        emoticonImageView.setImageResource(emotionalState.getDrawableId());
        stateTextView.setText(emotionalState.toString());

        return convertView;
    }


}