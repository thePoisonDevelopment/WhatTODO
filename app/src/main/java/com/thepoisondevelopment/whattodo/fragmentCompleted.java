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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.thepoisondevelopment.whattodo.MainActivity.prm_Deadline;
import static com.thepoisondevelopment.whattodo.MainActivity.prm_EditPriority;
import static com.thepoisondevelopment.whattodo.MainActivity.prm_Planned;

public class fragmentCompleted extends Fragment {



    private static final String TAG = "Edit Fragment";

    Button cmpl_back;
    TextView cmpl_caption, cmpl_todoname_label,
        cmpl_todoID_label, cmpl_taskDescription,
        cmpl_taskLabel, cmpl_created_label, cmpl_planned_on, cmpl_deadline_lbl, cmpl_timespent_label;

    ImageView cmplt_prio;


    String bdl_Name, bdl_TaskCreated, bdl_TaskPlanned, bdl_TaskDeadline, bdl_Label, bdl_LabelColor, bdl_Completed;
    int bdl_ID, bdl_Prio, bdl_Status, bdl_Log;

    DBHelper dbHelper;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_completed, container, false);

        cmpl_back = view.findViewById(R.id.cmpl_back);

        cmpl_caption = view.findViewById(R.id.cmpl_caption);
        cmpl_todoname_label = view.findViewById(R.id.cmpl_todoname_label);
        cmpl_todoID_label = view.findViewById(R.id.cmpl_todoID_label);
        cmpl_taskDescription = view.findViewById(R.id.cmpl_taskDescription);
        cmpl_taskLabel = view.findViewById(R.id.cmpl_taskLabel);
        cmpl_created_label = view.findViewById(R.id.cmpl_created_label);
        cmpl_planned_on = view.findViewById(R.id.cmpl_planned_on);
        cmpl_deadline_lbl = view.findViewById(R.id.cmpl_deadline_lbl);
        cmpl_timespent_label = view.findViewById(R.id.cmpl_timespent_label);

        cmplt_prio = view.findViewById(R.id.cmplt_prio);

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

        cmpl_todoID_label.setText("Task ID: " + bdl_ID);
        cmpl_taskDescription.setText(bdl_Name);
        cmpl_taskLabel.setText("" + bdl_Label);

        cmpl_planned_on.setText("Was planned on: " + bdl_TaskPlanned);
        cmpl_created_label.setText("Task created: " + bdl_TaskCreated);
        cmpl_deadline_lbl.setText("Deadline was: " + bdl_TaskDeadline);

        int hours = bdl_Log / 60;
        int mins = bdl_Log % 60;
        cmpl_timespent_label.setText("Time spent: " + hours + "h " + mins + "m");


        switch (bdl_Prio) {
            case 1: cmplt_prio.setBackgroundResource(R.drawable.todo_prio_1);break;
            case 2: cmplt_prio.setBackgroundResource(R.drawable.todo_prio_2);break;
            case 3: cmplt_prio.setBackgroundResource(R.drawable.todo_prio_3);break;
            case 4: cmplt_prio.setBackgroundResource(R.drawable.todo_prio_4);break;
            case 5: cmplt_prio.setBackgroundResource(R.drawable.todo_prio_5);break;
            default: cmplt_prio.setBackgroundResource(R.drawable.todo_prio_3);break;
        }


        bdl_TaskCreated = getArguments().getString(DBHelper.KEY_TASK_CREATED);
        bdl_TaskPlanned = getArguments().getString(DBHelper.KEY_TASK_PLANNED);
        bdl_TaskDeadline = getArguments().getString(DBHelper.KEY_TASK_DEADLINE);


        cmpl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment targetFragment;

                targetFragment = new fragmentTodo();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });




        return view;
    }



}
