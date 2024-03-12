package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.utilities.*;
import com.example.chatapp.models.*;
import com.example.chatapp.adapters.*;
import com.example.chatapp.listeners.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserListener{

    ProgressBar progressBar;
    private pManager manager;
    TextView errorMsg;
    RecyclerView userRecyclerView;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        progressBar = findViewById(R.id.progressbar_userActivity);
        manager = new pManager(getApplicationContext());
        errorMsg = findViewById(R.id.Error_Msg);
        userRecyclerView = findViewById(R.id.user_recyclerView);
        img_back = findViewById(R.id.img_back);

        getUsers();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loading(false);
                        String currentUserId = manager.getString(Constants.KEY_USER_ID);
                        if(task.isSuccessful() && task.getResult() != null){
                            List<User> users = new ArrayList<>();
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if(currentUserId.equals(documentSnapshot.getId())){
                                    continue;
                                }
                                User user = new User();
                                user.name = documentSnapshot.getString(Constants.KEY_NAME);
                                user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                                user.id = documentSnapshot.getId();
                                users.add(user);
                            }
                            if(users.size() > 0){
                                UserAdapter userAdapter = new UserAdapter(users, user -> {}, getApplicationContext());
                                userRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                userRecyclerView.setAdapter(userAdapter);
                                userRecyclerView.setVisibility(View.VISIBLE);
                            }else{
                                showErrorMessage();
                            }
                        }else{
                            showErrorMessage();
                        }
                    }
                });
    }

    private void showErrorMessage(){
        errorMsg.setText(String.format("%s", "No User Available"));
        errorMsg.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(String user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}