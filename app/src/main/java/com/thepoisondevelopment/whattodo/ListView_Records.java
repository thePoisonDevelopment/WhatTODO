package com.thepoisondevelopment.whattodo;

public class ListView_Records {

    private String record_created;
    private String record_deadline;
    private String record_planned;

    private String record_name;

    private int record_icon;
    private int record_prio;
    private int time_log;

    private String label;
    private String label_color;

    public ListView_Records(
            String record_created,
            String record_planned,
            String record_deadline,

            String record_name,

            int record_icon,
            int record_prio,
            int time_log,

            String label,
            String label_color
    ) {

        this.record_created = record_created;
        this.record_planned = record_planned;
        this.record_deadline = record_deadline;
        this.record_name = record_name;

        this.record_icon = record_icon;
        this.record_prio = record_prio;
        this.time_log = time_log;

        this.label = label;
        this.label_color = label_color;


    }


    public String getRecordCreated(){
        return this.record_created;
    }

    public String getRecordDeadline(){
        return this.record_deadline;
    }

    public String getRecordplanned(){
        return this.record_planned;
    }

    public String getRecordName(){
        return this.record_name;
    }

    public String getLabel(){
        return this.label;
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

    public int getRecordTimeLog(){
        return this.time_log;
    }
}