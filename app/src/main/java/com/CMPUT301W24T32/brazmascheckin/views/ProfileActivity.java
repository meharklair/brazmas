package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

/**
 * Profile activity
 */

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView userName;
    private Button EditProfileBtn;
    private StorageReference storageRef;
    private String deviceID;
    private UserController userController;
    private ImageController imageController;

    /**
     * This method initializes the Profile activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        configureViews();
        configureControllers();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == (R.id.bottom_announcement)) {
                    startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (id == (R.id.bottom_camera)) {
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (id == (R.id.bottom_profile)) {
                    return true;
                }
                if (id == (R.id.bottom_event)) {
                    startActivity(new Intent(getApplicationContext(), AttendeeOrganizerHome.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;

            }
        });
    }

    /**
     * configures the views for profile activity
     */
    public void configureViews() {
        profilePicture = findViewById(R.id.profile_picture);
        userName = findViewById(R.id.name_tv);
        EditProfileBtn = findViewById(R.id.edit_profile_btn);
        deviceID = DeviceID.getDeviceID(this);
        storageRef = FirestoreDB.getStorageReference("uploads");


    }

    /**
     * configure controllers for profile activity
     */
    public void configureControllers() {
        userController = new UserController(this);
        imageController = new ImageController(this);

        userController.getUser(deviceID, user ->{
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String fullName = firstName + " " + lastName;
            displayImage(user.getProfilePicture());
            userName.setText(fullName);
        },null);


        EditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view.
     * @param posterID the ID of the image in the database
     */
    private void displayImage(String posterID) {
        if (posterID != null) {
            imageController.getImage(ImageController.PROFILE_PICTURE, posterID, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePicture.setImageBitmap(bitmap);
            }, null);
        }
    }
}

