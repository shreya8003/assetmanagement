package com.wip.assetmanagementsystem.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

   
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    
    public static LocalDate stringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = 
            DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateStr, formatter);
    }

   
    public static String localDateToString(LocalDate date) {
        if (date == null) return null;
        DateTimeFormatter formatter = 
            DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }

   
    public static LocalDate sqlDateToLocalDate(Date date) {
        if (date == null) return null;
        return date.toLocalDate();
    }

   
    public static Date localDateToSqlDate(LocalDate date) {
        if (date == null) return null;
        return Date.valueOf(date);
    }

   
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

   
    public static boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

   
    public static boolean isFutureDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
}