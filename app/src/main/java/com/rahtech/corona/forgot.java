package com.rahtech.corona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot extends AppCompatActivity {

    EditText email_edit;
    Button send_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        email_edit=findViewById(R.id.forgotemail);
        send_link=findViewById(R.id.send);

        send_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email_edit.getText().toString().equals("")){

                    try {

                        FirebaseAuth.getInstance().sendPasswordResetEmail(email_edit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(forgot.this, "Link Sent...", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(forgot.this, "Plzz try again..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }catch (Exception e)
                    {
                        Toast.makeText(forgot.this, "Check Your Email and try again", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(forgot.this, "Give your mail id", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
