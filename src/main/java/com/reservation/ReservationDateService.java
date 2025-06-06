
// ReservationDateService.java
package com.reservation;

import com.tables.TableStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple in-memory “blackboard” that every controller can write to and read from
 * while the reservation wizard is open.
 *
 * All fields are <b>static</b>; nothing is persisted until {@link #saveReservation()}
 * succeeds.  Replace the stubbed DAO call inside that method with your real
 * database logic.
 */
public final class ReservationDateService {

    /* ── core reservation data ─────────────────────────────────────── */
    private static LocalDate reservationDate;
    private static LocalTime reservationTime;
    private static int       numberOfPeople;
    private static int       tableNumber;

    /* ── guest-entered details ─────────────────────────────────────── */
    private static String    firstName;
    private static String    lastName;
    private static String    telephoneNumber;
    private static String    arrangement;          // seating preference

    /* ── setters used by wizard steps ──────────────────────────────── */
    public static void setReservationDate(LocalDate date)    { reservationDate = date; }
    public static void setReservationTime(LocalTime time)    { reservationTime = time; }
    public static void setNumberOfPeople(int n)              { numberOfPeople = n;     }
    public static void setTableNumber(int t)                 { tableNumber    = t;     }

    /** Called by DetailsController */
    public static void setCustomerInfo(String first,
                                       String last,
                                       String phone,
                                       String arrang) {
        firstName       = first;
        lastName        = last;
        telephoneNumber = phone;
        arrangement     = arrang;
    }

    /* ── getters used by later screens ─────────────────────────────── */
    public static LocalDate  getReservationDate()  { return reservationDate;  }
    public static LocalTime  getReservationTime()  { return reservationTime;  }
    public static int        getNumberOfPeople()   { return numberOfPeople;   }
    public static int        getTableNumber()      { return tableNumber;      }

    public static String getFirstName()            { return firstName;        }
    public static String getLastName()             { return lastName;         }
    public static String getTelephoneNumber()      { return telephoneNumber;  }
    public static String getArrangement()          { return arrangement;      }

    /* ── persist everything to your DB ─────────────────────────────── */
    public static boolean saveReservation() {
        try {
            /* Replace with your real DAO / JDBC code: */
            // ReservationDao.insert(reservationDate,
            //                       reservationTime,
            //                       numberOfPeople,
            //                       tableNumber,
            //                       firstName,
            //                       lastName,
            //                       telephoneNumber,
            //                       arrangement);

            return true;  // success
        } catch (Exception ex) {
            ex.printStackTrace();    // log & let caller show an error page
            return false;
        }
    }

    /* ── optional: clear all fields when the wizard finishes ───────── */
    public static void reset() {
        reservationDate  = null;
        reservationTime  = null;
        numberOfPeople   = 0;
        tableNumber      = 0;

        firstName        = null;
        lastName         = null;
        telephoneNumber  = null;
        arrangement      = null;
    }

    /* No instances allowed */
    public ReservationDateService() { }

    public static String getSummaryText() {
        return String.format(
                "Reservation for %s %s on %s at %s — table %d, %d people (%s).  Phone: %s",
                firstName  == null ? "" : firstName,
                lastName   == null ? "" : lastName,
                reservationDate == null ? "??" :
                        reservationDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                reservationTime == null ? "??:??" :
                        reservationTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                tableNumber,
                numberOfPeople,
                arrangement == null ? "standard" : arrangement,
                telephoneNumber == null ? "-" : telephoneNumber
        );
    }

    public static void clear() {
        reservationDate   = null;
        reservationTime   = null;
        numberOfPeople    = 0;
        tableNumber = 0;     // or tableNumber, if that’s your field

        firstName         = null;
        lastName          = null;
        telephoneNumber   = null;
        arrangement       = null;
    }

    private static final List<TableStatus> ALL_TABLES = List.of(
//            new TableStatus(1, 2),
//            new TableStatus(2, 2),
//            new TableStatus(3, 4),
//            new TableStatus(4, 4),
//            new TableStatus(5, 4),
//            new TableStatus(6, 6),
//            new TableStatus(7, 6),
//            new TableStatus(8, 8)
    );
    private static final Set<Integer> BOOKED_TABLE_IDS = new HashSet<>();

    public static List<TableStatus> getAvailableTables(int numberOfPeople) {

        List<TableStatus> free = new ArrayList<>();

        for (TableStatus tbl : ALL_TABLES) {
            boolean fits = tbl.getSeatingCapacity() >= numberOfPeople;

            boolean unused = !BOOKED_TABLE_IDS.contains(tbl.getId());

            if (fits && unused) {
                free.add(tbl);
            }
        }
        return free;
    }

    /* call this helper right after saveReservation() succeeds         */
    private static void markTableAsBooked() {
        BOOKED_TABLE_IDS.add(tableNumber);
    }

   
}

