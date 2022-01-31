package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    AppCompatButton SignInBtn;
    TextView forgot_password;
    TextInputEditText username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SignInBtn = findViewById(R.id.SignIn_button);
        forgot_password = findViewById(R.id.forgot_password_txtbtn);


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenForgotPassword();
            }
        });

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMainActivity();
            }
        });

    }

    private void OpenForgotPassword() {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
        finish();
    }
    private Boolean validateUsername(){
        String val =username.getEditableText().toString();

            if(val.isEmpty()){
                username.requestFocus();
//                username.setErrorEnabled(true);
                username.setError("Can't be left empty");
                return false;

        }
            else{
                username.setError(null);
                return true;
            }
    }

    private Boolean validPassword(){
        String val= password.getEditableText().toString();
        if(val.isEmpty())
        {
            password.setError("Field can not be empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    public void loginUser(View view){
        if(!validateUsername() | !validPassword()){
            return;
        }
        isUser();
    }

    private void isUser() {
        String userEnteredUsername =username.getEditableText().toString().trim();
        String userEnteredPassword =username.getEditableText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("UniversityRollNumber:").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    username.setError(null);

                    String passwordFromDB =snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if(passwordFromDB.equals(userEnteredPassword)){
                        SignInBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OpenMainActivity();
                            }
                        });
                    }
                    else {
                        password.setError("Wrong passwprd");
                        password.requestFocus();
                    }
                }
                else{
                    username.setError("No such user exist");
                    username.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void OpenMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}