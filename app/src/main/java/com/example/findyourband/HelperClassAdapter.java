package com.example.findyourband;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HelperClassAdapter extends RecyclerView.Adapter<HelperClassAdapter.MyHolder> {
    Context context;
    List<HelperClass> userlist;
    String recepientid;

    public HelperClassAdapter(Context context,List<HelperClass> userlist) {
        this.context=context;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layoutforusersmallrep,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        HelperClass user=userlist.get(position);
        recepientid = user.getid();
        holder.Name.setText(user.getFullname());
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView Name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.fullnameinuserfragment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        HelperClass user =userlist.get(getAdapterPosition());
        recepientid= user.getid();
        Intent intent=new Intent(context,ChatActivity.class);
        intent.putExtra("recipientid",recepientid);
        context.startActivity(intent);
        }
    }
}
