package com.rahtech.corona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.TreeMap;

import static com.rahtech.corona.MainActivity.shared;

public class building extends AppCompatActivity {

    Button submit;
    EditText name;
    EditText mobile;
    EditText address;
    Spinner spinner;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        submit=findViewById(R.id.submit);
        name=findViewById(R.id.nameText);
        mobile=findViewById(R.id.mobile);
        address=findViewById(R.id.addresss);
        spinner=findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    type="Function Hall";
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("") || mobile.getText().toString().equals("") || address.getText().toString().equals("") ) {
                    Toast.makeText(building.this, "Fill all details...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String,String> details=new TreeMap<>();
                    details.put("Name",name.getText().toString());
                    details.put("Type",type);
                    details.put("Phone",mobile.getText().toString());
                    details.put("Address",address.getText().toString());

                    MainActivity.shared.edit().putInt("count",++MainActivity.count);
                    try{
                        FirebaseDatabase.getInstance().getReference().child(MainActivity.state).
                                child(MainActivity.district).child(MainActivity.city).child(MainActivity.sub_city).child("Buildings")
                                .child(MainActivity.emailId).child(String.valueOf(MainActivity.count)).setValue(details);
                    }
                    catch (Exception e){
                        Log.i("TAG", e.getMessage());
                    }
                    Toast.makeText(building.this, "Thank You For Your Help", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }
}
