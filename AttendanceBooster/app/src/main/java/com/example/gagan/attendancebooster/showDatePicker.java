package com.example.gagan.attendancebooster;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by gagan on 23/3/15.
 */
public class showDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    String LOG_TEXT=showDatePicker.class.getSimpleName();
    int M,D,Y;


    public Dialog onCreateDialog(Bundle sis) {
        Log.w(LOG_TEXT, "DialogStarted!!");
        D = getArguments().getInt("d", -1);
        Y = getArguments().getInt("y", -1);
        M = getArguments().getInt("m", -1)-1;



        if (D == -1)
        {
            final Calendar cal = Calendar.getInstance();
            Y = cal.get(Calendar.YEAR);
            M = cal.get(Calendar.MONTH);
            D = cal.get(Calendar.DAY_OF_MONTH);

        }


        return new DatePickerDialog(getActivity(),this,Y,M,D);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.w(LOG_TEXT, "Return Dialog!!");
        M=monthOfYear;
        D=dayOfMonth;
        Y=year;
        Information.update_date_picker();


    }
    public int getMonth()
    {
        return M+1;
    }
    public int getDate()
    {
        return  D;
    }
    public int getYear()
    {
        return Y;
    }

}
