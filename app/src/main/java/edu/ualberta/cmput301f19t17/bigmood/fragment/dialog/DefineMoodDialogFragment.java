package edu.ualberta.cmput301f19t17.bigmood.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.model.Mood;

public class DefineMoodDialogFragment extends DialogFragment {
    //PRIVATE VARIABLES
    private Spinner stateSpinner;
    private Spinner situationSpinner;
    private EditText reasonEditText;
    private Toolbar toolbar;

    public DefineMoodDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //make the attributes pan up when the soft keyboard appears while typing
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // initializations
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_define_mood, null);
        stateSpinner = view.findViewById(R.id.state_spinner);
        situationSpinner = view.findViewById(R.id.situation_spinner);
        reasonEditText = view.findViewById(R.id.reason_edit_text);

        // get the Mood if we sent one in through the args bundle
        Bundle args = getArguments();
        Mood mood = null;
        int moodPosition = -1;
        int statePosition = -1;
        int situationPosition = -1;
        if (args != null) {
            mood = (Mood) args.get("RIDE");
            moodPosition = (int) args.get("MOOD_POS");
            statePosition = (int) args.get("STATE_POS");
            situationPosition = (int) args.get("SITUATION_POS");
            if (mood != null) {
                stateSpinner.setSelection(statePosition);
                situationSpinner.setSelection(situationPosition);
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Mood finalMood = mood;
        final int finalMoodPosition = moodPosition;
        return builder
                .setTitle(getString(R.string.title_define))
                .setView(view)
                .setNegativeButton(getString(R.string.delete_txt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (finalMood != null) {
                            //delete the mood

                            ArrayList<Mood> moodDataList = listener.getMoodDataList();
                            ArrayAdapter<Mood> moodArrayAdapter = listener.getMoodArrayAdapter();
                            moodDataList.remove(finalMoodPosition);
                            moodArrayAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNeutralButton(getString(R.string.cancel_txt), null)
                .setPositiveButton(getString(R.string.confirm_txt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (finalMood != null) {
                            // edit the existing mood

                            //TODO Implement Images and location editing.
                            ArrayList<Mood> moodDataList = listener.getMoodDataList();
                            ArrayAdapter<Mood> moodArrayAdapter = listener.getMoodArrayAdapter();

                            String state = stateSpinner.getSelectedItem().toString();
                            String situation = situationSpinner.getSelectedItem().toString();
                            String reason = reasonEditText.getText().toString();

                            // the data is fine, so we can edit the ride object without fear
                            finalMood.setState(state);
                            finalMood.setSituation(situation);
                            finalMood.setReason(reason);
                            moodArrayAdapter.notifyDataSetChanged();
                        }
                        else {
                            // add a new mood

                            //get the date and time
                            Date calendarDate = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
                            String date = dateFormat.format(calendarDate);
                            dateFormat = new SimpleDateFormat("HH:mm", Locale.CANADA);
                            String time = dateFormat.format(calendarDate);

                            String state = stateSpinner.getSelectedItem().toString();
                            String situation = situationSpinner.getSelectedItem().toString();
                            String reason = reasonEditText.getText().toString();
                            //TODO Currently dummy data, implement
                            Pair<Double, Double> location = new Pair<>(0.0, 0.0);
                            Bitmap image = Bitmap.createBitmap(20,20, Bitmap.Config.ARGB_8888);

                            if (situation.equals("") || reason.equals("") || location == null || image == null) {
                                listener.addNewMood(new Mood(date, time, state), situation, reason);
                            }
                            else {
                                listener.addNewMood(new Mood(date, time, state, reason, situation, location, image));
                            }
                        }
                    }
                })
                .show();

    }
}
