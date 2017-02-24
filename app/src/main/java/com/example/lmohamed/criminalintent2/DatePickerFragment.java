package com.example.lmohamed.criminalintent2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lmohamed on 2/23/17.
 */

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.example.lmohamed.criminalIntent2.date";
    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    /** DatePickerFragment's argument bundle stashes the date
     *  The date can then be accessed by DatePickerFragment **/
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** create a calendar object to convert Crime's timestamp into Year Month Day ints **/
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        View dialogView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) dialogView.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())           // AlertDialog.Builder class provides a fluid interface for instantiating AlertDialog
                .setView(dialogView)
                .setTitle(R.string.date_picker_title)           // Pass a Context into the AlertDialog.Builder constructor which returns an instance of AlertDialog.Builder
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() { // implement DialogInterface.OnClickListener that retrieves the date and calls SendResult(...)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
