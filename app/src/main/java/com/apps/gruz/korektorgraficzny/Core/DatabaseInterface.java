package com.apps.gruz.korektorgraficzny.Core;

public interface DatabaseInterface {
    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "korektorGraficzny.db";
    String TABLE_NAME = "presety";

    String COLUMN_NAME_ID = "_id";
    String COLUMN_NAME_NAME ="nazwa";
    String COLUMN_NAME_BAND_1 = "band_1";
    String COLUMN_NAME_BAND_2 = "band_2";
    String COLUMN_NAME_BAND_3 = "band_3";
    String COLUMN_NAME_BAND_4 = "band_4";
    String COLUMN_NAME_BAND_5 = "band_5";
}
