package com.example.cse327projet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity {
    TextView email, fullName, accountType;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.pFullName);
        email = findViewById(R.id.pEmail);
        accountType = findViewById(R.id.pAccountType);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullName.setText("Full Name: "+documentSnapshot.getString("fullName"));
                email.setText("Email: "+documentSnapshot.getString("email"));


                String x = documentSnapshot.getString("isUser");
                int y = Integer.parseInt(x);

                if(y == 10)
                {
                    accountType.setText("Account Type: Admin");
                }else if(y == 1 || y == 2)
                {
                    accountType.setText("Account Type: General User");
                }else if(y == 3)
                {
                    accountType.setText("Account Type: Employee");
                }

            }
        });

    }

    public void ToUpdateProfile(View view) {

        startActivity(new Intent(getApplicationContext(),UpdateProfile.class));

    }
}