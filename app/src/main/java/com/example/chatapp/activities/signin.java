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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

public class signin extends AppCompatActivity {

    TextView newUser;
    Button btn_signin;
    EditText email, password;
    ProgressBar progressBar;
    String emailPattern =  "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
    private pManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email = findViewById(R.id.email_signin);
        password = findViewById(R.id.password_signin);
        progressBar = findViewById(R.id.progressbar_signin);
        manager = new pManager(getApplicationContext());

        if(manager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        newUser = findViewById(R.id.newUser_singin);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signin.this, signup.class);
                startActivity(intent);
                finish();
            }
        });

        btn_signin = findViewById(R.id.btn_singin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSignInDetails()){
                    signIn();
                }
            }
        });
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            btn_signin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            btn_signin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, email.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, password.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null
                        && task.getResult().getDocuments().size() > 0){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            manager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            manager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                            manager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            loading(false);
                            showToast("Unable to sign in");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails(){

        String EMAIL = email.getText().toString();
        String PASSWORD = password.getText().toString();

        if(EMAIL.isEmpty()){
            showToast("Enter Email");
            return false;
        }else if(!Pattern.matches(emailPattern, EMAIL)){
            showToast("Enter Valid Email");
            return false;
        }else if(PASSWORD.isEmpty()){
            showToast("Enter Password");
            return false;
        }else{
            return true;
        }
    }
}