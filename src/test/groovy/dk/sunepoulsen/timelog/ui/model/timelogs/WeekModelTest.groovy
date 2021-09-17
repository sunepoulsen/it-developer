package dk.sunepoulsen.timelog.ui.model.timelogs

import org.junit.Test

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class WeekModelTest {
    @Test
    void "Create WeekModels from of()"() {
        assert WeekModel.of(1, 2021).firstDate().toString() == '2021-01-04'
        assert WeekModel.of(37, 2021).firstDate().toString() == '2021-09-13'
        assert WeekModel.of(53, 2020).firstDate().toString() == '2020-12-28'
    }

    @Test
    void "Create WeekModels from now()"() {
        Clock clock = Clock.fixed(Instant.parse('2021-07-10T10:15:30.00Z'), ZoneId.of('Z'))
        assert WeekModel.now(clock).firstDate().toString() == '2021-07-05'
        assert WeekModel.now().firstDate().toString() == WeekModel.now(Clock.systemUTC()).firstDate().toString()
    }
}
