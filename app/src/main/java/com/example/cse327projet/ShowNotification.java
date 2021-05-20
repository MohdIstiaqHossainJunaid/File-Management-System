package com.example.cse327projet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class ShowNotification extends AppCompatActivity {

    ListView ShowNotification;

    ArrayList<String> notificationList = new ArrayList<>();

    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private DatabaseReference reference = db.getReference("requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);

        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(ShowNotification.this, android.R.layout.simple_list_item_1,notificationList);


        ShowNotification = (ListView) findViewById(R.id.ShowNotification);
        ShowNotification.setAdapter(ArrayAdapter);

        /*
        reference= FirebaseDatabase.getInstance().getReference("requests");
        */

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                String value= dataSnapshot.getValue(String.class);
                notificationList.add(value);
                ArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ShowNotification.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int item = position;

                String message = notificationList.get(item);

                String message2 = message.replaceAll("\\s","");
                String message3 = message2.replaceAll("[^A-Za-z0-9]","");

                new AlertDialog.Builder(ShowNotification.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("requests").child(message3).removeValue();
                                notificationList.remove(item);
                                ArrayAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });


    }
}