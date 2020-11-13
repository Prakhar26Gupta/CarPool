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

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextRePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
        editTextEmail =findViewById(R.id.editTextSEmail);
        editTextPassword =findViewById(R.id.editTextSPassword);
        editTextRePassword=findViewById(R.id.editTextSRePassword);
        findViewById(R.id.cardView2).setOnClickListener(this);
        findViewById(R.id.textViewBacktoLogin).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardView2:
                userRegister();
                break;
            case R.id.textViewBacktoLogin:
                finish();
                startActivity(new Intent(SignUp.this,loginActivity.class));
                break;
        }
    }

    private void userRegister() {
        String Email= editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();
        String RePassword = editTextRePassword.getText().toString();
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
        if (Password.length()<7){
            editTextPassword.setError("length should be greater than 6");
            editTextPassword.requestFocus();
            return;
        }
        if (!Password.equals(RePassword)){
            editTextRePassword.setError("Passwords doesn't match");
            editTextRePassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"You are succesfully registerd",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this,navigation_bottom.class));
                }
            }
        });
    }
}
