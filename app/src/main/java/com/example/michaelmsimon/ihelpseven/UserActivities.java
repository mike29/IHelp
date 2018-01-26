package com.example.michaelmsimon.ihelpseven;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceFragment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelmsimon.ihelpseven.user.SettingActivity;
import com.example.michaelmsimon.ihelpseven.user.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class UserActivities extends AppCompatActivity implements AcceptApplicationFragment.OnFragmentInteractionListener {

    private TextView singlePostedTaskTitle;
    private TextView singlePostedTaskCategory;
    private TextView singlePostedDescription;
    private TextView singlePostedDate;
    private TextView singlePostedTaskCity;
    private TextView singlePostedTaskCountry;
    private TextView applicants_printer;
    private String uIDToFrag;
    private RecyclerView rvTaskPrinter;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUser;
    private String TAG;
    private String allApplicantUserNamesReturned = "";

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private int menuItemID;

    //OPS
    private DatabaseReference fdatabase;
    private DatabaseReference fdatabaseTaskByUser;
    private DatabaseReference fdatabaseTaskByUsers;
    private DatabaseReference usersRef;


    private String specificKey = "";
    private ListView printApplicantsList;

    //ops
    ArrayList<String> taskList;
    String[] listOfAppliedByID;
    String returnedApplicantKey;
    FragmentTransaction fragmentTransaction;
    Toolbar toolbar;

    ArrayList<DataModel> dataModels;
    private static CustomAdapterApplicants adapter;
    ArrayList<DataModel> dataModelsToAccept;
    private static CustomAdapterToAcceptAplicant adapterToAccept;
    private ListView listView;
    private ImageButton accept_application_confirm;
    private ArrayList<String> keys;

    ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> tempapplicantHolder;
    UsersList holdNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activities);

        printApplicantsList = (ListView) findViewById(R.id.printContentTempo);
        taskList = new ArrayList<>();


        listView = (ListView) findViewById(R.id.list);
        dataModels = new ArrayList<>();
        dataModelsToAccept = new ArrayList<>();
        accept_application_confirm = (ImageButton) findViewById(R.id.accept_applications_confirm);

        // applicants_printer = (TextView) findViewById(R.id.applicants_printer);
        // accept_application = (ImageButton) findViewById(R.id.accept_application);
        //rvTaskPrinter = (RecyclerView) findViewById(R.id.lvPrinter);
        //rvTaskPrinter.setHasFixedSize(true);
        //rvTaskPrinter.setLayoutManager(new LinearLayoutManager(this));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home);
        navigationView = (NavigationView) findViewById(R.id.navigation);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Posted Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

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

        //Handle user activites
        //  tasksPostedByActiveUser();

        fdatabase = FirebaseDatabase.getInstance().getReference().child("uPostedTasks");
        fdatabaseTaskByUser = FirebaseDatabase.getInstance().getReference();
        fdatabaseTaskByUsers = FirebaseDatabase.getInstance().getReference().child("uPostedTasks");
        usersRef = FirebaseDatabase.getInstance().getReference();


        //user activities
        tasksPostedByActiveUser();

        //Handle fragment
         /*AcceptApplicationFragment acceptApplicationFragment = new AcceptApplicationFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.application_fragment_container, acceptApplicationFragment);
        ft.commit();
        */

    }

    private void loadFragment(View view) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    DataSnapshot keyFind;

    private void tasksPostedByActiveUser() {
        fdatabase.orderByChild("user_id").equalTo(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //MainActivity m = new MainActivity();
                //m.displayTasksFromDB(dataSnapshot);
                String userID = "";
                String taskTitle = "";
                String theKey = "";
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    keyFind = s;
                    TaskBlogModelInt m = new TaskBlogModelInt();

                    userID = s.child("user_id").getValue().toString();
                    String title = s.child("task_title").getValue().toString();
                    String taskID = s.getKey();
                    String name = s.child("task_posted_by_fName").getValue().toString();
                    String date = s.child("task_posted_date").getValue().toString();

                    int Count_applied_user_childs = (int) s.child("applied_Users").getChildrenCount();

                    uIDToFrag = userID;

                    //get the name of th applicant using his ID


                    String dateClipped = date;
                    String[] parts = dateClipped.split(",");
                    String monthAndDate = parts[0]; //Nov 16
                    String yearAndHour = parts[1];
                    String[] year = yearAndHour.trim().split("\\s+");
                    String exactYear = year[0];//2017

                    listOfAppliedByID = new String[Count_applied_user_childs];

                    String applied_by = "0";

                    for (int i = 0; i < Count_applied_user_childs; i++) {

                        try {
                            applied_by = s.child("applied_Users").child(i + "").getValue().toString();


                            listOfAppliedByID[i] = applied_by;
                            // taskList.add(applied_by);

                        } catch (Exception e) {
                            applied_by = "NO APPLICANTS";

                        }

                    }
                    Log.d(TAG, "Applid by person- in loop11----------" + s.getKey() + " ");
                    Log.d(TAG, "Applid by person- out of loop----------" + listOfAppliedByID.length);
                    //  taskList.add(title +  "\n" + "POSTED DATE: " + monthAndDate +", " + exactYear + "\n" +listOfAppliedByID.length + " Person applied " );


                    //send applied_id to the fragment display using listOfAppiedID array
                    for (int i = 0; i < listOfAppliedByID.length; i++) {
                        Log.d(TAG, "Applid by personS REAL----------" + listOfAppliedByID[i]);
                        // applicants_printer.append(listOfAppliedByID[i] + "\n");


                    }


                    dataModels.add(new DataModel(title, listOfAppliedByID.length + "", monthAndDate + ", " + exactYear, taskID, accept_application_confirm));

                    //    taskList.add(title + "\n" + "POSTED DATE: " + monthAndDate +", " + exactYear + "\n" + "applicant: " +  applied_by);


                    adapter = new CustomAdapterApplicants(dataModels, getApplicationContext());
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DataModel dataModel = dataModels.get(position);

                        /*    Toast.makeText(getApplicationContext(), "Task details"+ dataModel.getTaskID() +
                                    " : "+ dataModel.getAName()+" " +  dataModel.getDate() ,
                                    Toast.LENGTH_SHORT).show();
                                    */
                            // taskList.add(dataModel.getAName());

                            getThListOfApplicants(dataModel);

                            Log.d(TAG, "ITEM ADAPTER VALUE-----" + position);


                        }

                    });

                    //theKey = findTaskKey.getKey();
                    //get applicants information method
                    TaskBlogModelInt post = dataSnapshot.getValue(TaskBlogModelInt.class);


                    // String amount = dataSnapshot.toString();
                    String item = userID;
                    Log.d(TAG, "Current user-----------" + userID + ": " + mCurrentUser.getUid() + "--- " + theKey + ":" + specificKey);
                    Log.d(TAG, "Current item-----------" + item + "--TaskTitle--" + taskTitle);
                    Log.d(TAG, "posted info-----------" + dataSnapshot.getChildrenCount() + title);


                    // fdatabaseTaskByUser = fdatabase.child(specificKey);
                    fdatabaseTaskByUser = fdatabaseTaskByUser.child("uPostedTasks").child(theKey);

                    //specificKey is the snapshot of the task posted by user
                    // tasksPostedByActiveUser(specificKey);


                }
                //When Itemlist is clicked


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getThListOfApplicants(final DataModel dm) {
      /*  Toast.makeText(getApplicationContext(), "Task details"+ dm.getTaskID() +
                        " : "+ dm.getAName()+" " +  dm.getDate() ,
                Toast.LENGTH_SHORT).show();*/

        //GET LIST OF APPLICANTS
        fdatabaseTaskByUsers.child(dm.getTaskID()).child("applied_Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot s : dataSnapshot.getChildren()) {

                    fdatabaseTaskByUsers.child(dm.getTaskID()).child("applied_Users").child("0").toString();

                    if (s.getValue().toString() != null) {
                        //   Log.d("the out put ", s.child("applied_Users").child("0").getValue().toString());


                        //RETURN THE CLICKED TASK KEY
                        //fdatabaseTaskByUsers.child(dm.getTaskID()).getKey()
                        returnedApplicantKey = s.getValue().toString();
                        UsersList ul = new UsersList(s.getValue().toString());
                        //COMPARE APPLICANT USERS FROM DATABASE ALL USERS
                        ArrayList<String> a = new ArrayList<String>();
                        a.add(ul.getUidApplicants());
                        findApplicantFromUsers(a);


                        // Log.d("The user finder", String.valueOf(usersRef.child("Users").child("FuaEvJKNu0THHgTsVKqEutER0NO2")));
                        usersRef = usersRef.child("Users");


                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (final DataSnapshot ss : dataSnapshot.getChildren()) {
                                    final String iterate = s.getValue().toString();

                                    if (ss.getKey().equals(s.getValue())) {
                                        usersRef.child("Users").child(iterate).child("userfName").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String applicantFullName = ss.child("userfName").getValue().toString() + ss.child("userlName").getValue().toString();

                                                holdNames = new UsersList();
                                                holdNames.setUidApplicants(applicantFullName);

                                                applicantsNamesTransport(applicantFullName);

                                                // Log.d("YES",ss.child("userfName").getValue().toString());
                                                //Log.d("YES",ss.child("userlName").getValue().toString());

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    // if(ss.getKey().equals(s.getValue())){Log.d("SHIIIT",ss.child("userName").getValue() + " : "+s.getValue());}

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        String test = fdatabaseTaskByUsers.child(dm.getTaskID()).toString();
                        // UsersList v = new UsersList("hi");
                        //ASSIGN TO ADAPTER DATA FOR PRINT
                        dataModelsToAccept.add(new DataModel(s.getValue().toString(), "uid_123", "", accept_application_confirm));
                        adapterToAccept = new CustomAdapterToAcceptAplicant(dataModelsToAccept, getApplicationContext());
                        printApplicantsList.setAdapter(adapterToAccept);

                        printApplicantsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        DisplayDialogBox(s.getValue().toString(), dm.getTaskID());

                                        String selected = ((TextView) view.findViewById(R.id.applicant_name_tobe_accepted)).getText().toString();

                                        v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.cardview_shadow_start_color));

                                        adapterToAccept.notifyDataSetChanged();

                                        //Toast.makeText(getApplicationContext(), "Accepted\n"+ v.getId(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                switch (view.getId()) {
                                    case R.id.accept_applications_confirm:
                                        Toast.makeText(getApplicationContext(), "Task detail", Toast.LENGTH_SHORT).show();

                                        //  Snackbar.make(v, "Release date " +dataModel.getAName(), Snackbar.LENGTH_LONG)
                                        //         .setAction("No action", null).show();

                                        break;
                                }


                            }
                        });
                        //Log.d("TEST METHOD",findApplicantFromUsers().);


                        UsersList ui = new UsersList(s.getValue().toString(), "applicant");
                        ui.getUid();
                        ui.getUidApplicants("--");

                        // String f = String.valueOf(ui.compareApplicants());

                        Log.d("NULL IS NOT RETURNED", s.getValue() + "boolean: " + fdatabaseTaskByUsers.child(dm.getTaskID()).child("applied_Users").child("0").toString());
                        taskList.add(s.getValue().toString());
                        itemsAdapter =
                                new ArrayAdapter<String>(UserActivities.this, android.R.layout.simple_list_item_1, taskList);

                        //ArrayList <String> x = new ArrayList<>(Arrays.asList(s.getValue().toString()));
                        //      printContentTempo.setAdapter(itemsAdapter);
                        // printContentTempo.setAdapter();

                    } else {
                    }
                } //SnapshoLoopENDS

            }//SnapShot that gets the list of applicants ends

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//Listner that gets list of applicants ends
    }//getListOfApplicant ENDS

    private void DisplayDialogBox(final String uid, final String taskId) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("HIRE")
                .setMessage("Would you like to hire this task hunter\n" + uid)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // ok
                        hireAPerson(uid, taskId);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // cancel
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //store in db when hires
    private void hireAPerson(String uid, String taskId) {
        fdatabase.child(taskId).child("task_taken").setValue(uid);
        //TODO
        //SEND NOTIFICATION TO THE ACCEPTED USER
    }

    private void applicantsNamesTransport(String applicantFullName) {
        Log.d("ASYNC FUCKED IHELP", applicantFullName);
    }

    UsersList allUsersList;

    //GET THE USERS THAT APPLIED FROM ALL USERS IN THE DATABASE
    public ArrayList<String> findApplicantFromUsers(ArrayList<String> a) {


        keys = new ArrayList<String>();

        if (a.equals(a)) {

            //    Log.d("ALL APLICANTS",a);
        }


        tempapplicantHolder = a;


        //Get the username of applicants
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot allUsers : dataSnapshot.getChildren()) {

                    //GET THE USER NAME USING THE ID
                    if (allUsers.child("userfName").getValue() != null) {

                        allApplicantUserNamesReturned = allUsers.child("userfName").getValue().toString();
                        // keys.add(allApplicantUserNamesReturned);
                        keys.add(allUsers.getKey());
                        allUsersList = new UsersList(keys);
                        allUsersList.getUsersList();
                        allUsersList.setUsersList(allUsersList.getUsersList());

                        allUsersList.compareApplicants(allUsersList.getUsersList(), allUsersList.getUsersList());

                        //String.valueOf(tempapplicantHolder)
                        Log.d("IT WORKS", "" + "");

                        if (tempapplicantHolder.equals(allUsers.getKey())) {
                            Log.d("ALL USERS key", allUsers.getKey());
                            Log.d("Keys list", keys.get(0) + "");

                        }

                        //  if(returnedApplicantKey.equals(allUsers.getKey())){
                        //         Log.d("ACTUAL USER", allApplicantUserNamesReturned + " equals " + returnedApplicantKey);
                        //      dataModelsToAccept.add(new DataModel(allApplicantUserNamesReturned,"uid_123","TEST",accept_application_confirm));

                        //         Log.d("checkOUTPUT FOR COMPARE",returnedApplicantKey + allApplicantUserNamesReturned + " : " + allUsers.getKey() );
                        //    }


                    } else {

                        //   Log.d("ACTUAL USER ELSE", allUsers.child("userfName").getValue().toString());

                    }
                }
                // compareAppliedUserFromAllUsers(returnedApplicantKey,allApplicantUserNamesReturned);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //snapShot that gets te user name of the applicants ends


        Log.d("A value", String.valueOf(a));

        return (keys);
    }


    private void handleDrawer() {

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItemID = menuItem.getItemId();


                if (menuItemID == R.id.profile) {
                    Intent intent = new Intent(UserActivities.this, UserProfile.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.create_task) {
                    Intent intent = new Intent(UserActivities.this, PostTaskActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.user_message) {
                    Intent intent = new Intent(UserActivities.this, MessagesActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.home_button) {
                    Intent intent = new Intent(UserActivities.this, MainActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.setting) {
                    Intent intent = new Intent(UserActivities.this, SettingActivity.class);
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
            Intent intent = new Intent(UserActivities.this, PostTaskActivity.class);
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


    //set values to the list in UI
    public void displayTasksFromDB(final DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //WHEN ACCEPT IS CLICKED
    public void acceptApplication() {

        Log.d("Accept Application", "YOU ARE ACCEPTED");
    }
}
