package com.example.lmohamed.criminalintent2.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.lmohamed.criminalintent2.Crime;
import com.example.lmohamed.criminalintent2.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lmohamed on 2/24/17.
 * Sublcass of Cursor that retrieves data from Cursor
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    /** method that pulls out relevant column data **/
    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeTable.Columns.UUID));
        String title = getString(getColumnIndex(CrimeTable.Columns.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Columns.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Columns.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Columns.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }
}
