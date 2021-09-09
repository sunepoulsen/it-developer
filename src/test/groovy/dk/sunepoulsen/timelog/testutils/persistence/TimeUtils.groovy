package dk.sunepoulsen.timelog.testutils.persistence

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class TimeUtils {
    static LocalTime randomTime(String fromTime, String toTime) {
        LocalTime from = LocalTime.parse(fromTime)
        LocalTime to = LocalTime.parse(toTime)

        Long maxMinutes = ChronoUnit.MINUTES.between(from, to)
        return from.plusMinutes(new Double(Math.random() * maxMinutes).longValue())
    }

    static LocalTime randomEntryTime() {
        return randomTime('06:00', '09:00')
    }

    static LocalTime randomLeaveTime() {
        return randomTime('15:00', '18:00')
    }

    static LocalDate yesterday() {
        return LocalDate.now().minusDays(1)
    }

    static LocalDate tomorrow() {
        return LocalDate.now().plusDays(1)
    }
}
