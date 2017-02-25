package com.example.lmohamed.criminalintent2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
    private static final int REQUEST_CONTACT = 1;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mSuspectButton;
    private Button mReportButton;


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

    /** Crime instances get updated in CrimeFragmet and need to be written out to the DB when CrimeFragment is done **/
    @Override
    public void onPause(){
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
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

        // Show a dialog fragment
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

        // The solved checkbox
        mSolvedCheckbox = (CheckBox) fragmentView.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                mCrime.setSolved(isChecked);
            }
        });

        // The report button
        mReportButton = (Button)fragmentView.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report)); // user always chooses means of reporting
                startActivity(intent);
            }
        });

        // the Suspect button
        final Intent pickContant = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        // pickContant.addCategory(Intent.CATEGORY_HOME);       // test code for no contacts
        mSuspectButton = (Button)fragmentView.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivityForResult(pickContant, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        // if there is no Contacts app, don't crash the app - disable the button
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContant,
                packageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setText(R.string.no_contacts_found);
            mSuspectButton.setEnabled(false);
        }

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
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return values for
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform your query - the contactUri is like a Where clause
            Cursor cursor = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                // Double-check that you got results
                if(cursor.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data - suspect's name
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect + " suspected");
            } finally {
                cursor.close();
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(android.text.format.DateFormat.format("EEEE MMM dd, yyy", mCrime.getDate()));
    }

    private String getCrimeReport() {
        String solvedString = null;

        if(mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

}
