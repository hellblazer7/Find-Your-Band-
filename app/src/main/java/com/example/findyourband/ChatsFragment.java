package com.example.findyourband;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    List<Chatslist> userlist;
    List<HelperClass> mUsers;
    RecyclerView recyclerView;
    HelperClassAdapter mAdapter;
    FirebaseUser firebaseUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chats, container, false);
        userlist=new ArrayList<>();
        recyclerView=view.findViewById(R.id.chat_recyclerview_chatfrag);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("ChatsList").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlist.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Chatslist chatslist=ds.getValue(Chatslist.class);
                    userlist.add(chatslist);

                }
                DisplayTheChatUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void DisplayTheChatUI() {
        mUsers= new ArrayList<>();
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    HelperClass users=ds.getValue(HelperClass.class);
                    for(Chatslist chatslist:userlist){
                        if(users.getid().equals(chatslist.getId())){
                            mUsers.add(users);
                        }
                    }
                }
                mAdapter=new HelperClassAdapter(getContext(),mUsers);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}