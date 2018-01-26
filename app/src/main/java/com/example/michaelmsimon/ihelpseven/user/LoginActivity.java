package com.example.michaelmsimon.ihelpseven.user;

/**
 * Created by Michael M. Simon on 14.10.2017.
 */

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelmsimon.ihelpseven.ConnectionCheck;
import com.example.michaelmsimon.ihelpseven.MainActivity;
import com.example.michaelmsimon.ihelpseven.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private Button loginButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //CONNECTION
        ConnectionCheck check = new ConnectionCheck();
        if(!check.isConnectedToInternet(this.getApplicationContext())){
            check.informBadInternet(this);

            //TODO
            //Check connection and update user
        }


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        loginEmail = (EditText) findViewById(R.id.eMailField);
        loginPass = (EditText) findViewById(R.id.passField);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String pass = loginPass.getText().toString().trim();
                loginButtonClicked(email, pass);
            }
        });


    }

    public void loginButtonClicked(String email, String pass) {

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Log.d(TAG, "signInWithEmail:onCompl
                            if (task.isSuccessful()) {

                                final String user_id = mAuth.getCurrentUser().getUid();

                                //TODO
                                //This method was uppose to check if the useris found in the db then it will take theuser to the main page
                                //but not working at the moment
                                // checkIfUserExist();


                                Toast.makeText(LoginActivity.this, " loged in as: " + user_id,
                                        Toast.LENGTH_SHORT).show();

                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(loginIntent);

                            } else if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
        }

    }

    public void checkIfUserExist() {

        Toast.makeText(LoginActivity.this, mDatabase.getKey(),
                Toast.LENGTH_SHORT).show();

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild(user_id)) {
                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
