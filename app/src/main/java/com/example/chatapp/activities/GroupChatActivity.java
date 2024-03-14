package com.example.chatapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.adapters.GroupMemberAdapter;
import com.example.chatapp.models.Member;
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

public class GroupChatActivity extends AppCompatActivity {

    pManager manager;
    RecyclerView recyclerView;
    String groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        getUsers();
        recyclerView = findViewById(R.id.groupMemberRecyclerView);
        manager = new pManager(getApplicationContext());
        groupName = getIntent().getExtras().getString("GroupName");

    }

    public void addMember(Context context, String memberName, String memberId) {

        Toast.makeText(context, "Name : "+memberName + ", memberId : "+memberId, Toast.LENGTH_SHORT).show();

        pManager pManager = new pManager(context);
        String mName = memberName;
        String mId = memberId;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> group = new HashMap<>();
        group.put(Constants.KEY_GROUP_MEMBER_NAME, mName);
        group.put(Constants.KEY_GROUP_MEMBER_ID, mId);
        group.put(Constants.KEY_GROUP_ID, groupName);
        database.collection(Constants.KEY_COLLECTION_GROUP)
                .add(group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        pManager.putString(Constants.KEY_GROUP_MEMBER_NAME, mName);
                        pManager.putString(Constants.KEY_GROUP_MEMBER_ID, mId);
                        pManager.putString(Constants.KEY_GROUP_ID, groupName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Unable to add");
                    }
                });
    }

    public void removeMember(String memberName, String memberId) {
        Member memberModel = new Member();
        memberModel.setMemberName(memberName);
        memberModel.setMemberId(memberId);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> group = new HashMap<>();
        group.put(Constants.KEY_GROUP_MEMBER_NAME, memberName);
        group.put(Constants.KEY_GROUP_MEMBER_ID, memberId);
        group.put(Constants.KEY_GROUP_ID, null);
        database.collection(Constants.KEY_GROUP_MEMBER_NAME)
                .add(group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        manager.putString(Constants.KEY_GROUP_MEMBER_NAME, memberName);
                        manager.putString(Constants.KEY_GROUP_MEMBER_ID, memberId);
                        manager.putString(Constants.KEY_GROUP_ID, groupName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Unable to add");
                    }
                });
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
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}