package com.example.cse327projet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Dashboard extends AppCompatActivity {

    ImageButton mToUpload, mToPrivateFolder;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    long backPressedTime;
    Toast backToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mToUpload = findViewById(R.id.ToUpload);
        mToPrivateFolder =findViewById(R.id.ToPrivateFolder);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        mToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),UploadFiles.class));

            }

        });

        mToPrivateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                documentReference.addSnapshotListener(Dashboard.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        String x = documentSnapshot.getString("isUser");
                        int y = Integer.parseInt(x);

                        if(y==1){
                            Toast.makeText(Dashboard.this, "Please request admin for permission by sending a query", Toast.LENGTH_SHORT).show();
                        }else if(y==2||y==3){
                            startActivity(new Intent(getApplicationContext(),PrivateFolder.class));
                        }

                    }
                });

            }
        });



    }

    public void onBackPressed(){


        if(backPressedTime + 1000 > System.currentTimeMillis()){
            backToast.cancel();
        }else{
            backToast = Toast.makeText(getBaseContext(),"Press Logout", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }


    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();

    }


    public void ToProfile(View view) {
        startActivity(new Intent(getApplicationContext(),Profile.class));
    }


    public void ToScanner(View view) {
        startActivity(new Intent(getApplicationContext(),OCR.class));
    }


    public void ToPublicFolder(View view) {
        startActivity(new Intent(getApplicationContext(),PublicFolder.class));
    }

    public void ToSendNotification(View view) {
        startActivity(new Intent(getApplicationContext(),SendNotification.class));
    }
}