package com.mobdeve.s14.group24.everyday;

import java.util.Calendar;

public class Date {
    private static final String[] monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day, month, year;

    public Date() {
        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
    }

    public Date(String stringDate) {
        String[] dateParts = stringDate.split("/");
        year = Integer.parseInt(dateParts[0]);
        month = Integer.parseInt(dateParts[1]);
        day = Integer.parseInt(dateParts[2]);
    }

    public Date(int year, int month, int day) {
        this.year = year;
        this.day = day;
        this.month = month;
    }

    public String toStringFull() {
        return monthNames[month] + " " + day + ", " + year;
    }

    public String toStringNoYear() {
        return monthNames[month] + " " + day;
    }

    public String toStringDB() {
        return year + "/" + month + "/" + day;
    }

    public String toStringFileName() {
        return year + "_" + month + "_" + day + ".jpg";
    }
}