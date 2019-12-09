package com.thepoisondevelopment.whattodo;

/*

Hello, thePoison is here

I developed this application initially for my needs as
I wanted to record all tasks and ideas that suddenly appear in my head

I am not a super programmer, I am learning, so the code and solutions here might not be ideal.

This application you can find in public repository. I am open for any feedback, comments, ideas.

There are a lot of stuff that I want to include to this app, but due to lack of time and
developing other projects, I am slowly enhancing this app.

Anyways, if you can propose a better solutions and would like to add features, please do so!
I am gladly will release a new update

Cheers
thePoison development

*/


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefSettings;
    public static final String FILE_NAME = "AppSettings";

    public static String prm_Planned = "Not set";
    public static String prm_Deadline = "Not set";
    public static int prm_EditPriority = 0;
    public static String prm_LabelColor = "#ffffff";

    public static String prf_ColorTheme;
    public static String prf_ColorThemeAux1;

    public static int prf_FullView = 1;
    public static int filter_priority_highlow = 0;
    public static int filter_func_status = -1;

    public static int prf_Language = 0; // 0 - english,   1 - russian

    public static int prf_ListViewPosition = 0;
    public static int prf_VibraFeedback = 1;
    public static int prf_VibraDuration = 500;

    public static String ListView_DATE = "";
    public static int ListView_DAY;
    public static int ListView_MONTH;
    public static int ListView_MODE;

    public static int ScringWidthDP;
    // This variable is needed to check the screen size and choose propoer layout

    public static int TimeFrame_Month, TimeFrame_Year, TimeFrame;

    @SuppressLint("StaticFieldLeak")
    //public static BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefSettings = this.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
//        prf_ColorTheme = prefSettings.getString("prfColorTheme", "#B1F7FF");
//        prf_ColorThemeAux1 = prefSettings.getString("prfColorThemeAux1", "#3861a5");
        prf_ColorTheme = "#3a67b3";
        prf_ColorThemeAux1 = "#234379";
        prf_Language = prefSettings.getInt("prfLanguage",0); // Language
        prf_FullView = prefSettings.getInt("prfFullView",1); //
        prf_VibraFeedback = prefSettings.getInt("prfVibra",1); //


        Configuration config = getResources().getConfiguration();
        ScringWidthDP = config.smallestScreenWidthDp;

//        if (config.smallestScreenWidthDp >= 600) {
//            setContentView(R.layout.layout-w600dp);
//        } else {
//            setContentView(R.layout.main_activity);
//        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new fragmentCreate()).commit();


    }


}
