package com.thepoisondevelopment.whattodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class ListAdapter_Calendar extends ArrayAdapter<ListView_Calendar> {


    private static final String TAG = "TODO Records";
    private Context mContext;
    private int mResource;

    public ListAdapter_Calendar(Context context, int resource, ArrayList<ListView_Calendar> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @Nullable
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // date created
        String dbPlanned = Objects.requireNonNull(getItem(position)).getRecordplanned();
        String dbDeadline = Objects.requireNonNull(getItem(position)).getRecordDeadline();
        String dbName = Objects.requireNonNull(getItem(position)).getRecordName();
        String dbLabelColor = Objects.requireNonNull(getItem(position)).getLabelColor();
        int dbPrio = Objects.requireNonNull(getItem(position)).getRecordPrio();
        int dbStatus = Objects.requireNonNull(getItem(position)).getRecordStatus();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

//        TextView lw_timeplanned = convertView.findViewById(R.id.lw_timeplanned);
//        TextView lw_timedeadline = convertView.findViewById(R.id.lw_timedeadline);

        ImageView lw_calendar_prio = convertView.findViewById(R.id.lw_calendar_prio);
        ImageView lw_calendar_icon = convertView.findViewById(R.id.lw_calendar_icon);
        TextView lw_calendar_todoname = convertView.findViewById(R.id.lw_calendar_todoname);

        View lw_calendar_label_color = convertView.findViewById(R.id.lw_calendar_label_color);

        View lw_calendar_highlight_back = convertView.findViewById(R.id.lw_calendar_highlight_back);
        View lw_calendar_highlight_main1 = convertView.findViewById(R.id.lw_calendar_highlight_main1);
        View lw_calendar_highlight_main2 = convertView.findViewById(R.id.lw_calendar_highlight_main2);


        switch (dbStatus) {
            case 0:
                lw_calendar_icon.setBackgroundResource(R.drawable.todo_icon_0todo);
                lw_calendar_highlight_main1.setBackgroundColor(Color.parseColor("#4864dc"));
                lw_calendar_highlight_main2.setBackgroundColor(Color.parseColor("#4864dc"));
                lw_calendar_highlight_back.setBackgroundColor(Color.parseColor("#d4ddf8"));
            break;
            case 1:
                lw_calendar_icon.setBackgroundResource(R.drawable.todo_icon_1wip);
                lw_calendar_highlight_main1.setBackgroundColor(Color.parseColor("#ffff5a"));
                lw_calendar_highlight_main2.setBackgroundColor(Color.parseColor("#ffff5a"));
                lw_calendar_highlight_back.setBackgroundColor(Color.parseColor("#ffffda"));
                break;
            case 2:
                lw_calendar_icon.setBackgroundResource(R.drawable.todo_icon_2blocked);
                lw_calendar_highlight_main1.setBackgroundColor(Color.parseColor("#ff4e4e"));
                lw_calendar_highlight_main2.setBackgroundColor(Color.parseColor("#ff4e4e"));
                lw_calendar_highlight_back.setBackgroundColor(Color.parseColor("#ffd6d6"));
                break;
            case 4:
                lw_calendar_icon.setBackgroundResource(R.drawable.todo_icon_4planned);
                lw_calendar_highlight_main1.setBackgroundColor(Color.parseColor("#ff4ecb"));
                lw_calendar_highlight_main2.setBackgroundColor(Color.parseColor("#ff4ecb"));
                lw_calendar_highlight_back.setBackgroundColor(Color.parseColor("#ffd6fd"));
                break;
            case 5:
                lw_calendar_icon.setBackgroundResource(R.drawable.todo_icon_5onhold);
                lw_calendar_highlight_main1.setBackgroundColor(Color.parseColor("#757575"));
                lw_calendar_highlight_main1.setBackgroundColor(Color.parseColor("#757575"));
                lw_calendar_highlight_main2.setBackgroundColor(Color.parseColor("#757575"));
                lw_calendar_highlight_back.setBackgroundColor(Color.parseColor("#aaaaaa"));
                break;
            case 6:

            default:
                lw_calendar_icon.setBackgroundResource(R.drawable.todo_icon_0todo);
                lw_calendar_highlight_main1.setBackgroundColor(Color.parseColor("#0024ff"));
                lw_calendar_highlight_main2.setBackgroundColor(Color.parseColor("#0024ff"));
                lw_calendar_highlight_back.setBackgroundColor(Color.parseColor("#d4ddf8"));
            break;
        }


        switch (dbPrio) {
            case 1: lw_calendar_prio.setBackgroundResource(R.drawable.todo_prio_1);break;
            case 2: lw_calendar_prio.setBackgroundResource(R.drawable.todo_prio_2);break;
            case 3: lw_calendar_prio.setBackgroundResource(R.drawable.todo_prio_3);break;
            case 4: lw_calendar_prio.setBackgroundResource(R.drawable.todo_prio_4);break;
            case 5: lw_calendar_prio.setBackgroundResource(R.drawable.todo_prio_5);break;
            default: lw_calendar_prio.setBackgroundResource(R.drawable.todo_prio_3);break;
        }

//        lw_timeplanned.setText("P: " + dbPlanned);
//        lw_timedeadline.setText("D: " + dbDeadline);


        lw_calendar_todoname.setText(dbName); //TODO Kastil.. remove before release
        if (dbLabelColor.equals("ff0000")) dbLabelColor = "#ff0000";
            lw_calendar_label_color.setBackgroundColor(Color.parseColor(dbLabelColor));


            switch (dbLabelColor){
                case "#ffffff": lw_calendar_label_color.setVisibility(View.INVISIBLE);break;
                case "#ffae00": lw_calendar_label_color.setBackgroundResource(R.drawable.label_color_orange_selected);break;
                case "#5f72ff": lw_calendar_label_color.setBackgroundResource(R.drawable.label_color_blue_selected);break;
                case "#f6ff00": lw_calendar_label_color.setBackgroundResource(R.drawable.label_color_yellow_selected);break;
                case "#00ff2a": lw_calendar_label_color.setBackgroundResource(R.drawable.label_color_green_selected);break;
                case "#ff0000": lw_calendar_label_color.setBackgroundResource(R.drawable.label_color_red_selected);break;
                case "#ff00d8": lw_calendar_label_color.setBackgroundResource(R.drawable.label_color_pink_selected);break;

                default:  lw_calendar_label_color.setVisibility(View.INVISIBLE);break;
            }

        return convertView;

    }


}
