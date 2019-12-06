package com.thepoisondevelopment.whattodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Dialog_DeleteAllData extends DialogFragment {
    //MyCustomDialog
    private static final String TAG = "DELETE ALL DATA";

    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;

    TextView mActionDelete_deletealldata, mActionCancel_deletealldata, clear_data_text;

    public DBHelper dbHelper;

    int bdl_DeleteType;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_clearalldata, container, false);

        mActionDelete_deletealldata = view.findViewById(R.id.button_deletealldata_delete);
        mActionCancel_deletealldata = view.findViewById(R.id.button_deletealldata_cancel);
        clear_data_text = view.findViewById(R.id.clear_data_text);

        View cleardata_background_sub = view.findViewById(R.id.cleardata_background_sub);
        cleardata_background_sub.setBackgroundColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));

        TextView caption_cleardata = view.findViewById(R.id.caption_cleardata);
        TextView clear_data_text = view.findViewById(R.id.clear_data_text);

        assert getArguments() != null;
        bdl_DeleteType = getArguments().getInt("DELETETYPE");

        switch (MainActivity.prf_Language) {
            case 0:

                if (bdl_DeleteType == 0) {   // DELETE ALL
                    caption_cleardata.setText(R.string.caption_clear_all_data);
                    clear_data_text.setText(R.string.text_delete_data);
                    mActionDelete_deletealldata.setText(R.string.button_delete);
                } else
                if (bdl_DeleteType == 1) {  // CLEAR
                    caption_cleardata.setText(R.string.caption_clear_database);
                    clear_data_text.setText(R.string.text_clear_database);
                    mActionDelete_deletealldata.setText(R.string.button_clear);
                }

                mActionCancel_deletealldata.setText(R.string.button_cancel);

                break;
            case 1:

                if (bdl_DeleteType == 0) {   // DELETE ALL
                    caption_cleardata.setText(R.string.ru_caption_clear_all_data);
                    clear_data_text.setText(R.string.ru_text_delete_data);
                    mActionDelete_deletealldata.setText(R.string.ru_button_delete);
                }else
                if (bdl_DeleteType == 1) {  // CLEAR
                    caption_cleardata.setText(R.string.ru_caption_clear_database);
                    clear_data_text.setText(R.string.ru_text_clear_database);
                    mActionDelete_deletealldata.setText(R.string.ru_button_clear);
                }

                mActionCancel_deletealldata.setText(R.string.ru_button_cancel);
                break;
        }

        mActionCancel_deletealldata.setTextColor(Color.parseColor(MainActivity.prf_ColorThemeAux1));

        // OK CANCEL buttons
        mActionCancel_deletealldata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getDialog().dismiss();
            }
        });

        mActionDelete_deletealldata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //DELETING ALL DATA from data base
                dbHelper = new DBHelper(getActivity());
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                if (bdl_DeleteType == 0) {   // DELETE ALL

                    database.execSQL("delete from " + DBHelper.TABLE_TODO);

                    switch (MainActivity.prf_Language) {
                        case 0:
                            Toast.makeText(getActivity(), R.string.alldatadeleted, Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), R.string.ru_alldatadeleted, Toast.LENGTH_SHORT).show();
                            break;
                    }

                } else
                if (bdl_DeleteType == 1) {  // CLEAR

                    database.execSQL("delete from " + DBHelper.TABLE_TODO + " where STATUS IN (3,6)");

                    switch (MainActivity.prf_Language) {
                        case 0:
                            Toast.makeText(getActivity(), R.string.database_cleared, Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), R.string.ru_database_cleared, Toast.LENGTH_SHORT).show();

                            break;
                    }
                }

                dbHelper.close();

                    switch (MainActivity.prf_Language) {
                        case 0:
                            Toast.makeText(getActivity(), R.string.alldatadeleted, Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), R.string.ru_alldatadeleted, Toast.LENGTH_SHORT).show();
                            break;
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

                getDialog().dismiss();

            }
        });


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        } catch (ClassCastException e) {

            Log.e(TAG, "On Attach: Class Exception : " + e.getMessage());
        }

    }


}
