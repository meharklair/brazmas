package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewEventTests {
    @Rule
    public ActivityScenarioRule<UserHome> scenario = new ActivityScenarioRule<>(UserHome.class);
    private UserController userController;
    private EventController eventController;
    private boolean userCreated = false;

    private User user = new User(DeviceID.getDeviceID(
            ApplicationProvider.getApplicationContext()
    ), "test", "device", new ArrayList<>(), false, new ArrayList<>());

    @Before
    public void setUp() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        if(!userCreated) {
            userController.setUser(user, null ,null);
            userCreated = true;
        }
    }

    @After
    public void cleanUp() {
        userController.getUser(user.getID(), object -> user = object, null);

        ArrayList<String> signedUpEvents = user.getSignedUpEvents();
        ArrayList<String> organizedEvents = user.getOrganizedEvents();

        for(String event: signedUpEvents) {
            eventController.deleteEvent(event, null, null);
        }
        for(String event: organizedEvents) {
            eventController.deleteEvent(event, null, null);
        }
        userController.deleteUser(user, null, null);
        userCreated = false;
    }

    @Test
    public void testSignUpBelowLimit() {
        Event mockAttendEvent = new Event(
                null, "Test Attend Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn)).perform(
                click());

        try {
            Thread.sleep(3000);
        } catch (Exception ignored) {

        }
        onView(withText("Test Attend Event")).perform(click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(click());

        pressBack();

        onView(withId(R.id.user_home_attending_btn)).perform(click());
        onView(withText("Test Attend Event"))
                .check(matches(ViewMatchers.isDisplayed()));

    }

    @Test
    public void testAttendeeList() {
        Event mockAttendEvent = new Event(
                null, "Test Attend Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn)).perform(
                click());

        try {
            Thread.sleep(3000);
        } catch (Exception ignored) {

        }
        onView(withText("Test Attend Event")).perform(click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(click());

        pressBack();

        try {
            Thread.sleep(3000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn));
        onView(withText("Test Attend Event")).perform(click());
        onView(withId(R.id.view_event_see_signed_up_attendees_btn))
                        .perform(click());
        onView(withText(user.getID())).check(matches(ViewMatchers.isDisplayed()));


        pressBack();
        pressBack();
        try {

            Thread.sleep(5000);
        } catch (Exception ignored){

        }
    }

    @Test
    public void testSignUpFull() {
        Event mockAttendEvent = new Event(
                null, "Test Attend Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                0,
                "default_poster.png",
                null,
                null,
                user.getID(),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn)).perform(
                click());

        try {
            Thread.sleep(3000);
        } catch (Exception ignored) {

        }
        onView(withText("Test Attend Event")).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.view_event_signed_up_cb)).check(matches(isNotEnabled()));
    }

    @Test
    public void testCheckIn() {
        Event mockAttendEvent = new Event(
                null, "Test Check In Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        mockAttendEvent.checkIn(user.getID(), null);
        user.checkIn(mockAttendEvent.getID());

        eventController.setEvent(mockAttendEvent, null, null);
        userController.setUser(user, null, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withText("Test Check In Event")).perform(click());
        onView(withId(R.id.view_event_sv)).perform(swipeUp());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.view_event_see_checked_in_attendees_btn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withText(user.getID())).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNoCheckIns() {
        Event mockAttendEvent = new Event(
                null, "Test Check In Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withText("Test Check In Event")).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.view_event_social_tv)).check(matches(withText("0")));
    }

    @Test
    public void testCheckInMap() {
        Event mockAttendEvent = new Event(
                null, "Test Map Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                true,
                new Location( 53.5269,-113.5267),
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        mockAttendEvent.checkIn(user.getID(), new Location(51.0447, -114.0719));
        user.checkIn(mockAttendEvent.getID());

        eventController.setEvent(mockAttendEvent, null, null);
        userController.setUser(user, null, null);
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withText("Test Map Event")).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.view_event_map_btn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.map)).check(matches(isDisplayed()))
                .check((view, noViewFoundException) -> {
                    if(view instanceof MapView) {
                        MapView mapView = (MapView) view;
                        int markerCount = countMarkers(mapView);
                        assert(markerCount == 2);
                    }
                });
    }

    private int countMarkers(MapView mapView) {
        int markerCount = 0;
        if (mapView != null) {
            List<Marker> mapMarkers = mapView.getOverlays().stream()
                    .filter(overlay -> overlay instanceof Marker)
                    .map(overlay -> (Marker) overlay)
                    .collect(Collectors.toList());

            markerCount = mapMarkers.size();
        }
        return markerCount;
    }

    @Test
    public void testNoCheckInsMap() {
        Event mockAttendEvent = new Event(
                null, "Test Map Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                true,
                new Location( 53.5269,-113.5267),
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withText("Test Map Event")).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.view_event_map_btn)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.map)).check(matches(isDisplayed()))
                .check((view, noViewFoundException) -> {
                    if(view instanceof MapView) {
                        MapView mapView = (MapView) view;
                        int markerCount = countMarkers(mapView);
                        assert(markerCount == 1);
                    }
                });
    }


    @Test
    public void attendeeCannotSeeQR() {
        Event mockAttendEvent = new Event(
                null, "Test Attendee QR Code Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                true,
                new Location( 53.5269,-113.5267),
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        mockAttendEvent.checkIn(user.getID(), new Location(51.0447, -114.0719));
        user.signUpEvent(mockAttendEvent.getID());

        eventController.setEvent(mockAttendEvent, null, null);
        userController.setUser(user, null, null);
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_attending_btn)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withText("Test Attendee QR Code Event"))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.view_event_QR_iv))
                .check(matches(
                        withEffectiveVisibility(ViewMatchers.Visibility.GONE)
                ));
    }
}
