package com.example.lmohamed.criminalintent2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lmohamed.criminalintent2.database.CrimeDbSchema.CrimeTable;  // lets you refer to String constants by typing CrimeTAble.Cols.<name>

/**
 * Created by lmohamed on 2/24/17.
 * Creates the DB
 * Android provides some low-level methods on Context to open a DB file into an instance of SQLiteDatabase:
 * openOrCreateDatabase(...) and databaseList();
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    /** SQLiteOpenHelper is a class designed to get rid of the grunt work of opening a SQLiteDatabase
     * 1. Check to see if the DB exists
     * 2. if not, create it and its tables, and inital data it needs
     * 3. if exists, open DB and check the version of CrimeDbSchema
     * 4. update version if it is old
     */
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /** create the DB **/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDbSchema.CrimeTable.NAME +
                "(" + "_id integer primary key autoincrement, " +
                CrimeTable.Columns.UUID + "," +
                CrimeTable.Columns.TITLE + ", " +
                CrimeTable.Columns.DATE + ", " +
                CrimeTable.Columns.SOLVED + ", " +
                CrimeTable.Columns.SUSPECT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
