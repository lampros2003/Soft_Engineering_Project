package com.reservation;

import java.time.*;
import java.util.*;


public class ReservationTimeService {

    /* ------------ configurable “business rules” ------------ */

    private static final LocalTime OPENING_TIME  = LocalTime.of(18, 0);
    private static final LocalTime CLOSING_TIME  = LocalTime.of(22, 0);
    private static final int       SLOT_MINUTES  = 30;

    /* ------------ in-memory “database” of already-booked slots ------------ */

    /** date → list of start times that are no longer free */
    private static final Map<LocalDate, List<LocalTime>> booked = new HashMap<>();


    public List<LocalTime> getAvailableTimes(LocalDate date, int partySize) {

        if (date == null || partySize <= 0) {
            return Collections.emptyList();
        }

        /* Example rule: the restaurant is closed on Mondays */
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return Collections.emptyList();
        }

        List<LocalTime> result = new ArrayList<>();
        for (LocalTime t = OPENING_TIME;
             !t.isAfter(CLOSING_TIME.minusMinutes(SLOT_MINUTES));
             t = t.plusMinutes(SLOT_MINUTES)) {

            if (!isBooked(date, t)) {
                result.add(t);
            }
        }
        return result;
    }


    public static void registerBooking(LocalDate date, LocalTime time) {
        booked.computeIfAbsent(date, d -> new ArrayList<>()).add(time);
    }

    private static boolean isBooked(LocalDate date, LocalTime time) {
        return booked.getOrDefault(date, Collections.emptyList()).contains(time);
    }
}
