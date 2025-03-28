package com.ukma.pr2.utils;

import com.ukma.pr2.entity.EventEntity;
import com.ukma.pr2.entity.TicketEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static EventEntity getRandomEvent() {
        EventEntity event = new EventEntity();
        event.setId(getRandomIntInRange(1, 1000));
        event.setName(getRandomString(7));
        event.setAddress(getRandomString(15));
        YearMonth yearMonth = YearMonth.of(getRandomIntInRange(2022,2025), getRandomIntInRange(1, 12));
        int day = getRandomIntInRange(1, yearMonth.lengthOfMonth());
        event.setDate(LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day));
        event.setStart_time(LocalTime.of(getRandomIntInRange(8,20), 0));
        event.setEnd_time(LocalTime.of(event.getStart_time().getHour() + 1, 0));
        event.setPrice(getRandomIntInRange(100, 700));
        return event;
    }

    public static String getRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder res = new StringBuilder();
        Random rnd = new Random();
        while (res.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            res.append(SALTCHARS.charAt(index));
        }
        return res.toString();
    }

    public static Integer getRandomIntInRange(int from, int till) {
        return ThreadLocalRandom.current().nextInt(from, till + 1);
    }

    public static TicketEntity getRandomTicket(EventEntity event) {
        TicketEntity ticket = new TicketEntity();
        ticket.setId(getRandomIntInRange(1, 10000));
        ticket.setEvent(event);
        ticket.setOwnerName(getRandomString(8));
        ticket.setOwnerInstagram(getRandomString(8));
        ticket.setOwnerAge(getRandomIntInRange(17,90));
        ticket.setIsVIP(ThreadLocalRandom.current().nextBoolean());
        return ticket;
    }
}
