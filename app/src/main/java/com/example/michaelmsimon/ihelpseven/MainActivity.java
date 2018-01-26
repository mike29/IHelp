package com.example.michaelmsimon.ihelpseven;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.michaelmsimon.ihelpseven.user.RegisterActivity;
import com.example.michaelmsimon.ihelpseven.user.SettingActivity;
import com.example.michaelmsimon.ihelpseven.user.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rvTaskPrinter;
    private DatabaseReference fdatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private int menuItemID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CONNECTION
        ConnectionCheck check = new ConnectionCheck();
        if(!check.isConnectedToInternet(this.getApplicationContext())) {
            check.informBadInternet(this);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home);
        navigationView = (NavigationView) findViewById(R.id.navigation);

        //HIDE HOME
        Menu m = navigationView.getMenu();
        m.findItem(R.id.home_button).setVisible(false);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Posted Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Make the hamburger button work
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


        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItemID = menuItem.getItemId();


                if (menuItemID == R.id.profile) {
                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.create_task) {
                    Intent intent = new Intent(MainActivity.this, PostTaskActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.user_message) {
                    Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.activities) {
                    Intent intent = new Intent(MainActivity.this, UserActivities.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.setting) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.logout) {
                    mAuth.signOut();

                }

                return true;
            }
        });


        //Handle the drawer
      /*  mDrawerLayout = (DrawerLayout) findViewById(R.id.home);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        */


        //The recyclerview ref and size
        rvTaskPrinter = (RecyclerView) findViewById(R.id.lvPrinter);
        rvTaskPrinter.setHasFixedSize(true);
        rvTaskPrinter.setLayoutManager(new LinearLayoutManager(this));

        fdatabase = FirebaseDatabase.getInstance().getReference().child("uPostedTasks");

        //AUTH
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };


    }//onCreateEND


    @Override
    protected void onStart() {
        super.onStart();
        //listens to loging
        mAuth.addAuthStateListener(mAuthListener);

        fdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayTasksFromDB(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void displayTasksFromDB(final DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            FirebaseRecyclerAdapter<TaskBlogModelInt, TaskViewHolder> fireAdapter =
                    new FirebaseRecyclerAdapter<TaskBlogModelInt, TaskViewHolder>(

                            TaskBlogModelInt.class,
                            R.layout.custom_task_row,
                            TaskViewHolder.class,
                            fdatabase) {

                        @Override
                        protected void populateViewHolder(TaskViewHolder viewHolder, TaskBlogModelInt model, int position) {
                            //get the post key
                            final String postKey = getRef(position).getKey().toString();

                            viewHolder.setTitle(model.getTask_title());
                            viewHolder.setCat(model.getTask_category());
                            viewHolder.setPostedDate(model.getTask_posted_date());
                            viewHolder.setDesc(model.getTask_description());
                            viewHolder.setLocCity(model.getCity());
                            viewHolder.setPostedBy(model.getTask_posted_by_fName());
                            viewHolder.setPostedByUserKey(model.getUserID());
                            //  viewHolder.setLocCountry(model.getCountry());


                            // Log.d(TAG, "blaaaaref-----------" + dataSnapshot.getValue().toString());


                            //Listen to the viewholder
                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Intent toSingleTaskActivity = new Intent(MainActivity.this, SinglePostedTaskActivity.class);
                                    toSingleTaskActivity.putExtra("postedTaskId", postKey);
                                    //Here put extra the distance that is calculated
                                    startActivity(toSingleTaskActivity);
                                }
                            });


                        }
                    };
            rvTaskPrinter.setAdapter(fireAdapter);


        }
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
            Intent intent = new Intent(MainActivity.this, PostTaskActivity.class);
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
