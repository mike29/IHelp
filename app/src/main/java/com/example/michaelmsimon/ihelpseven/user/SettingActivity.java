package com.example.michaelmsimon.ihelpseven.user;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michaelmsimon.ihelpseven.ConnectionCheck;
import com.example.michaelmsimon.ihelpseven.MainActivity;
import com.example.michaelmsimon.ihelpseven.MessagesActivity;
import com.example.michaelmsimon.ihelpseven.PostTaskActivity;
import com.example.michaelmsimon.ihelpseven.R;
import com.example.michaelmsimon.ihelpseven.UserActivities;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Michael M. Simon on 14.10.2017.
 */

public class SettingActivity extends AppCompatActivity {


    private EditText fName;
    private EditText lName;
    private Spinner spinnerCategory;
    private EditText etDesc;
    private EditText etPaymentMethod;
    private EditText etCity;
    private double userLocLng = 32.1212324;
    private double userLocLat = 51.6304143;
    private Button btnDoneSetting;
    private ImageView uploadProfilePicture;

    private DrawerLayout mDrawerLayout;
    private String TAG;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private int menuItemID;

    private Uri mImageUri = null;
    private static final int GALLERY_REQ = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDBUsers;
    private StorageReference mStorageRef;


    private String fNameCollected;
    private String lNameCollected;
    private String descCollected;
    private String paymentCollected;
    private String locationCollected;

    private ConnectionCheck check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //CONNECTION
        check = new ConnectionCheck();
        if(!check.isConnectedToInternet(this.getApplicationContext())) {
            check.informBadInternet(this);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.setting_drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_setting_activity);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SETTING");
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

        etPaymentMethod = (EditText) findViewById(R.id.etPaymentMethod);
        fName = (EditText) findViewById(R.id.fName);
        lName = (EditText) findViewById(R.id.lName);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etCity = (EditText) findViewById(R.id.etlocation);
        //btnDoneSetting = (Button) findViewById(R.id.btnDoneSetting);
        uploadProfilePicture = (ImageView) findViewById(R.id.uploadProfilePicture);


        // mDBUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profileImage");
        mDBUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());


        Log.d("TAG", "createUserWithEmail:onComplete:" + mAuth.getCurrentUser());


        String CurrentUID = mAuth.getCurrentUser().getUid();
        // String email = mDBUsers.child(CurrentUID).child("email").getValue();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            mDBUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    fNameCollected = (String) dataSnapshot.child("userfName").getValue().toString();
                    lNameCollected = (String) dataSnapshot.child("userlName").getValue().toString();
                    descCollected = (String) dataSnapshot.child("userDesciption").getValue().toString();
                    locationCollected = (String) dataSnapshot.child("userCity").getValue().toString();
                    paymentCollected = (String) dataSnapshot.child("preferedPaymentMethod").getValue().toString();
                    String photoURL = (String) dataSnapshot.child("profileImage").getValue().toString();
                    Picasso.with(SettingActivity.this).load(photoURL).into(uploadProfilePicture);


                    //Toast.makeText(SettingActivity.this,  fNameCollected,Toast.LENGTH_SHORT).show();
                    fName.setText(fNameCollected);
                    etCity.setText(locationCollected);
                    fName.setText(fNameCollected);
                    lName.setText(lNameCollected);
                    etDesc.setText(fNameCollected);
                    etPaymentMethod.setText(paymentCollected);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    } //oncreate ends

    private void handleDrawer() {

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItemID = menuItem.getItemId();


                if (menuItemID == R.id.profile) {
                    Intent intent = new Intent(SettingActivity.this, UserProfile.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.create_task) {
                    Intent intent = new Intent(SettingActivity.this, PostTaskActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.user_message) {
                    Intent intent = new Intent(SettingActivity.this, MessagesActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.home_button) {
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intent);

                } else if (menuItemID == R.id.activities) {
                    Intent intent = new Intent(SettingActivity.this, UserActivities.class);
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
            Intent intent = new Intent(SettingActivity.this, PostTaskActivity.class);
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

    public void profileImageSet(View view) {
        Intent galleryIntent = new Intent();
// Show only images, no videos or anything else
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    // .setMaxCropResultSize(570,570)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                uploadProfilePicture.setImageURI(mImageUri);

                //startActivity(new Activity(,SettingActivity.class));
            }
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "ERROR: " + error, Toast.LENGTH_SHORT).show();
            }

        }
/*

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
             if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                uploadProfilePicture.setImageURI(resultUri);
             }
             else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
             }
        }
        */
    }

    //A USER IS DONE WITH SETTING UP
    public void profileSettingDone(View view) {
        final String userFName = fName.getText().toString().trim();
        final String userLName = lName.getText().toString().trim();
        final String preferedPaymentMethod = etPaymentMethod.getText().toString().trim();
        final String skillCategory = spinnerCategory.getSelectedItem().toString().trim();
        final String userDesciption = etDesc.getText().toString().trim();
        final String userCity = etCity.getText().toString().trim();


        //GET USERS
        final String userID = mAuth.getCurrentUser().getUid();
        if (!TextUtils.isEmpty(userFName) && !TextUtils.isEmpty(userLName) && !TextUtils.isEmpty(skillCategory) && mImageUri != null) {

            //Get the last path of the image uploaded(refer doc)
            final StorageReference imgPath = mStorageRef.child(mImageUri.getLastPathSegment());
            imgPath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //TODO
                    //THIS CODE THROUGH ERROR CHECK (taskSnapshot.getDownloadUrl().toString())
                    @SuppressWarnings("VisibleForTests") String downloadUrl = taskSnapshot.getDownloadUrl().toString();


                    mDBUsers.child(userID).child("userfName").setValue(userFName);
                    mDBUsers.child(userID).child("userlName").setValue(userLName);
                    mDBUsers.child(userID).child("preferedPaymentMethod").setValue(preferedPaymentMethod);
                    mDBUsers.child(userID).child("skillCategory").setValue(skillCategory);
                    mDBUsers.child(userID).child("userDesciption").setValue(userDesciption);
                    mDBUsers.child(userID).child("userLocLng").setValue(userLocLng);
                    mDBUsers.child(userID).child("userLocLat").setValue(userLocLng);
                    mDBUsers.child(userID).child("userCity").setValue(userCity);
                    mDBUsers.child(userID).child("profileImage").setValue(downloadUrl);

                    Toast.makeText(SettingActivity.this, "SUCESSFULL", Toast.LENGTH_SHORT).show();


                }

            });


        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
