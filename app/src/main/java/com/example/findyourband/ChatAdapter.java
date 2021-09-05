package com.example.findyourband;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    Context context;
    List<ChatHelper> chatList;
    public static final int Message_Right = 0;//sender
    public static final int Message_Left = 1;//recipient

    public ChatAdapter(Context context, List<ChatHelper> chatList) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Message_Right) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_sent, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_recieved, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatHelper chats=chatList.get(position);
        holder.message.setText(chats.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.showmessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (chatList.get(position).getSender().equals(user.getUid())) {
            return Message_Right;
        } else {
            return Message_Left;
        }
    }
}
