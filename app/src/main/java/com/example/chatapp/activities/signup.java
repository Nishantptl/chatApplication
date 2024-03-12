package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.pManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {

    TextView alreadyUser;
    Button btn_signup;
    EditText username, email, password, con_password;
    ProgressBar progressBar;
    String emailPattern =  "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    private pManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username_signup);
        email = findViewById(R.id.email_signup);
        password = findViewById(R.id.password_signup);
        con_password = findViewById(R.id.con_password_signup);
        btn_signup = findViewById(R.id.btn_singup);
        progressBar = findViewById(R.id.progressbar_signup);

        manager = new pManager(getApplicationContext());

        alreadyUser = findViewById(R.id.alreadyUser_singup);
        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, signin.class);
                startActivity(intent);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidSignUpDetails()){
                    signUp();
                }
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, username.getText().toString());
        user.put(Constants.KEY_EMAIL, email.getText().toString());
        user.put(Constants.KEY_PASSWORD, password.getText().toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        loading(false);
                        manager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        manager.putString(Constants.KEY_USER_ID, documentReference.getId());
                        manager.putString(Constants.KEY_NAME, username.getText().toString());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading(false);
                        showToast(e.getMessage());
                    }
                });
    }

    private Boolean isValidSignUpDetails(){

        String USERNAME = username.getText().toString().trim();
        String EMAIL = email.getText().toString().trim();
        String PASSWORD = password.getText().toString().trim();
        String CON_PASSWORD = con_password.getText().toString().trim();

        if(USERNAME.isEmpty()){
            showToast("Enter Username");
            return false;
        } else if (EMAIL.isEmpty()) {
            showToast("Enter Email");
            return false;
        }else if (!Pattern.matches(emailPattern, EMAIL)) {
            showToast("Enter Valid Email");
            return false;
        } else if(PASSWORD.isEmpty()){
            showToast("Enter Password");
            return false;
        } else if (CON_PASSWORD.isEmpty()) {
            showToast("Enter Confirm Password");
            return false;
        } else if (!PASSWORD.equals(CON_PASSWORD)) {
            showToast("Password & Confirm Password must be same");
            return false;
        }else{
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            btn_signup.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            btn_signup.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}