package edu.ualberta.cmput301f19t17.bigmood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import edu.ualberta.cmput301f19t17.bigmood.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /**
         * DEBUG
         */

        // Activate Back navigation
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * DEBUG
         */

    }

    // Hardware back navigation
    @Override
    public void onBackPressed() {

        Log.d(HomeActivity.LOG_TAG, "Back navigation (Hardware) from " + this.getClass().getSimpleName());
        this.finish();

    }

    // Software back navigation
    @Override
    public boolean onSupportNavigateUp() {

        Log.d(HomeActivity.LOG_TAG, "Back navigation (Software) from " + this.getClass().getSimpleName());
        this.finish();

        return true;

    }

}
