package com.rahtech.corona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.prefs.PreferenceChangeEvent;

import javax.net.ssl.HttpsURLConnection;

import static android.util.Log.i;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    String userLocation;
    ListView listView;
    Button callbutton;
    ImageView imageView;
    String addr_line;
    static String district;
    String[] temp=new String[2];
    static String emailId="";
    static String pswrd="";
    String pincode;
    static String city;
    static String state;
    String country;
    String extra_info;
    static String sub_city;
    static String full_addr;
    static SharedPreferences shared;
    static int count=0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
            {
                call(callbutton);
            }

        }

        if(requestCode==2){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);

                }

         }
    }


    public void call(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+91-11-23978046" ));
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
        {
            startActivity(intent);
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.list);
        callbutton=findViewById(R.id.call);
        imageView=findViewById(R.id.imageView);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Register Your Buildings");
        list.add("Donate To Your State");
        list.add("Give information to govt..");
        list.add("Provide Medical Equipment");
        list.add(" Help by giving Food ");
        shared= getSharedPreferences("logindetails",MODE_PRIVATE);
        temp=shared.getString("email","rahtechdefault@email").split("@");
        emailId=temp[0];
        pswrd=shared.getString("password","");
        count=shared.getInt("count", 1234);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                userLocation=location.toString();
                Geocoder geocoder=new Geocoder(MainActivity.this,Locale.getDefault());
                try {
                    List<Address> address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        addr_line = address.get(0).getAddressLine(0);
                        sub_city=address.get(0).getSubLocality();
                        city=address.get(0).getLocality();
                        district=address.get(0).getSubAdminArea();
                        state=address.get(0).getAdminArea();
                        country=address.get(0).getCountryName();
                        pincode=address.get(0).getPostalCode();
                        extra_info=address.get(0).getFeatureName();
                    if(!addr_line.equals("")){
                        full_addr=addr_line;
                    }
                    else
                    {
                        full_addr=extra_info+","+sub_city+","+city+","+district+","+state+","+pincode+","+country;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}

        };


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                    if (!(state.equals("") || city.equals("") || sub_city.equals("") || district.equals(""))) {

                        Intent intent;
                        if (position == 0) {
                            intent = new Intent(MainActivity.this, building.class);

                        } else if (position == 1) {
                            intent = new Intent(MainActivity.this, donate.class);

                        } else if (position == 2) {
                            intent = new Intent(MainActivity.this, information.class);

                        } else if (position == 3) {
                            intent = new Intent(MainActivity.this, instruments.class);

                        } else {
                            intent = new Intent(MainActivity.this, food.class);

                        }
                        startActivity(intent);
                    }
                }
                catch(Exception e)
                {
                    i("TAG", e.getMessage());
                    Toast.makeText(MainActivity.this, "Setting Up Network Connection Plz try after a moment", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,3000,0,locationListener);
        }

        callbutton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                startActivity(new Intent(MainActivity.this,helpline.class));
                return true;
            }
        });

    }

}
