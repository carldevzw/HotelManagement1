package com.example.hotelmanagement1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogIn extends AppCompatActivity {

    TextInputLayout txtUsername, txtPassword;
    Button btnLogin;
    TextView tvSignUp;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admim_login);

        txtUsername= findViewById(R.id.txtUsername);
        txtPassword= findViewById(R.id.txtPassword);

        btnLogin= findViewById(R.id.btn_Login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username= txtUsername.getEditText().getText().toString().trim();
                String Password= txtPassword.getEditText().getText().toString().trim();
                loginUser(Username, Password);
            }
        });

        tvSignUp= findViewById(R.id.tv_signup);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogIn.this, UsersHome.class));
            }
        });
    }
    private void loginUser(String username, String password) {

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(AdminLogIn.this, AdminDashboard.class));
                            txtPassword.getEditText().setText(null);
                            txtUsername.getEditText().setText(null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Incorrect Password or Username!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}