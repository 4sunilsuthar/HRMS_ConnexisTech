package com.lms.admin.lms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 21-03-2018.
 * created to convert the date strings into various formats
 * convert db dateFormat into ui dateFormat and vice versa
 */

public class DateHelper {
    private SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("hi", "in"));
    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat uiDateFormat = new SimpleDateFormat("dd MMMM", Locale.US);

    public String convertDateToUI(String dbDate) throws ParseException {
        //format date to UI format
        //I have a string so lets parse it to a date of a corresponding format
        Date dbDates = dbDateFormat.parse(dbDate);
        try {
            return uiDateFormat.format(dbDates);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String convertDateToDB(Date uiDate) {
        //format date to database format
        try {
            return dbDateFormat.format(uiDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateDifferences(String minDate, String maxDate) {
        String difference = "nil";
        return difference;
    }

    /**
     * Get a diff between two dates
     *
     * @param date the post date
     * @param time the post time
     * @return the diff value, in the minutes, hours, days, or date
     */
    public String getDateDiff(String date, String time) {
        try {
            final int SECOND = 1000;
            final int MINUTE = 60 * SECOND;
            final int HOUR = 60 * MINUTE;
            final int DAY = 24 * HOUR;

            //get date and time string and calculate difference bw now and post date time
            //get now value
            String postDate = date + " " + time;
            Date pDate = defaultDateFormat.parse(postDate);

            String currentDate = defaultDateFormat.format(new Date().getTime());
            Date cDate = defaultDateFormat.parse(currentDate);
//            Log.e(TAG, "Current date is : " + currentDate);
//            Log.e(TAG, "Current date timestamp is : " + cDate.getTime());
//            Log.e(TAG, "Post date timestamp is : " + pDate.getTime());
            //find difference between postTime and currentTime
            long ms2 = 43200000; //adding 12 hours (with milliseconds value ) to solve the problem of AM/PM
            long ms = Math.abs((cDate.getTime() + ms2) - pDate.getTime());
//            Log.e(TAG, "difference bw timestamps : " + ms);


            StringBuffer text = new StringBuffer("");

            // code for getting the locale date format like hindi or japanese
//            Log.e(TAG, " date and month is @@ -> " + new SimpleDateFormat("dd MMMM",new Locale("hi","IN")).format(postDate));

            if (ms > 4 * DAY) {
                text.append(new SimpleDateFormat("dd MMMM | hh:mm a", Locale.ENGLISH).format(pDate));//showing date and time directly
            } else if (ms > DAY) {
                if (ms / DAY == 1)
                    text.append(" Yesterday");
                else
                    text.append(ms / DAY).append(" DAYS AGO");
            } else if (ms > HOUR) {
                if (ms / HOUR == 1)
                    text.append(ms / HOUR).append(" HOUR AGO");
                else
                    text.append(ms / HOUR).append(" HOURS AGO");
            } else if (ms > MINUTE) {
                if (ms / MINUTE == 1)
                    text.append(ms / MINUTE).append(" MINUTE AGO");
                else
                    text.append(ms / MINUTE).append(" MINUTES AGO");
            } else if (ms > SECOND) {
                text.append(" JUST NOW");
            }
//            Log.e(TAG, "time is " + text);
            return String.valueOf(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //brought from LeaveRequestFragment
    private String changeDateFormat(Date date) {
        //format date to database format
        try {
            return dbDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //brought from LeaveRequestFragment
    //function to calculate total leave days from two given dates
    public int daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        int totalLeaveDuration = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start) + 1);
//        return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start)+1);

        //logic to exclude weekends from total leave days
        int day1 = startDate.get(Calendar.DAY_OF_WEEK);
//        Log.e(TAG, "startDate Day is : " + day1);
        int totalWorkingDays = 0;
        for (int i = 0; i <= totalLeaveDuration; i++, day1++) {
            if (day1 != Calendar.SATURDAY && day1 != Calendar.SUNDAY) {
                totalWorkingDays += 1;//=> ++
            }
            if (day1 == 7) {
                day1 = 0;
            }
        }
        return totalWorkingDays;

    }

}
