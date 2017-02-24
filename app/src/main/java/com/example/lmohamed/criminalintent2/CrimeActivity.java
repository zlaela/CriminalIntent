package com.example.lmohamed.criminalintent2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    /** Putting an extra:
     * tell CrimeFragment which Crime to display by passing
     * the crime ID as an extra on the Intent when CrimeActivity is started
     * **/
    private static final String EXTRA_CRIME_ID = "com.example.lmohamed.criminalintent2.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        /**
         * pass in the UUID from the extra to CrimeFragment.java
         */
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);     // get the crime ID from the extra
        return CrimeFragment.newInstance(crimeId);      // pass the crime ID into the fragment method
    }
}
