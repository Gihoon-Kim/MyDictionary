package com.hoonydictionary.mydictionary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // WORD table to save words (without meaning)
        String createWORDTable = "CREATE TABLE IF NOT EXISTS WORDS ("
                + "_id integer PRIMARY KEY AUTOINCREMENT,"
                + "word text);";

        // MEAN table to save meaning and part of speech. To get a matched word, word column added.
        String createMEANTable = "CREATE TABLE IF NOT EXISTS MEANS ("
                + "_id integer PRIMARY KEY AUTOINCREMENT,"
                + "pos text,"
                + "mean text,"
                + "word text);";
        sqLiteDatabase.execSQL(createMEANTable);
        sqLiteDatabase.execSQL(createWORDTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS WORDS";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
