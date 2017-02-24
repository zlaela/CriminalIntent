package com.example.lmohamed.criminalintent2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;
import java.util.UUID;

/**
 * Created by lmohamed on 2/23/17.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "com.example.lmohamed.criminalIntent2.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packagContext, UUID crimeId) {
        Intent intent = new Intent(packagContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);  // 0. set the activity's View

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager); // 1. find the ViewPager in the activity's View

        mCrimes = CrimeLab.get(this).getCrimes();       // 2. get the dataset from CrimeLab (list of crimes)

        FragmentManager fragmentManager = getSupportFragmentManager();  // 3. get an instance of FragmentManager

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {  // 4. set the adapter to be an unnamed instance of FragmentStatePagerAdapter

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        /** Set the inital Pager item
         * find the index of the crime to display by
         * lopping through and checking each crime's ID
         * When the Crime instance whose crimeId matches the crimeId
         * in the intent's extra, set the current item to the index of that Crime
         * **/
        for (int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
