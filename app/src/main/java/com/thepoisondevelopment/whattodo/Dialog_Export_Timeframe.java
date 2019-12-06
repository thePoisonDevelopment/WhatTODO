package com.thepoisondevelopment.whattodo;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class Dialog_Export_Timeframe extends DialogFragment implements AdapterView.OnItemSelectedListener{
    //MyCustomDialog
    private static final String TAG = "export timeframe";

    TextView radio_allrecords, radio_currentmonth, radio_exportspecific, label_optimized;
    ImageView checkbox_allrecords, checkbox_currentmonth, checkbox_exportspecific, toggle_optimized;

    String FileName_Total = "" + DBHelper.getTodayDate_forExport()+ "_ResolvedReport_for_";

    String dbName, dbCreated, dbPlanned, dbDeadline, dbLabel, dbLabel_Color, dbDone;
    int dbID, dbPriority, dbStatus, dbTimeLogged, ID;
    DBHelper dbHelper;
    Cursor data;

    Spinner spinner_month, spinner_year;

    int TimeFrame = 1;
    int setOptimization = 0;
    int tmp_Month, tmp_Year;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_export_timeframe, container, false);

        TextView button_cancel = view.findViewById(R.id.button_cancel);
        TextView button_exportR = view.findViewById(R.id.button_set_timeframe);

        TextView caption_savecsv = view.findViewById(R.id.caption_savecsv);
        label_optimized = view.findViewById(R.id.label_optimized);
        toggle_optimized = view.findViewById(R.id.toggle_optimized);

        radio_allrecords = view.findViewById(R.id.radio_allrecords);
        radio_currentmonth = view.findViewById(R.id.radio_currentmonth);
        radio_exportspecific = view.findViewById(R.id.radio_exportspecific);

        checkbox_allrecords = view.findViewById(R.id.checkbox_allrecords);
        checkbox_currentmonth = view.findViewById(R.id.checkbox_currentmonth);
        checkbox_exportspecific = view.findViewById(R.id.checkbox_exportspecific);

        checkbox_allrecords.setBackgroundResource(R.drawable.radio2_true);

        switch (MainActivity.prf_Language)
        {
            case 0:
                caption_savecsv.setText(R.string.set_export_time_period);
                radio_allrecords.setText(R.string.export_all_records);
                radio_currentmonth.setText(R.string.export_current_month);
                radio_exportspecific.setText(R.string.export_specific_month);
                label_optimized.setText(R.string.optimization_of_pdf_file);

                break;
            case 1:
                caption_savecsv.setText(R.string.ru_set_export_time_period);
                radio_allrecords.setText(R.string.ru_export_all_records);
                radio_currentmonth.setText(R.string.ru_export_current_month);
                radio_exportspecific.setText(R.string.ru_export_specific_month);
                label_optimized.setText(R.string.ru_optimization_of_pdf_file);

                break;
        }


        spinner_month = view.findViewById(R.id.spinner_month);
        ArrayAdapter<CharSequence> adapter_month = ArrayAdapter.createFromResource  (this.getActivity(), R.array.spinner_month_array, android.R.layout.simple_spinner_dropdown_item);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setOnItemSelectedListener(this);
        spinner_month.setAdapter(adapter_month);

        spinner_year = view.findViewById(R.id.spinner_year);
        ArrayAdapter<CharSequence> adapter_year = ArrayAdapter.createFromResource  (this.getActivity(), R.array.spinner_year_array, android.R.layout.simple_spinner_dropdown_item);
        adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setOnItemSelectedListener(this);
        spinner_year.setAdapter(adapter_year);

        spinner_month.setEnabled(false);
        spinner_year.setEnabled(false);


        label_optimized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (setOptimization == 0) {
                    setOptimization = 1;
                    toggle_optimized.setBackgroundResource(R.drawable.toggle_on);

                } else if (setOptimization == 1) {
                    setOptimization = 0;
                    toggle_optimized.setBackgroundResource(R.drawable.toggle_off);

                }
            }
        });


        toggle_optimized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (setOptimization == 0) {
                    setOptimization = 1;
                    toggle_optimized.setBackgroundResource(R.drawable.toggle_on);

                } else if (setOptimization == 1) {
                    setOptimization = 0;
                    toggle_optimized.setBackgroundResource(R.drawable.toggle_off);

                }
            }
        });


        radio_allrecords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimeFrame = 1;
                checkbox_allrecords.setBackgroundResource(R.drawable.radio2_true);
                checkbox_currentmonth.setBackgroundResource(R.drawable.radio2_false);
                checkbox_exportspecific.setBackgroundResource(R.drawable.radio2_false);

                spinner_month.setEnabled(false);
                spinner_year.setEnabled(false);
            }
        });

        checkbox_allrecords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimeFrame = 1;
                checkbox_allrecords.setBackgroundResource(R.drawable.radio2_true);
                checkbox_currentmonth.setBackgroundResource(R.drawable.radio2_false);
                checkbox_exportspecific.setBackgroundResource(R.drawable.radio2_false);
                spinner_month.setEnabled(false);
                spinner_year.setEnabled(false);
            }
        });

        radio_currentmonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimeFrame = 2;
                checkbox_allrecords.setBackgroundResource(R.drawable.radio2_false);
                checkbox_currentmonth.setBackgroundResource(R.drawable.radio2_true);
                checkbox_exportspecific.setBackgroundResource(R.drawable.radio2_false);
                spinner_month.setEnabled(false);
                spinner_year.setEnabled(false);
            }
        });

        checkbox_currentmonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimeFrame = 2;
                checkbox_allrecords.setBackgroundResource(R.drawable.radio2_false);
                checkbox_currentmonth.setBackgroundResource(R.drawable.radio2_true);
                checkbox_exportspecific.setBackgroundResource(R.drawable.radio2_false);
                spinner_month.setEnabled(false);
                spinner_year.setEnabled(false);
            }
        });


        radio_exportspecific.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimeFrame = 3;
                checkbox_allrecords.setBackgroundResource(R.drawable.radio2_false);
                checkbox_currentmonth.setBackgroundResource(R.drawable.radio2_false);
                checkbox_exportspecific.setBackgroundResource(R.drawable.radio2_true);
                spinner_month.setEnabled(true);
                spinner_year.setEnabled(true);
            }
        });

        checkbox_exportspecific.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimeFrame = 3;
                checkbox_allrecords.setBackgroundResource(R.drawable.radio2_false);
                checkbox_currentmonth.setBackgroundResource(R.drawable.radio2_false);
                checkbox_exportspecific.setBackgroundResource(R.drawable.radio2_true);
                spinner_month.setEnabled(true);
                spinner_year.setEnabled(true);
            }
        });

        // OK CANCEL buttons
        button_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                getDialog().dismiss();
            }
        });


        button_exportR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                MainActivity.TimeFrame_Month = tmp_Month;
                MainActivity.TimeFrame_Year = tmp_Year;

                export_all_completed_PDF(TimeFrame);

                getDialog().dismiss();
            }
        });


        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_month: tmp_Month = position + 1;

                break;
            case R.id.spinner_year: tmp_Year = position + 2019;

                break;
        }


    };

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void export_all_completed_PDF(int _timeframe){


        Calendar TodaysDate = Calendar.getInstance();
        String TodaysDate_MONTH, DBDateQuerry;
        int TodaysDate_MONTHtmp, TodaysDate_year;
        TodaysDate_MONTHtmp = TodaysDate.get(Calendar.MONTH) + 1;
        if (TodaysDate_MONTHtmp < 10) TodaysDate_MONTH = "0" + TodaysDate_MONTHtmp; else TodaysDate_MONTH = "" + TodaysDate_MONTHtmp;
        TodaysDate_year = TodaysDate.get(Calendar.YEAR);

        dbHelper = new DBHelper(getActivity());

        switch (_timeframe) {
            case 1:
                DBDateQuerry = "All Completed";
                data = dbHelper.getDataOfStatus(3);
                break;

            case 2:
                DBDateQuerry = TodaysDate_MONTH + "." + TodaysDate_year;
                data = dbHelper.getSpecificMonthCompleted(DBDateQuerry);
                DBDateQuerry = DBHelper.ConvertMonthToString(TodaysDate_MONTHtmp) + tmp_Year;
                break;

            case 3:
                DBDateQuerry = tmp_Month + "." + tmp_Year;
                data = dbHelper.getSpecificMonthCompleted(DBDateQuerry);
                DBDateQuerry = DBHelper.ConvertMonthToString(tmp_Month) + tmp_Year;
                break;

            default:
                DBDateQuerry = "All Completed";
                data = dbHelper.getDataOfStatus(3);
                break;
        }



        // crate a page description
        int PrioW = 190, PrioH = 250;
        int pageWidth = 800, pageHeight = 1000;
        ID = 0;

        int
                xPrio = 20,
                xLabel = 85,
                xDescription = 185,
                xCreated = 520,
                xTimeLog = 665,
                xStatus = 718,
                yCaptionLine = 60,
                yRecordLine;

        int MaxLineChars = 70;  //total 256 characters, 40+, 80+, 120+, 160+, 200, 240,
        int separatorX = 10;

        Bitmap bitmap;

        try {

            File FILE_NAME = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir(null), FileName_Total + DBDateQuerry + ".pdf");

            FILE_NAME.createNewFile();
            FileOutputStream fOut = new FileOutputStream(FILE_NAME, false);

            PdfDocument document = new PdfDocument();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            Resources mResources = getResources();

            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            canvas.setDensity(32);
            Paint paint = new Paint();

            //HEADER
            paint.setColor(Color.parseColor(MainActivity.prf_ColorTheme));
            canvas.drawRect(0,0,pageWidth, 30, paint);

            paint.setColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));
            paint.setStrokeWidth(3);
            canvas.drawLine(0,31,pageWidth, 31, paint);
            paint.setStrokeWidth(1);

            paint.setColor(Color.WHITE);
            canvas.drawText("WhatTODO: Total report of resolved tasks for " + DBDateQuerry, 30, 20, paint);
            canvas.drawText("thePoison Development", 600, 20, paint);

            // HERE I add all data from SQLite data base
            int ExtraY = 0;
            int PageYID = 0;
            int PageNumber = 1;


            while (data.moveToNext()) {
                ID++;
                PageYID++;
                yRecordLine = (PageYID * 60) + ExtraY  + 20 ;

                dbID = data.getInt(data.getColumnIndex("_id"));
                dbPriority = data.getInt(data.getColumnIndex("PRIO"));
                dbName = data.getString(data.getColumnIndex("NAME"));
                dbName = dbName.replaceAll("\n"," ");
                dbStatus = data.getInt(data.getColumnIndex("STATUS"));
                dbCreated = data.getString(data.getColumnIndex("TASK_CREATED"));
                dbPlanned = data.getString(data.getColumnIndex("TASK_PLANNED"));
                dbDone = data.getString(data.getColumnIndex("TASK_DONE"));
                dbDeadline = data.getString(data.getColumnIndex("TASK_DEADLINE"));
                dbTimeLogged = data.getInt(data.getColumnIndex("LOGGED"));
                dbLabel = data.getString(data.getColumnIndex("LABEL"));
                dbLabel_Color = data.getString(data.getColumnIndex("LABEL_COLOR"));

                paint.setStrokeWidth(1);
                paint.setColor(Color.parseColor("#999999"));
                canvas.drawLine(10,yRecordLine - 13,pageWidth - 10, yRecordLine - 13, paint);
//ID
                canvas.drawText("ID: " + dbID, xPrio - 5, yRecordLine + 40, paint);  //this is real ID


                if (setOptimization == 0) {

//TIME LOGGED
                    bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_3done);
                    bitmap = Bitmap.createScaledBitmap(bitmap, PrioH, PrioH, true);
                    canvas.drawBitmap(bitmap, xStatus + 20, yRecordLine - 8, null);

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

                } else {

                    switch (dbPriority) {
                        case 1:  canvas.drawText("VERY HIGH", xPrio - 7, yRecordLine + 10, paint);break;
                        case 2:  canvas.drawText("HIGH", xPrio + 10, yRecordLine + 10, paint);break;
                        case 3:  canvas.drawText("MEDIUM", xPrio, yRecordLine + 10, paint);break;
                        case 4:  canvas.drawText("LOW", xPrio + 10, yRecordLine + 10, paint);break;
                        case 5:  canvas.drawText("VERY LOW", xPrio - 7, yRecordLine + 10, paint);break;
                        }
                    canvas.drawText("DONE", xStatus + 15, yRecordLine + 15, paint);

                }




//LABEL
                canvas.drawText("" + dbLabel, xLabel, yRecordLine + 11, paint);
                paint.setStrokeWidth(3);

                //TODO kostil. remove before releiese
                if ((dbLabel_Color.equals("RED")||(dbLabel_Color.equals("ff0000")))) dbLabel_Color = "#ff0000";

                paint.setColor(Color.parseColor(dbLabel_Color));

                if (dbLabel.equals("")) {
                    canvas.drawRect(xLabel - 1,yRecordLine - 4,xLabel + 84, yRecordLine + 16, paint);
                } else {
                    canvas.drawLine(xLabel - 1,yRecordLine - 4,xLabel + 84, yRecordLine - 4, paint);
                    canvas.drawLine(xLabel - 1,yRecordLine + 16,xLabel + 84, yRecordLine + 16, paint);
                }

                canvas.drawLine(xLabel - 1,yRecordLine - 4,xLabel + 84, yRecordLine - 4, paint);
                canvas.drawLine(xLabel - 1,yRecordLine + 16,xLabel + 84, yRecordLine + 16, paint);
                paint.setColor(Color.parseColor("#000000"));
                //TODO count number of characters and show long or short line


//CREATED, PLANNED. LOGGED. COMPLETED
                canvas.drawText("Created:  " + dbCreated, xLabel, yRecordLine + 38, paint);
                if (!dbPlanned.equals("Not set")) canvas.drawText("Planned:  " + dbPlanned, xLabel + 150, yRecordLine + 38, paint);
                if (!dbDeadline.equals("Not set")) canvas.drawText("Deadline: " + dbDeadline, xLabel + 300, yRecordLine + 38, paint);
                canvas.drawText("Completed:  " + dbDone, xLabel + 470, yRecordLine + 38, paint);


                if (dbTimeLogged == 0) canvas.drawText("-", xStatus + 20, yRecordLine + 35, paint);
                else {
                    int hours = dbTimeLogged / 60; int mins = dbTimeLogged % 60;
                    canvas.drawText("" + hours + "h " + mins + "m", xStatus + 10, yRecordLine + 35, paint);}

//NAME and logic for separation onto 2 lines
                int dbNameTmp = dbName.length() - 1;
                //TODO. check this algorythm.. Why line is showing incorrectly.
                if ((dbNameTmp > MaxLineChars)&&(dbNameTmp <= MaxLineChars*2)) {
                    canvas.drawText("" + dbName.substring(0,MaxLineChars), xDescription, yRecordLine, paint);
                    canvas.drawText("" + dbName.substring(MaxLineChars, dbNameTmp), xDescription, yRecordLine + 20, paint);
                    ExtraY = 0;
                } else {
                    ExtraY = 0;
                    canvas.drawText("" + dbName, xDescription, yRecordLine, paint);
                }

//separation lines between columns

                paint.setStrokeWidth(1);
                canvas.drawLine(xPrio - separatorX,yRecordLine - 13, xPrio - separatorX, yRecordLine + 44 + ExtraY, paint);
                canvas.drawLine(xLabel - separatorX,yRecordLine - 13, xLabel - separatorX, yRecordLine + 44 + ExtraY, paint);
                canvas.drawLine(xDescription - separatorX,yRecordLine - 13, xDescription - separatorX, yRecordLine + 24 + ExtraY, paint);
                canvas.drawLine(xStatus - separatorX,yRecordLine - 13, xStatus - separatorX, yRecordLine + 44 + ExtraY, paint);
                canvas.drawLine(pageWidth - 10,yRecordLine - 13, pageWidth - 10, yRecordLine + 44 + ExtraY, paint);

                canvas.drawLine(xLabel - separatorX,yRecordLine + 24, xStatus - separatorX, yRecordLine + 24 + ExtraY, paint);
//BOTTOM RECORD LINE
                paint.setColor(Color.parseColor("#888888"));
                canvas.drawLine(10,yRecordLine + 44 + ExtraY,pageWidth - 10, yRecordLine + 44 + ExtraY, paint);

                if (PageYID >=15) {
// LOGIC FOR STARTING A NEW PAGE if records more than 15.
                    PageYID = 0;

                    canvas.drawText("- " + PageNumber + " -", 700, pageHeight - 15, paint);
                    PageNumber ++;
                    document.finishPage(page);
                    page = document.startPage(pageInfo);

//HEADER
                    paint.setColor(Color.parseColor(MainActivity.prf_ColorTheme));
                    canvas.drawRect(0,0,pageWidth, 30, paint);

                    paint.setColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));
                    paint.setStrokeWidth(3);
                    canvas.drawLine(0,31,pageWidth, 31, paint);
                    paint.setStrokeWidth(1);

                    paint.setColor(Color.WHITE);
                    canvas.drawText("WhatTODO tasks list", 30, 20, paint);
                    canvas.drawText("thePoison Development", 600, 20, paint);

                    paint.setColor(Color.BLACK);
                    canvas.drawText("ID", xPrio, yCaptionLine, paint); //+
                    canvas.drawText("Prio", xPrio - 4, yCaptionLine, paint); //+
                    canvas.drawText("Label", xLabel, yCaptionLine, paint); //+
                    canvas.drawText("Description", xDescription, yCaptionLine, paint); //+
                    canvas.drawText("Created", xCreated, yCaptionLine, paint);
                    canvas.drawText("Time", xTimeLog, yCaptionLine, paint);
                    canvas.drawText("Status", xStatus, yCaptionLine, paint);
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

        } catch (IOException e){
            Log.i("error",e.getLocalizedMessage());
        }
    }




}
