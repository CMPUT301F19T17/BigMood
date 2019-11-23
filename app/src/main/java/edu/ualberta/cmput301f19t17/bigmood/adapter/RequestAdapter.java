package edu.ualberta.cmput301f19t17.bigmood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import edu.ualberta.cmput301f19t17.bigmood.R;
import edu.ualberta.cmput301f19t17.bigmood.activity.AppPreferences;
import edu.ualberta.cmput301f19t17.bigmood.model.Request;

public class RequestAdapter extends ArrayAdapter<Request> {
    private AppPreferences appPreferences;
    private int resource;

    /**
     *
     * This constructor is used to create a new RequestAdapter
     *
     * @param context the activity that the MoodAdapter is created in
     * @param resource the ID of the layout resource that getView() would inflate to create the view
     * @param requestList the list of requests
     */
    public RequestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Request> requestList) {  //Request[] objects) {
        super(context, resource, requestList);
        this.resource = resource;
        this.appPreferences = AppPreferences.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //This RequestHolder will hold our views while we create them
        RequestHolder requestHolder;

        // We test if convertView is null so we can know if we have to inflate it or not (findViewById)
        if (convertView == null) {

            // Define new inflater and inflate the view.
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);

            // Create new holder object since we are in a part of execution where the row has not been cached yet.
            requestHolder = new RequestHolder();

            // Set all fields of the holder class
            requestHolder.requester = convertView.findViewById(R.id.textview_from);

            // Cache views for that row using setTag on the full row view
            convertView.setTag(requestHolder);

        } else {

            // The row has been created and we can reuse it, but to change the fields in
            // the row we need to pull the holder from cache using getTag
            requestHolder = (RequestHolder) convertView.getTag();

        }

        // Get the current ride in the array using methods in ArrayAdapter
        final Request currentRequest = this.getItem(position);

        // Set the requester's name in the row.
        requestHolder.requester.setText(String.format("@%s", currentRequest.getFrom()));

        // Set listener for dismiss and accept TextView buttons
        TextView buttonDismiss = convertView.findViewById(R.id.textview_button_reject);
        TextView buttonAccept = convertView.findViewById(R.id.textview_button_accept);

        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPreferences.getRepository().declineRequest(currentRequest);
                Toast.makeText(getContext(), "Request declined", Toast.LENGTH_LONG).show();
            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPreferences.getRepository().acceptRequest(currentRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Request accepted", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        // Return the created/reused view as per the method signature
        return convertView;
    }

    /**
     * This class is a small helper class to cache the views taken from
     * convertView.findViewById() since these finds can be expensive when in a ListView.
     * It just holds TextView resources we'll get and set in this class only.
     */
    private static class RequestHolder {
        TextView requester;
    }
}
