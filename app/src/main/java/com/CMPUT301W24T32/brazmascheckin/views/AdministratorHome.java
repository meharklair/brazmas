package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.helper.EventRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Author: OpenAI
 * Tool: ChatGPT
 * Prompt: "Please add the Javadocs."
 * Date: March 5, 2024
 */

/**
 * This class will be the home page for organizer.
 * This is where the admin will browse events.
 */
public class AdministratorHome extends AppCompatActivity {

    private ArrayList<Event> eventDataList;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;

    private RecyclerView eventRecyclerView;

    private EventController eventController;

    /**
     * This method initializes the attendee/organizer home activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        configureViews();
        configureControllers();

        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_home_bnv);
        bottomNavigationView.setSelectedItemId(R.id.admin_event);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == (R.id.admin_event)){
                    return true;
                }
                if (id == (R.id.admin_profile)){
                    startActivity(new Intent(getApplicationContext(), AdministratorBrowseProfiles.class));
                    overridePendingTransition(0,0);
                }
                if (id == (R.id.admin_image)){
                    startActivity(new Intent(getApplicationContext(), AdministratorBrowseImages.class));
                    overridePendingTransition(0,0);
                }
                return false;
            }
        });

    }

    /**
     * This method initializes the views, adapters, and models required for the activity.
     */
    private void configureViews() {
        eventDataList = new ArrayList<>();

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventDataList);
        eventRecyclerView = findViewById(R.id.all_events_rv_admin);
        eventRecyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * This method defines the controllers for the views of the activity.
     */
    private void configureControllers() {
        eventController = new EventController(FirestoreDB.getDatabaseInstance());

        showAllEvents();

        // to access event details by clicking single event
        eventRecyclerViewAdapter.setOnItemClickListener(position -> {
            Event clickedEvent = eventDataList.get(position);
            ViewEventFragment fragment = ViewEventFragment.sendEvent(clickedEvent, 2);
            fragment.show(getSupportFragmentManager(), "Display Event");
        });
    }

    /**
     * Fetches and displays all events that have not passed yet.
     * Events that have passed are not displayed.
     */
    private void showAllEvents() {

        // getting current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DATE);

        eventController.addSnapshotListener(new SnapshotListener<Event>() {
            @Override
            public void snapshotListenerCallback(ArrayList<Event> events) {
                eventDataList.clear();
                for (Event event : events) {

                    // to prevent events that have passed to be displayed
                    if (year < event.getDate().getYear()) {
                        eventDataList.add(event);
                    } else if (year == event.getDate().getYear()) {
                        if (month < event.getDate().getMonth()) {
                            eventDataList.add(event);
                        } else if (month == event.getDate().getMonth()) {
                            if (day <= event.getDate().getDay()) {
                                eventDataList.add(event);
                            }
                        }
                    }
                }
                eventRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(AdministratorHome.this, "Unable to connect to the " + "database", Toast.LENGTH_LONG).show();
            }
        });
    }
    //TODO: implement a long click action for event clicked then delete or swipe?
}