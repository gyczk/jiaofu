package com.ryqg.jiaofu.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtils {
    public static LocalDateTime getEndOfDay(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().atTime(LocalTime.MAX);
    }
}
