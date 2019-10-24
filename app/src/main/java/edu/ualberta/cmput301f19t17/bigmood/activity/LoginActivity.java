package edu.ualberta.cmput301f19t17.bigmood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.ualberta.cmput301f19t17.bigmood.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * DEBUG
         */

        Button debugButtonLogin = this.findViewById(R.id.debug_button_login);
        Button debugButtonSignUp = this.findViewById(R.id.debug_button_sign_up);

        debugButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        });

        debugButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        });


        /**
         * DEBUG
         */

    }
}
