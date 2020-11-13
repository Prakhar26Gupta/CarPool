package com.example.carpool02;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        findViewById(R.id.cardView).setOnClickListener(this);
        findViewById(R.id.textViewRegister).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),navigation_bottom.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardView:
                userLogin();
                break;
            case R.id.textViewRegister:
                finish();
                startActivity(new Intent(loginActivity.this,SignUp.class));
                break;

        }

    }

    private void userLogin() {
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        if (Email.isEmpty()){
            editTextEmail.setError("email can not be empty");
            editTextEmail.requestFocus();
            return;
        }
        if (Password.isEmpty()){
            editTextPassword.setError("password can not be empty");
            editTextPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("please enter a valid email address");
            editTextEmail.requestFocus();
            return;
        }
        if (Password.length()<5){
            editTextPassword.setError("length should be greater than 4");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"You are succesfully logged in",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(loginActivity.this,navigation_bottom.class));

                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
