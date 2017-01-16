package com.start_up.dev.apilinkus.Auth;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Vignesh on 11/8/2016.
 */

public class MyDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    // Use date of birth in string format
    String dateofBirth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the current day, month, year fron the calendar object
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(),this,year,month,day);

    }

    // gets the data set by the user
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // DateTimeFormat. ISO Date : yyyy-MM-dd
        dateofBirth = ""+ year + "-"+ month + "-" + day;
    }

    public String getDateofBirthInISOFormat(){
        return dateofBirth;
    }
}