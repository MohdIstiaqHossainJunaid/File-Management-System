package com.example.cse327projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView user_list;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user_list = findViewById(R.id.user_list);

        Query query = firebaseFirestore.collection("users");

        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query,Users.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Users, UsersViewHolder>(options) {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);

                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {

                holder.list_name.setText("Name: " + model.getFullName());
                holder.list_email.setText("Email: " + model.getEmail());

                String userID = model.getUserID();

                String x = model.getIsUser();

                int y = Integer.parseInt(x);

                if(y==1){
                    holder.list_userType.setText("Account Type: User without private folder access");
                }else if(y==2){
                    holder.list_userType.setText("Account Type: User with private folder access");
                }else if(y==3){
                    holder.list_userType.setText("Account Type: Employee");
                }else if(y==10){
                    holder.list_userType.setText("Account Type: Admin");
                }

                //experiment start

                fStore = FirebaseFirestore.getInstance();

                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();

                holder.denyAccess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.put("email",model.getEmail());
                        user.put("fullName",model.getFullName());
                        user.put("isUser","1");
                        user.put("userID",userID);
                        documentReference.set(user);
                    }
                });

                holder.giveAccess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.put("email",model.getEmail());
                        user.put("fullName",model.getFullName());
                        user.put("isUser","2");
                        user.put("userID",userID);
                        documentReference.set(user);
                    }
                });

                holder.makeEmployee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.put("email",model.getEmail());
                        user.put("fullName",model.getFullName());
                        user.put("isUser","3");
                        user.put("userID",userID);
                        documentReference.set(user);
                    }
                });

                holder.makeAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.put("email",model.getEmail());
                        user.put("fullName",model.getFullName());
                        user.put("isUser","10");
                        user.put("userID",userID);
                        documentReference.set(user);
                    }
                });

                //experiment end


            }
        };

        user_list.setHasFixedSize(true);
        user_list.setLayoutManager(new LinearLayoutManager(this));
        user_list.setAdapter(adapter);

    }

    private class UsersViewHolder extends RecyclerView.ViewHolder{

        private TextView list_name;
        private TextView list_email;
        private TextView list_userType;
        private Button denyAccess;
        private Button giveAccess;
        private Button makeEmployee;
        private Button makeAdmin;


        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_email = itemView.findViewById(R.id.list_email);
            list_userType = itemView.findViewById(R.id.list_userType);
            denyAccess = itemView.findViewById(R.id.denyAccess);
            giveAccess = itemView.findViewById(R.id.giveAccess);
            makeEmployee = itemView.findViewById(R.id.makeEmployee);
            makeAdmin = itemView.findViewById(R.id.makeAdmin);


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}