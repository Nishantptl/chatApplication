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
                    obj.addMember(context, userList.get(position).name, userList.get(position).id);
                }else{
//                    obj.addMember(userList.get(position).name, userList.get(position).id);
                }
            }
        });
    }


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
