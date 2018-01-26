package com.example.michaelmsimon.ihelpseven;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class SinglePostedTaskActivity extends AppCompatActivity {

    private String postKey = null;
    private String distance = null;


    private DatabaseReference mDatabase;
    private ImageView singlePostedTaskImage;
    private TextView singlePostedTaskTitle;
    private TextView singlePostedTaskCategory;
    private TextView singlePostedDescription;
    private TextView singlePostedDate;
    private TextView singlePotedTaskUserName;
    private TextView singlePostedTaskCity;
    private TextView singlePostedTaskCountry;
    private TextView singlePostedTaskDistance;
    private TextView applyButton;

    private String userKey = "";

    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_posted_task);


        //Retrieve the post key
        postKey = getIntent().getExtras().getString("postedTaskId");

        //Get calculated distance
        //  distance = getIntent().getExtras().getString("distance");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("uPostedTasks");
        //Handle the user application
        // mRef = mDatabase.getInstance().getReference().child("uPostedTasks");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        singlePostedTaskTitle = (TextView) findViewById(R.id.single_post_title);
        singlePostedTaskCategory = (TextView) findViewById(R.id.single_post_category);
        singlePostedDescription = (TextView) findViewById(R.id.single_post_desc);
        singlePostedDate = (TextView) findViewById(R.id.single_post_date);
        singlePotedTaskUserName = (TextView) findViewById(R.id.single_post_name);
        singlePostedTaskCity = (TextView) findViewById(R.id.single_post_city);
        singlePostedTaskCountry = (TextView) findViewById(R.id.single_post_country);
        singlePostedTaskDistance = (TextView) findViewById(R.id.single_post_distance);
        //  singlePostedTaskImage = (ImageView) findViewById(R.id.single_posted_image_view);

        applyButton = (TextView) findViewById(R.id.apply_to_task);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String postTitle = (String) dataSnapshot.child("task_title").getValue();
                String postCategory = (String) dataSnapshot.child("task_category").getValue();
                String postDesc = (String) dataSnapshot.child("task_description").getValue();
                String postDate = (String) dataSnapshot.child("task_posted_date").getValue();
                String postUName = (String) dataSnapshot.child("task_posted_by_fName").getValue();
                String postLName = (String) dataSnapshot.child("task_posted_by_lName").getValue();
                String postCity = (String) dataSnapshot.child("city").getValue();
                String postCountry = (String) dataSnapshot.child("country").getValue();
                String uKey = (String) dataSnapshot.child("user_id").getValue();
                //  String postImage = (String) dataSnapshot.child("image").getValue();

                userKey = uKey;


                String titleToUpperCase = postTitle.toUpperCase();
                singlePostedTaskTitle.setText(titleToUpperCase);
                singlePostedTaskCategory.setText(postCategory);
                singlePostedDescription.setText(postDesc);
                singlePostedDate.setText(postDate);
                singlePotedTaskUserName.setText("Posted By: " + postUName + " " + postLName);
                singlePostedTaskCity.setText(postCity);
                singlePostedTaskCountry.setText(postCountry);

                //Temporary distance
                Random rand = new Random();
                int tempDistance = rand.nextInt(120) + 1;
                String toS = String.valueOf(tempDistance);
                singlePostedTaskDistance.setText(toS + " " + "Km");

                //If images will be used
                // Picaso.with(SinglePostedTaskActivity.this).load(postImage).into(singlePostedTaskImage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleUserApplication(mCurrentUser.getUid());

            }
        });


        singlePotedTaskUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Toast.makeText(getApplicationContext(),
                        "You clicked on Name", Toast.LENGTH_SHORT)
                        .show();
               */
                // navigate to the ShowClickedUserProfile
                Intent toSingleTaskActivity = new Intent(SinglePostedTaskActivity.this, ShowSelectedUserProfile.class);
                toSingleTaskActivity.putExtra("clickedUserId", userKey);
                //Here put extra the distance that is calculated
                startActivity(toSingleTaskActivity);


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    int applicationCounter = 0;

    String strI = "";

    private void handleUserApplication(final String applied_user) {


        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //check if the user applied earlier
                //    if(applicationCounter >=0 && applicationCounter <=10){

                if (!applyButton.isActivated()) {

                    Toast.makeText(getApplicationContext(),
                            "Pending answer, you applied to this task", Toast.LENGTH_LONG)
                            .show();

                    applyButton.setEnabled(false);
                    applyButton.setBackgroundColor(Color.GRAY);
                    applyButton.setTextColor(Color.LTGRAY);

                }
                //     else {

                strI = String.valueOf(applicationCounter);
                //TODO
                //test id the user has applied earlier by comparing the user id to registered applications user ID
                //user can't apply to it's own task

                DatabaseReference apply = mDatabase.child(postKey).child("applied_Users");
                //DatabaseReference newTaskPost = apply.push();
                apply.child(strI).setValue(applied_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),
                                    "APPLICATION SENT" + postKey + " : " + mCurrentUser.getUid(), Toast.LENGTH_LONG)
                                    .show();
                            applicationCounter = Integer.parseInt(String.valueOf(applicationCounter));
                            applicationCounter++;
                            applyButton.setEnabled(false);
                            applyButton.setBackgroundColor(Color.GRAY);
                            applyButton.setTextColor(Color.LTGRAY);


                        }
                    }

                });
                //      }//close else
                //   }//if statement


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

