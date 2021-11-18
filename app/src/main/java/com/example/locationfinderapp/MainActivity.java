package com.example.locationfinderapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//ddeclaring and finding all the buttons textviews and edittexts
        EditText searchaddress = findViewById(R.id.searchaddress);
        String Saddress = searchaddress.getText().toString();
        EditText id = findViewById(R.id.id);
        EditText address_update = findViewById(R.id.address);

        TextView Display = findViewById(R.id.Display);

        Button Insert = findViewById(R.id.Insert);
        Button Update = findViewById(R.id.Update);
        Button Delete = findViewById(R.id.Delete);
        Button Search = findViewById(R.id.Search);
        Button test = findViewById(R.id.test);

        DB = new Database(this);

        // Handling the adding/inserting location data into the database
        Insert.setOnClickListener(new View.OnClickListener() {

            @Override
            //onClick function for insert button
            public void onClick(View view) {

                //Getting variables to inputed into the edittext
                Integer ID = Integer.valueOf(id.getText().toString());
                String ADDRESS = address_update.getText().toString();

                //getting the longitude and latitude using geolocation method
                Double LONGITUDE = geocoderlocation(ADDRESS).getLongitude();
                Double LATITUDE = geocoderlocation(ADDRESS).getLatitude();

                //boolean that checks the database using the Insertdata method and inserts data, also outputs a toast method
                Boolean Insertdatacheck = DB.Insertdata(ID, ADDRESS, LONGITUDE, LATITUDE);
                if (Insertdatacheck == true)
                    Toast.makeText(MainActivity.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Insert Could not be Complete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handling the updating of location data into the database
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //gets id and address variables for edittexts
                int ID = Integer.parseInt(id.getText().toString());
                String ADDRESS = address_update.getText().toString();
                //using geolocation method to update the logitude and latitude
                Double LONGITUDE = geocoderlocation(ADDRESS).getLongitude();
                Double LATITUDE = geocoderlocation(ADDRESS).getLatitude();
                //boolean that checks the database using the updataData method and updates the data, also outputs a toast method
                Boolean Updatedatacheck = DB.Updatedata(ID, ADDRESS, LONGITUDE, LATITUDE);
                if (Updatedatacheck == true)
                    Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Update Could not be Complete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handling the deleting of data from the database
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = id.getText().toString();

                //boolean that checks the database using the Deletedata method and deletes data, also outputs a toast method
                Boolean Deletedatacheck = DB.Deletedata(Integer.parseInt(ID));
                if (Deletedatacheck == true)
                    Toast.makeText(MainActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Delete Could not be Complete", Toast.LENGTH_SHORT).show();
            }
        });

        // Handling the the search function for searching addresses through the database
        Search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //getting input variable for search edittext
                String Saddress = searchaddress.getText().toString();
                //using a cursor object with SearchQ method to check the database and output data if it matches
                Cursor c = DB.searchQ(Saddress);
                //outputting does not exist if data is not in the database
                if (c.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Does not Exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                //if it is in the database
                if (c.getCount() > 0) {
                    //creating a buffer to store data from database
                    StringBuffer buff = new StringBuffer();

                    while (c.moveToNext()) {
                        //append the data from the database into the buffer
                        buff.append("address: " + c.getString(1) + "\n");
                        buff.append("longitude: " + c.getString(2) + "\n");
                        buff.append("latitude: " + c.getString(3) + "\n\n");
                    }
                    //displaying the data onto the screen
                    Display.setText(buff);
                }
            }
        });


        // Handling the the search function for searching addresses through the database
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //using a cursor object with getdata method to check the database and output all the data
                Cursor c = DB.getdata();
                if (c.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Does not Exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                //creating a buffer to store data from database
                StringBuffer buffer = new StringBuffer();
                //looping through and checking the entire database
                while (c.moveToNext()) {
                    //append the data from the database into the buffer
                    buffer.append("id: " + c.getString(0) + "\n");
                    buffer.append("address: " + c.getString(1) + "\n");
                    buffer.append("longitude: " + c.getString(2) + "\n");
                    buffer.append("latitude: " + c.getString(3) + "\n\n");
                }
                //displaying the data on a alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Location Information");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });

    }

    //using geocoding to get the address, longitude and latitude
    private Address geocoderlocation(String Laddress) {

        //utilizing the geocoder for this main activity
        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> arr = new ArrayList<>(); //creating an array list for to store the geocoder information
        try {
            //getting information about a address entered and limited to only 1 address
            //storing data into array
            arr = geocoder.getFromLocationName(Laddress, 1);
        } catch (IOException e) {
            System.out.println("error occured");
        }
        //if data is in array
        if (arr.size() > 0) {
            //getting address data
            Address radd = arr.get(0);
            return radd;
        }
        return null;
    }

}