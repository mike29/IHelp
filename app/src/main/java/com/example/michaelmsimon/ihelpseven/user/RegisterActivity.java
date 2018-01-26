package com.example.michaelmsimon.ihelpseven.user;

/**
 * Created by Michael M. Simon on 14.10.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michaelmsimon.ihelpseven.ConnectionCheck;
import com.example.michaelmsimon.ihelpseven.MainActivity;
import com.example.michaelmsimon.ihelpseven.PostTaskActivity;
import com.example.michaelmsimon.ihelpseven.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passField;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText repPassField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //CONNECTION
        ConnectionCheck check = new ConnectionCheck();
        if(!check.isConnectedToInternet(this.getApplicationContext())){
            check.informBadInternet(this);
        }

        //visibility actionbar
        getSupportActionBar().setTitle("REGISTER");

        emailField = (EditText) findViewById(R.id.eMailField);
        passField = (EditText) findViewById(R.id.passField);
        repPassField = (EditText) findViewById(R.id.repPassField);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }
    public void registerButtonClicked(View view){
        final String email = emailField.getText().toString().trim();
        final String pass = passField.getText().toString().trim();
        final String pass1 = repPassField.getText().toString().trim();

        //TODO
       //Check length, email character details
        if(!TextUtils.isEmpty(email)&&
                !TextUtils.isEmpty(pass)
                       )
            {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override public void onComplete(@NonNull Task<AuthResult> task) {
                    //   Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the lsistener.
                    if (task.isSuccessful()) {
                        // Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                        //       Toast.LENGTH_SHORT).show();
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_User_db = mDatabase.child(user_id);
                        current_User_db.child("email").setValue(email);
                        current_User_db.child("pass").setValue(pass);
                        current_User_db.child("image").setValue("iMG_DEFAULT");


                        //TODO
                        //Check if the user is registered before sending further

                        Toast.makeText(getApplicationContext(), "REGISTERED \n" ,
                                Toast.LENGTH_SHORT).show();

                        Intent toLoginIntent = new Intent(RegisterActivity.this, SettingActivity.class);
                        toLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(toLoginIntent);

                    }

                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill the required information, Use 8 or more character password ;)" ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void toLoginActivity(View view){
        Intent tologin = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(tologin);

    }



}


