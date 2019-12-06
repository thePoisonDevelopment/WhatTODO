package com.thepoisondevelopment.whattodo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.thepoisondevelopment.whattodo.MainActivity.FILE_NAME;
import static com.thepoisondevelopment.whattodo.MainActivity.ListView_DATE;
import static com.thepoisondevelopment.whattodo.MainActivity.prf_VibraFeedback;

public class fragmentSettings extends Fragment {

    SharedPreferences prefSettings;

    private static final String TAG = "Settings Fragment";

    Button nav4_top_1create, nav4_top_2todolist,  nav4_top_3calendar,
            btn_langEN, btn_langRU, btn_clear_all_data, btn_clear_database;

    TextView nav4_top_1create_text, nav4_top_2todolist_text, nav4_top_3calendar_text, nav4_top_4settings_text;
    TextView settings_text1, settings_text2, langauge_name;

    TextView stgs_view_label, stgs_view_full, stgs_view_compact;
    ImageView stgs_view_full_radio, stgs_view_compact_radio, stgs_vibra_toggle;

    TextView settings_appl;
    Button settings_rate_the_app, settings_more_apps;

    TextView stgs_vibra_label;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        prefSettings = Objects.requireNonNull(this.getActivity()).getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor ed = prefSettings.edit();

        // implement theme
        btn_clear_all_data = view.findViewById(R.id.btn_clear_all_data);
        btn_clear_database = view.findViewById(R.id.btn_clear_database);

        nav4_top_1create = view.findViewById(R.id.nav4_top_1create);
        nav4_top_2todolist = view.findViewById(R.id.nav4_top_2todolist);
        nav4_top_3calendar = view.findViewById(R.id.nav4_top_3calendar);
        btn_langEN = view.findViewById(R.id.btn_langEN);
        btn_langRU = view.findViewById(R.id.btn_langRU);

        nav4_top_1create_text = view.findViewById(R.id.nav4_top_1create_text);
        nav4_top_2todolist_text = view.findViewById(R.id.nav4_top_2todolist_text);
        nav4_top_3calendar_text = view.findViewById(R.id.nav4_top_3calendar_text);
        nav4_top_4settings_text = view.findViewById(R.id.nav4_top_4settings_text);
        langauge_name = view.findViewById(R.id.stgs_langauge_label);

        stgs_view_label = view.findViewById(R.id.stgs_view_label);
        stgs_view_full_radio = view.findViewById(R.id.stgs_view_full_radio);
        stgs_view_full = view.findViewById(R.id.stgs_view_full);
        stgs_view_compact_radio = view.findViewById(R.id.stgs_view_compact_radio);
        stgs_view_compact = view.findViewById(R.id.stgs_view_compact);

        settings_appl = view.findViewById(R.id.settings_appl);
        settings_rate_the_app = view.findViewById(R.id.settings_rate_the_app);
        settings_more_apps = view.findViewById(R.id.settings_more_apps);

        settings_text1 = view.findViewById(R.id.settings_text1);
        settings_appl = view.findViewById(R.id.settings_appl);
        settings_text2 = view.findViewById(R.id.settings_text2);

        stgs_vibra_toggle = view.findViewById(R.id.stgs_vibra_toggle);
        stgs_vibra_label = view.findViewById(R.id.stgs_vibra_label);

        switch (MainActivity.prf_FullView) {
            case 0:
                stgs_view_full_radio.setBackgroundResource(R.drawable.radio_false);
                stgs_view_compact_radio.setBackgroundResource(R.drawable.radio_true);
                break;

            case 1:
                stgs_view_full_radio.setBackgroundResource(R.drawable.radio_true);
                stgs_view_compact_radio.setBackgroundResource(R.drawable.radio_false);

                break;
        }

        if (prf_VibraFeedback == 1) {
            stgs_vibra_toggle.setBackgroundResource(R.drawable.toggle_on);
        } else if (prf_VibraFeedback == 0) {
            stgs_vibra_toggle.setBackgroundResource(R.drawable.toggle_off);
        }

        //Localisation
        ChanceLanguage(MainActivity.prf_Language);


        settings_rate_the_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=com.thepoisondevelopment.whattodo");
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }catch (ActivityNotFoundException e){
//                    https://play.google.com/store/apps/details?id=com.thepoisondevelopment.whattodo
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.thepoisondevelopment.whattodo");
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }

            }
        });


        settings_more_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Uri uri = Uri.parse("market://developer?id=thePoison+Development");
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }catch (ActivityNotFoundException e){
                    Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=thePoison+Development");
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }

            }
        });



        stgs_view_compact_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.prf_FullView = 0;
                stgs_view_full_radio.setBackgroundResource(R.drawable.radio_false);
                stgs_view_compact_radio.setBackgroundResource(R.drawable.radio_true);

                ed.putInt("prfFullView", MainActivity.prf_FullView);
                ed.apply();

            }
        });

        stgs_view_compact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.prf_FullView = 0;
                stgs_view_full_radio.setBackgroundResource(R.drawable.radio_false);
                stgs_view_compact_radio.setBackgroundResource(R.drawable.radio_true);

                ed.putInt("prfFullView", MainActivity.prf_FullView);
                ed.apply();

            }
        });

        stgs_view_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.prf_FullView = 1;
                stgs_view_full_radio.setBackgroundResource(R.drawable.radio_true);
                stgs_view_compact_radio.setBackgroundResource(R.drawable.radio_false);

                ed.putInt("prfFullView", MainActivity.prf_FullView);
                ed.apply();

            }
        });

        stgs_view_full_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.prf_FullView = 1;
                stgs_view_full_radio.setBackgroundResource(R.drawable.radio_true);
                stgs_view_compact_radio.setBackgroundResource(R.drawable.radio_false);

                ed.putInt("prfFullView", MainActivity.prf_FullView);
                ed.apply();

            }
        });


        btn_langEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.prf_Language = 0;
                btn_langRU.setBackgroundResource(R.drawable.btn_lang_ru);
                btn_langEN.setBackgroundResource(R.drawable.btn_lang_en_selected);

                ChanceLanguage(MainActivity.prf_Language);

                ed.putInt("prfLanguage", MainActivity.prf_Language);
                ed.apply();

            }
        });


        btn_langRU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.prf_Language = 1;
                btn_langRU.setBackgroundResource(R.drawable.btn_lang_ru_selected);
                btn_langEN.setBackgroundResource(R.drawable.btn_lang_en);

                ChanceLanguage(MainActivity.prf_Language);

                ed.putInt("prfLanguage", MainActivity.prf_Language);
                ed.apply();

            }
        });


        nav4_top_1create.setOnClickListener(new View.OnClickListener() {
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


        nav4_top_2todolist.setOnClickListener(new View.OnClickListener() {
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


        nav4_top_3calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                MainActivity.prf_ListViewPosition = 0;
                ListView_DATE = "";

                Fragment targetFragment = new fragmentCalendar();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, targetFragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });




        btn_clear_all_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putInt("DELETETYPE", 0);

                Dialog_DeleteAllData dialog = new Dialog_DeleteAllData();
                dialog.setTargetFragment(fragmentSettings.this, 1);
                dialog.setStyle(Dialog_DeleteAllData.STYLE_NO_TITLE, 0);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "Delete All Data");

            }
        });


        btn_clear_database.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putInt("DELETETYPE", 1);

                Dialog_DeleteAllData dialog = new Dialog_DeleteAllData();
                dialog.setTargetFragment(fragmentSettings.this, 1);
                dialog.setStyle(Dialog_DeleteAllData.STYLE_NO_TITLE, 0);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "Delete All Data");

            }
        });



        stgs_vibra_label.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (prf_VibraFeedback == 0) {
                    prf_VibraFeedback = 1;
                    stgs_vibra_toggle.setBackgroundResource(R.drawable.toggle_on);

                    Vibrator vibra = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibra.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibra.vibrate(MainActivity.prf_VibraDuration);
                    }

                } else {
                    prf_VibraFeedback = 0;
                    stgs_vibra_toggle.setBackgroundResource(R.drawable.toggle_off);
                }

                ed.putInt("prfVibra", MainActivity.prf_VibraFeedback);
                ed.apply();
            }
        });

        stgs_vibra_toggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (prf_VibraFeedback == 0) {
                    prf_VibraFeedback = 1;
                    stgs_vibra_toggle.setBackgroundResource(R.drawable.toggle_on);

                    Vibrator vibra = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibra.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibra.vibrate(MainActivity.prf_VibraDuration);
                    }

                } else if (prf_VibraFeedback == 1) {
                    prf_VibraFeedback = 0;
                    stgs_vibra_toggle.setBackgroundResource(R.drawable.toggle_off);
                }

                ed.putInt("prfVibra", MainActivity.prf_VibraFeedback);
                ed.apply();
            }
        });



        return view;
    }


    public void ChanceLanguage(int lang){

        switch (MainActivity.prf_Language) {
            case 0:
                btn_langRU.setBackgroundResource(R.drawable.btn_lang_ru);
                btn_langEN.setBackgroundResource(R.drawable.btn_lang_en_selected);

                nav4_top_1create_text.setText(R.string.create);
                nav4_top_2todolist_text.setText(R.string.todos);
                nav4_top_4settings_text.setText(R.string.settings);
                settings_text1.setText(R.string.general);
                settings_text2.setText(R.string.data);
                btn_clear_all_data.setText(R.string.clear_all_data);
                langauge_name.setText(R.string.language);
                stgs_view_label.setText(R.string.view_on_todos_screen);
                stgs_view_full.setText(R.string.full);
                stgs_view_compact.setText(R.string.compact);
                settings_appl.setText(R.string.application);

                settings_rate_the_app.setText(R.string.rate_the_app);
                settings_more_apps.setText(R.string.more_apps);
                nav4_top_3calendar_text.setText(R.string.calendar);

                btn_clear_database.setText(R.string.clear_database);

                stgs_vibra_label.setText(R.string.use_vibration_feedback);

                break;

            case 1:
                btn_langRU.setBackgroundResource(R.drawable.btn_lang_ru_selected);
                btn_langEN.setBackgroundResource(R.drawable.btn_lang_en);
                nav4_top_1create_text.setText(R.string.ru_create);
                nav4_top_2todolist_text.setText(R.string.ru_todos);
                nav4_top_4settings_text.setText(R.string.ru_settings);
                settings_text1.setText(R.string.ru_general);
                settings_text2.setText(R.string.ru_data);
                btn_clear_all_data.setText(R.string.ru_clear_all_data);
                settings_appl.setText(R.string.ru_application);
                langauge_name.setText(R.string.ru_language);
                stgs_view_label.setText(R.string.ru_view_on_todos_screen);
                stgs_view_full.setText(R.string.ru_full);
                stgs_view_compact.setText(R.string.ru_compact);

                settings_rate_the_app.setText(R.string.ru_rate_the_app);
                settings_more_apps.setText(R.string.ru_more_apps);
                nav4_top_3calendar_text.setText(R.string.ru_calendar);

                btn_clear_database.setText(R.string.ru_clear_database);

                stgs_vibra_label.setText(R.string.ru_use_vibration_feedback);

                break;
        }
    }

}
