package com.mobdeve.s14.group24.everyday;

import java.util.Calendar;

public class CustomDate {
    private static final String[] monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day, month, year;

    //Custom date constructor
    public CustomDate() {
        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
    }

    //Splits the generated date instance to day, month, and years in integer format
    public CustomDate(String stringDate) {
        String[] dateParts = stringDate.split("/");
        year = Integer.parseInt(dateParts[0]);
        month = Integer.parseInt(dateParts[1]);
        day = Integer.parseInt(dateParts[2]);
    }

    //Overloaded method that sets values of date instance variables according to arguments in constructor
    public CustomDate(int year, int month, int day) {
        this.year = year;
        this.day = day;
        this.month = month;
    }

    //Returns a string that represents formatted date with the month as string, and day and year as integers
    public String toStringFull() {
        return monthNames[month] + " " + day + ", " + year;
    }

    //Returns a string that represents formatted date without the year, and with the month as string and day as integer
    public String toStringNoYear() {
        return monthNames[month] + " " + day;
    }

    //Returns a string that represents a YYYY-MM-DD format
    public String toString () {
        return year + "-" + String.format("%02d" , month + 1) + "-" + String.format("%02d" , day);
    }

    //Returns a string that represents a YYYY/MM/DD format for storage in the database
    public String toStringDB() {
        return year + "/" + month + "/" + day;
    }

    //Returns a string that represents a YYYY_MM_DD format for file naming convention
    public String toStringFileName() {
        return year + "_" + month + "_" + day + ".jpg";
    }
}
