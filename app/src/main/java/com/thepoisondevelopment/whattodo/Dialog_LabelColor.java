package com.thepoisondevelopment.whattodo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Dialog_LabelColor extends DialogFragment {
    //MyCustomDialog
    private static final String TAG = "Edit Priority";

    public interface OnInputSelected_LC{
        void sendInput_LC(String input);
    }

    public OnInputSelected_LC mOnInputSelected_LC;

    Button
            label_color_none,
            label_color_red,
            label_color_orange,
            label_color_blue,
            label_color_yellow,
            label_color_green,
            label_color_pink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_labelcolor, container, false);

        assert getArguments() != null;

        label_color_none = view.findViewById(R.id.label_color_none);
        label_color_red = view.findViewById(R.id.label_color_red);
        label_color_orange = view.findViewById(R.id.label_color_orange);
        label_color_blue = view.findViewById(R.id.label_color_blue);
        label_color_yellow = view.findViewById(R.id.label_color_yellow);
        label_color_green = view.findViewById(R.id.label_color_green);
        label_color_pink = view.findViewById(R.id.label_color_pink);

        TextView caption_datapicker = view.findViewById(R.id.caption_datapicker);

        switch (MainActivity.prm_LabelColor){
            case "NONE": label_color_none.setBackgroundResource(R.drawable.label_color_none_selected);break;
            case "RED": label_color_red.setBackgroundResource(R.drawable.label_color_red_selected);break;
            case "ORANGE": label_color_orange.setBackgroundResource(R.drawable.label_color_orange_selected);break;
            case "BLUE": label_color_blue.setBackgroundResource(R.drawable.label_color_blue_selected);break;
            case "YELLOW": label_color_yellow.setBackgroundResource(R.drawable.label_color_yellow_selected);break;
            case "GREEN": label_color_green.setBackgroundResource(R.drawable.label_color_green_selected);break;
            case "PINK": label_color_pink.setBackgroundResource(R.drawable.label_color_pink_selected);break;
        }



        switch (MainActivity.prf_Language)
        {
            case 0:

                caption_datapicker.setText(R.string.label_color);
                break;
            case 1:

                caption_datapicker.setText(R.string.ru_label_color);

                break;
        }







        label_color_none.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                MainActivity.prm_LabelColor = "#ffffff";
                mOnInputSelected_LC.sendInput_LC("NONE");
                getDialog().dismiss();
            }
        });

        label_color_red.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                MainActivity.prm_LabelColor = "#ff0000";
                mOnInputSelected_LC.sendInput_LC("RED");
                getDialog().dismiss();
            }
        });


        label_color_orange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity.prm_LabelColor = "#ffae00";
                mOnInputSelected_LC.sendInput_LC("ORANGE");
                getDialog().dismiss();
            }
        });

        label_color_blue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                MainActivity.prm_LabelColor = "#5f72ff";
                mOnInputSelected_LC.sendInput_LC("BLUE");
                getDialog().dismiss();
            }
        });

        label_color_yellow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                MainActivity.prm_LabelColor = "#f6ff00";
                mOnInputSelected_LC.sendInput_LC("YELLOW");
                getDialog().dismiss();
            }
        });

        label_color_green.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                MainActivity.prm_LabelColor = "#00ff2a";
                mOnInputSelected_LC.sendInput_LC("GREEN");
                getDialog().dismiss();
            }
        });

        label_color_pink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                MainActivity.prm_LabelColor = "#ff00d8";
                mOnInputSelected_LC.sendInput_LC("PINK");
                getDialog().dismiss();
            }
        });


        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected_LC = (OnInputSelected_LC) getTargetFragment();

        } catch (ClassCastException e) {
            Log.e(TAG, "On Attach: Class Exception : " + e.getMessage());
        }

    }

}
