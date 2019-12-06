package com.thepoisondevelopment.whattodo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.thepoisondevelopment.whattodo.MainActivity.ListView_DATE;
import static com.thepoisondevelopment.whattodo.MainActivity.filter_func_status;
import static com.thepoisondevelopment.whattodo.MainActivity.filter_priority_highlow;

public class fragmentTodo extends Fragment {

    private static final String TAG = "Todo Fragment";

    ListView listview_records;
    DBHelper dbHelper;
    Cursor data;

    int
            filterstate_todo = 0,
            filterstate_planned = 0,
            filterstate_wip = 0,
            filterstate_outoflist = 0;

    Button nav2_top_1create, nav2_top_3calendar, nav2_top_4settings, btn_pdfexport_todos;
    Button filter_priority, filter_todo, filter_planned, filter_wip, filter_outoflist;


    TextView filter_wip_number, filter_planned_number, filter_todo_number, filter_nothingfound;
    int filter_wip_numberint, filter_planned_numberint, filter_todo_numberint;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_todo, container, false);


        // implement theme
        nav2_top_1create = view.findViewById(R.id.nav2_top_1create);
        nav2_top_3calendar = view.findViewById(R.id.nav2_top_3calendar);
        nav2_top_4settings = view.findViewById(R.id.nav2_top_4settings);
        TextView nav2_top_1create_text = view.findViewById(R.id.nav2_top_1create_text);
        TextView nav2_top_2todolist_text = view.findViewById(R.id.nav2_top_2todolist_text);
        TextView nav2_top_3calendar_text = view.findViewById(R.id.nav2_top_3calendar_text);
        TextView nav2_top_4settings_text = view.findViewById(R.id.nav2_top_4settings_text);

        btn_pdfexport_todos = view.findViewById(R.id.btn_pdfexport_todos);
        filter_nothingfound = view.findViewById(R.id.filter_nothingfound);
        filter_nothingfound.setVisibility(View.INVISIBLE);

        filter_priority = view.findViewById(R.id.filter_priority);
        filter_priority.setBackgroundResource(R.drawable.filter_priority_all);
        filter_todo = view.findViewById(R.id.filter_todo);
        filter_planned = view.findViewById(R.id.filter_planned);
        filter_wip = view.findViewById(R.id.filter_wip);
        filter_outoflist = view.findViewById(R.id.filter_outoflist);

        filter_todo_number = view.findViewById(R.id.filter_todo_number);
        filter_planned_number = view.findViewById(R.id.filter_planned_number);
        filter_wip_number = view.findViewById(R.id.filter_wip_number);


        switch (filter_func_status){
            case 0: filter_todo.setBackgroundResource(R.drawable.filter_todo_selected); break;
            case 1: filter_wip.setBackgroundResource(R.drawable.filter_wip_selected);break;
            case 4: filter_planned.setBackgroundResource(R.drawable.filter_planned_selected);break;
            case 100:  filter_outoflist.setBackgroundResource(R.drawable.filter_outoflist_selected);break;
        }

        switch (filter_priority_highlow){
            case 0: filter_priority.setBackgroundResource(R.drawable.filter_priority_all); break;
            case 1: filter_priority.setBackgroundResource(R.drawable.filter_priority_high); break;
            case 2: filter_priority.setBackgroundResource(R.drawable.filter_priority_normal);break;
            case 3: filter_priority.setBackgroundResource(R.drawable.filter_priority_low);break;
        }

        switch (MainActivity.prf_Language)
        {
            case 0:
                nav2_top_1create_text.setText(R.string.create);
                nav2_top_2todolist_text.setText(R.string.todos);
                nav2_top_4settings_text.setText(R.string.settings);
                filter_nothingfound.setText(R.string.no_tasks_found);
                nav2_top_3calendar_text.setText(R.string.calendar);
                break;
            case 1:
                nav2_top_1create_text.setText(R.string.ru_create);
                nav2_top_2todolist_text.setText(R.string.ru_todos);
                nav2_top_4settings_text.setText(R.string.ru_settings);
                filter_nothingfound.setText(R.string.ru_no_tasks_found);
                nav2_top_3calendar_text.setText(R.string.ru_calendar);
                break;
        }


        filter_outoflist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilterButtons();

                switch (filterstate_outoflist) {
                    case 0:
                        filterstate_outoflist = 1;
                        filterstate_todo = 0;
                        filterstate_planned = 0;
                        filterstate_wip = 0;
                        filter_func_status = 100;
                        filter_outoflist.setBackgroundResource(R.drawable.filter_outoflist_selected);
                        MainActivity.prf_ListViewPosition = 0;
                        ShowTODORecords(filter_priority_highlow, -1, 100);
                        break;

                    case 1:
                        filterstate_outoflist = 0;
                        filter_func_status = -1;
                        filter_outoflist.setBackgroundResource(R.drawable.filter_outoflist);
                        MainActivity.prf_ListViewPosition = 0;
                        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
                        break;
                }

            }
        });


        filter_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (filter_priority_highlow) {

                    case 0:
                        filter_priority.setBackgroundResource(R.drawable.filter_priority_high);
                        filter_priority_highlow = 1;
                        break;

                    case 1:
                        filter_priority.setBackgroundResource(R.drawable.filter_priority_normal);
                        filter_priority_highlow = 2;
                        break;

                    case 2:
                        filter_priority.setBackgroundResource(R.drawable.filter_priority_low);
                        filter_priority_highlow = 3;
                        break;

                    case 3:
                        filter_priority.setBackgroundResource(R.drawable.filter_priority_all);
                        filter_priority_highlow = 0;
                        break;
                }

                ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
            }
        });

        filter_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearFilterButtons();
                switch (filterstate_todo) {
                    case 0:
                        filterstate_todo = 1;
                        filterstate_wip = 0;
                        filterstate_planned = 0;
                        filterstate_outoflist = 0;
                        filter_func_status = 0;
                        filter_todo.setBackgroundResource(R.drawable.filter_todo_selected);
                        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
                        break;

                    case 1:
                        filterstate_todo = 0;
                        filter_func_status = -1;
                        filter_todo.setBackgroundResource(R.drawable.filter_todo);
                        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
                        break;
                }
            }
        });

        filter_planned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearFilterButtons();
                switch (filterstate_planned) {
                    case 0:
                        filterstate_planned = 1;
                        filterstate_todo = 0;
                        filterstate_wip = 0;
                        filterstate_outoflist = 0;
                        filter_func_status = 4;
                        filter_planned.setBackgroundResource(R.drawable.filter_planned_selected);
                        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
                        break;

                    case 1:
                        filterstate_planned = 0;
                        filter_func_status = -1;
                        filter_planned.setBackgroundResource(R.drawable.filter_planned);
                        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
                        break;
                }

            }
        });

        filter_wip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearFilterButtons();
                switch (filterstate_wip) {
                    case 0:
                        filterstate_todo = 0;
                        filterstate_planned = 0;
                        filterstate_outoflist = 0;
                        filterstate_wip = 1;
                        filter_func_status = 1;
                        filter_wip.setBackgroundResource(R.drawable.filter_wip_selected);
                        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
                        break;

                    case 1:
                        filterstate_wip = 0;
                        filter_func_status = -1;
                        filter_wip.setBackgroundResource(R.drawable.filter_wip);
                        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);
                        break;
                }


            }
        });

        nav2_top_1create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                Fragment targetFragment = new fragmentCreate();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });


        nav2_top_3calendar.setOnClickListener(new View.OnClickListener() {
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


        nav2_top_4settings.setOnClickListener(new View.OnClickListener() {
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


        btn_pdfexport_todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                if (filter_func_status == 100 ) {

                    Dialog_Export_Timeframe dialog = new Dialog_Export_Timeframe();
                    dialog.setTargetFragment(fragmentTodo.this, 1);
                    dialog.setStyle(Dialog_Export_Timeframe.STYLE_NO_TITLE, 0);
                    assert getFragmentManager() != null;
                    dialog.show(getFragmentManager(), "Save PDF");

                } else {
                    Fragment targetFragment = new fragmentExport();
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                }

            }
        });

        listview_records = view.findViewById(R.id.listview_records);

        ShowTODORecords(filter_priority_highlow, -1, filter_func_status);

        return view;
    }

    int dbId;
    String dbDateCreated;
    String dbDatePlanned;
    String dbDateDeadline;
    String dbLabel, dbLabelColor;
    String dbName;
    int dbPrio;
    int dbStatus;
    int dbTimeLog;


    public void ShowTODORecords(int _priority, int _newfirst, int _status){

        int NumberOfRecords = 0;
        filter_todo_numberint = 0;
        filter_planned_numberint = 0;
        filter_wip_numberint = 0;

        dbHelper = new DBHelper(getActivity());
        ArrayList<ListView_Records> RecordsList = new ArrayList<>();

        data = dbHelper.getPrioStatusData(_priority, _status);

            while (data.moveToNext()) {

                NumberOfRecords++;

                dbId = data.getInt(data.getColumnIndex(DBHelper.KEY_ID));

                dbName = data.getString(data.getColumnIndex(DBHelper.KEY_NAME));

                dbStatus = data.getInt(data.getColumnIndex(DBHelper.KEY_STATUS));
                dbPrio = data.getInt(data.getColumnIndex(DBHelper.KEY_PRIO));
                dbTimeLog = data.getInt(data.getColumnIndex(DBHelper.KEY_TIME_LOGGED));

                dbDateCreated = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_CREATED));
                dbDatePlanned = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_PLANNED));
                dbDateDeadline = data.getString(data.getColumnIndex(DBHelper.KEY_TASK_DEADLINE));

                dbLabel = data.getString(data.getColumnIndex(DBHelper.KEY_LABEL));
                dbLabelColor = data.getString(data.getColumnIndex(DBHelper.KEY_LABEL_COLOR));

                switch (dbStatus){
                    case 0: filter_todo_numberint++; break;
                    case 1: filter_wip_numberint++; break;
                    case 4: filter_planned_numberint++; break;
                }

                ListView_Records Records = new ListView_Records(dbDateCreated, dbDatePlanned, dbDateDeadline, dbName, dbStatus, dbPrio, dbTimeLog, dbLabel, dbLabelColor);
                RecordsList.add(Records);

                ListAdapter_Records adapter;

                if (MainActivity.prf_FullView == 1)
                    adapter = new ListAdapter_Records(getActivity(), R.layout.listview_records_full, RecordsList);else
                    adapter = new ListAdapter_Records(getActivity(), R.layout.listview_records_compact, RecordsList);

                listview_records.setAdapter(adapter);

                listview_records.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        bundle.putString(DBHelper.KEY_TASK_DONE, data.getString(data.getColumnIndex(DBHelper.KEY_TASK_DONE)));

                        bundle.putInt(DBHelper.KEY_TIME_LOGGED, data.getInt(data.getColumnIndex(DBHelper.KEY_TIME_LOGGED)));

                        bundle.putString("MODE", "EDIT");

                        if (dbStatus == 3) {

                            Fragment targetFragment = new fragmentCompleted();
                            assert getFragmentManager() != null;
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            targetFragment.setArguments(bundle);
                            transaction.commit();

                        }else{

                            Fragment targetFragment = new fragmentEdit();
                            assert getFragmentManager() != null;
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                            targetFragment.setArguments(bundle);
                            transaction.commit();

                        }


                    }
                });
        }


        if (NumberOfRecords == 0) {
            listview_records.setVisibility(View.INVISIBLE);
            filter_nothingfound.setVisibility(View.VISIBLE);
        } else {
            listview_records.setVisibility(View.VISIBLE);
            filter_nothingfound.setVisibility(View.INVISIBLE);
        }

        listview_records.setSelection(MainActivity.prf_ListViewPosition);

        filter_wip_number.setText("" + filter_wip_numberint);
        filter_planned_number.setText("" + filter_planned_numberint);
        filter_todo_number.setText("" + filter_todo_numberint);

        dbHelper.close();
    }



    public void clearFilterButtons(){

        filter_outoflist.setBackgroundResource(R.drawable.filter_outoflist);
        filter_todo.setBackgroundResource(R.drawable.filter_todo);
        filter_wip.setBackgroundResource(R.drawable.filter_wip);
        filter_planned.setBackgroundResource(R.drawable.filter_planned);
    }

}
