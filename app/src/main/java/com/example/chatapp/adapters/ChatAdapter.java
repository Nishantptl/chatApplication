package com.example.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.chatapp.R;
import com.example.chatapp.models.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMessage> chatMessages;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card_sent_message, parent, false);
            return new SendMessageViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card_receive_message, parent, false);
            return new ReceiverMessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SendMessageViewHolder) holder).send_msg.setText(chatMessages.get(position).message);
            ((SendMessageViewHolder) holder).send_dateTime.setText(chatMessages.get(position).dateTime);
        }else {
            ((ReceiverMessageViewHolder) holder).receive_msg.setText(chatMessages.get(position).message);
            ((ReceiverMessageViewHolder) holder).receive_dateTime.setText(chatMessages.get(position).dateTime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SendMessageViewHolder extends RecyclerView.ViewHolder{

        TextView send_msg, send_dateTime;

        public SendMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            send_msg = itemView.findViewById(R.id.card_sentMsg);
            send_dateTime = itemView.findViewById(R.id.card_sentDateTime);
        }
    }

    static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder{

        TextView receive_msg, receive_dateTime;

        public ReceiverMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            receive_msg = itemView.findViewById(R.id.card_receiveMsg);
            receive_dateTime = itemView.findViewById(R.id.card_receiveDateTime);
        }
    }

}
