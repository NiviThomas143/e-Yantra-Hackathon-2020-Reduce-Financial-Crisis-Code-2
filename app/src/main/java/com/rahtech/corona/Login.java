package com.rahtech.corona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText email;
    EditText pswd;
    TextView forgotpass;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        forgotpass=findViewById(R.id.forgot);
        email=findViewById(R.id.email);
        pswd=findViewById(R.id.password);
        login=findViewById(R.id.button);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,forgot.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),pswd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                        }
                        else
                        {
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),pswd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(email.getText().toString());
                                       splashscreen.sharedPreferences.edit().putBoolean("loggedIn",true).commit();
                                       splashscreen.sharedPreferences.edit().putInt("count",0).commit();
                                       splashscreen.sharedPreferences.edit().putString("email",email.getText().toString()).commit();
                                       splashscreen.sharedPreferences.edit().putString("password",pswd.getText().toString()).commit();
                                        startActivity(new Intent(Login.this,MainActivity.class));

                                    }
                                    else
                                    {
                                        Toast.makeText(Login.this,"Login Failed. Try Again.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }
}
