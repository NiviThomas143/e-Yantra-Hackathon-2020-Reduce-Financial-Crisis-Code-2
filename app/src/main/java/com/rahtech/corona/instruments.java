package com.rahtech.corona;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Tree;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class instruments extends AppCompatActivity {
    String type="";
    FirebaseAuth auth;
    EditText institute_name_edit;
    String institute_name="";
    String pincode="";
    String phno="";
    String address="";
    String equip="";
    EditText phno_edit;
    EditText pincode_edit;
    EditText address_edit;
    Spinner type_spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruments);
        institute_name_edit=findViewById(R.id.nameText);
        pincode_edit=findViewById(R.id.pincode);
        phno_edit=findViewById(R.id.phno);
        address_edit=findViewById(R.id.address);
        type_spin=findViewById(R.id.spinner);

        type_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type="masks";
            }
        });

    }

    public void submit(View view)
    {
        if(phno_edit.getText().toString().equals("") || pincode_edit.getText().toString().equals("") || institute_name_edit.getText().toString().equals("")  || type.equals("") || address_edit.getText().toString().equals("")) {
            Toast.makeText(this, "Fill all details...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Map<String,String> details=new TreeMap<>();
            details.put("Name",institute_name_edit.getText().toString());
            details.put("Type",type);
            details.put("Phone",phno_edit.getText().toString());
            details.put("Pincode",pincode_edit.getText().toString());
            details.put("Address",address_edit.getText().toString().replace(",","."));
            MainActivity.shared.edit().putInt("count",++MainActivity.count);
            try {
                FirebaseDatabase.getInstance().getReference().child(MainActivity.state).
                        child(MainActivity.district).child(MainActivity.city).child(MainActivity.sub_city).child("Equipment")
                        .child(MainActivity.emailId).child(String.valueOf(MainActivity.count)).setValue(details);
            }
            catch (Exception e){
                Log.i("TAG", e.getMessage());
            }

            Toast.makeText(this, "Thanks For the Help", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
