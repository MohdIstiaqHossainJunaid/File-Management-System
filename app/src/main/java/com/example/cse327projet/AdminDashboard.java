package com.example.cse327projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity {

    long backPressedTime;
    Toast backToast;

    ImageButton mToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mToUpload = findViewById(R.id.ToUpload);

        mToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),UploadFiles.class));

            }
        });

    }

    @Override
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
        startActivity(new Intent(getApplicationContext(),AdminLogin.class));
        finish();

    }

    public void ToProfile(View view) {
        startActivity(new Intent(getApplicationContext(),Profile.class));
    }


    public void ToAdminPanel(View view) {
        startActivity(new Intent(getApplicationContext(),AdminPanel.class));
    }

    public void ToScanner(View view){
        startActivity(new Intent(getApplicationContext(),OCR.class));
    }

    public void ToPublicFolder(View view) {
        startActivity(new Intent(getApplicationContext(),PublicFolder.class));
    }

    public void ToPrivateFolder(View view) {
        startActivity(new Intent(getApplicationContext(),PrivateFolder.class));
    }

    public void ToShowNotification(View view) {
        startActivity(new Intent(getApplicationContext(),ShowNotification.class));
    }
}