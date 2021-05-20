package com.example.cse327projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrivateFolder extends AppCompatActivity {

    ListView myListView;
    DatabaseReference databaseReference;
    List<UploadFile> uploadFiles;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_folder);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Private Folder");

        myListView = (ListView)findViewById(R.id.myListView);
        uploadFiles = new ArrayList<>();

        viewAllFiles();

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UploadFile uploadFile = uploadFiles.get(position);

                Intent intent = new Intent();
                intent.setData(Uri.parse(uploadFile.getUrl()));
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem((R.id.action_search));

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search in Private Folder");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("private");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    UploadFile uploadFile= postSnapshot.getValue(com.example.cse327projet.UploadFile.class);
                    uploadFiles.add(uploadFile);
                }

                String[] uploads = new String[uploadFiles.size()];
                for(int i=0; i<uploads.length; i++){

                    uploads[i] = uploadFiles.get(i).getName();

                }

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads);
                myListView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}