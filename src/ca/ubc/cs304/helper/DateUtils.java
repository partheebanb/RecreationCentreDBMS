package ca.ubc.cs304.helper;

import java.sql.Date;
import java.util.Calendar;

public class DateUtils {
    private static int minutesIncrement = 15;

    public static Date normalize(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE) / minutesIncrement) * minutesIncrement);

        return new Date(calendar.getTime().getTime());
    }
}
