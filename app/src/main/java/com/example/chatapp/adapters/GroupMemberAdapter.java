package com.example.chatapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.activities.*;
import com.example.chatapp.models.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder>{

    private final List<User> userList;
    private final Context context;

    public GroupMemberAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupMemberAdapter.GroupMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_group_member, parent,false);
        return  new GroupMemberAdapter.GroupMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberViewHolder holder, int position) {

        holder.memberName.setText(userList.get(position).name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GroupChatActivity obj = new GroupChatActivity();
                if(isChecked){
                    obj.addMember(userList.get(position).name, userList.get(position).id);
                }else{
                    obj.addMember(userList.get(position).name, userList.get(position).id);
                }
            }
        });
    }

//    private void addMember(String memberName, String memberId) {
//        pManager manager = new pManager(context);
//        Member memberModel = new Member();
//        memberModel.setMemberName(memberName);
//        memberModel.setMemberId(memberId);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String, Object> group = new HashMap<>();
//        group.put(Constants.KEY_GROUP_MEMBER_NAME, memberName);
//        group.put(Constants.KEY_GROUP_MEMBER_ID, memberId);
//        group.put(Constants.KEY_GROUP_ID, group);
//        database.collection(Constants.KEY_GROUP_MEMBER_NAME)
//                .add(group)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        manager.putString(Constants.KEY_GROUP_MEMBER_NAME, memberName);
//                        manager.putString(Constants.KEY_GROUP_MEMBER_ID, memberId);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, "Unable to add", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    class GroupMemberViewHolder extends RecyclerView.ViewHolder{

        TextView memberName;
        CheckBox checkBox;

        public GroupMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName= itemView.findViewById(R.id.card_groupMemberName);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
