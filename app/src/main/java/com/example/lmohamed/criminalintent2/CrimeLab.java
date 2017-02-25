package com.example.lmohamed.criminalintent2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lmohamed.criminalintent2.database.CrimeBaseHelper;
import com.example.lmohamed.criminalintent2.database.CrimeCursorWrapper;
import com.example.lmohamed.criminalintent2.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lmohamed on 2/22/17.
 */

public class CrimeLab {

    /**
     * Private constructor for the singleton
     * Other classes will not be able to create a CrimeLab, bypassing the get() method
     * get() method takes a Context object
     **/
    private static CrimeLab sCrimeLab;  // s prefix for static variable

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get (Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        /** SQLiteOpenHelper is a class designed to get rid of the grunt work of opening a SQLiteDatabase **/
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();

    }

    /** Add a new Crime to the Database **/
    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    // TODO: delete crimes

    /**
     * public void removeCrime(Crime crime) {
     *     mCrimes.remove(crime);
     * }
     * @return
     */

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);    // get all crimes by passing null for hereClause and whereArgs

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {    // Returns the crime with the passed-in ID
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Columns.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Columns.UUID + " = ?",   // Where clause -- ? prevents SQL injection by making sure the string doesn't contain SQL code
                new String[] { uuidString });       // Values for the argument in the Where clause
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,       // columns - null selects all columns
                whereClause,
                whereArgs,
                null,   // groupBy
                null,   // having
                null    // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    /** ContentValues is a key-value store class
     * this method shuttles a Crime into a ContentValues **/
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();

        values.put(CrimeTable.Columns.UUID, crime.getId().toString());
        values.put(CrimeTable.Columns.TITLE, crime.getTitle());
        values.put(CrimeTable.Columns.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Columns.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Columns.SUSPECT, crime.getSuspect());

        return values;
    }


}
