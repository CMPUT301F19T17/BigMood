package edu.ualberta.cmput301f19t17.bigmood.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.ualberta.cmput301f19t17.bigmood.R;

/**
 * SignUpActivity allows the user to create a new account. It is then uploaded to firestore, if it is valid.
 */
public class SignUpActivity extends AppCompatActivity {

    AppPreferences appPreferences;

    /**
     * onCreate is called when the Activity is created, and it is used to perform the logic that the Activity
     * needs, such as setting onClickListeners.
     * @param savedInstanceState if the instance was saved, this would be sent in when the Activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.appPreferences = AppPreferences.getInstance();

        // get Toolbar
        Toolbar toolbar = this.findViewById(R.id.toolbar_activity_sign_up);

        // Set Title
        toolbar.setTitle(this.getString(R.string.title_activity_sign_up));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.this.onBackNavigationClicked();
            }
        });

    }

    /**
     * This method is for hardware back navigation, so if the phone that the user uses has an physical back button
     * and pressed the button, then this method will be called
     */
    private void onBackNavigationClicked() {

        Log.d(HomeActivity.LOG_TAG, "Back navigation (Software) from " + this.getClass().getSimpleName());
        this.finish();

    }

    /**
     * This method is for hardware back navigation, so if the phone that the user uses has an physical back button
     * and pressed the button, then this method will be called
     */    @Override
    public void onBackPressed() {

        Log.d(HomeActivity.LOG_TAG, "Back navigation (Hardware) from " + this.getClass().getSimpleName());
        this.finish();

    }

}
