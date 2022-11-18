package com.quarry.management.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String getDateAsString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }


    public static Date getDateAlone(Date date) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(getDateAsString(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getPreviousDate(Date date, Integer days) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return getDateAlone(calendar.getTime());
    }
}
