package com.example.cse327projet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadFiles extends AppCompatActivity {

    Button selectFile, upload, privateUpload;
    TextView notification;
    Uri pdfUri; //URL of local storage
    String fileName, path;

    FirebaseStorage storage; // to upload files
    FirebaseDatabase database; // to store URLs of uploaded files
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);

        storage = FirebaseStorage.getInstance(); //return an object of FireBase Storage
        database = FirebaseDatabase.getInstance(); //return an object of FireBase Database


        selectFile = findViewById(R.id.selectFile);
        upload = findViewById(R.id.upload);
        privateUpload = findViewById(R.id.privateUpload);
        notification = findViewById(R.id.notification);


        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(UploadFiles.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectFile();
                }
                else{
                    ActivityCompat.requestPermissions(UploadFiles.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(pdfUri!=null)
                    uploadFile(pdfUri);
                else
                    Toast.makeText(UploadFiles.this,"Select a File",Toast.LENGTH_SHORT).show();

            }
        });

        privateUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(pdfUri!=null)
                    uploadPrivateFile(pdfUri);
                else
                    Toast.makeText(UploadFiles.this,"Select a File",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Upload file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        StorageReference storageReference = storage.getReference(); //returns root path
        String fileName2 = fileName.substring(0, fileName.lastIndexOf('.'));

        storageReference.child("public").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();
                        // store the url in realtime database




                        UploadFile uploadFile = new UploadFile(fileName2,url.toString());


                        DatabaseReference reference = database.getReference("public"); //return path to root


                        reference.child(reference.push().getKey()).setValue(uploadFile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                    Toast.makeText(UploadFiles.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(UploadFiles.this,"File upload was unsuccessful",Toast.LENGTH_SHORT).show();

                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UploadFiles.this,"File upload was unsuccessful",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                //track progress of upload
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });


    }

    private void uploadPrivateFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Upload file...");
        progressDialog.setProgress(0);
        progressDialog.show();


        StorageReference storageReference = storage.getReference(); //returns root path
        String fileName3 = fileName.substring(0, fileName.lastIndexOf('.'));

        storageReference.child("private").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();
                        // store the url in realtime database




                        UploadFile uploadFile = new UploadFile(fileName3,url.toString());

                        DatabaseReference reference = database.getReference("private"); //return path to root

                        reference.child(reference.push().getKey()).setValue(uploadFile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                    Toast.makeText(UploadFiles.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(UploadFiles.this,"File upload was unsuccessful",Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UploadFiles.this,"File upload was unsuccessful",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                //track progress of upload
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectFile();
        }else{
            Toast.makeText(UploadFiles.this,"Please provide permission to proceed", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectFile() {

        //to offer user to select a file using file manager

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); // to fetch files
        startActivityForResult(intent, 12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //check whether user has selected a file or not

        if(requestCode==12&& resultCode==RESULT_OK && data!=null)
        {
            pdfUri = data.getData(); //return uri of selected file
            path = pdfUri.getLastPathSegment();
            fileName=path.substring(path.lastIndexOf("/")+1);

            notification.setText("A file is selected : " + data.getData().getLastPathSegment());
        }else{
            Toast.makeText(UploadFiles.this,"Please select a file",Toast.LENGTH_SHORT).show();
        }



    }
}