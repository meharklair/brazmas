package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AddAnnouncementFragment extends DialogFragment {
    private EditText editTitle;
    private EditText editDesc;
    private DatePicker datePicker;

    interface AddAnnouncementDialogListener{
        void addAnnouncement(Announcement announcement);
    }

    private AddAnnouncementDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddAnnouncementDialogListener) {
            listener = (AddAnnouncementFragment.AddAnnouncementDialogListener) context;
        } else {
            throw new RuntimeException(context + "must implement AddAnnouncementDialogListener");
        }
    }

    public AddAnnouncementFragment newInstance(Announcement announcement) {
        Bundle args = new Bundle();
        args.putSerializable("announcement", announcement);
        AddAnnouncementFragment fragment = new AddAnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_announcement_fragment, null);
        configureViews(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Add an event")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Add", (dialog, which) -> {
                    retrieveInput();
                });

        return builder.create();
    }

    private void retrieveInput() {
        String title = editTitle.getText().toString();
        String desc = editDesc.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() ;
        int year = datePicker.getYear();
        Date date = new Date(day, month, year);

        if(title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(getContext(), "Enter all text fields", Toast.LENGTH_SHORT).show();
        } else {
            listener.addAnnouncement(new Announcement(title, desc, date, "hello"));
        }

    }

    private void configureViews(View view) {
        editDesc = view.findViewById(R.id.add_announcement_desc_et);
        editTitle = view.findViewById(R.id.add_announcement_name_tv);
        datePicker = view.findViewById(R.id.add_announcement_date_dp);


    }
}
