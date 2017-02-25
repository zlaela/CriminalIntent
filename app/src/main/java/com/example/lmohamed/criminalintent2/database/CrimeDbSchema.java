package com.example.lmohamed.criminalintent2.database;

/**
 * Created by lmohamed on 2/24/17.
 * The schema for crimes DB
 */

public class CrimeDbSchema {
    // Inner class to describe the table CrimeTable
    public static final class CrimeTable {          // CrimeTAble only exists to define the String constants that describe the parts of the table
        public static final String NAME = "crimes";

        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
