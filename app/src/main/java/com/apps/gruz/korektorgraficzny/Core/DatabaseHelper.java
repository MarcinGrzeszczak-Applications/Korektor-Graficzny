package com.apps.gruz.korektorgraficzny.Core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE = "CREATE TABLE "+DatabaseInterface.TABLE_NAME +
            " ("+ DatabaseInterface.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            DatabaseInterface.COLUMN_NAME_NAME +" TEXT,"+
            DatabaseInterface.COLUMN_NAME_BAND_1 +" INTEGER,"+
            DatabaseInterface.COLUMN_NAME_BAND_2 +" INTEGER,"+
            DatabaseInterface.COLUMN_NAME_BAND_3 +" INTEGER,"+
            DatabaseInterface.COLUMN_NAME_BAND_4 +" INTEGER,"+
            DatabaseInterface.COLUMN_NAME_BAND_5 +" INTEGER)";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + DatabaseInterface.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DatabaseInterface.DATABASE_NAME, null, DatabaseInterface.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE);
        onCreate(sqLiteDatabase);
    }
}
