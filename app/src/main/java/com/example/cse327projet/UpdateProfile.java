package com.example.cse327projet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    Button UpdateName, UpdatePassword;
    TextView EditName, EditPassword;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser userCurrent;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        UpdateName = findViewById(R.id.UpdateName);
        UpdatePassword = findViewById(R.id.UpdatePassword);
        EditName = findViewById(R.id.EditName);
        EditPassword = findViewById(R.id.EditPassword);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        userCurrent = fAuth.getCurrentUser();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String,Object> user = new HashMap<>();

        UpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = EditName.getText().toString().trim();

                user.put("fullName",name);


                documentReference.update(user);

                Toast.makeText(UpdateProfile.this, "Username Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        UpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = EditPassword.getText().toString().trim();


                if(password.length()<8){

                    Toast.makeText(UpdateProfile.this, "Password length should be greater than 8", Toast.LENGTH_SHORT).show();

                }else{

                    userCurrent.updatePassword(password);

                    Toast.makeText(UpdateProfile.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();

                }




            }
        });





    }
}