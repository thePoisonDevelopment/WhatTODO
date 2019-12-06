package com.thepoisondevelopment.whattodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static com.thepoisondevelopment.whattodo.DBHelper.KEY_ASSIGNED;
import static com.thepoisondevelopment.whattodo.DBHelper.KEY_TASK_CREATED;
import static com.thepoisondevelopment.whattodo.DBHelper.KEY_TASK_DEADLINE;
import static com.thepoisondevelopment.whattodo.DBHelper.KEY_TASK_DONE;
import static com.thepoisondevelopment.whattodo.DBHelper.KEY_TASK_PLANNED;
import static com.thepoisondevelopment.whattodo.MainActivity.ListView_DATE;
import static com.thepoisondevelopment.whattodo.MainActivity.prm_Deadline;
import static com.thepoisondevelopment.whattodo.MainActivity.prm_Planned;

public class fragmentCreate  extends Fragment implements Dialog_DataPicker.OnInputSelected, Dialog_LabelColor.OnInputSelected_LC {

    @Override
    public void sendInput(String input) {
        planned_txt.setText(prm_Planned);
        deadline_txt.setText(prm_Deadline);

        if (!prm_Planned.equals("Not set")) {
            btn_c_status_value = 4; btn_c_status.setBackgroundResource(R.drawable.todo_icon_4planned);}

    }

     @Override
    public void sendInput_LC(String input) {

         switch (input){
             case "NONE": btn_label_color.setBackgroundResource(R.drawable.label_color_none_selected);break;
             case "RED": btn_label_color.setBackgroundResource(R.drawable.label_color_red_selected);break;
             case "ORANGE": btn_label_color.setBackgroundResource(R.drawable.label_color_orange_selected);break;
             case "BLUE": btn_label_color.setBackgroundResource(R.drawable.label_color_blue_selected);break;
             case "YELLOW": btn_label_color.setBackgroundResource(R.drawable.label_color_yellow_selected);break;
             case "GREEN": btn_label_color.setBackgroundResource(R.drawable.label_color_green_selected);break;
             case "PINK": btn_label_color.setBackgroundResource(R.drawable.label_color_pink_selected);break;
         }
    }


    private static final String TAG = "Create Fragment";

    public DBHelper dbHelper;

    Button f_create_prio_1;
    Button f_create_prio_2;
    Button f_create_prio_3;
    Button f_create_prio_4;
    Button f_create_prio_5;
    Button btn_label_color;

    Button btn_c_status;
    int btn_c_status_value = 0;

    int TODO_prio = 3;

    Button nav_top_1create, nav_top_2todolist, nav_top_3calendar, nav_top_4settings, create_puttodays_date;
    View planned_viewbtn, deadline_viewbtn;
    TextView planned_txt, deadline_txt;


    Button create_btn_create ;
    EditText create_taskname, create_label_input;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create, container, false);

        MainActivity.prm_Planned = "Not set";
        MainActivity.prm_Deadline = "Not set";

        planned_txt = view.findViewById(R.id.planned_txt);
        planned_txt.setText(prm_Planned);
        deadline_txt = view.findViewById(R.id.deadline_txt);
        deadline_txt.setText(prm_Deadline);

        f_create_prio_1 = view.findViewById(R.id.f_create_prio_1);
        f_create_prio_2 = view.findViewById(R.id.f_create_prio_2);
        f_create_prio_3 = view.findViewById(R.id.f_create_prio_3);
        f_create_prio_4 = view.findViewById(R.id.f_create_prio_4);
        f_create_prio_5 = view.findViewById(R.id.f_create_prio_5);

        btn_c_status = view.findViewById(R.id.btn_c_status);

        btn_label_color = view.findViewById(R.id.btn_label_color);

        create_puttodays_date = view.findViewById(R.id.create_puttodays_date);

        create_taskname = view.findViewById(R.id.create_taskname);
        create_label_input = view.findViewById(R.id.create_label_input);

        planned_viewbtn = view.findViewById(R.id.planned_viewbtn);
        deadline_viewbtn = view.findViewById(R.id.deadline_viewbtn);
        create_btn_create = view.findViewById(R.id.create_btn_create);

        create_taskname.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        create_label_input.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        //create_taskname.requestFocus();

        // implement theme

        nav_top_1create = view.findViewById(R.id.nav1_top_1create);
        nav_top_2todolist = view.findViewById(R.id.nav1_top_2todolist);
        nav_top_3calendar = view.findViewById(R.id.nav1_top_3calendar);
        nav_top_4settings = view.findViewById(R.id.nav1_top_4settings);

        TextView nav1_top_1create_text  = view.findViewById(R.id.nav1_top_1create_text);
        TextView nav1_top_2todolist_text  = view.findViewById(R.id.nav1_top_2todolist_text);
        TextView nav1_top_3calendar_text  = view.findViewById(R.id.nav1_top_3calendar_text);
        TextView nav1_top_4settings_text  = view.findViewById(R.id.nav1_top_4settings_text);
        TextView create_hint_2  = view.findViewById(R.id.create_hint_2);
        TextView create_hint_1  = view.findViewById(R.id.create_hint_1);

        TextView create_label_label  = view.findViewById(R.id.create_label_label);
        TextView deadline_lbl  = view.findViewById(R.id.deadline_lbl);
        TextView planned_lbl  = view.findViewById(R.id.planned_lbl);
        TextView create_status_label  = view.findViewById(R.id.create_status_label);

        switch (MainActivity.prf_Language)
        {
            case 0:
                nav1_top_1create_text.setText(R.string.create);
                nav1_top_2todolist_text.setText(R.string.todos);
                nav1_top_4settings_text.setText(R.string.settings);
                create_hint_2.setText(R.string.priority);
                create_hint_1.setText(R.string.todo_name_caption);
                create_taskname.setHint(R.string.todo_name);
                create_btn_create.setText(R.string.btn_create);
                nav1_top_3calendar_text.setText(R.string.calendar);
                create_label_label.setText(R.string.label);
                deadline_lbl.setText(R.string.deadline_);
                planned_lbl.setText(R.string.planned_on_);
                create_status_label.setText(R.string.status);
                create_label_input.setHint(R.string.label_);
                break;
            case 1:
                nav1_top_1create_text.setText(R.string.ru_create);
                nav1_top_2todolist_text.setText(R.string.ru_todos);
                nav1_top_4settings_text.setText(R.string.ru_settings);
                create_hint_2.setText(R.string.ru_priority);
                create_hint_1.setText(R.string.ru_todo_name_caption);
                create_taskname.setHint(R.string.ru_todo_name);
                create_btn_create.setText(R.string.ru_btn_create);
                nav1_top_3calendar_text.setText(R.string.ru_calendar);
                create_label_label.setText(R.string.ru_label_);
                deadline_lbl.setText(R.string.ru_deadline_);
                planned_lbl.setText(R.string.ru_planned_on_);
                create_status_label.setText(R.string.ru_status);
                create_label_input.setHint(R.string.ru_label);
                break;
        }


        btn_c_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (btn_c_status_value){
                    case 0: btn_c_status_value = 4; btn_c_status.setBackgroundResource(R.drawable.todo_icon_4planned); break;
                    case 4: btn_c_status_value = 1; btn_c_status.setBackgroundResource(R.drawable.todo_icon_1wip); break;
                    case 1: btn_c_status_value = 0; btn_c_status.setBackgroundResource(R.drawable.todo_icon_0todo); break;
                }
            }
        });



        f_create_prio_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshPriorities(); //Resetting all buttons and then only activate the one that is pressed;
                f_create_prio_1.setBackgroundResource(R.drawable.todo_prio_1_selected);
                TODO_prio = 1;
            }
        });


        f_create_prio_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshPriorities(); //Resetting all buttons and then only activate the one that is pressed;
                f_create_prio_2.setBackgroundResource(R.drawable.todo_prio_2_selected);
                TODO_prio = 2;
            }
        });

        f_create_prio_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshPriorities(); //Resetting all buttons and then only activate the one that is pressed;
                f_create_prio_3.setBackgroundResource(R.drawable.todo_prio_3_selected);
                TODO_prio = 3;
            }
        });

        f_create_prio_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshPriorities(); //Resetting all buttons and then only activate the one that is pressed;
                f_create_prio_4.setBackgroundResource(R.drawable.todo_prio_4_selected);
                TODO_prio = 4;
            }
        });

        f_create_prio_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefreshPriorities(); //Resetting all buttons and then only activate the one that is pressed;
                f_create_prio_5.setBackgroundResource(R.drawable.todo_prio_5_selected);
                TODO_prio = 5;
            }
        });

        nav_top_2todolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                MainActivity.prf_ListViewPosition = 0;

                Fragment targetFragment = new fragmentTodo();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

        nav_top_3calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                ListView_DATE = "";

                Fragment targetFragment = new fragmentCalendar();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        nav_top_4settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                Fragment targetFragment = new fragmentSettings();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        planned_viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("DialogParameter", 0);

                Dialog_DataPicker dialog = new Dialog_DataPicker();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(fragmentCreate.this, 1);
                dialog.setStyle(Dialog_DataPicker.STYLE_NO_TITLE, 0);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "Planned");

            }

        });


        btn_label_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_LabelColor dialog = new Dialog_LabelColor();
                dialog.setTargetFragment(fragmentCreate.this, 1);
                dialog.setStyle(Dialog_LabelColor.STYLE_NO_TITLE, 0);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "Planned");

            }

        });


        deadline_viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("PlannedDeadline", 1);

                Dialog_DataPicker dialog = new Dialog_DataPicker();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(fragmentCreate.this, 1);
                dialog.setStyle(Dialog_DataPicker.STYLE_NO_TITLE, 0);
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "Deadline");

            }

        });



        create_puttodays_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prm_Planned = "";

                Calendar today = Calendar.getInstance();

                String TodaysMonth = "";
                int tmp_month = today.get(Calendar.MONTH) + 1;
                if (tmp_month < 10) TodaysMonth = "0" + tmp_month; else TodaysMonth = "" + tmp_month;
                prm_Planned = "" + today.get(Calendar.DAY_OF_MONTH) + "." + TodaysMonth + "." + today.get(Calendar.YEAR);

                planned_txt.setText(prm_Planned);

                btn_c_status_value = 4; btn_c_status.setBackgroundResource(R.drawable.todo_icon_4planned);

            }

        });


        create_btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_input_TODO = create_taskname.getText().toString();
                String str_input_LABEL = create_label_input.getText().toString();

                if (!str_input_TODO.equals("")) {

                    dbHelper = new DBHelper(getActivity());
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(KEY_TASK_CREATED, DBHelper.getTodayDate());

                    contentValues.put(DBHelper.KEY_PRIO, TODO_prio);

                    contentValues.put(DBHelper.KEY_NAME, str_input_TODO);
                    contentValues.put(DBHelper.KEY_STATUS, btn_c_status_value);

                    contentValues.put(DBHelper.KEY_LABEL, str_input_LABEL);
                    contentValues.put(DBHelper.KEY_LABEL_COLOR, MainActivity.prm_LabelColor);

                    contentValues.put(KEY_TASK_PLANNED, MainActivity.prm_Planned);
                    contentValues.put(KEY_TASK_DEADLINE, MainActivity.prm_Deadline);

                    contentValues.put(KEY_TASK_DONE, "");
                    contentValues.put(KEY_ASSIGNED, "");


                    switch (MainActivity.prf_Language) {
                        case 0:
                            Toast.makeText(getActivity(), "SAVED !", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }

                    // clear all fields after saving a record
                    RefreshPriorities();
                    TODO_prio = 3;
                    f_create_prio_3.setBackgroundResource(R.drawable.todo_prio_3_selected);
                    btn_c_status_value = 0;
                    btn_c_status.setBackgroundResource(R.drawable.todo_icon_0todo);

                    create_taskname.setText("");
                    create_label_input.setText("");
                    MainActivity.prm_Planned = "Not set";
                    MainActivity.prm_Deadline = "Not set";
                    planned_txt.setText(MainActivity.prm_Planned);
                    deadline_txt.setText(MainActivity.prm_Deadline);

                    database.insert(DBHelper.TABLE_TODO, null, contentValues);
                    dbHelper.close();

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

                } else
                    Toast.makeText(getActivity(), "Input the name of the task", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    public void RefreshPriorities(){

        f_create_prio_1.setBackgroundResource(R.drawable.todo_prio_1);
        f_create_prio_2.setBackgroundResource(R.drawable.todo_prio_2);
        f_create_prio_3.setBackgroundResource(R.drawable.todo_prio_3);
        f_create_prio_4.setBackgroundResource(R.drawable.todo_prio_4);
        f_create_prio_5.setBackgroundResource(R.drawable.todo_prio_5);

    }


}
