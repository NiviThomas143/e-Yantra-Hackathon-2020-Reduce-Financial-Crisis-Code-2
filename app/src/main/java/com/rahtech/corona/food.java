package com.rahtech.corona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.TreeMap;

public class food extends AppCompatActivity {

    EditText  name;
    EditText landmark;
    EditText mobile;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        name=findViewById(R.id.name);
        landmark=findViewById(R.id.landmark);
        button=findViewById(R.id.submit);
        mobile=findViewById(R.id.mobile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("") ||mobile.getText().toString().equals("") ||  landmark.getText().toString().equals("")) {
                    Toast.makeText(food.this, "Fill all details...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String,String> details=new TreeMap<>();
                    details.put("Name",name.getText().toString());
                    details.put("Phone",mobile.getText().toString());
                    details.put("Landmark",landmark.getText().toString());
                    details.put("Address",MainActivity.full_addr);
                    MainActivity.shared.edit().putInt("count",++MainActivity.count);
                    try {
                        FirebaseDatabase.getInstance().getReference().child(MainActivity.state).
                                child(MainActivity.district).child(MainActivity.city).child(MainActivity.sub_city).child("Food").
                                child(MainActivity.emailId).child(String.valueOf(MainActivity.count)).setValue(details);
                    }
                    catch (Exception e){
                        Log.i("TAG", e.getMessage());
                    }

                    Toast.makeText(food.this, "Thank You For Your Help", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
}
