package com.thepoisondevelopment.whattodo;

        import android.content.Intent;
        import android.content.res.Resources;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.pdf.PdfDocument;
        import android.media.MediaScannerConnection;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.util.Objects;

public class fragmentExport extends Fragment implements View.OnClickListener {


    private static final String TAG = "Export Fragment";


    TextView button_export, export_btn_CSV, export_btn_PDF, export_btn_MAIL, export_text_label, resolved_period_value;

    DBHelper dbHelper;
    Cursor data;
    String FileName = "" + DBHelper.getTodayDate_forExport()+ "_MyTODOs";

    String dbName, dbCreated, dbPlanned, dbDeadline, dbLabel, dbLabel_Color, dbCompleted;
    int dbID, dbPriority, dbStatus, dbTimeLogged, ID;


    boolean chk_priority_b = true;
    boolean chk_taskdescription_b = true;
    boolean chk_status_b = true;
    boolean chk_created_b = true;
    boolean chk_planned_b = true;
    boolean chk_deadline_b = true;
    boolean chk_timelogged_b = true;
    boolean chk_label_b = true;

    Button e_btn_todo, e_btn_planned, e_btn_wip, e_btn_blocked, e_btn_onhold, e_btn_done;

    boolean b_btn_todo = true;
    boolean b_btn_planned = true;
    boolean b_btn_wip = true;
    boolean b_btn_blocked = true;
    boolean b_btn_onhold = true;
    boolean b_btn_done = false;

    int export_state = 0; // 0 - CSV,    1 - pdf,   2 - email
    int setOptimization = 0;

    CheckBox chk_priority, chk_taskdescription, chk_status, chk_created, chk_planned, chk_deadline, chk_timelogged, chk_label;
    ImageView exp_toggle_optimized;
    TextView exp_label_optimized, export_additional_label;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_export, container, false);

        button_export = view.findViewById(R.id.btn_export);
        Button export_back = view.findViewById(R.id.export_back);

        export_btn_CSV = view.findViewById(R.id.export_btn_CSV);
        export_btn_PDF = view.findViewById(R.id.export_btn_PDF);
        export_btn_MAIL = view.findViewById(R.id.export_btn_MAIL);

        export_text_label = view.findViewById(R.id.export_text_label);

        chk_priority  = view.findViewById(R.id.chk_priority);
        chk_taskdescription = view.findViewById(R.id.chk_taskdescription);
        chk_status = view.findViewById(R.id.chk_status);
        chk_created = view.findViewById(R.id.chk_created);
        chk_planned = view.findViewById(R.id.chk_planned);
        chk_deadline = view.findViewById(R.id.chk_deadline);
        chk_timelogged = view.findViewById(R.id.chk_timelogged);
        chk_label = view.findViewById(R.id.chk_label);

        e_btn_todo = view.findViewById(R.id.e_btn_todo);
        e_btn_planned = view.findViewById(R.id.e_btn_planned);
        e_btn_wip = view.findViewById(R.id.e_btn_wip);
        e_btn_blocked = view.findViewById(R.id.e_btn_blocked);
        e_btn_onhold = view.findViewById(R.id.e_btn_onhold);
        e_btn_done = view.findViewById(R.id.e_btn_done);

        TextView export_caption = view.findViewById(R.id.export_caption);
        TextView export_fields_label = view.findViewById(R.id.export_fields_label);
        TextView export_statuses_label = view.findViewById(R.id.export_statuses_label);
        exp_toggle_optimized = view.findViewById(R.id.exp_toggle_optimized);
        exp_label_optimized = view.findViewById(R.id.exp_label_optimized);

        chk_priority.setOnClickListener(this);
        chk_taskdescription.setOnClickListener(this);
        chk_status.setOnClickListener(this);
        chk_created.setOnClickListener(this);
        chk_planned.setOnClickListener(this);
        chk_deadline.setOnClickListener(this);
        chk_timelogged.setOnClickListener(this);
        chk_label.setOnClickListener(this);

        exp_toggle_optimized.setEnabled(false);
        exp_label_optimized.setEnabled(false);

       switch (MainActivity.prf_Language)
        {
            case 0:
                export_caption.setText(R.string.export);
                export_fields_label.setText(R.string.fields_to_export);
                export_statuses_label.setText(R.string.export_statuses);
                chk_priority.setText(R.string.priority);
                chk_taskdescription.setText(R.string.description);
                chk_status.setText(R.string.status);
                chk_timelogged.setText(R.string.time_logged);
                chk_created.setText(R.string.created);
                chk_planned.setText(R.string.planned);
                chk_deadline.setText(R.string.deadline);
                chk_label.setText(R.string.label_);
                button_export.setText(R.string.btn_export);
                exp_label_optimized.setText(R.string.optimization_of_pdf_file);
                //export_additional_label.setText(R.string.additional);

                break;
            case 1:
                export_caption.setText(R.string.ru_export);
                export_fields_label.setText(R.string.ru_fields_to_export);
                export_statuses_label.setText(R.string.ru_export_statuses);
                chk_priority.setText(R.string.ru_priority);
                chk_taskdescription.setText(R.string.ru_description);
                chk_status.setText(R.string.ru_status);
                chk_timelogged.setText(R.string.ru_time_logged);
                chk_created.setText(R.string.ru_created);
                chk_planned.setText(R.string.ru_planned);
                chk_deadline.setText(R.string.ru_deadline);
                chk_label.setText(R.string.ru_label);
                button_export.setText(R.string.ru_btn_export);
                exp_label_optimized.setText(R.string.ru_optimization_of_pdf_file);
                //export_additional_label.setText(R.string.ru_additional);
                break;
        }


        export_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment targetFragment = new fragmentTodo();
                assert getFragmentManager() != null;

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        exp_label_optimized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (setOptimization == 0) {
                    setOptimization = 1;
                    exp_toggle_optimized.setBackgroundResource(R.drawable.toggle_on);

                } else if (setOptimization == 1) {
                    setOptimization = 0;
                    exp_toggle_optimized.setBackgroundResource(R.drawable.toggle_off);

                }
            }
        });

        exp_toggle_optimized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (setOptimization == 0) {
                    setOptimization = 1;
                    exp_toggle_optimized.setBackgroundResource(R.drawable.toggle_on);

                } else if (setOptimization == 1) {
                    setOptimization = 0;
                    exp_toggle_optimized.setBackgroundResource(R.drawable.toggle_off);

                }
            }
        });

        e_btn_todo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (b_btn_todo) {
                    e_btn_todo.setBackgroundResource(R.drawable.todo_icon_0todo);
                    b_btn_todo = false;
                }else {
                    e_btn_todo.setBackgroundResource(R.drawable.todo_icon_0todo_selected);
                    b_btn_todo = true;
                }
            }
        });

        e_btn_planned.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (b_btn_planned) {
                    e_btn_planned.setBackgroundResource(R.drawable.todo_icon_4planned);
                    b_btn_planned = false;
                }else {
                    e_btn_planned.setBackgroundResource(R.drawable.todo_icon_4planned_selected);
                    b_btn_planned = true;
                }
            }
        });

        e_btn_wip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (b_btn_wip) {
                    e_btn_wip.setBackgroundResource(R.drawable.todo_icon_1wip);
                    b_btn_wip = false;
                }else {
                    e_btn_wip.setBackgroundResource(R.drawable.todo_icon_1wip_selected);
                    b_btn_wip = true;
                }
            }
        });

        e_btn_blocked.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (b_btn_blocked) {
                    e_btn_blocked.setBackgroundResource(R.drawable.todo_icon_2blocked);
                    b_btn_blocked = false;
                }else {
                    e_btn_blocked.setBackgroundResource(R.drawable.todo_icon_2blocked_selected);
                    b_btn_blocked = true;
                }
            }
        });

        e_btn_onhold.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (b_btn_onhold) {
                    e_btn_onhold.setBackgroundResource(R.drawable.todo_icon_5onhold);
                    b_btn_onhold = false;
                }else {
                    e_btn_onhold.setBackgroundResource(R.drawable.todo_icon_5onhold_selected);
                    b_btn_onhold = true;
                }
            }
        });


        e_btn_done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (b_btn_done) {
                    e_btn_done.setBackgroundResource(R.drawable.todo_icon_3done);
                    b_btn_done = false;
                }else {
                    e_btn_done.setBackgroundResource(R.drawable.todo_icon_3done_selected);
                    b_btn_done = true;
                }
            }
        });


        export_btn_CSV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                export_state = 0;
                export_btn_CSV.setBackgroundResource(R.drawable.export_csv_selected);
                export_btn_PDF.setBackgroundResource(R.drawable.export_pdf);
                export_btn_MAIL.setBackgroundResource(R.drawable.export_email);
                export_text_label.setText("Export to: CSV");

                chk_priority.setEnabled(true);chk_priority.setButtonDrawable(R.drawable.selector_checkbox);
                chk_taskdescription.setEnabled(true);chk_taskdescription.setButtonDrawable(R.drawable.selector_checkbox);
                chk_status.setEnabled(true);chk_status.setButtonDrawable(R.drawable.selector_checkbox);
                chk_created.setEnabled(true);chk_created.setButtonDrawable(R.drawable.selector_checkbox);
                chk_planned.setEnabled(true);chk_planned.setButtonDrawable(R.drawable.selector_checkbox);
                chk_deadline.setEnabled(true);chk_deadline.setButtonDrawable(R.drawable.selector_checkbox);
                chk_timelogged.setEnabled(true);chk_timelogged.setButtonDrawable(R.drawable.selector_checkbox);
                chk_label.setEnabled(true);chk_label.setButtonDrawable(R.drawable.selector_checkbox);

                setOptimization = 0;
                exp_toggle_optimized.setBackgroundResource(R.drawable.toggle_off);
                exp_toggle_optimized.setEnabled(false);
                exp_label_optimized.setEnabled(false);

            }
        });



        export_btn_PDF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                export_state = 1;
                export_btn_CSV.setBackgroundResource(R.drawable.export_csv);
                export_btn_PDF.setBackgroundResource(R.drawable.export_pdf_selected);
                export_btn_MAIL.setBackgroundResource(R.drawable.export_email);
                export_text_label.setText("Export to: PDF");

                chk_priority.setEnabled(false);chk_priority.setButtonDrawable(R.drawable.chk_disable);
                chk_taskdescription.setEnabled(false);chk_taskdescription.setButtonDrawable(R.drawable.chk_disable);
                chk_status.setEnabled(false);chk_status.setButtonDrawable(R.drawable.chk_disable);
                chk_created.setEnabled(false);chk_created.setButtonDrawable(R.drawable.chk_disable);
                chk_planned.setEnabled(false);chk_planned.setButtonDrawable(R.drawable.chk_disable);
                chk_deadline.setEnabled(false);chk_deadline.setButtonDrawable(R.drawable.chk_disable);
                chk_timelogged.setEnabled(false);chk_timelogged.setButtonDrawable(R.drawable.chk_disable);
                chk_label.setEnabled(false);chk_label.setButtonDrawable(R.drawable.chk_disable);

                exp_toggle_optimized.setEnabled(true);
                exp_label_optimized.setEnabled(true);
            }
        });

        export_btn_MAIL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                export_state = 2;
                export_btn_CSV.setBackgroundResource(R.drawable.export_csv);
                export_btn_PDF.setBackgroundResource(R.drawable.export_pdf);
                export_btn_MAIL.setBackgroundResource(R.drawable.export_email_selected);
                export_text_label.setText("Export to: Email (text)");

                chk_priority.setEnabled(true);chk_priority.setButtonDrawable(R.drawable.selector_checkbox);
                chk_taskdescription.setEnabled(true);chk_taskdescription.setButtonDrawable(R.drawable.selector_checkbox);
                chk_status.setEnabled(true);chk_status.setButtonDrawable(R.drawable.selector_checkbox);
                chk_created.setEnabled(true);chk_created.setButtonDrawable(R.drawable.selector_checkbox);
                chk_planned.setEnabled(true);chk_planned.setButtonDrawable(R.drawable.selector_checkbox);
                chk_deadline.setEnabled(true);chk_deadline.setButtonDrawable(R.drawable.selector_checkbox);
                chk_timelogged.setEnabled(true);chk_timelogged.setButtonDrawable(R.drawable.selector_checkbox);
                chk_label.setEnabled(true);chk_label.setButtonDrawable(R.drawable.selector_checkbox);

                setOptimization = 0;
                exp_toggle_optimized.setBackgroundResource(R.drawable.toggle_off);
                exp_toggle_optimized.setEnabled(false);
                exp_label_optimized.setEnabled(false);

            }
        });



        // OK CANCEL buttons
        button_export.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // EXPORT
                switch (export_state) {
                    case 0: export_CSV();break;
                    case 1: export_PDF();break;
                    case 2: export_EMAIL();break;
                }

            }
        });

        return view;
    }


    public void export_EMAIL(){

        String subS = "TODOs Task list export";
        ID = 0;

        String GetData = "";
        GetData = GetData + getDataByStatus(0);
        GetData = GetData + getDataByStatus(1);
        GetData = GetData + getDataByStatus(4);
        GetData = GetData + getDataByStatus(2);

        // Do not export tasks that are completed. Othervise it doesn't make sense.
        GetData = GetData + "\n--------------------------------------\n\n";
        GetData = GetData + getDataByStatus(5);

        //TODO export tasks that are blocked or onhold

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
        email.putExtra(Intent.EXTRA_SUBJECT, "WhatTODO: Task Export List" + DBHelper.getTodayDate());
        email.putExtra(Intent.EXTRA_TEXT, GetData);
        startActivity(Intent.createChooser(email, subS));

    }


    String strPrio, strStatus;

    public void export_PDF(){

        dbHelper = new DBHelper(getActivity());
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
                xTickBox = 755,
                yCaptionLine = 60,
                yRecordLine;
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
            canvas.drawText("WhatTODO tasks list", 30, 20, paint);
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

            for (int _y = 0; _y<=5;_y++)
            {  // I go through each status and put it to data base.
                switch (_y){
                    case 0: if (b_btn_blocked) data = dbHelper.getDataOfStatus(2);break;
                    case 1: if (b_btn_wip) data = dbHelper.getDataOfStatus(1);break;
                    case 2: if (b_btn_planned) data = dbHelper.getDataOfStatus(4);break;
                    case 3: if (b_btn_todo) data = dbHelper.getDataOfStatus(0);break;
                    case 4: if (b_btn_onhold) data = dbHelper.getDataOfStatus(5);break;
                    case 5: if (b_btn_done) data = dbHelper.getDataOfStatus(3);break;
                    //case 6: if (b_btn_rejected) data = dbHelper.getDataOfStatus(6);break;
                }

                while (data.moveToNext()) {
                    ID++;
                    PageYID++;
                    yRecordLine = (PageYID * 60) + ExtraY  + 20 ;

                    dbID = data.getInt(data.getColumnIndex(DBHelper.KEY_ID));
                    dbPriority = data.getInt(data.getColumnIndex(DBHelper.KEY_PRIO));
                    dbName = data.getString(data.getColumnIndex(DBHelper.KEY_NAME));
                    dbName = dbName.replaceAll("\n"," ");
                    dbStatus = data.getInt(data.getColumnIndex(DBHelper.KEY_STATUS));
                    dbCreated = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_CREATED));
                    dbPlanned = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_PLANNED));
                    dbDeadline = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_DEADLINE));
                    dbCompleted = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_DONE));
                    dbTimeLogged = data.getInt(data.getColumnIndex(DBHelper.KEY_TIME_LOGGED));
                    dbLabel = data.getString(data.getColumnIndex(DBHelper.KEY_LABEL));
                    dbLabel_Color = data.getString(data.getColumnIndex(DBHelper.KEY_LABEL_COLOR));

                    paint.setStrokeWidth(1);
                    paint.setColor(Color.parseColor("#999999"));
                    canvas.drawLine(10,yRecordLine - 13,pageWidth - 10, yRecordLine - 13, paint);
//ID
                    canvas.drawText("ID: " + dbID, xPrio - 5, yRecordLine + 40, paint);  //this is real ID
                    //canvas.drawText("" + ID, xID, yRecordLine + 20, paint);  this is incremental ID

                    if (setOptimization == 0 ) {

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

                        switch (dbStatus) {
                            case 0:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_0todo);break;//
                            case 1:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_1wip);break;
                            case 2:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_2blocked);break;
                            case 3:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_3done);break;
                            case 4:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_4planned);break;//
                            case 5:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_5onhold);break;
                            case 6:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_6rejected);break;
                            default:  bitmap = BitmapFactory.decodeResource(mResources, R.drawable.todo_icon_0todo);break;
                        }
                        bitmap = Bitmap.createScaledBitmap(bitmap, PrioH, PrioH, true);
                        canvas.drawBitmap(bitmap, xStatus - 1, yRecordLine + 3, null);

                        bitmap = BitmapFactory.decodeResource(mResources, R.drawable.export_tickbox);
                        bitmap = Bitmap.createScaledBitmap(bitmap, PrioH, PrioH, true);
                        canvas.drawBitmap(bitmap, xTickBox+1, yRecordLine + 3, null);

                         // images
                    } else {
                         // text
                        switch (dbPriority) {
                            case 1:  canvas.drawText("VERY HIGH", xPrio - 7, yRecordLine + 10, paint);break;
                            case 2:  canvas.drawText("HIGH", xPrio + 10, yRecordLine + 10, paint);break;
                            case 3:  canvas.drawText("MEDIUM", xPrio, yRecordLine + 10, paint);break;
                            case 4:  canvas.drawText("LOW", xPrio + 10, yRecordLine + 10, paint);break;
                            case 5:  canvas.drawText("VERY LOW", xPrio - 7, yRecordLine + 10, paint);break;
                        }


                        switch (dbStatus) {
                            case 0:  canvas.drawText("TODO", xStatus + 15, yRecordLine + 10, paint);break;//
                            case 1:  canvas.drawText("WIP", xStatus + 18, yRecordLine + 10, paint);break;
                            case 2:  canvas.drawText("BLOCKED", xStatus + 3, yRecordLine + 10, paint);break;
                            case 3:  canvas.drawText("DONE", xStatus + 15, yRecordLine + 10, paint);break;
                            case 4:  canvas.drawText("PLANNED", xStatus + 3, yRecordLine + 10, paint);break;//
                            case 5:  canvas.drawText("ON HOLD", xStatus + 3, yRecordLine + 10, paint);break;
                            case 6:  canvas.drawText("REJECTED", xStatus, yRecordLine + 10, paint);break;
                            default:  canvas.drawText("NO DATA", xStatus, yRecordLine + 10, paint);break;
                        }

                    }


//LABEL
                    canvas.drawText("" + dbLabel, xLabel, yRecordLine + 17, paint);
                    paint.setStrokeWidth(3);

                    //TODO kostil. remove before releiese
                    if ((dbLabel_Color.equals("RED")||(dbLabel_Color.equals("ff0000")))) dbLabel_Color = "#ff0000";paint.setColor(Color.parseColor(dbLabel_Color));
                    canvas.drawLine(xLabel - 1,yRecordLine + 22,xLabel + 84, yRecordLine + 22, paint);
                    canvas.drawLine(xLabel - 1,yRecordLine+2,xLabel + 84, yRecordLine+2, paint);
                    paint.setColor(Color.parseColor("#000000"));
                    //TODO count number of characters and show long or short line


//CREATED, PLANNED. LOGGED
                    canvas.drawText("Created:  " + dbCreated, xCreated, yRecordLine, paint);
                    if (!dbDeadline.equals("Not set")) canvas.drawText("Deadline: " + dbDeadline, xCreated, yRecordLine + 40, paint);

                    if (dbStatus == 3) {
                        canvas.drawText("Completed:  " + dbCompleted, xCreated, yRecordLine + 20, paint);
                    } else  {
                        if (!dbPlanned.equals("Not set")) canvas.drawText("Planned:  " + dbPlanned, xCreated, yRecordLine + 20, paint);
                    }

//TIME LOGGED
                    if (dbTimeLogged == 0) canvas.drawText("-", xTimeLog, yRecordLine + 20, paint);
                    else {
                        int hours = dbTimeLogged / 60; int mins = dbTimeLogged % 60;
                        canvas.drawText("" + hours + "h " + mins + "m", xTimeLog, yRecordLine + 20, paint);}




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



    public void export_CSV(){

        // EXPORT TO CSV
        int ID = 0;
        dbHelper = new DBHelper(getActivity());

        //Creating a names for the table.
        String DataFromBase = "ID;";
        if (chk_priority_b) DataFromBase = DataFromBase + "Priority;";
        if (chk_label_b) DataFromBase = DataFromBase + "Label;";
        if (chk_taskdescription_b) DataFromBase = DataFromBase + "Description;";
        if (chk_status_b) DataFromBase = DataFromBase + "Status;";
        if (chk_created_b) DataFromBase = DataFromBase + "Created;";
        if (chk_planned_b) DataFromBase = DataFromBase + "Planned;";
        if (chk_deadline_b) DataFromBase = DataFromBase + "Deadline;";

        if (b_btn_done) DataFromBase = DataFromBase + "Completed on;";

        if (chk_timelogged_b) DataFromBase = DataFromBase + "Time logged";


        try {
            // Creates a file in the primary external storage space of the current application.
            File FILE_NAME = new File(Objects.requireNonNull(getActivity()).getExternalFilesDir(null), FileName + ".csv");
            FILE_NAME.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false /*append*/));

            writer.write(DataFromBase);
            writer.newLine();

        if ((!b_btn_todo)&&(!b_btn_planned)&&(!b_btn_wip)&&(!b_btn_blocked)&&(!b_btn_onhold)&&(!b_btn_done))
            data = dbHelper.getAllData();
        else //TODO,  double check this piece. When nothing is selected - all records should be exported

            for (int _y = 0; _y<=5;_y++)
            {  // I go through each status and put it to data base.
                switch (_y){
                    case 0: if (b_btn_blocked) data = dbHelper.getDataOfStatus(2);break;
                    case 1: if (b_btn_wip) data = dbHelper.getDataOfStatus(1);break;
                    case 2: if (b_btn_planned) data = dbHelper.getDataOfStatus(4);break;
                    case 3: if (b_btn_done) data = dbHelper.getDataOfStatus(3);break;
                    case 4: if (b_btn_todo) data = dbHelper.getDataOfStatus(0);break;
                    case 5: if (b_btn_onhold) data = dbHelper.getDataOfStatus(5);break;
                    // case 6: if (b_btn_rejected) data = dbHelper.getDataOfStatus(6);break;
                }

                // HERE I add all data from SQLite data base
                while (data.moveToNext()) {
                    ID++;
                    dbPriority = data.getInt(data.getColumnIndex("PRIO"));
                    dbName = data.getString(data.getColumnIndex("NAME"));
                    dbStatus = data.getInt(data.getColumnIndex("STATUS"));
                    dbCreated = data.getString(data.getColumnIndex("TASK_CREATED"));
                    dbPlanned = data.getString(data.getColumnIndex("TASK_PLANNED"));
                    dbDeadline = data.getString(data.getColumnIndex("TASK_DEADLINE"));
                    dbTimeLogged = data.getInt(data.getColumnIndex("LOGGED"));
                    dbLabel = data.getString(data.getColumnIndex("LABEL"));
                    dbLabel_Color = data.getString(data.getColumnIndex("LABEL_COLOR"));
                    dbCompleted = data.getString(data.getColumnIndex("TASK_DONE"));

                    switch (dbPriority) {
                        case 1:  strPrio = "Very High";break;
                        case 2:  strPrio = "High";break;
                        case 3:  strPrio = "Normal";break;
                        case 4:  strPrio = "Low";break;
                        case 5:  strPrio = "Very Low";break;
                        default: strPrio = "Normal";break;
                    }

                    switch (dbStatus) {
                        case 0:  strStatus = "TODO";break;
                        case 1:  strStatus = "WIP";break;
                        case 2:  strStatus = "BLOCKED";break;
                        case 3:  strStatus = "DONE";break;
                        case 4:  strStatus = "PLANNED";break;
                        case 5:  strStatus = "ON HOLD";break;
                        case 6:  strStatus = "REJECTED";break;
                        default: strStatus = "-";break;
                    }

                    dbName = dbName.replaceAll("\n"," ");

                    DataFromBase = "" + ID + ";";

                    if (chk_priority_b) DataFromBase = DataFromBase + strPrio + ";";
                    if (chk_label_b) DataFromBase = DataFromBase + dbLabel + ";";
                    if (chk_taskdescription_b) DataFromBase = DataFromBase + "\"" + dbName + "\";";
                    if (chk_status_b) DataFromBase = DataFromBase + strStatus + ";";
                    if (chk_created_b) DataFromBase = DataFromBase + dbCreated + ";";
                    if (chk_planned_b) {
                        if (dbPlanned.equals("Not set"))
                            DataFromBase = DataFromBase + ";";else
                            DataFromBase = DataFromBase + dbPlanned + ";";
                    }

                    if (chk_deadline_b) {
                        if (dbDeadline.equals("Not set"))
                            DataFromBase = DataFromBase + ";";else
                            DataFromBase = DataFromBase + dbDeadline + ";";
                    }

                    if (b_btn_done) {

                        if (dbCompleted == null)
                            dbCompleted = "";
                        DataFromBase = DataFromBase + dbCompleted + ";";

                    }

                    if (chk_timelogged_b){
                        if (dbTimeLogged == 0)  DataFromBase = DataFromBase + "";
                        else {
                            int hours = dbTimeLogged / 60;
                            int mins = dbTimeLogged % 60;
                            DataFromBase = DataFromBase + hours + "h " + mins + "m";
                        }
                    }
                    writer.write(DataFromBase);
                    writer.newLine();
                }
            }

            writer.close();
            dbHelper.close();

            switch (MainActivity.prf_Language){

                case 0: Toast.makeText(getActivity(), "CSV saved to " + FILE_NAME, Toast.LENGTH_LONG).show();break;
                case 1: Toast.makeText(getActivity(), "CSV сохранен в " + FILE_NAME, Toast.LENGTH_LONG).show();break;
                default: Toast.makeText(getActivity(), "CSV saved to " + FILE_NAME, Toast.LENGTH_LONG).show(); break;
            }

            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{FILE_NAME.toString()}, null, null);
        } catch (IOException e) { Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file."); }

    }





    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chk_priority:
                if (chk_priority.isChecked()) chk_priority_b = true;
                else chk_priority_b = false;
                break;
            case R.id.chk_taskdescription:
                if (chk_taskdescription.isChecked()) chk_taskdescription_b = true;
                else chk_taskdescription_b = false;
                break;
            case R.id.chk_status:
                if (chk_status.isChecked()) chk_status_b = true;
                else chk_status_b = false;
                break;
            case R.id.chk_created:
                if (chk_created.isChecked()) chk_created_b = true;
                else chk_created_b = false;
                break;
            case R.id.chk_planned:
                if (chk_planned.isChecked()) chk_planned_b = true;
                else chk_planned_b = false;
                break;
            case R.id.chk_deadline:
                if (chk_deadline.isChecked()) chk_deadline_b = true;
                else chk_deadline_b = false;
                break;
            case R.id.chk_timelogged:
                if (chk_timelogged.isChecked()) chk_timelogged_b = true;
                else chk_timelogged_b = false;
                break;
            case R.id.chk_label:
                if (chk_label.isChecked()) chk_label_b = true;
                else chk_label_b = false;
                break;

        }
    }


    public String getDataByStatus(int _status) {

        String DataFromBase = "";
        String strPrio, strStatus;

        dbHelper = new DBHelper(getActivity());
        data = dbHelper.getDataOfStatus(_status);

        // HERE I add all data from SQLite data base
        while (data.moveToNext()) {
            ID++;
            dbPriority = data.getInt(data.getColumnIndex("PRIO"));
            dbName = data.getString(data.getColumnIndex("NAME"));
            dbStatus = data.getInt(data.getColumnIndex("STATUS"));

            switch (dbPriority) {
                case 1:  strPrio = "Very High";break;
                case 2:  strPrio = "High";break;
                case 3:  strPrio = "Normal";break;
                case 4:  strPrio = "Low";break;
                case 5:  strPrio = "Very Low";break;
                default: strPrio = "Normal";break;
            }

            switch (dbStatus) {
                case 0:  strStatus = "TODO";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | [ ]\n";break;
                case 1:  strStatus = "WIP";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | [ ]\n";break;
                case 2:  strStatus = "BLOCKED";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | [ ]\n";break;
                case 3:  strStatus = "DONE";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | [✓]\n";break;
                case 4:  strStatus = "PLANNED";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | [ ]\n";break;
                case 5:  strStatus = "ON HOLD";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | X\n";break;
                case 6:  strStatus = "REJECTED";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | X\n";break;

                default: strStatus = "TODO";
                    DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | [ ]\n";break;
            }

            //DataFromBase = DataFromBase + "" + ID + ": " + strPrio + " | " + dbLabel + " | " + dbName + " | " + strStatus + " | [ ]\n";

        }
        dbHelper.close();

        return DataFromBase;

    }

}
