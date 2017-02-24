package com.example.lmohamed.criminalintent2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lmohamed on 2/21/17.
 */

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;


    /** Attaching arguments to a fragment
     * Every fragment instance can have a Bundle object attached to it
     * Bundle contains key-value pairs that work just like intent Extras
     * each pair is an 'argument'
     * **/
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);    // put the crime ID in the bundle

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);        // put the crime ID (argument) into the fragment
        return fragment;                    // now when CrimeActivity calls a fragment, it includes a Crime ID from its extra
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);     // access the fragment's arguments
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View fragmentView = inflater.inflate(R.layout.fragment_crime, container, false); // false because adding the view in the activity's code

        /**
         * Wiring widgets in the fragment
         */
        // The title field
        mTitleField = (EditText) fragmentView.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle()); // get the title from the intent
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Intentionally blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Set the crime's title to the text entered
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally blank
            }
        });

        // The date button
        mDateButton = (Button) fragmentView.findViewById(R.id.crime_date);
        // TODO: Challenge 2 - date format
        //mDateButton.setText(android.text.format.DateFormat.format("EEEE MMM dd, yyy", mCrime.getDate()));
        updateDate();

        /** show a dialog fragment **/
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();                 // get the Fragment Manager
                DatePickerFragment dialog = DatePickerFragment                  // call DatePickerFragment.newInstance(Date)
                        .newInstance(mCrime.getDate());                         // -- initialize using the information held in the Crime's Date
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);     // Set the target fragment as CrimeFragment and the request code as the Constant
                dialog.show(manager, DIALOG_DATE);                              // pass the instance the FragmentManager and the Constant
            }
        });

        // THe solved checkbox
        mSolvedCheckbox = (CheckBox) fragmentView.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                mCrime.setSolved(isChecked);
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);   // get the Date stored in the Intent's extra
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(android.text.format.DateFormat.format("EEEE MMM dd, yyy", mCrime.getDate()));
    }


}
