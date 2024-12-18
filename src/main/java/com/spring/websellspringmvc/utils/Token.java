package com.spring.websellspringmvc.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Token {
    public static String generateToken(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static Timestamp addTime(LocalDateTime dateTime, String duration) {
        // Format duration to Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime durationTime = LocalTime.parse(duration, formatter);

        // CurrentTime + durationTime
        LocalDateTime newDateTime = dateTime
                .plusHours(durationTime.getHour())
                .plusMinutes(durationTime.getMinute())
                .plusSeconds(durationTime.getSecond());

        Timestamp result = Timestamp.valueOf(newDateTime);
        return result;
    }

    public static String generateOTPCode(){
        int random = (int) (Math.random() * 1000000);
        return String.format("%06d", random);
    }

}
