package com.thepoisondevelopment.whattodo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "TODODataBase";
    public static final String TABLE_TODO = "TODO";

    public static final String

            KEY_ID = "_id",

    // TASK CREATED

    // FORMAT FOR CREATING ALL DATES:
    // XXXXDDMMYYYYW
    // XXXX  =  Time,  2330 should be parced as 23:30
    // DD - day,   MM - month,   YYYY - year,   W - day of the week
    // =============================



    KEY_TASK_CREATED = "TASK_CREATED",
    KEY_TASK_DEADLINE = "TASK_DEADLINE",
    KEY_PRIO = "PRIO",
    KEY_NAME = "NAME",

    KEY_ASSIGNED = "ASSIGNED",
    KEY_LABEL = "LABEL",
    KEY_LABEL_COLOR = "LABEL_COLOR",  //FORMAT - HEX or RED/GREAN/BLUE.....

    KEY_STATUS = "STATUS",
    KEY_TASK_PLANNED = "TASK_PLANNED",
    KEY_TASK_STARTED = "TASK_STARTED",
    KEY_TIME_LOGGED = "LOGGED",

    KEY_TASK_DONE = "TASK_DONE";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_TODO + "(" +

                KEY_ID + " integer primary key," +

                KEY_TASK_CREATED + " text," +
                KEY_TASK_PLANNED + " text," +
                KEY_TASK_DEADLINE + " text," +

                KEY_PRIO + " integer," +
                KEY_NAME + " text," +

                KEY_ASSIGNED + " text," +
                KEY_LABEL + " text," +
                KEY_LABEL_COLOR + " text," +

                KEY_STATUS + " integer," +
                KEY_TASK_STARTED + " text," +
                KEY_TIME_LOGGED + " integer," +

                KEY_TASK_DONE + " text" +

                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_TODO);
        onCreate(db);

//        switch( newVersion ) {
//            case 6: /* this is your new version number */
//                db.execSQL( "alter table " + TABLE_TODO + " add column CARDCASH" ) ;
//                break ;
//        }

    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TODO;

        return db.rawQuery(query, null);
    }


    public Cursor getSpecificDay(String DAY) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_TODO + " where TASK_PLANNED = '" + DAY + "' AND STATUS IN (0,1,2,4)";

        return db.rawQuery(query, null);

    }


    public Cursor getSpecificMonth(String MONTH) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_TODO + " where TASK_PLANNED LIKE '%" + MONTH + "%' AND STATUS IN (0,1,2,4)";

        return db.rawQuery(query, null);

    }



    public Cursor getSpecificMonthCompleted(String MONTH) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_TODO + " where TASK_DONE LIKE '%" + MONTH + "%' AND STATUS = 3";

        return db.rawQuery(query, null);

    }

    public Cursor getOnePrioData(int _prio, int _status) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_TODO + " where PRIO = " + _prio + " AND STATUS = " + _status;

        return db.rawQuery(query, null);

    }

    public Cursor getPrioStatusData(int _priority, int _status) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TODO;

        if (_status == -1) {  // get all active TODOs
            switch (_priority) {
                case 0:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (1,2,3,4,5) AND STATUS NOT IN (3,6)";
                    break;

                case 1:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (1,2) AND STATUS NOT IN (3,6)";
                    break;

                case 2:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (3) AND STATUS NOT IN (3,6)";
                    break;

                case 3:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (4,5) AND STATUS NOT IN (3,6)";
                    break;
            }
        } else if (_status == 100) {  //Get all todos that are blocekd/cancelled/completed
            switch (_priority) {

                case 0:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (1,2,3,4,5) AND STATUS IN (3,6)";
                    break;

                case 1:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (1,2) AND STATUS IN (3,6)";
                    break;

                case 2:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (3) AND STATUS IN (3,6)";
                    break;

                case 3:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (4,5) AND STATUS IN (3,6)";
                    break;
            }
        } else {  // filter buy priorities//
            switch (_priority) {

                case 0:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (1,2,3,4,5) AND STATUS = " + _status;
                    break;

                case 1:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (1,2) AND STATUS = " + _status;
                    break;

                case 2:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (3) AND STATUS = " + _status;
                    break;

                case 3:
                    query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (4,5) AND STATUS = " + _status;
                    break;
            }
        }

        return db.rawQuery(query, null);

    }


    public Cursor getDataOfStatus(int _status) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TODO + " where PRIO IN (1,2,3,4,5) AND STATUS ="+_status ;

        return db.rawQuery(query, null);

    }


    public static String getTodayTimeDate(){
        // FORMAT OF THE DATE:   14:42 SUN 17.05.2019

        Calendar TodaysDate = Calendar.getInstance();
        String KEY_TASK_CREATED = "";

        // --> TIME: XX:XX 24h format
        String TodaysTime = "";
        int tmp_hr24 = TodaysDate.get(Calendar.HOUR_OF_DAY);
        TodaysTime = tmp_hr24 + ":";

        int tmp_mins = TodaysDate.get(Calendar.MINUTE);
        if (tmp_mins < 10) TodaysTime = TodaysTime + "0" + tmp_mins;
        else TodaysTime = TodaysTime + tmp_mins;

        String TodaysMonth = "";
        int tmp_month = TodaysDate.get(Calendar.MONTH) + 1;
        if (tmp_month <10) TodaysMonth = "0" + tmp_month; else TodaysMonth = "" + tmp_month;

        String TodaysWeekDay = "";
        int tmp_weekday = TodaysDate.get(Calendar.DAY_OF_WEEK);
        switch (tmp_weekday) {
            case Calendar.MONDAY: TodaysWeekDay = "MON";break;  // 2
            case Calendar.TUESDAY: TodaysWeekDay = "TUE";break;  //3
            case Calendar.WEDNESDAY: TodaysWeekDay = "WED";break; //4
            case Calendar.THURSDAY: TodaysWeekDay = "THU";break; //5
            case Calendar.FRIDAY: TodaysWeekDay = "FRI";break;  //6
            case Calendar.SATURDAY: TodaysWeekDay = "SAT";break; //7
            case Calendar.SUNDAY: TodaysWeekDay = "SUN";break;  //1
        }

        KEY_TASK_CREATED =
                TodaysTime + " " +
                        TodaysWeekDay + " " +
                TodaysDate.get(Calendar.DAY_OF_MONTH) + "." +
                TodaysMonth + "." +
                TodaysDate.get(Calendar.YEAR);

        return KEY_TASK_CREATED;
    }



    public static String getTodayDate(){
        // FORMAT OF THE DATE:   17.05.2019

        Calendar TodaysDate = Calendar.getInstance();
        String KEY_TASK_CREATED = "";


        String TodaysMonth = "";
        int tmp_month = TodaysDate.get(Calendar.MONTH) + 1;
        if (tmp_month <10) TodaysMonth = "0" + tmp_month; else TodaysMonth = "" + tmp_month;


        KEY_TASK_CREATED =
                        TodaysDate.get(Calendar.DAY_OF_MONTH) + "." +
                        TodaysMonth + "." +
                        TodaysDate.get(Calendar.YEAR);

        return KEY_TASK_CREATED;
    }

    public static String getTodayDate_forExport(){
        // FORMAT OF THE DATE:   17.05.2019

        Calendar TodaysDate = Calendar.getInstance();
        String KEY_TASK_CREATED = "";

        String TodaysMonth = "";
        int tmp_month = TodaysDate.get(Calendar.MONTH) + 1;
        if (tmp_month <10) TodaysMonth = "0" + tmp_month; else TodaysMonth = "" + tmp_month;

        KEY_TASK_CREATED =
                TodaysDate.get(Calendar.DAY_OF_MONTH) +
                        TodaysMonth +
                        TodaysDate.get(Calendar.YEAR);

        return KEY_TASK_CREATED;
    }


    public static String getTodayTime(){
        // FORMAT OF THE DATE:   14:42 SUN 17.05.2019

        Calendar TodaysDate = Calendar.getInstance();
        String KEY_TASK_CREATED = "";

        // --> TIME: XX:XX 24h format
        String TodaysTime = "";
        int tmp_hr24 = TodaysDate.get(Calendar.HOUR_OF_DAY);
        TodaysTime = tmp_hr24 + ":";

        int tmp_mins = TodaysDate.get(Calendar.MINUTE);
        if (tmp_mins < 10) TodaysTime = TodaysTime + "0" + tmp_mins;
        else TodaysTime = TodaysTime + tmp_mins;



        return TodaysTime;
    }


    public static String ConvertMonthToString(int month){

        String returnMonth = "XXX";

        switch (month) {
            case 1: returnMonth = "JAN";break;
            case 2: returnMonth = "FEB";break;
            case 3: returnMonth = "MAR";break;
            case 4: returnMonth = "APR";break;
            case 5: returnMonth = "MAY";break;
            case 6: returnMonth = "JUN";break;
            case 7: returnMonth = "JUL";break;
            case 8: returnMonth = "AUG";break;
            case 9: returnMonth = "SEP";break;
            case 10: returnMonth = "OCT";break;
            case 11: returnMonth = "NOV";break;
            case 12: returnMonth = "DEC";break;

        }

        return returnMonth;

    }


}
