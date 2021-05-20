package com.example.cse327projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mAdminLoginBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mEmail = findViewById(R.id.AdminEmail);
        mPassword = findViewById(R.id.AdminPassword);
        progressBar =findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mAdminLoginBtn = findViewById(R.id.adminloginbtn);

        mAdminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mEmail.setError("Password is Required");
                    return;
                }

                if(password.length()<8){
                    mPassword.setError("Password length must be 8 or more");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user

                fAuth.signInWithEmailAndPassword(mEmail.getText().toString(),mPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        String uid = authResult.getUser().getUid();

                        DocumentReference df = fStore.collection("users").document(uid);


                        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.d("TAG","onSuccess" + documentSnapshot.getData());

                                String x = documentSnapshot.getString("isUser");
                                int y = Integer.parseInt(x);

                                if(y == 1 || y== 2 || y == 3){
                                    Toast.makeText(AdminLogin.this, "Please login through User Login", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                    finish();
                                }else if(y == 10){
                                    Toast.makeText(AdminLogin.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),AdminDashboard.class));
                                    finish();
                                }

                            }

                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AdminLogin.this, "Wrong email or password, or please check your internet connection", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }
                });


            }
        });

    }

    public void ToUserLogin(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}