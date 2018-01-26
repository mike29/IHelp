package com.example.michaelmsimon.ihelpseven;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelmsimon.ihelpseven.user.SettingActivity;
import com.example.michaelmsimon.ihelpseven.user.UserProfile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostTaskActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    EditText taskTitle;
    Spinner taskCategory;
    EditText taskDesc;
    Button btnPostTask;
    TextView printer;
    String taskLat = "59.124970";
    String taskLng = "11.364515";
    ImageButton postImg;
    private String taskPostedDateTimeString;

    private TextView tvTaskLocationToVerify;
    CheckBox cbTaskLocationVerify;

    Button btnToProfile;
    Button PostedTasks;

    private static final int GALLERY_REQUEST = 2;
    private Uri mUri = null;
    ImageButton iB;

    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser mCurrentUser;
    private StorageReference mStorageRef;

    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private int menuItemID;

    private Uri downloadurl;

    String taskTitleVFinal;
    String taskCategoryVFinal;
    String taskDescVFinal;

    private GoogleApiClient googleApiClient;
    private TextView printLocation;
    private String TAG;

    Location lastLocation;
    double lat;
    double lon;

    String reUserCity;
    String reUserCountry;

    private int PLACE_PICKER_REQUEST = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_task);
        getSupportActionBar().setTitle("NEW TASK");

        //CONNECTION
        ConnectionCheck check = new ConnectionCheck();
        if(!check.isConnectedToInternet(this.getApplicationContext())) {
            check.informBadInternet(this);
        }

        tvTaskLocationToVerify = (TextView) findViewById(R.id.tvTaskLocationToVerify);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.post_task_activity_drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_post_task_activity);
        //HIDE HOME
        Menu m = navigationView.getMenu();
        m.findItem(R.id.create_task).setVisible(false);


        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close){
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Navigation item selected
         menuItemControll();

        //Handle location
        runTime_permission();
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();


        postImg = (ImageButton) findViewById(R.id.addImage);
        taskDesc = (EditText) findViewById(R.id.etDisc);
        taskCategory = (Spinner) findViewById(R.id.spinnerCategory);
        //printer = (TextView) findViewById(R.id.tvPrinter);
        taskTitle = (EditText) findViewById(R.id.etTaskTitle);
        cbTaskLocationVerify = (CheckBox) findViewById(R.id.cbTaskLocationVerify);
        btnPostTask = (Button) findViewById(R.id.btnPostTask);

        //FIREBASE
        storageReference = FirebaseStorage.getInstance().getReference();
        mRef = database.getInstance().getReference().child("uPostedTasks");


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mStorageRef= FirebaseStorage.getInstance().getReference().child("taskPostedImage");


        //google mapfind place and set here
        //final TextView tvTaskLocationToVerify = (TextView) findViewById(R.id.tvTaskLocationToVerify);
        //tvTaskLocationToVerify.setText("oslo, Karl Johan");

        cbTaskLocationVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!cbTaskLocationVerify.isChecked()){

                    alerDialogBoxHandler();

                }
                else {


                 /*   tvTaskLocationToVerify.setText("Select location manually");
                    tvTaskLocationToVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //THE METHOD THAT THAKS USERS TO GOOGLE MAP TO SET LOCATION MANUALLY
                            setLocationManually();

                        }
                    });
                    */
                }
            }
        });

        btnPostTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPostedTask();
            }
        });



    }

    private void menuItemControll() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItemID= menuItem.getItemId();


                if (menuItemID == R.id.home_button){
                    Intent intent = new Intent(PostTaskActivity.this, MainActivity.class);
                    startActivity(intent);

                }

                if (menuItemID == R.id.profile){
                    Intent intent = new Intent(PostTaskActivity.this, UserProfile.class);
                    startActivity(intent);

                }

                else if (menuItemID == R.id.user_message){
                    Intent intent = new Intent(PostTaskActivity.this, MessagesActivity.class);
                    startActivity(intent);

                }
                else if (menuItemID == R.id.activities){
                    Intent intent = new Intent(PostTaskActivity.this, UserActivities.class);
                    startActivity(intent);

                }
                else if (menuItemID == R.id.setting){
                    Intent intent = new Intent(PostTaskActivity.this, SettingActivity.class);
                    startActivity(intent);

                }
                else if (menuItemID == R.id.logout){
                    mAuth.signOut();

                }

                return true;
            }
        });
    }

    //Location handle get permission
        private boolean runTime_permission(){

        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
          //  Log.d(TAG, "lOCATION-----------");
            return true;
        }
        //Log.d(TAG, "lOCATION------NO PERMISSION-----");
        return false;


    }

    //onCreateEnds
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id  = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


        @Override
    protected void onStart() {
        super.onStart();
        taskPostedDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            if (googleApiClient != null) {
                googleApiClient.connect();
            }

    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //GALLERY
    public void imageButtonClick(View view){
        Intent galleryIntent = new Intent();
// Show only images, no videos or anything else
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setMaxCropResultSize(500, 500)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mUri = result.getUri();
                postImg.setImageURI(mUri);
            }
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "ERROR: " + error, Toast.LENGTH_SHORT).show();
            }
            if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    public void submitPostedTask(){
        taskTitleVFinal = taskTitle.getText().toString().trim();
        taskCategoryVFinal = taskCategory.getSelectedItem().toString().trim();
        taskDescVFinal  = taskDesc.getText().toString().trim();

        if (taskTitle.getText().toString().length() == 0) {
            taskTitle.setError("Task Title required!");
        } else if (taskDesc.getText().toString().length() == 0) {
            taskDesc.setError("Task Discription is required!");
        }

        else
        {

            if(mUri != null){
                final StorageReference imgPath = mStorageRef.child(mUri.getLastPathSegment());

                imgPath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //TODO
                        //taskSnapshot.getDownloadUrl().toString(); //shows error
            final String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            sendFinalTask("taskIDNr",taskTitleVFinal,taskCategoryVFinal,taskDescVFinal,downloadUrl );

                }
            });
          }
            else{
                sendFinalTask("taskIDNr",taskTitleVFinal,taskCategoryVFinal,taskDescVFinal,"null" );
            }

        }
        //    });
    }

    private void sendFinalTask (final String taskID, final String taskTitleV, final String taskCategoryV, final String taskDescV, final String downloadUrl ) {
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    DatabaseReference newTaskPost = mRef.push();
                    newTaskPost.child("user_id").setValue(mCurrentUser.getUid());
                    newTaskPost.child("task_id").setValue(taskID);
                    newTaskPost.child("task_title").setValue(taskTitleV);
                    newTaskPost.child("task_category").setValue(taskCategoryV);
                    newTaskPost.child("task_description").setValue(taskDescV);
                    newTaskPost.child("task_posted_date").setValue(taskPostedDateTimeString);
                    newTaskPost.child("city").setValue("Halden");
                    newTaskPost.child("country").setValue("Norway");
                    //   newTaskPost.child("location").child("taskLng").setValue(taskLng);
                    //   newTaskPost.child("location").child("taskLat").setValue(taskLat);
                    newTaskPost.child("task_lat").setValue(taskLat);
                    newTaskPost.child("task_lng").setValue(taskLng);
                    newTaskPost.child("task_taken").setValue("false");
                    newTaskPost.child("image").setValue(downloadUrl);
                    newTaskPost.child("task_posted_by_lName").setValue(dataSnapshot.child("userlName").getValue());
                    newTaskPost.child("task_posted_by_fName").setValue(dataSnapshot.child("userfName").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "Your Task is Posted Successfully", Toast.LENGTH_SHORT).show();
                                Intent toHomePageIntent = new Intent(PostTaskActivity.this, MainActivity.class);
                                startActivity(toHomePageIntent);

                            }
                        }
                    });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Ops, Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }


    //TAKE USER TO FRAGMENT WITH GOOGLE MAP SO THE USER CAN MANUALLY SELECT LOCATION
    public void setLocationManually() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        //TODO
        //Display google map here so the user can select a location
       // Toast.makeText(getApplicationContext(), "google Map Appears", Toast.LENGTH_SHORT).show();


            /*String action = "com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS";
                Intent settings = new Intent(action);
                startActivity(settings);
                */
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if( lastLocation!= null) {
                lat = lastLocation.getLatitude();
                lon = lastLocation.getLongitude();
                //lastLocation.distanceTo(lastLocation);
                Log.d(TAG, "lOCATION------FINAL RESULT with diiiistance-----" + lastLocation.distanceTo(lastLocation));
                tvTaskLocationToVerify.setText("\n"+"Latitude "  + lat + "\n" + " Longtide" + lon);
                //tvTaskLocationToVerify.append("\n"+"Latitude "  + lat + "\n" + " Longtide" + lon);

                getAddress(lat,lon);
            }
            else {

                tvTaskLocationToVerify.setText("Can't access location");

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Context mContext = this;
        Toast.makeText(mContext, "suspended", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Context mContext = this;
        Toast.makeText(mContext, "failed", Toast.LENGTH_LONG).show();

    }

    //takes care of the location selection by user
    public void alerDialogBoxHandler(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Select Location")
                .setMessage("Would like to select location using the map?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // ok
                        try {
                            setLocationManually();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }


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

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(PostTaskActivity.this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

                while (addresses.size()==0) {
                    addresses = geocoder.getFromLocation(lat, lng, 1);
                }

            if(addresses.size() > 0) {
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);

                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();//østfold
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();//halden
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();

                Log.v("IGA", "Address" + add);

                         // Log.d(TAG, "obj.getCountryName()" + obj.getCountryName());

                reUserCity = obj.getSubAdminArea();
                reUserCountry = obj.getCountryName();
                if(reUserCity == null){
                    reUserCity = "Unknown city";
                }

                //print the city and country
                tvTaskLocationToVerify.setText(reUserCity + " , " + reUserCountry);


            }

            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
