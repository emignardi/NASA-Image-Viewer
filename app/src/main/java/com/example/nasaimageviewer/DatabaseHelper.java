package com.example.nasaimageviewer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * This class/activity is used as a helper for reading and writing to a SQLiteDatabase.
 * @author Eric Mignardi
 * @version 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * This is the name of the database used to store NASA image records
     */
    protected final static String DB_NAME = "NASA_IMAGES";
    /**
     * The database version
     */
    protected final static int DB_VERSION = 1;
    /**
     * The name of the table
     */
    public final static String TABLE_NAME = "NASA_IMAGES";
    /**
     * The first column name
     */
    public final static String COL_NAME_1 = "ID";
    /**
     * The second column name
     */
    public final static String COL_NAME_2 = "DATE";
    /**
     * The third column name
     */
    public final static String COL_NAME_3 = "HDURL";
    /**
     * The fourth column name
     */
    public final static String COL_NAME_4 = "URL";

    /**
     * The DatabaseHelper constructor
     * @param context The activity where the database is being opened
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * This method is called if the database file doesn't already exist.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_NAME_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME_2 + " TEXT, " + COL_NAME_3 + " TEXT, " + COL_NAME_4 + " TEXT);");
    }

    /**
     * This method is called if the database version in the constructor is higher than the version on the device.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * This method is called if the database version in the constructor is lower than the version on the device.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * This method is called last if {@link #onCreate} and {@link #onUpgrade}/{@link #onDowngrade} get called.
     * This method will be called regardless
     * @param db The database.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}