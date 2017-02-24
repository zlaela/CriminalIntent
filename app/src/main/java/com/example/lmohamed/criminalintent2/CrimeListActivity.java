package com.example.lmohamed.criminalintent2;

import android.support.v4.app.Fragment;

/**
 * Created by lmohamed on 2/22/17.
 * A controller class
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
