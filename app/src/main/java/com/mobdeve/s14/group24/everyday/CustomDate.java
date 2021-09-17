package com.mobdeve.s14.group24.everyday;

import java.util.Calendar;

public class CustomDate {
    private static final String[] monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day, month, year;

    public CustomDate() {
        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
    }

    public CustomDate(String stringDate) {
        String[] dateParts = stringDate.split("/");
        year = Integer.parseInt(dateParts[0]);
        month = Integer.parseInt(dateParts[1]);
        day = Integer.parseInt(dateParts[2]);
    }

    public CustomDate(int year, int month, int day) {
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

    public String toString () {
        return year + "-" + String.format("%02d" , month + 1) + "-" + String.format("%02d" , day);
    }

    public String toStringDB() {
        return year + "/" + month + "/" + day;
    }

    public String toStringFileName() {
        return year + "_" + month + "_" + day + ".jpg";
    }
}