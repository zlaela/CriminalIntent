package com.example.lmohamed.criminalintent2;

import android.content.Context;

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
    private List<Crime> mCrimes;        // Crime objects to store

    public static CrimeLab get (Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
    }

    /** Add a new Crime to the list of Crimes when user presses '+' in action bar **/
    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }

    public List<Crime> getCrimes() {
        return mCrimes;                 // Returns the List of crimes
    }

    public Crime getCrime(UUID id) {    // Returns the crime with the passed-in ID
        for (Crime crime : mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
