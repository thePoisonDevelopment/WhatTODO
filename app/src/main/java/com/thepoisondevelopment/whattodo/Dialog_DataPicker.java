package com.thepoisondevelopment.whattodo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import static com.thepoisondevelopment.whattodo.MainActivity.prm_Deadline;
import static com.thepoisondevelopment.whattodo.MainActivity.prm_Planned;

public class Dialog_DataPicker extends DialogFragment implements View.OnClickListener{
    //MyCustomDialog
    private static final String TAG = "Data Picker";

    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;

    int bld_PlannedDeadline;
    TextView button_cancel, button_choose, button_na;
    DatePicker mDatePicker;
    String tmpDate = "not set";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_datapicker, container, false);

        assert getArguments() != null;
        bld_PlannedDeadline = getArguments().getInt("PlannedDeadline");

        button_cancel = view.findViewById(R.id.button_cancel);
        button_choose = view.findViewById(R.id.button_choose);
        button_na = view.findViewById(R.id.button_na);
        mDatePicker = view.findViewById(R.id.datePicker);
        TextView caption_datapicker = view.findViewById(R.id.caption_datapicker);

        switch (bld_PlannedDeadline)
        {
            case 0:
                switch (MainActivity.prf_Language)
                {
                    case 0:
                        caption_datapicker.setText(R.string.planned_date);
                        break;
                    case 1:
                        caption_datapicker.setText(R.string.ru_planned_date);

                        break;
                }
                break;
            case 1:
                switch (MainActivity.prf_Language)
                {
                    case 0:
                        caption_datapicker.setText(R.string.deadline_date);
                        break;
                    case 1:
                        caption_datapicker.setText(R.string.ru_deadline_date);

                        break;
                }
            break;
        }


        switch (MainActivity.prf_Language)
        {
            case 0:
                button_na.setText(R.string.not_set);
                button_cancel.setText(R.string.button_cancel);
                button_choose.setText(R.string.pick);
                break;
            case 1:
                button_na.setText(R.string.ru_not_set);
                button_cancel.setText(R.string.ru_button_cancel);
                button_choose.setText(R.string.ru_pick);
                break;
        }

        Calendar today = Calendar.getInstance();

        String TodaysMonth;
        int tmp_month = today.get(Calendar.MONTH) + 1;
        if (tmp_month < 10) TodaysMonth = "0" + tmp_month; else TodaysMonth = "" + tmp_month;

        String TodayDay;
        int tmp_day = today.get(Calendar.DAY_OF_MONTH);
        if (tmp_day < 10) TodayDay = "0" + tmp_day; else TodayDay = "" + tmp_day;

        tmpDate = "" + TodayDay + "." + TodaysMonth + "." + today.get(Calendar.YEAR);

        mDatePicker.getCalendarView().setFirstDayOfWeek(Calendar.MONDAY);

        mDatePicker.init(

                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),

                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String TodayDay;
                        int tmp_day = dayOfMonth;
                        if (dayOfMonth < 10) TodayDay = "0" + tmp_day; else TodayDay = "" + tmp_day;

                        String TodaysMonth;
                        int tmp_month = monthOfYear + 1;
                        if (tmp_month <10) TodaysMonth = "0" + tmp_month; else TodaysMonth = "" + tmp_month;

                        tmpDate = "" + TodayDay + "." + TodaysMonth + "." + year;


                    }
                });



        // OK CANCEL buttons
        button_na.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String input = "Not set";

                switch (bld_PlannedDeadline) {
                    case 0: input = prm_Planned = "Not set";break;
                    case 1: input = prm_Deadline = "Not set";break;
                }

                mOnInputSelected.sendInput(input);

                getDialog().dismiss();
            }
        });


        // OK CANCEL buttons
        button_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                getDialog().dismiss();
            }
        });



        button_choose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String input = "";

                switch (bld_PlannedDeadline) {
                    case 0: input = prm_Planned = tmpDate;
                        Log.d(TAG, " ============ " + prm_Planned);

                    break;
                    case 1: input = prm_Deadline = tmpDate;
                        Log.d(TAG, " ============ " + prm_Deadline);
                    break;
                }

                mOnInputSelected.sendInput(input);
                getDialog().dismiss();
            }
        });



        return view;
    }

        @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();

        } catch (ClassCastException e) {
            Log.e(TAG, "On Attach: Class Exception : " + e.getMessage());
        }

    }

}
