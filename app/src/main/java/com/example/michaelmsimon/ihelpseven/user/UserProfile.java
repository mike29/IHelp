package com.example.michaelmsimon.ihelpseven.user;

/**
 * Created by Michael M. Simon on 14.10.2017.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelmsimon.ihelpseven.ConnectionCheck;
import com.example.michaelmsimon.ihelpseven.FindUserLocationActivity;
import com.example.michaelmsimon.ihelpseven.MainActivity;
import com.example.michaelmsimon.ihelpseven.PostTaskActivity;
import com.example.michaelmsimon.ihelpseven.R;
import com.example.michaelmsimon.ihelpseven.UserActivities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {
    private TextView userId;
    private TextView userEmail;
    private TextView theUserName;
    private TextView theUserDesc;
    private ImageView profileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageRef;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //CONNECTION
        ConnectionCheck check = new ConnectionCheck();
        if(!check.isConnectedToInternet(this.getApplicationContext())) {
            check.informBadInternet(this);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      //  getSupportActionBar().setTitle("PROFILE");

        toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);



        userId  = (TextView) findViewById(R.id.usersID);
        userEmail  = (TextView) findViewById(R.id.usersEmail);
        theUserName = (TextView) findViewById(R.id.the_user_name);
        theUserDesc = (TextView) findViewById(R.id.tvUserDisc);
        profileImage = (ImageView) findViewById(R.id.profileImgView);




        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mStorageRef= FirebaseStorage.getInstance().getReference().child("taskPostedImage");


        if (user != null) {


            final String email = user.getEmail();
            final String id = user.getUid();
            final String uname = null ;

            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String fName = (String) dataSnapshot.child("userfName").getValue().toString().toUpperCase();
                    String lName = (String) dataSnapshot.child("userlName").getValue().toString().toUpperCase();
                    String userDesc = (String) dataSnapshot.child("userDesciption").getValue().toString().toUpperCase();

                    String photoURL = (String) dataSnapshot.child("profileImage").getValue().toString();
                    Picasso.with(UserProfile.this).load(photoURL).into(profileImage);

                    toolbar.setTitle( fName + " " + lName);
                    theUserName.setText(fName + " " + lName);
                    theUserDesc.setText(userDesc);
                    // userId.setText(fName);
                    //userEmail.setText(email);
                   // Toast.makeText(UserProfile.this, fName + "   " + lName,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        //boolean emailVerified = user.isEmailVerified();


    }}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent toLoginIntent = new Intent(UserProfile.this, FindUserLocationActivity.class);
            toLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toLoginIntent);
        }
        else if(id == R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.profile_setting){
            Intent toLoginIntent = new Intent(UserProfile.this, SettingActivity.class);
            toLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toLoginIntent);
        }
        else if (id==R.id.logout){
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }


}

