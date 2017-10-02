package com.apps.gruz.korektorgraficzny.Core;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseReader {

    private final DatabaseHelper databaseHelper;
    private  SQLiteDatabase db;

    public DatabaseReader(Context context){
        this.databaseHelper = new DatabaseHelper(context);
    }


    public void save(Pdata data){
        db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseInterface.COLUMN_NAME_NAME,data.getName());
        values.put(DatabaseInterface.COLUMN_NAME_BAND_1,data.getBands()[0]);
        values.put(DatabaseInterface.COLUMN_NAME_BAND_2,data.getBands()[1]);
        values.put(DatabaseInterface.COLUMN_NAME_BAND_3,data.getBands()[2]);
        values.put(DatabaseInterface.COLUMN_NAME_BAND_4,data.getBands()[3]);
        values.put(DatabaseInterface.COLUMN_NAME_BAND_5,data.getBands()[4]);
        db.insert(DatabaseInterface.TABLE_NAME,null,values);
        db.close();
    }


    public List<Pdata> read(){
        db = databaseHelper.getReadableDatabase();

        String[] projection = {"*"};


        Cursor cursor = db.rawQuery("select * from "+DatabaseInterface.TABLE_NAME,null);

        List<Pdata> listData = new ArrayList<>();
        Pdata data;
        int band1,band2,band3,band4,band5;
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                data = new Pdata();

                data.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInterface.COLUMN_NAME_ID)));
                data.setName(cursor.getString(cursor.getColumnIndex(DatabaseInterface.COLUMN_NAME_NAME)));

                band1 = cursor.getInt(cursor.getColumnIndex(DatabaseInterface.COLUMN_NAME_BAND_1));
                band2 = cursor.getInt(cursor.getColumnIndex(DatabaseInterface.COLUMN_NAME_BAND_2));
                band3 = cursor.getInt(cursor.getColumnIndex(DatabaseInterface.COLUMN_NAME_BAND_3));
                band4 = cursor.getInt(cursor.getColumnIndex(DatabaseInterface.COLUMN_NAME_BAND_4));
                band5 = cursor.getInt(cursor.getColumnIndex(DatabaseInterface.COLUMN_NAME_BAND_5));

                data.setBands(band1, band2, band3, band4, band5);

                listData.add(data);
            }

            db.close();
        }
        return listData;
    }

    public void delete(int id){
        db = databaseHelper.getWritableDatabase();
        String selection = DatabaseInterface.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(DatabaseInterface.TABLE_NAME,selection,selectionArgs);
        db.close();
    }

}
