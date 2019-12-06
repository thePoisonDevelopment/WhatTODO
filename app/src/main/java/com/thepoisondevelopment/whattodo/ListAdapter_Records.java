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

public class ListAdapter_Records extends ArrayAdapter<ListView_Records> {


    private static final String TAG = "TODO Records";
    private Context mContext;
    private int mResource;

    public ListAdapter_Records(Context context, int resource, ArrayList<ListView_Records> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("ViewHolder")
    @Nullable
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {


        // date created
        String dbCreated = Objects.requireNonNull(getItem(position)).getRecordCreated();
        String dbPlanned = Objects.requireNonNull(getItem(position)).getRecordplanned();
        String dbDeadline = Objects.requireNonNull(getItem(position)).getRecordDeadline();

        String dbName = Objects.requireNonNull(getItem(position)).getRecordName();
        String dbLabel = Objects.requireNonNull(getItem(position)).getLabel();
        String dbLabelColor = Objects.requireNonNull(getItem(position)).getLabelColor();
        int dbPrio = Objects.requireNonNull(getItem(position)).getRecordPrio();
        int dbStatus = Objects.requireNonNull(getItem(position)).getRecordStatus();
        int dbTimeLog = Objects.requireNonNull(getItem(position)).getRecordTimeLog();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView lw_timeplanned = convertView.findViewById(R.id.lw_timeplanned);
        TextView lw_timedeadline = convertView.findViewById(R.id.lw_timedeadline);

        TextView lw_full_label = convertView.findViewById(R.id.lw_full_label);



        TextView lw_full_timelogged = convertView.findViewById(R.id.lw_full_timelogged);
        TextView lw_compact_timelogged = convertView.findViewById(R.id.lw_compact_timelogged);

        if (MainActivity.prf_FullView == 0){
            lw_timeplanned.setVisibility(View.INVISIBLE);
            lw_timedeadline.setVisibility(View.INVISIBLE);
            lw_full_label.setVisibility(View.INVISIBLE);
            lw_full_timelogged.setVisibility(View.INVISIBLE);

        } else {
            lw_compact_timelogged.setVisibility(View.INVISIBLE);
        }

        TextView tvName = convertView.findViewById(R.id.lw_todoname);
        ImageView tvPrio = convertView.findViewById(R.id.lw_prio);
        ImageView tv_icon = convertView.findViewById(R.id.lw_icon);
        View tvColorA = convertView.findViewById(R.id.lw_record_highlight_main1);
        View tvColorB = convertView.findViewById(R.id.lw_record_highlight_main2);
        View tvColor0 = convertView.findViewById(R.id.lw_record_highlight_back);


        switch (dbStatus) {
            case 0:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_0todo);
                tvColorA.setBackgroundColor(Color.parseColor("#4864dc"));
                tvColorB.setBackgroundColor(Color.parseColor("#4864dc"));
                tvColor0.setBackgroundColor(Color.parseColor("#d4ddf8"));
            break;
            case 1:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_1wip);
                tvColorA.setBackgroundColor(Color.parseColor("#ffff5a"));
                tvColorB.setBackgroundColor(Color.parseColor("#ffff5a"));
                tvColor0.setBackgroundColor(Color.parseColor("#ffffda"));
                break;
            case 2:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_2blocked);
                tvColorA.setBackgroundColor(Color.parseColor("#ff4e4e"));
                tvColorB.setBackgroundColor(Color.parseColor("#ff4e4e"));
                tvColor0.setBackgroundColor(Color.parseColor("#ffd6d6"));
                break;
            case 3:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_3done);
                tvColorA.setBackgroundColor(Color.parseColor("#4edc7d"));
                tvColorB.setBackgroundColor(Color.parseColor("#4edc7d"));
                tvColor0.setBackgroundColor(Color.parseColor("#d6f8e4"));
                break;
            case 4:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_4planned);
                tvColorA.setBackgroundColor(Color.parseColor("#ff4ecb"));
                tvColorB.setBackgroundColor(Color.parseColor("#ff4ecb"));
                tvColor0.setBackgroundColor(Color.parseColor("#ffd6fd"));
                break;
            case 5:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_5onhold);
                tvColorA.setBackgroundColor(Color.parseColor("#757575"));
                tvColorB.setBackgroundColor(Color.parseColor("#757575"));
                tvColor0.setBackgroundColor(Color.parseColor("#aaaaaa"));
                break;
            case 6:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_6rejected);
                tvColorA.setBackgroundColor(Color.parseColor("#c61e1e"));
                tvColorB.setBackgroundColor(Color.parseColor("#c61e1e"));
                tvColor0.setBackgroundColor(Color.parseColor("#f1b5b5"));
                break;

            default:
                tv_icon.setBackgroundResource(R.drawable.todo_icon_0todo);
                tvColorA.setBackgroundColor(Color.parseColor("#0024ff"));
                tvColorB.setBackgroundColor(Color.parseColor("#0024ff"));
                tvColor0.setBackgroundColor(Color.parseColor("#d4ddf8"));
            break;
        }


        switch (dbPrio) {
            case 1: tvPrio.setBackgroundResource(R.drawable.todo_prio_1);break;
            case 2: tvPrio.setBackgroundResource(R.drawable.todo_prio_2);break;
            case 3: tvPrio.setBackgroundResource(R.drawable.todo_prio_3);break;
            case 4: tvPrio.setBackgroundResource(R.drawable.todo_prio_4);break;
            case 5: tvPrio.setBackgroundResource(R.drawable.todo_prio_5);break;
            default: tvPrio.setBackgroundResource(R.drawable.todo_prio_3);break;
        }

//        String dbCreated_aux;
//        dbCreated_aux = dbCreated.replaceAll("MON", "-");
//        dbCreated_aux = dbCreated_aux.replaceAll("TUE", "-");
//        dbCreated_aux = dbCreated_aux.replaceAll("WED", "-");
//        dbCreated_aux = dbCreated_aux.replaceAll("THU", "-");
//        dbCreated_aux = dbCreated_aux.replaceAll("FRI", "-");
//        dbCreated_aux = dbCreated_aux.replaceAll("SAT", "-");
//        dbCreated_aux = dbCreated_aux.replaceAll("SUN", "-");

        lw_timeplanned.setText("P: " + dbPlanned);
        lw_timedeadline.setText("D: " + dbDeadline);

        if (dbTimeLog >= 60) {
            int hours = dbTimeLog / 60;
            int mins = dbTimeLog % 60;
            lw_full_timelogged.setText("" + hours + "h " + mins + "m");
            lw_compact_timelogged.setText("" + hours + "h " + mins + "m");

        } else {
            lw_compact_timelogged.setText("" + dbTimeLog + "m");
            lw_full_timelogged.setText("" + dbTimeLog + "m");
        }

        tvName.setText(dbName); //TODO Kastil.. remove before release
        if (dbLabelColor.equals("ff0000")) dbLabelColor = "#ff0000";

        if (dbLabel.isEmpty()) {
            lw_full_label.setVisibility(View.INVISIBLE);
        } else{
            if (!dbLabelColor.equals("#ffffff"))  //check if the color is not white - paint the label
            {
                lw_full_label.setBackgroundColor(Color.parseColor(dbLabelColor));
            }
            lw_full_label.setText(" " + dbLabel + " ");
        }

        return convertView;

    }


}
