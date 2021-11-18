package com.example.locationfinderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    //creating the databse table with id, address, longitude and latitude
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Locationdata (id INTEGER PRIMARY KEY AUTOINCREMENT , address TEXT, longitude REAL, latitude REAL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Locationdata");

    }

    //Setting up and insertdata function that inserts the data into the database
    public boolean Insertdata(int id, String address, Double longitude, Double latitude) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("address", address);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        long result = DB.insert("Locationdata", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Setting up and Updating data function that updates the data into the database
    //Only updates the address and coordinates
    public boolean Updatedata(int id, String address, double longitude, double latitude) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        Cursor cursor = DB.rawQuery("select * from Locationdata where id =?", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = DB.update("Locationdata", contentValues, "id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //Setting up and deleteing function that deletes the data into the database based off id
    //only needs the id which is primary key to delete data form database
    public boolean Deletedata(int id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Locationdata where id =?", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {

            long result = DB.delete("Locationdata", "id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //cursor object that query throughs the database to pull data
    //used to show the contents of the database
    public Cursor getdata() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Locationdata", null);
        return cursor;
    }

    //cursor object that querys through the databse, compares to check if address exists in the database and
    //returns the data if it does exist
    public Cursor searchQ(String Saddress) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Locationdata where address like \"%" + Saddress + "%\"", null);
        return cursor;
    }


}
