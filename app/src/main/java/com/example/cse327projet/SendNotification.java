package com.example.cse327projet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SendNotification extends AppCompatActivity {

    EditText eReq;
    Button reqbtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        eReq = findViewById(R.id.eReq);
        reqbtn = findViewById(R.id.reqbtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        reqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentReference.addSnapshotListener(SendNotification.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        String name = documentSnapshot.getString("fullName");
                        String email = documentSnapshot.getString("email");

                        String req = eReq.getText().toString();

                        String message = "Name: " + name + "\n" + "Email: " + email + "\n" + "Request: " + req;

                        String message2 = message.replaceAll("\\s","");
                        String message3 = message2.replaceAll("[^A-Za-z0-9]","");

                        reference.child(message3).setValue(message);

                        Toast.makeText(SendNotification.this,"Query sent to the admins successfully",Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }
}