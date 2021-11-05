package com.example.findyourband;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    Spinner mSpinner;
    String searchinstrument;
    FirebaseAuth mAuth;
    Button submitbutton;
    RecyclerView recyclerView;
    List<HelperClass> usersList;
    HelperClassAdapter mHelperClassAdapter;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSpinner = findViewById(R.id.searchinstrumentspinner);
        mAuth = FirebaseAuth.getInstance();
        submitbutton = (Button) findViewById(R.id.search_button);
        SetUpSpinner();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewofsearchusers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayrequestedUsers();
            }
        });
    }

    private void SetUpSpinner() {
        ArrayAdapter InstrumentSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_instrument_options, android.R.layout.simple_spinner_item);
        InstrumentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(InstrumentSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Piano")) {
                        searchinstrument = "Piano";
                    } else if (selection.equals("Guitar")) {
                        searchinstrument = "Guitar";
                    } else if (selection.equals("Drums")) {
                        searchinstrument = "Drums";
                    } else if (selection.equals("Violin")) {
                        searchinstrument = "Violin";
                    } else if (selection.equals("Keyboard")) {
                        searchinstrument = "Keyboard";
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchinstrument = "Piano"; // Unknown
            }
        });
    }

    private void displayrequestedUsers() {
        usersList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HelperClass users = ds.getValue(HelperClass.class);
                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                    if ((users.getInstrument().equals(searchinstrument))&&(!users.getid().equals(mUser.getUid()))) {
                        usersList.add(users);
                    }
                    mHelperClassAdapter = new HelperClassAdapter(SearchActivity.this, usersList);
                    recyclerView.setAdapter(mHelperClassAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}