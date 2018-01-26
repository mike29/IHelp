package com.example.michaelmsimon.ihelpseven;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelmsimon.ihelpseven.user.SettingActivity;
import com.example.michaelmsimon.ihelpseven.user.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowSelectedUserProfile extends AppCompatActivity {
    TextView userKey_print;

    private TextView userFNames;
    private TextView userLNames;
    private TextView userDescs;
    private String fName;
    private String lName;
    private String userDesc;
    private ImageView profileImage;
    private TextView userDist;

    private DrawerLayout mDrawerLayout;
    private String TAG;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private int menuItemID;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected_user_profile);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.user_visited_drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_user_visited_activity);

        userFNames = (TextView) findViewById(R.id.cUserFName);
        userLNames = (TextView) findViewById(R.id.cUserLName);
        userDescs = (TextView) findViewById(R.id.tvUserDisc);
        profileImage = (ImageView) findViewById(R.id.profileImgView);
        userDist = (TextView) findViewById(R.id.userDistance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user_visited);
        toolbar.setTitle("User");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                // navigationView.getMenu().findItem(menuItemID).setChecked(false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        handleDrawer();


        final String userVisitedKey = getIntent().getExtras().getString("clickedUserId");

        userKey_print = findViewById(R.id.userKey);
        //userKey_print.append("" + userVisitedKey);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userVisitedKey);
        mAuth = FirebaseAuth.getInstance();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                fName = (String) dataSnapshot.child("userfName").getValue().toString().toUpperCase();
                lName = (String) dataSnapshot.child("userlName").getValue().toString().toUpperCase();
                userDesc = (String) dataSnapshot.child("userDesciption").getValue().toString().toUpperCase();
                String photoURL = (String) dataSnapshot.child("profileImage").getValue().toString();
                Picasso.with(ShowSelectedUserProfile.this).load(photoURL).into(profileImage);


                userFNames.append(fName + " " + lName);
                userDescs.append(userDesc);


                Toast.makeText(ShowSelectedUserProfile.this, fName + "   " + lName + ": " + userDesc, Toast.LENGTH_SHORT).show();
                //  getActionBar().setTitle(fName + " " + lName);
                // getActionBar().setSubtitle(fName + " " + lName);
                // theUserDesc.setText(userDesc);
                // userId.setText(fName);
                //userEmail.setText(email);
                // Toast.makeText(UserProfile.this, fName + "   " + lName,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // }//endIF


    }

    private void handleDrawer() {

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItemID = menuItem.getItemId();


                if (menuItemID == R.id.profile) {
                    Intent intent = new Intent(ShowSelectedUserProfile.this, UserProfile.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.create_task) {
                    Intent intent = new Intent(ShowSelectedUserProfile.this, PostTaskActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.user_message) {
                    Intent intent = new Intent(ShowSelectedUserProfile.this, MessagesActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.home_button) {
                    Intent intent = new Intent(ShowSelectedUserProfile.this, MainActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.activities) {
                    Intent intent = new Intent(ShowSelectedUserProfile.this, UserActivities.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.setting) {
                    Intent intent = new Intent(ShowSelectedUserProfile.this, SettingActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.logout) {
                    mAuth.signOut();

                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        } else if (id == R.id.addTask) {
            Intent intent = new Intent(ShowSelectedUserProfile.this, PostTaskActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            mAuth.signOut();
        } else if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
