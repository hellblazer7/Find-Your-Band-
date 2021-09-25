package com.example.findyourband;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    String recipientid,message,myid;
    TextView fullnameontoolbar;
    Toolbar toolbar;
    FirebaseUser mUser;
    EditText MessageeditText;
    Button send;
    List<ChatHelper> chatlist;
    ChatAdapter mChatAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fullnameontoolbar=findViewById(R.id.fullnameontoolbarchat);
        MessageeditText=findViewById(R.id.editmessagetext);
        recyclerView=findViewById(R.id.recyclerviewformessages);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        send=findViewById(R.id.sendmessage_button);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        myid=mUser.getUid();//Logged in person
        recipientid=getIntent().getStringExtra("recipientid");//recipient id from tapped item
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(recipientid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass user=snapshot.getValue(HelperClass.class);
                fullnameontoolbar.setText(user.getFullname());
                readMessages(myid,recipientid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MessageeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().length()>0){
                    send.setEnabled(true);
                }
                else{
                    send.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=MessageeditText.getText().toString();
                if(!text.startsWith(" ")){
                    MessageeditText.getText().insert(0," ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message=MessageeditText.getText().toString();
                sendmessagetorecipient(myid,recipientid,message);
                MessageeditText.setText(" ");
            }
        });

    }

    private void readMessages(String myid, String recipientid) {
        chatlist=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlist.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ChatHelper chats=ds.getValue(ChatHelper.class);
                    if((chats.getSender().equals(myid) && chats.getReciever().equals(recipientid))||(chats.getSender().equals(recipientid) && chats.getReciever().equals(myid))){
                        chatlist.add(chats);
                    }
                    mChatAdapter=new ChatAdapter(ChatActivity.this,chatlist);
                    recyclerView.setAdapter(mChatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendmessagetorecipient(String s1,String s2,String s3) {
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("sender",myid);
            hashMap.put("reciever",recipientid);
            hashMap.put("message",message);
            reference.child("Chats").push().setValue(hashMap);
            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("ChatsList").child(recipientid).child(myid);
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        reference1.child("id").setValue(myid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

}