package com.thepoisondevelopment.whattodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.thepoisondevelopment.whattodo.MainActivity.prm_Deadline;
import static com.thepoisondevelopment.whattodo.MainActivity.prm_EditPriority;
import static com.thepoisondevelopment.whattodo.MainActivity.prm_Planned;

public class fragmentEdit extends Fragment implements
        Dialog_DataPicker.OnInputSelected,
        Dialog_Priority.OnInputSelected_P,
        Dialog_LabelColor.OnInputSelected_LC{

    @Override
    public void sendInput_P(int input) {

        switch (input){
            case 1: edit_prio.setBackgroundResource(R.drawable.todo_prio_1_selected);         break;
            case 2: edit_prio.setBackgroundResource(R.drawable.todo_prio_2_selected); break;
            case 3: edit_prio.setBackgroundResource(R.drawable.todo_prio_3_selected); break;
            case 4: edit_prio.setBackgroundResource(R.drawable.todo_prio_4_selected); break;
            case 5: edit_prio.setBackgroundResource(R.drawable.todo_prio_5_selected); break;
            default: edit_prio.setBackgroundResource(R.drawable.todo_prio_3_selected); break;
        }
    }

    public void sendInput_LC(String input) {

        switch (input){
            case "NONE": btn_edit_label_color.setBackgroundResource(R.drawable.label_color_none_selected);break;
            case "RED": btn_edit_label_color.setBackgroundResource(R.drawable.label_color_red_selected);break;
            case "ORANGE": btn_edit_label_color.setBackgroundResource(R.drawable.label_color_orange_selected);break;
            case "BLUE": btn_edit_label_color.setBackgroundResource(R.drawable.label_color_blue_selected);break;
            case "YELLOW": btn_edit_label_color.setBackgroundResource(R.drawable.label_color_yellow_selected);break;
            case "GREEN": btn_edit_label_color.setBackgroundResource(R.drawable.label_color_green_selected);break;
            case "PINK": btn_edit_label_color.setBackgroundResource(R.drawable.label_color_pink_selected);break;
        }
    }

    @Override
    public void sendInput(String input) {

            e_planned_txt.setText(prm_Planned);
            e_deadline_txt.setText(prm_Deadline);

        if (!prm_Planned.equals("Not set")) {
            RefreshAllStatuses();
            bdl_Status = 4;
            edit_bkgr.setBackgroundColor(Color.parseColor("#ff4ecb"));
            edit_btn_planned.setBackgroundResource(R.drawable.todo_icon_4planned_selected);
            edit_status_text.setText(R.string.tst_planned);
        }

    }


    private static final String TAG = "Edit Fragment";

    String bdl_Name, bdl_TaskCreated, bdl_TaskPlanned, bdl_TaskDeadline, bdl_Label, bdl_LabelColor, bdl_Completed;
    int bdl_ID, bdl_Prio, bdl_Status, bdl_Log;
    String bdl_MODE;

    Button edit_btn_todo, edit_btn_wip, edit_btn_blocked, edit_btn_done, edit_prio;
    Button edit_back, edit_btn_delete, edit_btn_reject, edit_btn_update,
            edit_btn_planned, edit_btn_onhold;
    TextView edit_status_text, e_planned_txt, e_deadline_txt, edit_created_label, edit_caption;
    View edit_bkgr, btn_edit_label_color, buttons_area;

    EditText edit_taskname, edit_logtime, edit_label_input;
    Button edit_stopwatch_btn;

    boolean stopwatch = false;
    Chronometer edit_stopwatch_values;
    private long pauseOffset;

    DBHelper dbHelper;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_edit, container, false);

        // implement theme
        edit_bkgr = view.findViewById(R.id.edit_bkgr);
        edit_bkgr.setBackgroundColor(Color.parseColor(MainActivity.prf_ColorTheme));

        View edit_bkgr_sub = view.findViewById(R.id.edit_bkgr_sub);
        edit_bkgr_sub.setBackgroundColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));

        edit_caption = view.findViewById(R.id.edit_caption);
        edit_status_text = view.findViewById(R.id.edit_status_text);

        View e_planned_viewbtn = view.findViewById(R.id.e_planned_viewbtn);
        TextView e_planned_lbl = view.findViewById(R.id.e_planned_lbl);
        e_planned_txt = view.findViewById(R.id.e_planned_txt);

        View e_deadline_viewbtn = view.findViewById(R.id.e_deadline_viewbtn);
        TextView e_deadline_lbl = view.findViewById(R.id.e_deadline_lbl);
        e_deadline_txt = view.findViewById(R.id.e_deadline_txt);
        buttons_area = view.findViewById(R.id.buttons_area);

        edit_taskname = view.findViewById(R.id.edit_taskname);

        edit_taskname.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        edit_taskname.requestFocus();

        edit_btn_todo = view.findViewById(R.id.edit_btn_todo);
        edit_btn_wip = view.findViewById(R.id.edit_btn_wip);
        edit_btn_blocked = view.findViewById(R.id.edit_btn_blocked);
        edit_btn_done = view.findViewById(R.id.edit_btn_done);
        edit_btn_planned = view.findViewById(R.id.edit_btn_planned);
        edit_btn_onhold = view.findViewById(R.id.edit_btn_onhold);
        edit_btn_reject = view.findViewById(R.id.edit_btn_reject);

        edit_created_label = view.findViewById(R.id.edit_created_label);

        TextView edit_logtime_label = view.findViewById(R.id.edit_logtime_label);
        edit_logtime = view.findViewById(R.id.edit_logtime);
        edit_prio = view.findViewById(R.id.edit_prio);

        edit_label_input = view.findViewById(R.id.edit_label_input);
        btn_edit_label_color = view.findViewById(R.id.btn_edit_label_color);


        edit_back = view.findViewById(R.id.edit_back);
        edit_btn_delete = view.findViewById(R.id.edit_btn_delete);
        edit_btn_update = view.findViewById(R.id.edit_btn_update);

        assert getArguments() != null;
        bdl_ID = getArguments().getInt(DBHelper.KEY_ID);
        bdl_Name = getArguments().getString(DBHelper.KEY_NAME);
        bdl_Prio = getArguments().getInt(DBHelper.KEY_PRIO);
        bdl_Status = getArguments().getInt(DBHelper.KEY_STATUS);
        bdl_Log = getArguments().getInt(DBHelper.KEY_TIME_LOGGED);
        bdl_TaskCreated = getArguments().getString(DBHelper.KEY_TASK_CREATED);
        bdl_TaskPlanned = getArguments().getString(DBHelper.KEY_TASK_PLANNED);
        bdl_TaskDeadline = getArguments().getString(DBHelper.KEY_TASK_DEADLINE);
        bdl_Label = getArguments().getString(DBHelper.KEY_LABEL);
        bdl_LabelColor = getArguments().getString(DBHelper.KEY_LABEL_COLOR);
        bdl_Completed = getArguments().getString(DBHelper.KEY_TASK_DONE);
        bdl_MODE = getArguments().getString("MODE");

        edit_label_input.setText("" + bdl_Label);

        // TODO Kastil, remove before release
        if (bdl_LabelColor.equals("ff0000")) bdl_LabelColor = "#ff0000";else
            if (bdl_LabelColor.equals("#ffffff")) btn_edit_label_color.setBackgroundResource(R.drawable.label_color_none_selected);else
                btn_edit_label_color.setBackgroundColor(Color.parseColor(bdl_LabelColor));

        MainActivity.prm_LabelColor = bdl_LabelColor;
        prm_Planned = bdl_TaskPlanned;
        prm_Deadline = bdl_TaskDeadline;

        TextView edit_timespent_label = view.findViewById(R.id.edit_timespent_label);


        edit_stopwatch_btn = view.findViewById(R.id.edit_stopwatch_btn);
        edit_stopwatch_values = view.findViewById(R.id.edit_stopwatch_values);
        //edit_stopwatch_values.setFormat("00:00:00");
        edit_stopwatch_values.setBase(SystemClock.elapsedRealtime());

        //edit_stopwatch_values.setText("00:00:00");
        edit_stopwatch_values.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                    long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    int h   = (int)(time /3600000);
                    int m = (int)(time - h*3600000)/60000;
                    int s= (int)(time - h*3600000- m*60000)/1000 ;
                    String t = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
                    chronometer.setText(t);
                    //TODO, in future make seconds as a grey color
            }
        });

        e_planned_txt.setText(bdl_TaskPlanned);
        e_deadline_txt.setText(bdl_TaskDeadline);

        switch (MainActivity.prf_Language)
        {
            case 0:
                edit_created_label.setText("Task Created: " + bdl_TaskCreated);
                if (bdl_Log == 0)  edit_timespent_label.setText("Time logged: N/A");
                else {
                    int hours = bdl_Log / 60;
                    int mins = bdl_Log % 60;
                    edit_timespent_label.setText("Time logged: " + hours + "h " + mins + "m");
                }
                break;
            case 1:
                edit_created_label.setText("Создана: " + bdl_TaskCreated);
                if (bdl_Log == 0)  edit_timespent_label.setText("Времени потрачено: N/A");
                else {
                    int hours = bdl_Log / 60;
                    int mins = bdl_Log % 60;
                    edit_timespent_label.setText("Времени потрачено: " + hours + "h " + mins + "m");
                }
                break;
        }


        switch (bdl_Prio) {
            case 1: edit_prio.setBackgroundResource(R.drawable.todo_prio_1_selected);break;
            case 2: edit_prio.setBackgroundResource(R.drawable.todo_prio_2_selected);break;
            case 3: edit_prio.setBackgroundResource(R.drawable.todo_prio_3_selected);break;
            case 4: edit_prio.setBackgroundResource(R.drawable.todo_prio_4_selected);break;
            case 5: edit_prio.setBackgroundResource(R.drawable.todo_prio_5_selected);break;
            default: edit_prio.setBackgroundResource(R.drawable.todo_prio_3_selected);break;
        }
        prm_EditPriority = bdl_Prio;

        switch (bdl_Status) {
            case 0:    //TODO
                if (MainActivity.prf_Language == 1) edit_status_text.setText(R.string.ru_tst_todo);else edit_status_text.setText(R.string.tst_todo);
                edit_btn_todo.setBackgroundResource(R.drawable.todo_icon_0todo_selected);
                edit_bkgr.setBackgroundColor(Color.parseColor("#4864dc"));
                break;
            case 1:   // WIP
                if (MainActivity.prf_Language == 1) edit_status_text.setText(R.string.ru_tst_wip);else edit_status_text.setText(R.string.tst_wip);
                edit_btn_wip.setBackgroundResource(R.drawable.todo_icon_1wip_selected);
                edit_bkgr.setBackgroundColor(Color.parseColor("#ffff5a"));
                edit_caption.setTextColor(Color.parseColor("#000000"));
                break;
            case 2:   //BLOCKED
                if (MainActivity.prf_Language == 1) edit_status_text.setText(R.string.ru_tst_blocked);else edit_status_text.setText(R.string.tst_blocked);
                edit_btn_blocked.setBackgroundResource(R.drawable.todo_icon_2blocked_selected);
                edit_bkgr.setBackgroundColor(Color.parseColor("#ff4e4e"));
                break;

            case 4:   //PLANNED
                if (MainActivity.prf_Language == 1) edit_status_text.setText(R.string.ru_tst_planned);else edit_status_text.setText(R.string.tst_planned);
                edit_btn_planned.setBackgroundResource(R.drawable.todo_icon_4planned_selected);
                edit_bkgr.setBackgroundColor(Color.parseColor("#ff4ecb"));break;
            case 5:   //ON HOLD
                if (MainActivity.prf_Language == 1) edit_status_text.setText(R.string.ru_tst_on_hold);else edit_status_text.setText(R.string.tst_on_hold);
                edit_btn_onhold.setBackgroundResource(R.drawable.todo_icon_5onhold_selected);
                edit_bkgr.setBackgroundColor(Color.parseColor("#757575"));break;
            case 6:   //
                if (MainActivity.prf_Language == 1) edit_status_text.setText(R.string.ru_tst_reject);else edit_status_text.setText(R.string.tst_reject);
                edit_btn_reject.setBackgroundResource(R.drawable.todo_icon_6rejected_selected);
                edit_bkgr.setBackgroundColor(Color.parseColor("#c61e1e"));break;

            default:
                if (MainActivity.prf_Language == 1) edit_status_text.setText(R.string.ru_tst_todo);else edit_status_text.setText(R.string.tst_todo);
                edit_btn_todo.setBackgroundResource(R.drawable.todo_icon_0todo_selected);
                edit_bkgr.setBackgroundColor(Color.parseColor("#0024ff"));break;
        }

        TextView edit_todoname_label = view.findViewById(R.id.edit_todoname_label);
        TextView edit_todoid_label = view.findViewById(R.id.edit_todoid_label);
        edit_todoid_label.setText("Task ID: " + bdl_ID);

        bdl_TaskCreated = getArguments().getString(DBHelper.KEY_TASK_CREATED);
        bdl_TaskPlanned = getArguments().getString(DBHelper.KEY_TASK_PLANNED);
        bdl_TaskDeadline = getArguments().getString(DBHelper.KEY_TASK_DEADLINE);

        edit_taskname.setText(bdl_Name);

        switch (MainActivity.prf_Language)
        {
            case 0:
                edit_caption.setText(R.string.caption_edit);
                edit_taskname.setHint(R.string.todo_name);
                edit_todoname_label.setText(R.string.todo_name_caption);
//                edit_status_text.setText(R.string.tst_todo);
                //edit_created.setText("Created: " + bdl_TaskCreated);
                e_planned_lbl.setText("Planned:");
                e_deadline_lbl.setText("Deadline:");
                if (bdl_Status !=3 ) edit_logtime_label.setText(R.string.log_time);

                break;
            case 1:
                edit_caption.setText(R.string.ru_caption_edit);
                edit_taskname.setHint(R.string.ru_todo_name);
                edit_todoname_label.setText(R.string.ru_todo_name_caption);
//                edit_status_text.setText(R.string.ru_tst_todo);
                //edit_created.setText("Создано: " + bdl_TaskCreated);
                e_planned_lbl.setText("План:");
                e_deadline_lbl.setText("Срок:");
                if (bdl_Status !=3 ) edit_logtime_label.setText(R.string.ru_log_time);
                break;
        }


        edit_stopwatch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (stopwatch) {   //we stop the stopwatch
                    edit_stopwatch_btn.setBackgroundResource(R.drawable.stopwatch);
                    edit_stopwatch_values.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - edit_stopwatch_values.getBase();
                    stopwatch = false;
                }else{    //we run the stopwatch
                    stopwatch = true;
                    edit_stopwatch_btn.setBackgroundResource(R.drawable.stopwatch_pressed);
                    edit_stopwatch_values.start();
                    edit_stopwatch_values.setBase(SystemClock.elapsedRealtime() - pauseOffset);

                    RefreshAllStatuses();
                    edit_btn_wip.setBackgroundResource(R.drawable.todo_icon_1wip_selected);
                    edit_caption.setTextColor(Color.parseColor("#000000"));
                    edit_bkgr.setBackgroundColor(Color.parseColor("#ffff5a"));
                    bdl_Status = 1;
                    edit_status_text.setText(R.string.tst_wip);
                }
            }
        });

        e_planned_viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("DialogParameter", 0);

                Dialog_DataPicker dialog = new Dialog_DataPicker();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(fragmentEdit.this, 1);
                dialog.setStyle(Dialog_DataPicker.STYLE_NO_TITLE, 0);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "Planned");

            }

        });

        btn_edit_label_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_LabelColor dialog = new Dialog_LabelColor();
                dialog.setTargetFragment(fragmentEdit.this, 1);
                dialog.setStyle(Dialog_LabelColor.STYLE_NO_TITLE, 0);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "Planned");

            }

        });



        e_deadline_viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("PlannedDeadline", 1);

                Dialog_DataPicker dialog = new Dialog_DataPicker();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(fragmentEdit.this, 1);
                dialog.setStyle(Dialog_DataPicker.STYLE_NO_TITLE, 0);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "Deadline");

            }

        });


        edit_prio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                Dialog_Priority dialog = new Dialog_Priority();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(fragmentEdit.this, 1);
                dialog.setStyle(Dialog_Priority.STYLE_NO_TITLE, 0);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "Edit Dialog");

            }

        });



        edit_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dbHelper = new DBHelper(getActivity());
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                if (bdl_Status == 7) { //Status 7 = delete record from data base,  or update the record

                    database.delete(DBHelper.TABLE_TODO,"_id=" + bdl_ID,null);

                    switch (MainActivity.prf_Language) {
                        case 0:Toast.makeText(getActivity(), "Record is deleted", Toast.LENGTH_SHORT).show();  break;
                        case 1:Toast.makeText(getActivity(), "Запись удалена", Toast.LENGTH_SHORT).show(); break;
                        default: Toast.makeText(getActivity(), "Record is deleted", Toast.LENGTH_SHORT).show();break;
                    }

                } else {
                    ContentValues contentValues = new ContentValues();

                    String inputName =  edit_taskname.getText().toString();
                    String inputTimelog = edit_logtime.getText().toString();
                    String inputLabel = edit_label_input.getText().toString();

                    int inputTimeInt = 0;
                    int elapsedMins = (int) (SystemClock.elapsedRealtime() - edit_stopwatch_values.getBase()) / 60000;

                    if (elapsedMins == 0) {
                        if (inputTimelog.equals("")) inputTimeInt = 0; else inputTimeInt = Integer.parseInt(inputTimelog);
                        contentValues.put(DBHelper.KEY_TIME_LOGGED, bdl_Log + inputTimeInt);
                    } else
                        contentValues.put(DBHelper.KEY_TIME_LOGGED, bdl_Log + elapsedMins);

                    if (inputName.equals("")) {inputName = "<Empty>"; }

                    contentValues.put(DBHelper.KEY_NAME,inputName);
                    contentValues.put(DBHelper.KEY_PRIO,prm_EditPriority);
                    contentValues.put(DBHelper.KEY_STATUS,bdl_Status);

                    if ((bdl_Status == 0)||(bdl_Status == 5)||(bdl_Status == 6))  MainActivity.prm_Planned = "Not set";
                    if ((bdl_Status == 6))  MainActivity.prm_Deadline = "Not set";

                    //TODO  looks like kastil !
                    if (!MainActivity.prm_Planned.equals("Not set")) contentValues.put(DBHelper.KEY_TASK_PLANNED, MainActivity.prm_Planned);
                    else contentValues.put(DBHelper.KEY_TASK_PLANNED, "Not set");

                    if (!MainActivity.prm_Deadline.equals("Not set")) contentValues.put(DBHelper.KEY_TASK_DEADLINE, MainActivity.prm_Deadline);
                    else  contentValues.put(DBHelper.KEY_TASK_DEADLINE, "Not set");

                    contentValues.put(DBHelper.KEY_LABEL, inputLabel);
                    contentValues.put(DBHelper.KEY_LABEL_COLOR, MainActivity.prm_LabelColor);
                    contentValues.put(DBHelper.KEY_TASK_DONE, DBHelper.getTodayDate());

                    database.update(DBHelper.TABLE_TODO, contentValues, "_id=" + bdl_ID,null);

                    switch (MainActivity.prf_Language) {
                        case 0:
                            Toast.makeText(getActivity(), "Record is updated", Toast.LENGTH_SHORT).show();  break;
                        case 1:Toast.makeText(getActivity(), "Запись обновлена", Toast.LENGTH_SHORT).show(); break;
                        default: Toast.makeText(getActivity(), "Record is updated", Toast.LENGTH_SHORT).show();break;
                    }
                }

                if (MainActivity.prf_VibraFeedback == 1) {

                    Vibrator vibra = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibra.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibra.vibrate(MainActivity.prf_VibraDuration);
                    }
                }

                Fragment targetFragment;

                if (bdl_MODE.equals("EDIT")) targetFragment = new fragmentTodo();
                else targetFragment = new fragmentCalendar();

                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }
        });


        edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                Fragment targetFragment;

                if (bdl_MODE.equals("EDIT")) targetFragment = new fragmentTodo();
                else targetFragment = new fragmentCalendar();

                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        edit_btn_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_todo.setBackgroundResource(R.drawable.todo_icon_0todo_selected);
                bdl_Status = 0;
                RefreshStatusText(bdl_Status);
            }
        });

        edit_btn_wip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_wip.setBackgroundResource(R.drawable.todo_icon_1wip_selected);
                bdl_Status = 1;
                RefreshStatusText(bdl_Status);
            }
        });

        edit_btn_blocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_blocked.setBackgroundResource(R.drawable.todo_icon_2blocked_selected);
                bdl_Status = 2;
                RefreshStatusText(bdl_Status);
            }
        });

        edit_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_done.setBackgroundResource(R.drawable.todo_icon_3done_selected);
                bdl_Status = 3;
                RefreshStatusText(bdl_Status);
            }
        });

        edit_btn_planned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_planned.setBackgroundResource(R.drawable.todo_icon_4planned_selected);
                bdl_Status = 4;
                RefreshStatusText(bdl_Status);
            }
        });

        edit_btn_onhold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_onhold.setBackgroundResource(R.drawable.todo_icon_5onhold_selected);
                bdl_Status = 5;
                RefreshStatusText(bdl_Status);
            }
        });

        edit_btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_reject.setBackgroundResource(R.drawable.todo_icon_6rejected_selected);
                bdl_Status = 6;
                RefreshStatusText(bdl_Status);
            }
        });


        edit_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshAllStatuses(); //Resetting all buttons and then only activate the one that is pressed;
                edit_btn_delete.setBackgroundResource(R.drawable.todo_icon_7delete_selected);
                bdl_Status = 7;
                RefreshStatusText(bdl_Status);
            }
        });


        return view;
    }


    public void RefreshStatusText(int status){
        switch (MainActivity.prf_Language)
        {
            case 0:  // for English

                switch (status){
                    case 0: edit_status_text.setText(R.string.tst_todo);break;
                    case 1: edit_status_text.setText(R.string.tst_wip);break;
                    case 2: edit_status_text.setText(R.string.tst_blocked);break;
                    case 3: edit_status_text.setText(R.string.tst_done);break;
                    case 4: edit_status_text.setText(R.string.tst_planned);break;
                    case 5: edit_status_text.setText(R.string.tst_on_hold);break;
                    case 6: edit_status_text.setText(R.string.tst_reject);break;
                    case 7: edit_status_text.setText(R.string.tst_delete);break;
                }
                break;
            case 1:  // for Russian
                switch (status){
                    case 0: edit_status_text.setText(R.string.ru_tst_todo);break;
                    case 1: edit_status_text.setText(R.string.ru_tst_wip);break;
                    case 2: edit_status_text.setText(R.string.ru_tst_blocked);break;
                    case 3: edit_status_text.setText(R.string.ru_tst_done);break;
                    case 4: edit_status_text.setText(R.string.ru_tst_planned);break;
                    case 5: edit_status_text.setText(R.string.ru_tst_on_hold);break;
                    case 6: edit_status_text.setText(R.string.ru_tst_reject);break;
                    case 7: edit_status_text.setText(R.string.ru_tst_delete);break;
                }


                break;
        }

    }


    public void RefreshAllStatuses(){

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        edit_btn_todo.setBackgroundResource(R.drawable.todo_icon_0todo);
        edit_btn_wip.setBackgroundResource(R.drawable.todo_icon_1wip);
        edit_btn_blocked.setBackgroundResource(R.drawable.todo_icon_2blocked);
        edit_btn_done.setBackgroundResource(R.drawable.todo_icon_3done);
        edit_btn_planned.setBackgroundResource(R.drawable.todo_icon_4planned);
        edit_btn_onhold.setBackgroundResource(R.drawable.todo_icon_5onhold);
        edit_btn_reject.setBackgroundResource(R.drawable.todo_icon_6rejected);
        edit_btn_delete.setBackgroundResource(R.drawable.todo_icon_7delete);

    }



}
