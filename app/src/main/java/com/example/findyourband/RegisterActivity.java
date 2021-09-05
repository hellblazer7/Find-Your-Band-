package com.example.findyourband;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText fullname;
    EditText email;
    EditText username;
    String instrument;
    EditText password;
    Button register;
    Button gotologin;
    Spinner mInstrumentSpinner;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        mInstrumentSpinner = findViewById(R.id.instrument_spinner);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        gotologin = findViewById(R.id.goto_login);
        mAuth=FirebaseAuth.getInstance();
        SetUpSpinner();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting Values
                String Name = fullname.getText().toString();
                String Email = email.getText().toString();
                String Username = username.getText().toString();
                String Password = password.getText().toString();

                if (checkentriesEmail(Email) || checkentriesPassword(Password) || checkentriesFullname(Name)||checkentriesUsername(Username)) {
                    //Toast.makeText(getApplicationContext(), "Successfully Registered. Please Login", Toast.LENGTH_SHORT).show();
                    //Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    //RegisterActivity.this.startActivity(myIntent);
                    registerUser(Name,Username,Email,instrument,Password);
                }
            }
        });
        Button button = (Button) findViewById(R.id.goto_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(myIntent);
            }
        });
    }

    public Boolean checkentriesEmail(String s1) {
        if (s1.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!checkwhitespaces(s1)) {
            email.setError("No whitespaces allowed");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public Boolean checkentriesPassword(String s1) {
        if (s1.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!checkwhitespaces(s1)) {
            password.setError("No whitespaces allowed");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public Boolean checkwhitespaces(String s1) {
        for (int i = 0; i < s1.length(); i++) {
            char check = s1.charAt(i);
            String s2 = Character.toString(check);
            if (s2.matches(" ")) {
                return false;
            }
        }
        return true;
    }

    public Boolean checkentriesFullname(String s1) {
        if (s1.isEmpty()) {
            fullname.setError("Field cannot be empty");
            return false;
        } else {
            fullname.setError(null);
            return true;
        }
    }

    public Boolean checkentriesUsername(String s1) {
        if (s1.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (!checkwhitespaces(s1)) {
            username.setError("No whitespaces allowed");
            return false;
        }  else {
            username.setError(null);
            return true;
        }
    }


    private void SetUpSpinner() {
        ArrayAdapter InstrumentSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_instrument_options, android.R.layout.simple_spinner_item);
        InstrumentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mInstrumentSpinner.setAdapter(InstrumentSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mInstrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Piano")) {
                        instrument = "Piano";
                    } else if (selection.equals("Guitar")) {
                        instrument = "Guitar";
                    } else if (selection.equals("Drums")) {
                        instrument = "Drums";
                    } else if (selection.equals("Violin")) {
                        instrument = "Violin";
                    } else if (selection.equals("Keyboard")) {
                        instrument = "Keyboard";
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                instrument = "Piano"; // Unknown
            }
        });
    }
    private void registerUser(String n,String u,final String e,String i,final String p){
        mAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                FirebaseUser user=mAuth.getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                if(user!=null){
                    HelperClass temp=new HelperClass(u,n,user.getUid(),e,i,p);

                    reference.setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                RegisterActivity.this.startActivity(myIntent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Unfortunately,we have encountered a technical error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Account with the given email already exists", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}