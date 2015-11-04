package me.zingle.atlas_adoption.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.zingle.atlas_adoption.R;

/**
 * Created by SLAVA 09 2015.
 */
public class TimeUtils {

    public static final int TIME_HOURS_24 = 24 * 60 * 60 * 1000;
    public static final SimpleDateFormat sdfDayOfWeek = new SimpleDateFormat("EEE, LLL dd,");

    /** Today, Yesterday, Weekday or Weekday + date */
    public static String formatTimeDay(Context context,Date sentAt) {

        final String[] TIME_WEEKDAYS_NAMES = context.getResources().getStringArray(R.array.weekdays_names); /*new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}*/

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long todayMidnight = cal.getTimeInMillis();
        long yesterMidnight = todayMidnight - TIME_HOURS_24;
        long weekAgoMidnight = todayMidnight - TIME_HOURS_24 * 7;

        String timeBarDayText = null;
        if (sentAt.getTime() > todayMidnight) {
            timeBarDayText = "Today";
        } else if (sentAt.getTime() > yesterMidnight) {
            timeBarDayText = "Yesterday";
        } else if (sentAt.getTime() > weekAgoMidnight) {
            cal.setTime(sentAt);
            timeBarDayText = TIME_WEEKDAYS_NAMES[cal.get(Calendar.DAY_OF_WEEK) - 1];
        } else {
            timeBarDayText = sdfDayOfWeek.format(sentAt);
        }
        return timeBarDayText;
    }
}
