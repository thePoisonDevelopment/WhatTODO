package com.thepoisondevelopment.whattodo;

public class ListView_Calendar {

    private int record_prio;
    private int record_icon;
    private String record_name;
    private String label_color;
    private String record_planned;
    private String record_deadline;


    public ListView_Calendar(
            String record_planned,
            String record_deadline,

            String record_name,

            int record_icon,
            int record_prio,

            String label_color
    ) {

        this.record_planned = record_planned;
        this.record_deadline = record_deadline;
        this.record_name = record_name;

        this.record_icon = record_icon;
        this.record_prio = record_prio;
        this.label_color = label_color;

    }


    public String getRecordplanned(){
        return this.record_planned;
    }

    public String getRecordDeadline(){
        return this.record_deadline;
    }

    public String getRecordName(){
        return this.record_name;
    }

    public String getLabelColor(){
        return this.label_color;
    }

    public int getRecordStatus(){
        return this.record_icon;
    }

    public int getRecordPrio(){
        return this.record_prio;
    }

}
