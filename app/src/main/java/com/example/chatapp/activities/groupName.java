package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.adapters.GroupMemberAdapter;
import com.example.chatapp.models.Group;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.pManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class groupName extends AppCompatActivity {

    EditText add_groupName;
    Button btnSave;
    pManager manager;
    RecyclerView recyclerView;
    ImageView i_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);

        add_groupName = findViewById(R.id.edt_add_name);
        btnSave = findViewById(R.id.btn_save);
        recyclerView = findViewById(R.id.groupMemberRecyclerView);
        i_back = findViewById(R.id.i_back);

        manager = new pManager(getApplicationContext());
        getUsers();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroupDetails();
            }
        });

        i_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveGroupDetails(){

        String groupName = add_groupName.getText().toString();
        if(groupName.isEmpty()){
            showToast("Enter group name");
        }else{
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            HashMap<String, Object> group = new HashMap<>();
            group.put(Constants.KEY_GROUP_NAME, groupName);
            database.collection(Constants.KEY_COLLECTION_GROUP)
                    .add(group)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            manager.putString(Constants.KEY_GROUP_ID, documentReference.getId());
                            manager.putString(Constants.KEY_GROUP_NAME, groupName);
                            Intent intent = new Intent(getApplicationContext(), GroupChatActivity.class);
                            intent.putExtra("GroupName", groupName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Unable to create group");
                        }
                    });
        }
    }


    private void getUsers(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                GroupMemberAdapter groupMemberAdapter = new GroupMemberAdapter(users,getApplicationContext());
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(groupMemberAdapter);
                                recyclerView.setVisibility(View.VISIBLE);
                            }else{
                                showToast("Unable to fetch users");
                            }
                        }else{
                            showToast("Unable to fetch users");
                        }
                    }
                });
    }
}