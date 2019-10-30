package edu.ualberta.cmput301f19t17.bigmood.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import edu.ualberta.cmput301f19t17.bigmood.model.Request;

public class RequestAdapter extends ArrayAdapter<Request> {

    public RequestAdapter(@NonNull Context context, int resource, @NonNull Request[] objects) {
        super(context, resource, objects);

    }



}
