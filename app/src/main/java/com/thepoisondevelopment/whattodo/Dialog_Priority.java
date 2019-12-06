package com.thepoisondevelopment.whattodo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.thepoisondevelopment.whattodo.MainActivity.prm_EditPriority;

public class Dialog_Priority extends DialogFragment {
    //MyCustomDialog
    private static final String TAG = "Edit Priority";

    public interface OnInputSelected_P{
        void sendInput_P(int input);
    }
    public OnInputSelected_P mOnInputSelected_P;

    TextView btn_ePrio1, btn_ePrio2, btn_ePrio3, btn_ePrio4, btn_ePrio5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_priority, container, false);

        assert getArguments() != null;

        btn_ePrio1 = view.findViewById(R.id.btn_ePrio1);
        btn_ePrio2 = view.findViewById(R.id.btn_ePrio2);
        btn_ePrio3 = view.findViewById(R.id.btn_ePrio3);
        btn_ePrio4 = view.findViewById(R.id.btn_ePrio4);
        btn_ePrio5 = view.findViewById(R.id.btn_ePrio5);

        switch (prm_EditPriority){

            case 1: btn_ePrio1.setBackgroundResource(R.drawable.todo_prio_1_selected); break;
            case 2: btn_ePrio2.setBackgroundResource(R.drawable.todo_prio_2_selected); break;
            case 3: btn_ePrio3.setBackgroundResource(R.drawable.todo_prio_3_selected); break;
            case 4: btn_ePrio4.setBackgroundResource(R.drawable.todo_prio_4_selected); break;
            case 5: btn_ePrio5.setBackgroundResource(R.drawable.todo_prio_5_selected); break;
            default: btn_ePrio3.setBackgroundResource(R.drawable.todo_prio_3_selected); break;

        }

        btn_ePrio1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                prm_EditPriority = 1;
                mOnInputSelected_P.sendInput_P(prm_EditPriority);
                getDialog().dismiss();
            }
        });

        btn_ePrio2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                prm_EditPriority = 2;
                mOnInputSelected_P.sendInput_P(prm_EditPriority);
                getDialog().dismiss();
            }
        });

        btn_ePrio3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                prm_EditPriority = 3;
                mOnInputSelected_P.sendInput_P(prm_EditPriority);
                getDialog().dismiss();
            }
        });

        btn_ePrio4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                prm_EditPriority = 4;
                mOnInputSelected_P.sendInput_P(prm_EditPriority);
                getDialog().dismiss();
            }
        });

        btn_ePrio5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                prm_EditPriority = 5;
                mOnInputSelected_P.sendInput_P(prm_EditPriority);
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected_P = (OnInputSelected_P) getTargetFragment();

        } catch (ClassCastException e) {
            Log.e(TAG, "On Attach: Class Exception : " + e.getMessage());
        }

    }

}
