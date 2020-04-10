package com.rahtech.corona;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class information extends AppCompatActivity {

    Uri uri;
    Spinner spinner;
    String type="";
    ImageView imageView;
    Button button;
    EditText editText;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.CorornaAware);
            String description = getString(R.string.Aware);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CoronaAware", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        spinner=findViewById(R.id.spinner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")){
                    if(!(MainActivity.full_addr.equals("")))
                    {
                        Map<String,String> details=new HashMap<>();
                        details.put("Type",type);
                        details.put("Landmark",editText.getText().toString());
                        MainActivity.shared.edit().putInt("count",++MainActivity.count);
                        try {
                            FirebaseDatabase.getInstance().getReference().child(MainActivity.state).
                                    child(MainActivity.district).child(MainActivity.city).child(MainActivity.sub_city).
                                    child("Information").child(MainActivity.emailId).child(String.valueOf(MainActivity.count)).setValue(details);
                        }
                        catch (Exception e){
                            Log.i("TAG", e.getMessage());
                        }

                        Toast.makeText(information.this, "Thanks For The Help..", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else
                    {
                        Toast.makeText(information.this, "Plzz try again..", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(information.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type="Beggars/Helpless people";
            }
        });

    }
}
