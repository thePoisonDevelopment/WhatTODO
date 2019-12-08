package com.thepoisondevelopment.whattodo;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

import static android.content.Context.MODE_PRIVATE;
import static com.thepoisondevelopment.whattodo.MainActivity.FILE_NAME;
import static com.thepoisondevelopment.whattodo.MainActivity.ListView_DATE;

public class fragmentCalendar extends Fragment {

    SharedPreferences prefSettings;

    private static final String TAG = "Settings Fragment";
    Button nav3_top_1create, nav3_top_2todolist, nav3_top_4settings;
    TextView nav3_top_1create_text, nav3_top_2todolist_text, nav3_top_4settings_text, calendar_nothingfound;

    Button cal_left, cal_right, cal_exportPDF;
    Button btn_cal_timeframe;
    TextView cal_date, cal_show_date;

    private MCalendarView custom_calendar;
    ListView lw_calendar;

    Calendar TodaysDate = Calendar.getInstance();

    String TodaysDate_MONTHstr, TodaysDate_DAYstr;
    int TodaysDate_DAYint, TodaysDate_MONTHint, TodaysDate_year;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        prefSettings = Objects.requireNonNull(this.getActivity()).getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        nav3_top_1create = view.findViewById(R.id.nav3_top_1create);
        nav3_top_2todolist = view.findViewById(R.id.nav3_top_2todolist);
        nav3_top_4settings = view.findViewById(R.id.nav3_top_4settings);

        nav3_top_1create_text = view.findViewById(R.id.nav3_top_1create_text);
        nav3_top_2todolist_text = view.findViewById(R.id.nav3_top_2todolist_text);
        nav3_top_4settings_text = view.findViewById(R.id.nav3_top_4settings_text);
        TextView nav3_top_3calendar_text = view.findViewById(R.id.nav3_top_3calendar_text);

        cal_left = view.findViewById(R.id.cal_left);
        cal_right = view.findViewById(R.id.cal_right);
        cal_date = view.findViewById(R.id.cal_date);

        cal_exportPDF = view.findViewById(R.id.cal_exportPDF);

        custom_calendar = view.findViewById(R.id.custom_calendar);
        lw_calendar = view.findViewById(R.id.lw_calendar);
        calendar_nothingfound = view.findViewById(R.id.calendar_nothingfound);
        calendar_nothingfound.setVisibility(View.INVISIBLE);

        MainActivity.ListView_MODE = 2; // I set this value to show calendar from the start

        // Here I get today's day in a format DD.MM.YYYY
        TodaysDate_DAYint = TodaysDate.get(Calendar.DAY_OF_MONTH);
        if (TodaysDate_DAYint < 10) TodaysDate_DAYstr = "0" + TodaysDate_DAYint; else TodaysDate_DAYstr = "" + TodaysDate_DAYint;
        TodaysDate_MONTHint = TodaysDate.get(Calendar.MONTH) + 1;
        if (TodaysDate_MONTHint < 10) TodaysDate_MONTHstr = "0" + TodaysDate_MONTHint; else TodaysDate_MONTHstr = "" + TodaysDate_MONTHint;
        TodaysDate_year = TodaysDate.get(Calendar.YEAR);

        btn_cal_timeframe = view.findViewById(R.id.btn_cal_timeframe);
        cal_show_date = view.findViewById(R.id.cal_show_date);
        cal_show_date.setText(TodaysDate_DAYstr);

        lw_calendar.setVisibility(View.INVISIBLE);
        cal_show_date.setVisibility(View.INVISIBLE);
        cal_date.setVisibility(View.INVISIBLE);
        cal_right.setVisibility(View.INVISIBLE);
        cal_left.setVisibility(View.INVISIBLE);


        ShowRecordsDAY("0", 2);

        switch (MainActivity.prf_Language)
        {
            case 0:
                nav3_top_1create_text.setText(R.string.create);
                nav3_top_2todolist_text.setText(R.string.todos);
                nav3_top_3calendar_text.setText(R.string.calendar);
                nav3_top_4settings_text.setText(R.string.settings);
                calendar_nothingfound.setText(R.string.no_tasks_found);
                break;
            case 1:
                nav3_top_1create_text.setText(R.string.ru_create);
                nav3_top_2todolist_text.setText(R.string.ru_todos);
                nav3_top_3calendar_text.setText(R.string.ru_calendar);
                nav3_top_4settings_text.setText(R.string.ru_settings);
                calendar_nothingfound.setText(R.string.ru_no_tasks_found);
                break;
        }


        cal_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //Jump to current day or current month and show all tasks from it

                TodaysDate_DAYint = TodaysDate.get(Calendar.DAY_OF_MONTH);
                if (TodaysDate_DAYint < 10) TodaysDate_DAYstr = "0" + TodaysDate_DAYint; else TodaysDate_DAYstr = "" + TodaysDate_DAYint;

                TodaysDate_MONTHint = TodaysDate.get(Calendar.MONTH) + 1;
                if (TodaysDate_MONTHint < 10) TodaysDate_MONTHstr = "0" + TodaysDate_MONTHint; else TodaysDate_MONTHstr = "" + TodaysDate_MONTHint;
                TodaysDate_year = TodaysDate.get(Calendar.YEAR);

                switch (MainActivity.ListView_MODE) {
                    case 0:
                        ListView_DATE = TodaysDate_DAYstr + "." + TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,0);
                        break;

                    case 1:
                        ListView_DATE = TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,1);
                        break;

                }
            }
        });


        cal_exportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO  not finished !!! ExportPDF_asCalendar(ListView_DATE);
                ExportPDF(ListView_DATE, MainActivity.ListView_MODE);

            }
        });

        btn_cal_timeframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (MainActivity.ListView_MODE) {
                    case 0:
                        MainActivity.ListView_MODE = 1;
                        custom_calendar.setVisibility(View.VISIBLE);
                        cal_show_date.setVisibility(View.INVISIBLE);

                        btn_cal_timeframe.setBackgroundResource(R.drawable.cal_showmonth);
                        ListView_DATE = TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,1);

                        break;

                    case 1:
                        MainActivity.ListView_MODE = 2;

                        custom_calendar.setVisibility(View.VISIBLE);
                        cal_show_date.setVisibility(View.INVISIBLE);
                        btn_cal_timeframe.setBackgroundResource(R.drawable.cal_showlist);
                        lw_calendar.setVisibility(View.INVISIBLE);
                        calendar_nothingfound.setVisibility(View.VISIBLE);
                        cal_date.setVisibility(View.INVISIBLE);
                        cal_right.setVisibility(View.INVISIBLE);
                        cal_left.setVisibility(View.INVISIBLE);
                        break;

                    case 2:
                        MainActivity.ListView_MODE = 0;
                        custom_calendar.setVisibility(View.INVISIBLE);
                        cal_show_date.setVisibility(View.VISIBLE);
                        lw_calendar.setVisibility(View.VISIBLE);
                        calendar_nothingfound.setVisibility(View.INVISIBLE);
                        cal_date.setVisibility(View.VISIBLE);
                        cal_right.setVisibility(View.VISIBLE);
                        cal_left.setVisibility(View.VISIBLE);

                        btn_cal_timeframe.setBackgroundResource(R.drawable.cal_showday);
                        ListView_DATE = TodaysDate_DAYstr + "." + TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,0);


                        break;
                }
            }
        });

        cal_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (MainActivity.ListView_MODE) {
                    case 0:
                        TodaysDate_DAYint--;

                        if (TodaysDate_DAYint < 10) TodaysDate_DAYstr = "0" + TodaysDate_DAYint; else TodaysDate_DAYstr = "" + TodaysDate_DAYint;

                        CalculateDAY_NEXT_PREV(true);
                        ListView_DATE = TodaysDate_DAYstr + "." + TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,0);
                        break;

                    case 1:
                        TodaysDate_MONTHint--;

                        CalculateMONTH_NEXT_PREV();
                        ListView_DATE = TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,1);
                        break;
                    case 2:
                        break;
                }

            }
        });

        cal_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (MainActivity.ListView_MODE) {
                    case 0:
                        TodaysDate_DAYint++;

                        if (TodaysDate_DAYint < 10) TodaysDate_DAYstr = "0" + TodaysDate_DAYint; else TodaysDate_DAYstr = "" + TodaysDate_DAYint;

                        CalculateDAY_NEXT_PREV(false);
                        ListView_DATE = TodaysDate_DAYstr + "." + TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,0);
                        break;

                    case 1:
                        TodaysDate_MONTHint++;
                        CalculateMONTH_NEXT_PREV();
                        ListView_DATE = TodaysDate_MONTHstr + "." + TodaysDate_year;
                        cal_date.setText(ListView_DATE);
                        ShowRecordsDAY(ListView_DATE,1);
                        break;
                    case 2:
                        break;
                }

            }
        });


        nav3_top_1create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearVariables();

                Fragment targetFragment = new fragmentCreate();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        nav3_top_2todolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearVariables();

                Fragment targetFragment = new fragmentTodo();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        nav3_top_4settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearVariables();

                Fragment targetFragment = new fragmentSettings();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

        return view;
    }




    public void ExportPDF_asCalendar(String date) {

        // Period 0 = day,   1 = month,   2 = list
        //TODO finish the calendar export to PDF

        dbHelper = new DBHelper(getActivity());

        String FileName, timeframe_label;

        data = dbHelper.getSpecificMonth(date);
        FileName = "" + date + "_MONTH_TODO";
        timeframe_label = "| TODO list for the month " + date;

        int PrioW = 190, PrioH = 250;
        int pageWidth = 800, pageHeight = 1000;
        int
                xPrio = 20,
                LineX = 15,
                LineY = 90,
                LineXStep = 110,
                LineYStep = 150;

        String dbName, dbCreated, dbPlanned, dbDeadline, dbLabel, dbLabel_Color;
        int dbID, dbPriority, dbStatus, dbTimeLogged;

        int MaxLineChars = 40;  //total 256 characters, 40+, 80+, 120+, 160+, 200, 240,
        int separatorX = 10;

        Bitmap bitmap;

        try {

            File FILE_NAME = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir(null), FileName + "_Calendar.pdf");

            FILE_NAME.createNewFile();
            FileOutputStream fOut = new FileOutputStream(FILE_NAME, false);

            PdfDocument document = new PdfDocument();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            Resources mResources = getResources();

            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            canvas.setDensity(36);
            Paint paint = new Paint();

            //HEADER
            paint.setColor(Color.parseColor(MainActivity.prf_ColorTheme));
            canvas.drawRect(0,0,pageWidth, 30, paint);

            paint.setColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));
            paint.setStrokeWidth(3);
            canvas.drawLine(0,31,pageWidth, 31, paint);
            paint.setStrokeWidth(1);

            paint.setColor(Color.WHITE);
            canvas.drawText("WhatTODO Calendar tasks list " + timeframe_label, 30, 20, paint);
            canvas.drawText("thePoison Development", 600, 20, paint);

            paint.setColor(Color.BLACK);
            canvas.drawText("" + timeframe_label, 30, 50, paint);

// HERE I add all data from SQLite data base
            int ExtraY = 0;
            int PageYID = 0;
            int PageNumber = 1;

            //Calendar frame
            paint.setColor(Color.BLACK);
            // Draw X lines
            canvas.drawLine(LineX,LineY, LineX, LineY + 750, paint);
            canvas.drawLine(LineX + LineXStep,  LineY, LineX + LineXStep,   LineY + 750, paint);
            canvas.drawLine(LineX + LineXStep*2,LineY, LineX + LineXStep*2, LineY + 750, paint);
            canvas.drawLine(LineX + LineXStep*3,LineY, LineX + LineXStep*3, LineY + 750, paint);
            canvas.drawLine(LineX + LineXStep*4,LineY, LineX + LineXStep*4, LineY + 750, paint);
            canvas.drawLine(LineX + LineXStep*5,LineY, LineX + LineXStep*5, LineY + 750, paint);
            canvas.drawLine(LineX + LineXStep*6,LineY, LineX + LineXStep*6, LineY + 750, paint);
            canvas.drawLine(LineX + LineXStep*7,LineY, LineX + LineXStep*7, LineY + 750, paint);

            // Draw Y lines
            canvas.drawLine(LineX,LineY, LineX + 770, LineY, paint);
            canvas.drawLine(LineX,LineY + LineYStep,   LineX + 770, LineY + LineYStep, paint);
            canvas.drawLine(LineX,LineY + LineYStep*2, LineX + 770, LineY + LineYStep*2, paint);
            canvas.drawLine(LineX,LineY + LineYStep*3, LineX + 770, LineY + LineYStep*3, paint);
            canvas.drawLine(LineX,LineY + LineYStep*4, LineX + 770, LineY + LineYStep*4, paint);
            canvas.drawLine(LineX,LineY + LineYStep*5, LineX + 770, LineY + LineYStep*5, paint);

            int daydate = 1;
            for (int j=0; j<=4;j++) {
                for (int i=0;i<=6;i++) {
                    if (daydate <= 31) {
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(i*LineXStep + 55,LineYStep*j + LineY, i*LineXStep + 85, LineYStep*j + LineY + 15, paint);
                        paint.setColor(Color.WHITE);
                        canvas.drawText("" + daydate, i*LineXStep + LineX + 50,LineYStep*j + LineY + 12, paint);
                        daydate++;
                     }
                }
            }

//            {
//                while (data.moveToNext()) {
//                    PageYID++;
//                    yRecordLine = (PageYID * 60) + ExtraY + 20;
//
//                    dbID = data.getInt(data.getColumnIndex("_id"));
//                    dbPriority = data.getInt(data.getColumnIndex("PRIO"));
//                    dbName = data.getString(data.getColumnIndex("NAME"));
//                    dbName = dbName.replaceAll("\n", " ");
//                    dbStatus = data.getInt(data.getColumnIndex("STATUS"));
//                    dbCreated = data.getString(data.getColumnIndex("TASK_CREATED"));
//                    dbPlanned = data.getString(data.getColumnIndex("TASK_PLANNED"));
//                    dbDeadline = data.getString(data.getColumnIndex("TASK_DEADLINE"));
//                    dbTimeLogged = data.getInt(data.getColumnIndex("LOGGED"));
//                    dbLabel = data.getString(data.getColumnIndex("LABEL"));
//                    dbLabel_Color = data.getString(data.getColumnIndex("LABEL_COLOR"));
//
//
//
//                    paint.setStrokeWidth(1);
//                    paint.setColor(Color.parseColor("#999999"));
//                    canvas.drawLine(10,yRecordLine - 13,pageWidth - 10, yRecordLine - 13, paint);
////ID
//                    canvas.drawText("ID: " + dbID, xPrio - 5, yRecordLine + 40, paint);  //this is real ID
//
////PRIORITY
//                    switch (dbPriority) {
//                        case 1:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_1);break;
//                        case 2:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_2);break;
//                        case 3:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_3);break;
//                        case 4:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_4);break;
//                        case 5:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_5);break;
//                        default:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_3);break; }
//                    bitmap = Bitmap.createScaledBitmap(bitmap, PrioW, PrioH, true);
//                    canvas.drawBitmap(bitmap, xPrio + 12, yRecordLine - 10 , null);
//
////LABEL
//                    canvas.drawText("" + dbLabel, xLabel, yRecordLine + 19, paint);
//                    paint.setStrokeWidth(3);
//
//                    //TODO kostil. remove before releiese
//                    if ((dbLabel_Color.equals("RED")||(dbLabel_Color.equals("ff0000")))) dbLabel_Color = "#ff0000";paint.setColor(Color.parseColor(dbLabel_Color));
//
//                    if (dbLabel.equals("")) {
//                        canvas.drawRect(xLabel - 1,yRecordLine+4, xLabel + 84, yRecordLine + 24, paint);
//                    } else {
//                        canvas.drawLine(xLabel - 1,yRecordLine+4,xLabel + 84, yRecordLine+4, paint);
//                        canvas.drawLine(xLabel - 1,yRecordLine + 24,xLabel + 84, yRecordLine + 24, paint);
//                    }
//                    paint.setColor(Color.parseColor("#000000"));
//                    //TODO count number of characters and show long or short line
//
//
////CREATED, PLANNED. LOGGED
//                    canvas.drawText("Created:  " + dbCreated, xCreated, yRecordLine, paint);
//                    if (!dbPlanned.equals("Not set")) canvas.drawText("Planned:  " + dbPlanned, xCreated, yRecordLine + 20, paint);
//                    if (!dbDeadline.equals("Not set")) canvas.drawText("Deadline: " + dbDeadline, xCreated, yRecordLine + 40, paint);
//
////TIME LOGGED
//                    if (dbTimeLogged == 0) canvas.drawText("-", xTimeLog - 5, yRecordLine + 20, paint);
//                    else {
//                        int hours = dbTimeLogged / 60; int mins = dbTimeLogged % 60;
//                        canvas.drawText("" + hours + "h " + mins + "m", xTimeLog - 5, yRecordLine + 20, paint);}
//
//                    switch (dbStatus) {
//                        case 0:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_0todo);break;//
//                        case 1:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_1wip);break;
//                        case 2:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_2blocked);break;
//                        case 4:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_4planned);break;//
//                        default:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_0todo);break;
//                    }
//                    bitmap = Bitmap.createScaledBitmap(bitmap, PrioH, PrioH, true);
//                    canvas.drawBitmap(bitmap, xStatus - 1, yRecordLine + 3, null);
//
//                    bitmap = BitmapFactory.decodeResource(mResources, R.drawable.export_tickbox);
//                    bitmap = Bitmap.createScaledBitmap(bitmap, PrioH, PrioH, true);
//                    canvas.drawBitmap(bitmap, xTickBox+3, yRecordLine + 3, null);
//
//
////NAME and logic for separation onto 2 lines
//                    int dbNameTmp = dbName.length() - 1;
//                    if ((dbNameTmp > MaxLineChars)&&(dbNameTmp <= MaxLineChars*2)) {  // 40 < x <  80
//                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars, dbNameTmp), xDescription, yRecordLine + 20, paint);
//                        ExtraY = 0;
//                    } else  if ((dbNameTmp > MaxLineChars*2)&&(dbNameTmp <= MaxLineChars*3)) { // 80 < x < 120
//                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars*2, dbNameTmp), xDescription, yRecordLine + 40, paint);
//                        ExtraY = 0;
//                    } else if ((dbNameTmp > MaxLineChars*3)&&(dbNameTmp <= MaxLineChars*4)) {  //120 - 160
//                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);  // 0 - 40
//                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint); //40 - 120
//                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint); //40 - 120
//                    }  else if ((dbNameTmp > MaxLineChars*4)&&(dbNameTmp <= MaxLineChars*5)) {  // 160 - 200
//                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint);
//                    } else if ((dbNameTmp > MaxLineChars*5)&&(dbNameTmp <= MaxLineChars*6)) {  //200 - 240
//                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint);
//                    } else if ((dbNameTmp > MaxLineChars*6)&&(dbNameTmp <= MaxLineChars*7)) { // 240 - 256
//                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
//                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint);
//                    } else {
//                        ExtraY = 0;
//                        canvas.drawText("" + dbName, xDescription, yRecordLine, paint);
//                    }
//
//                    //separation lines between columns
//
//                    paint.setStrokeWidth(1);
//                    canvas.drawLine(xPrio - separatorX,yRecordLine - 13, xPrio - separatorX, yRecordLine + 44 + ExtraY, paint);
//                    canvas.drawLine(xLabel - separatorX,yRecordLine - 13, xLabel - separatorX, yRecordLine + 44 + ExtraY, paint);
//                    canvas.drawLine(xDescription - separatorX,yRecordLine - 13, xDescription - separatorX, yRecordLine + 44 + ExtraY, paint);
//                    canvas.drawLine(xCreated - separatorX,yRecordLine - 13, xCreated - separatorX, yRecordLine + 44 + ExtraY, paint);
//                    canvas.drawLine(xTimeLog - separatorX,yRecordLine - 13, xTimeLog - separatorX, yRecordLine + 44 + ExtraY, paint);
//                    canvas.drawLine(xStatus - separatorX,yRecordLine - 13, xStatus - separatorX, yRecordLine + 44 + ExtraY, paint);
//                    canvas.drawLine(pageWidth - 10,yRecordLine - 13, pageWidth - 10, yRecordLine + 44 + ExtraY, paint);
//
////BOTTOM RECORD LINE
//                    paint.setColor(Color.parseColor("#888888"));
//                    canvas.drawLine(10,yRecordLine + 44 + ExtraY,pageWidth - 10, yRecordLine + 44 + ExtraY, paint);
//
//

            // finish the page
            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

            switch (MainActivity.prf_Language){

                case 0: Toast.makeText(getActivity(), "PDF saved to " + FILE_NAME , Toast.LENGTH_LONG).show();break;
                case 1: Toast.makeText(getActivity(), "PDF сохранен в "+ FILE_NAME , Toast.LENGTH_LONG).show();break;
                default: Toast.makeText(getActivity(), "PDF saved to "+ FILE_NAME , Toast.LENGTH_LONG).show(); break;
            }

        }  catch (IOException e){
            Log.i("error",e.getLocalizedMessage());
        }



    }

    public void ExportPDF(String date, int period) {

        // Period 0 = day,   1 = month,   2 = list

        dbHelper = new DBHelper(getActivity());

        String FileName, timeframe_label;

        switch (period){
            case 0:
                data = dbHelper.getSpecificDay(date);
                FileName = "" + date.replace(".", "") + "_DAY_TODO";
                timeframe_label = "| TODO list for the day " + date;
            break;
            case 1:
                data = dbHelper.getSpecificMonth(date);
                FileName = "" + date.replace(".", "") + "_MONTH_TODO";
                timeframe_label = "| TODO list for the month " + date;
            break;
            default:
                data = dbHelper.getSpecificDay(date);
                FileName = "" + DBHelper.getTodayDate_forExport()+ "_Calendar";
                timeframe_label = "| TODO list for the day " + date;
            break;
        }

        int PrioW = 190, PrioH = 250;
        int pageWidth = 800, pageHeight = 1000;
        int
                xPrio = 20,
                xLabel = 85,
                xDescription = 185,
                xCreated = 520,
                xTimeLog = 665,
                xStatus = 720,
                xTickBox = 750,
                yCaptionLine = 60,
                yRecordLine;

        String dbName, dbCreated, dbPlanned, dbDeadline, dbLabel, dbLabel_Color;
        int dbID, dbPriority, dbStatus, dbTimeLogged;

        int MaxLineChars = 40;  //total 256 characters, 40+, 80+, 120+, 160+, 200, 240,
        int separatorX = 10;

        Bitmap bitmap;

        try {

            File FILE_NAME = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir(null), FileName + ".pdf");

            FILE_NAME.createNewFile();
            FileOutputStream fOut = new FileOutputStream(FILE_NAME, false);

            PdfDocument document = new PdfDocument();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            Resources mResources = getResources();

            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            canvas.setDensity(36);
            Paint paint = new Paint();

            //HEADER
            paint.setColor(Color.parseColor(MainActivity.prf_ColorTheme));
            canvas.drawRect(0,0,pageWidth, 30, paint);

            paint.setColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));
            paint.setStrokeWidth(3);
            canvas.drawLine(0,31,pageWidth, 31, paint);
            paint.setStrokeWidth(1);

            paint.setColor(Color.WHITE);
            canvas.drawText("WhatTODO tasks list " + timeframe_label, 30, 20, paint);
            canvas.drawText("thePoison Development", 600, 20, paint);

            paint.setColor(Color.BLACK);
            canvas.drawText("ID  Prio", xPrio, yCaptionLine, paint); //+
            canvas.drawText("Label", xLabel, yCaptionLine, paint); //+
            canvas.drawText("Description", xDescription, yCaptionLine, paint); //+
            canvas.drawText("Created", xCreated, yCaptionLine, paint);
            canvas.drawText("Time", xTimeLog, yCaptionLine, paint);
            canvas.drawText("Status", xStatus, yCaptionLine, paint);

// HERE I add all data from SQLite data base
            int ExtraY = 0;
            int PageYID = 0;
            int PageNumber = 1;


            // HERE I do sorting. BLOCKER - WIP - PLANNED - TO DO ETC)
            //for (int _y = 0; _y<=3;_y++)
            {  // I go through each status and put it to data base.
//                switch (_y){
//                    case 0: data = dbHelper.getDataOfStatus(2);break;
//                    case 1: data = dbHelper.getDataOfStatus(1);break;
//                    case 2: data = dbHelper.getDataOfStatus(4);break;
//                    case 3: data = dbHelper.getDataOfStatus(0);break;
//                }

                while (data.moveToNext()) {
                    PageYID++;
                    yRecordLine = (PageYID * 60) + ExtraY + 20;

                    dbID = data.getInt(data.getColumnIndex("_id"));
                    dbPriority = data.getInt(data.getColumnIndex("PRIO"));
                    dbName = data.getString(data.getColumnIndex("NAME"));
                    dbName = dbName.replaceAll("\n", " ");
                    dbStatus = data.getInt(data.getColumnIndex("STATUS"));
                    dbCreated = data.getString(data.getColumnIndex("TASK_CREATED"));
                    dbPlanned = data.getString(data.getColumnIndex("TASK_PLANNED"));
                    dbDeadline = data.getString(data.getColumnIndex("TASK_DEADLINE"));
                    dbTimeLogged = data.getInt(data.getColumnIndex("LOGGED"));
                    dbLabel = data.getString(data.getColumnIndex("LABEL"));
                    dbLabel_Color = data.getString(data.getColumnIndex("LABEL_COLOR"));



                    paint.setStrokeWidth(1);
                    paint.setColor(Color.parseColor("#999999"));
                    canvas.drawLine(10,yRecordLine - 13,pageWidth - 10, yRecordLine - 13, paint);
//ID
                    canvas.drawText("ID: " + dbID, xPrio - 5, yRecordLine + 40, paint);  //this is real ID

//PRIORITY
                    switch (dbPriority) {
                        case 1:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_1);break;
                        case 2:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_2);break;
                        case 3:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_3);break;
                        case 4:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_4);break;
                        case 5:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_5);break;
                        default:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_prio_3);break; }
                    bitmap = Bitmap.createScaledBitmap(bitmap, PrioW, PrioH, true);
                    canvas.drawBitmap(bitmap, xPrio + 12, yRecordLine - 10 , null);

//LABEL
                    canvas.drawText("" + dbLabel, xLabel, yRecordLine + 19, paint);
                    paint.setStrokeWidth(3);

                    //TODO kostil. remove before releiese
                    if ((dbLabel_Color.equals("RED")||(dbLabel_Color.equals("ff0000")))) dbLabel_Color = "#ff0000";paint.setColor(Color.parseColor(dbLabel_Color));

                    if (dbLabel.equals("")) {
                        canvas.drawRect(xLabel - 1,yRecordLine+4, xLabel + 84, yRecordLine + 24, paint);
                    } else {
                        canvas.drawLine(xLabel - 1,yRecordLine+4,xLabel + 84, yRecordLine+4, paint);
                        canvas.drawLine(xLabel - 1,yRecordLine + 24,xLabel + 84, yRecordLine + 24, paint);
                    }
                    paint.setColor(Color.parseColor("#000000"));
                    //TODO count number of characters and show long or short line


//CREATED, PLANNED. LOGGED
                    canvas.drawText("Created:  " + dbCreated, xCreated, yRecordLine, paint);
                    if (!dbPlanned.equals("Not set")) canvas.drawText("Planned:  " + dbPlanned, xCreated, yRecordLine + 20, paint);
                    if (!dbDeadline.equals("Not set")) canvas.drawText("Deadline: " + dbDeadline, xCreated, yRecordLine + 40, paint);

//TIME LOGGED
                    if (dbTimeLogged == 0) canvas.drawText("-", xTimeLog - 5, yRecordLine + 20, paint);
                    else {
                        int hours = dbTimeLogged / 60; int mins = dbTimeLogged % 60;
                        canvas.drawText("" + hours + "h " + mins + "m", xTimeLog - 5, yRecordLine + 20, paint);}

                    switch (dbStatus) {
                        case 0:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_0todo);break;//
                        case 1:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_1wip);break;
                        case 2:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_2blocked);break;
                        case 4:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_4planned);break;//
                        default:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_0todo);break;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, PrioH, PrioH, true);
                    canvas.drawBitmap(bitmap, xStatus - 1, yRecordLine + 3, null);

                    bitmap = BitmapFactory.decodeResource(mResources, R.drawable.export_tickbox);
                    bitmap = Bitmap.createScaledBitmap(bitmap, PrioH, PrioH, true);
                    canvas.drawBitmap(bitmap, xTickBox+3, yRecordLine + 3, null);


//NAME and logic for separation onto 2 lines
                    int dbNameTmp = dbName.length() - 1;
                    if ((dbNameTmp > MaxLineChars)&&(dbNameTmp <= MaxLineChars*2)) {  // 40 < x <  80
                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars, dbNameTmp), xDescription, yRecordLine + 20, paint);
                        ExtraY = 0;
                    } else  if ((dbNameTmp > MaxLineChars*2)&&(dbNameTmp <= MaxLineChars*3)) { // 80 < x < 120
                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars*2, dbNameTmp), xDescription, yRecordLine + 40, paint);
                        ExtraY = 0;
                    } else if ((dbNameTmp > MaxLineChars*3)&&(dbNameTmp <= MaxLineChars*4)) {  //120 - 160
                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);  // 0 - 40
                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint); //40 - 120
                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint); //40 - 120
                    }  else if ((dbNameTmp > MaxLineChars*4)&&(dbNameTmp <= MaxLineChars*5)) {  // 160 - 200
                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint);
                    } else if ((dbNameTmp > MaxLineChars*5)&&(dbNameTmp <= MaxLineChars*6)) {  //200 - 240
                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint);
                    } else if ((dbNameTmp > MaxLineChars*6)&&(dbNameTmp <= MaxLineChars*7)) { // 240 - 256
                        canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars,MaxLineChars*2), xDescription, yRecordLine + 20, paint);
                        canvas.drawText("" + dbName.substring(MaxLineChars*2,MaxLineChars*3), xDescription, yRecordLine + 40, paint);
                    } else {
                        ExtraY = 0;
                        canvas.drawText("" + dbName, xDescription, yRecordLine, paint);
                    }

                    //separation lines between columns

                    paint.setStrokeWidth(1);
                    canvas.drawLine(xPrio - separatorX,yRecordLine - 13, xPrio - separatorX, yRecordLine + 44 + ExtraY, paint);
                    canvas.drawLine(xLabel - separatorX,yRecordLine - 13, xLabel - separatorX, yRecordLine + 44 + ExtraY, paint);
                    canvas.drawLine(xDescription - separatorX,yRecordLine - 13, xDescription - separatorX, yRecordLine + 44 + ExtraY, paint);
                    canvas.drawLine(xCreated - separatorX,yRecordLine - 13, xCreated - separatorX, yRecordLine + 44 + ExtraY, paint);
                    canvas.drawLine(xTimeLog - separatorX,yRecordLine - 13, xTimeLog - separatorX, yRecordLine + 44 + ExtraY, paint);
                    canvas.drawLine(xStatus - separatorX,yRecordLine - 13, xStatus - separatorX, yRecordLine + 44 + ExtraY, paint);
                    canvas.drawLine(pageWidth - 10,yRecordLine - 13, pageWidth - 10, yRecordLine + 44 + ExtraY, paint);

//BOTTOM RECORD LINE
                    paint.setColor(Color.parseColor("#888888"));
                    canvas.drawLine(10,yRecordLine + 44 + ExtraY,pageWidth - 10, yRecordLine + 44 + ExtraY, paint);

                    if (PageYID >=15) {
// LOGIC FOR STARTING A NEW PAGE if records more than 15.
                        PageYID = 0;

                        canvas.drawText("- " + PageNumber + " -", 700, pageHeight - 15, paint);
                        PageNumber++;
                        document.finishPage(page);
                        page = document.startPage(pageInfo);

                        //HEADER
                        //HEADER
                        paint.setColor(Color.parseColor(MainActivity.prf_ColorTheme));
                        canvas.drawRect(0,0,pageWidth, 30, paint);

                        paint.setColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));
                        paint.setStrokeWidth(3);
                        canvas.drawLine(0,31,pageWidth, 31, paint);
                        paint.setStrokeWidth(1);

                        paint.setColor(Color.WHITE);
                        canvas.drawText("WhatTODO tasks list " + timeframe_label, 30, 20, paint);
                        canvas.drawText("thePoison Development", 600, 20, paint);

                        paint.setColor(Color.BLACK);
                        canvas.drawText("ID  Prio", xPrio, yCaptionLine, paint); //+
                        canvas.drawText("Label", xLabel, yCaptionLine, paint); //+
                        canvas.drawText("Description", xDescription, yCaptionLine, paint); //+
                        canvas.drawText("Created", xCreated, yCaptionLine, paint);
                        canvas.drawText("Time", xTimeLog, yCaptionLine, paint);
                        canvas.drawText("Status", xStatus, yCaptionLine, paint);
                    }
                }
            }


            canvas.drawText("- " + PageNumber + " -", 500, pageHeight - 20, paint);
            // finish the page
            document.finishPage(page);
            document.writeTo(fOut);
            document.close();

            switch (MainActivity.prf_Language){

                case 0: Toast.makeText(getActivity(), "PDF saved to " + FILE_NAME , Toast.LENGTH_LONG).show();break;
                case 1: Toast.makeText(getActivity(), "PDF сохранен в "+ FILE_NAME , Toast.LENGTH_LONG).show();break;
                default: Toast.makeText(getActivity(), "PDF saved to "+ FILE_NAME , Toast.LENGTH_LONG).show(); break;
            }

        }  catch (IOException e){
            Log.i("error",e.getLocalizedMessage());
        }



    }

    public void clearVariables(){

        MainActivity.prf_ListViewPosition = 0;
        MainActivity.ListView_DAY = 0;
        MainActivity.ListView_MONTH = 0;
        MainActivity.ListView_MODE = 0;

    }

    public void CalculateMONTH_NEXT_PREV(){

        if (TodaysDate_MONTHint <= 0) {
            TodaysDate_MONTHint = 12;
            TodaysDate_year--;
        } else if (TodaysDate_MONTHint >= 13) {
            TodaysDate_MONTHint = 1;
            TodaysDate_year++;
        }

        if (TodaysDate_MONTHint < 10) TodaysDate_MONTHstr = "0" + TodaysDate_MONTHint; else TodaysDate_MONTHstr = "" + TodaysDate_MONTHint;

    }

    private void CalculateDAY_NEXT_PREV(boolean NextDay){
        // TRUE - I decrease day
        // FALSE - increase
        if (NextDay) {

            if ((TodaysDate_DAYint <= 0) &&
                    ((TodaysDate_MONTHint == 5) || (TodaysDate_MONTHint == 7) ||
                            (TodaysDate_MONTHint == 10) || (TodaysDate_MONTHint == 12))) {
                TodaysDate_DAYint = 30;
                TodaysDate_MONTHint--;
            } else if ((TodaysDate_DAYint <= 0) &&
                    ((TodaysDate_MONTHint == 2) || (TodaysDate_MONTHint == 4) ||
                            (TodaysDate_MONTHint == 6) || (TodaysDate_MONTHint == 8) || (TodaysDate_MONTHint == 9) || (TodaysDate_MONTHint == 11))) {
                TodaysDate_DAYint = 31; TodaysDate_MONTHint--;
            } else
                //Condition for February
                if ((TodaysDate_DAYint <= 0) && (TodaysDate_MONTHint == 3) && (TodaysDate_year % 4 == 0)) {
                    TodaysDate_DAYint = 29; TodaysDate_MONTHint--;
                } else if ((TodaysDate_DAYint <= 0) && (TodaysDate_MONTHint == 3) && (TodaysDate_year % 4 != 0)) {
                    TodaysDate_DAYint = 28;
                    TodaysDate_MONTHint--;
                } else if ((TodaysDate_DAYint <= 0) && (TodaysDate_MONTHint == 1)) {
                    TodaysDate_DAYint = 31;   TodaysDate_MONTHint = 12; TodaysDate_year--;
                }

        } else if (!NextDay){
            if ((TodaysDate_DAYint >= 32)&&
                    ((TodaysDate_MONTHint == 1)||(TodaysDate_MONTHint == 3)||(TodaysDate_MONTHint == 5)||(TodaysDate_MONTHint == 7)||
                            (TodaysDate_MONTHint == 8)||(TodaysDate_MONTHint == 10)||(TodaysDate_MONTHint == 12)))
            { TodaysDate_DAYint = 1; TodaysDate_MONTHint++; }

            else

            if ((TodaysDate_DAYint >= 31)&&
                    ((TodaysDate_MONTHint == 4)||(TodaysDate_MONTHint == 6)||
                            (TodaysDate_MONTHint == 9)||(TodaysDate_MONTHint == 11)))
            {
                TodaysDate_DAYint = 1; TodaysDate_MONTHint++;
            } else
                //Condition for February
                if  ((TodaysDate_DAYint >= 30)&&(TodaysDate_MONTHint == 2)&&(TodaysDate_year%4 == 0)){
                    TodaysDate_DAYint = 1; TodaysDate_MONTHint++;
                } else
                if  ((TodaysDate_DAYint >= 29)&&(TodaysDate_MONTHint == 2)&&(TodaysDate_year%4 != 0)){
                    TodaysDate_DAYint = 1; TodaysDate_MONTHint++;
                }
            if (TodaysDate_MONTHint >= 13)
            {
                TodaysDate_DAYint = 1; TodaysDate_MONTHint = 1; TodaysDate_year = TodaysDate_year + 1;
            }
        }
        if (TodaysDate_MONTHint < 10) TodaysDate_MONTHstr = "0" + TodaysDate_MONTHint; else TodaysDate_MONTHstr = "" + TodaysDate_MONTHint;
    }

    int dbId;
    String dbDatePlanned, dbDateDeadline, dbLabelColor, dbName;
    int dbPrio;
    int dbStatus;
    DBHelper dbHelper;
    Cursor data;

    private void ShowRecordsDAY(String date, int period){
        // Here i show info in following formats:
        // 0 - I show specific day and what is planned there
        // 1 - I show specific month
        // 2 - I show calendar

        int NumberOfRecords = 0;
        dbHelper = new DBHelper(getActivity());
        ArrayList<ListView_Calendar> RecordsList = new ArrayList<>();

        switch (period){
            case 0: data = dbHelper.getSpecificDay(date);break;
            case 1: data = dbHelper.getSpecificMonth(date);break;
            case 2: data = dbHelper.getAllData();break;
            default: data = dbHelper.getSpecificDay(date);break;
        }

        while (data.moveToNext()) {

            NumberOfRecords++;

            dbId = data.getInt(data.getColumnIndex(DBHelper.KEY_ID));
            dbName = data.getString(data.getColumnIndex(DBHelper.KEY_NAME));
            dbStatus = data.getInt(data.getColumnIndex(DBHelper.KEY_STATUS));
            dbPrio = data.getInt(data.getColumnIndex(DBHelper.KEY_PRIO));
            dbDatePlanned = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_PLANNED));
            dbDateDeadline = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_DEADLINE));
            dbLabelColor = data.getString(data.getColumnIndex(DBHelper.KEY_LABEL_COLOR));

            // Here I fill 2 adapters, for DAY/TASKS and for MONTH TASKS.
            if ((period == 0)||(period == 1)){

                ListView_Calendar Records = new ListView_Calendar(dbDatePlanned, dbDateDeadline, dbName, dbStatus, dbPrio, dbLabelColor);
                RecordsList.add(Records);
                ListAdapter_Calendar adapter;
                adapter = new ListAdapter_Calendar(getActivity(), R.layout.listview_calendar, RecordsList);
                lw_calendar.setAdapter(adapter);

                lw_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        data.moveToPosition(position);

                        MainActivity.prf_ListViewPosition = position;

                        bundle.putInt(DBHelper.KEY_ID, data.getInt(data.getColumnIndex(DBHelper.KEY_ID)));
                        bundle.putString(DBHelper.KEY_NAME, data.getString(data.getColumnIndex(DBHelper.KEY_NAME)));
                        bundle.putInt(DBHelper.KEY_STATUS, data.getInt(data.getColumnIndex(DBHelper.KEY_STATUS)));
                        bundle.putInt(DBHelper.KEY_PRIO, data.getInt(data.getColumnIndex(DBHelper.KEY_PRIO)));
                        bundle.putString(DBHelper.KEY_TASK_CREATED, data.getString(data.getColumnIndex(DBHelper.KEY_TASK_CREATED)));
                        bundle.putString(DBHelper.KEY_TASK_PLANNED, data.getString(data.getColumnIndex(DBHelper.KEY_TASK_PLANNED)));
                        bundle.putString(DBHelper.KEY_TASK_DEADLINE, data.getString(data.getColumnIndex(DBHelper.KEY_TASK_DEADLINE)));
                        bundle.putString(DBHelper.KEY_LABEL, data.getString(data.getColumnIndex(DBHelper.KEY_LABEL)));
                        bundle.putString(DBHelper.KEY_LABEL_COLOR, data.getString(data.getColumnIndex(DBHelper.KEY_LABEL_COLOR)));
                        bundle.putInt(DBHelper.KEY_TIME_LOGGED, data.getInt(data.getColumnIndex(DBHelper.KEY_TIME_LOGGED)));

                        bundle.putString("MODE", "CALENDAR");

                        // I need this to be able to return back after I edit a record
                        MainActivity.ListView_DAY = TodaysDate_DAYint;
                        MainActivity.ListView_MONTH = TodaysDate_MONTHint;

                        Fragment targetFragment = new fragmentEdit();
                        assert getFragmentManager() != null;
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        targetFragment.setArguments(bundle);
                        transaction.commit();

                    }
                });
            }

            // Here I fill only calendar and color days which have tasks.
            if ((period == 2)&&(!dbDatePlanned.equals("Not set"))){

                int _year, _month, _day;
                if (dbDatePlanned.length() <= 9 ) {
                    // This code I use for old version when date format doesn't count 0 in the day. For ex. 1.05.2019
                    _day = Integer.parseInt(dbDatePlanned.substring(0, 1));
                    _month = Integer.parseInt(dbDatePlanned.substring(2, 4));
                    _year = Integer.parseInt(dbDatePlanned.substring(5, 9));
                }else
                {
                    // This code I use for old version when date format counts 0 in the day. For ex. 01.05.2019
                    _day = Integer.parseInt(dbDatePlanned.substring(0, 2));
                    _month = Integer.parseInt(dbDatePlanned.substring(3, 5));
                    _year = Integer.parseInt(dbDatePlanned.substring(6, 10));
                }

                custom_calendar.markDate(new DateData(_year, _month, _day).
                        setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.RED)));

                custom_calendar.setOnDateClickListener(new OnDateClickListener() {
                    @Override
                    public void onDateClick(View view, DateData date) {

                        String getDAYstr, getMONTHstr;
                        if (date.getDay()< 10) getDAYstr = "0" + date.getDay();else getDAYstr = "" + date.getDay();
                        if (date.getMonth() < 10) getMONTHstr = "0" + date.getMonth();else getMONTHstr = "" + date.getMonth();
                        String ShowTasksInDate = getDAYstr + "." + getMONTHstr + "." + date.getYear();

                        // TODO,  if there are tasks in the date, I need to switch to DAY TASKS view
                        // and show these tasks
                        custom_calendar.setVisibility(View.INVISIBLE);
                        lw_calendar.setVisibility(View.VISIBLE);
                        btn_cal_timeframe.setBackgroundResource(R.drawable.cal_showday);
                        cal_show_date.setVisibility(View.VISIBLE);
                        ShowRecordsDAY(ShowTasksInDate,0);

                        cal_date.setVisibility(View.VISIBLE);
                        cal_right.setVisibility(View.VISIBLE);
                        cal_left.setVisibility(View.VISIBLE);

                        MainActivity.ListView_MODE = 0;

                        Toast.makeText(getActivity(), ShowTasksInDate, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

        if ((NumberOfRecords == 0)&&(period != 2))
        {
            // If no records this day or month, I show NO TASKS message
            lw_calendar.setVisibility(View.INVISIBLE);

            switch (MainActivity.prf_Language)
            {
                case 0:calendar_nothingfound.setText(R.string.no_tasks_found);break;
                case 1:calendar_nothingfound.setText(R.string.ru_no_tasks_found);break;
            }

            calendar_nothingfound.setVisibility(View.VISIBLE);

        }
        if ((NumberOfRecords != 0)&&(period != 2))
        {
            // If there are any records this day or month found, I show them in the list
            lw_calendar.setVisibility(View.VISIBLE);
            calendar_nothingfound.setVisibility(View.INVISIBLE);
        }
        if (period == 2)
        {
            //Here I show calendar. Doesn't matter if there are records or not.
            //Calendar will be empty or with dates marked
            lw_calendar.setVisibility(View.INVISIBLE);
            custom_calendar.setVisibility(View.VISIBLE);
            calendar_nothingfound.setVisibility(View.INVISIBLE);
        }

    }


    private static String MonthToString(int Month){
        String Month_Name;

        switch (MainActivity.prf_Language)
        {
            case 0:
                switch (Month){
                    case 1: Month_Name = "JAN";  break;
                    case 2: Month_Name = "FEB";  break;
                    case 3: Month_Name = "MAR";  break;
                    case 4: Month_Name = "APR";  break;
                    case 5: Month_Name = "MAY";  break;
                    case 6: Month_Name = "JUN";  break;
                    case 7: Month_Name = "JUL";  break;
                    case 8: Month_Name = "AUG";  break;
                    case 9: Month_Name = "SEP";  break;
                    case 10: Month_Name = "OCT";  break;
                    case 11: Month_Name = "NOV";  break;
                    case 12: Month_Name = "DEC";  break;
                    default: Month_Name = "n/a";break;
                }
                break;

            case 1:
                switch (Month){
                    case 1: Month_Name = "ЯНВ";  break;
                    case 2: Month_Name = "ФЕВ";  break;
                    case 3: Month_Name = "МАР";  break;
                    case 4: Month_Name = "АПР";  break;
                    case 5: Month_Name = "МАЙ";  break;
                    case 6: Month_Name = "ИЮН";  break;
                    case 7: Month_Name = "ИЮЛ";  break;
                    case 8: Month_Name = "АВГ";  break;
                    case 9: Month_Name = "СЕН";  break;
                    case 10: Month_Name = "ОКТ";  break;
                    case 11: Month_Name = "НОЯ";  break;
                    case 12: Month_Name = "ДЕК";  break;
                    default: Month_Name = "404 Ошибка";break;
                }
                break;

            default:
                switch (Month){
                    case 1: Month_Name = "JAN";  break;
                    case 2: Month_Name = "FEB";  break;
                    case 3: Month_Name = "MAR";  break;
                    case 4: Month_Name = "APR";  break;
                    case 5: Month_Name = "MAY";  break;
                    case 6: Month_Name = "JUN";  break;
                    case 7: Month_Name = "JUL";  break;
                    case 8: Month_Name = "AUG";  break;
                    case 9: Month_Name = "SEP";  break;
                    case 10: Month_Name = "OCT";  break;
                    case 11: Month_Name = "NOV";  break;
                    case 12: Month_Name = "DEC";  break;
                    default: Month_Name = "n/a";break;
                }
                break;

        }
        return Month_Name;
    }




}
