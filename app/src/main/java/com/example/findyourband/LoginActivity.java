package com.example.findyourband;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText loginemail,loginpassword;
    Button loginbutton;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth= FirebaseAuth.getInstance();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginemail=(EditText) findViewById(R.id.login_email);
        loginpassword=(EditText) findViewById(R.id.login_password);
        loginbutton=(Button) findViewById(R.id.login_button);
        mAuth= FirebaseAuth.getInstance();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
        }
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LoginEmail=loginemail.getText().toString();
                String LoginPassword=loginpassword.getText().toString();
                if(!checkentriesPassword(LoginPassword)||!checkentriesemail(LoginEmail)){
                    return;
                }
                else{
                    LoginUser(LoginEmail,LoginPassword);
                }
            }
        });
        Button button= (Button) findViewById(R.id.goto_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(myIntent);
            }
        });
    }
    public Boolean checkentriesPassword(String s1) {
        if (s1.isEmpty()) {
            loginpassword.setError("Field cannot be empty");
            return false;
        } else if (!checkwhitespaces(s1)) {
            loginpassword.setError("No whitespaces allowed");
            return false;
        } else {
            loginpassword.setError(null);
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
    public Boolean checkentriesemail(String s1) {
        if (s1.isEmpty()) {
            loginemail.setError("Field cannot be empty");
            return false;
        } else if (!checkwhitespaces(s1)) {
            loginemail.setError("No whitespaces allowed");
            return false;
        }  else {
            loginemail.setError(null);
            return true;
        }
    }
    private void LoginUser(String s1,String s2){
        mAuth.signInWithEmailAndPassword(s1,s2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check your username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}