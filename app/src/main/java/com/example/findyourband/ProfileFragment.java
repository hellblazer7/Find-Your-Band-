package com.example.findyourband;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    TextView fullname,username,instrument;
    FirebaseUser mUser;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        fullname=view.findViewById(R.id.fullnameinprofilefrag);
        username=view.findViewById(R.id.usernameinprofilefrag);
        instrument=view.findViewById(R.id.instrumentinprofilefrag);
        updateUI();
        return view;
    }

    private void updateUI() {
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass user=snapshot.getValue(HelperClass.class);
                fullname.setText("Full Name: " + user.getFullname());
                username.setText("Username: "+user.getUsername());
                instrument.setText("Instrument: "+user.getInstrument());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}