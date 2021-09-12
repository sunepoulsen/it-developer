package dk.sunepoulsen.timelog.ui.tasks.backend;


import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.ui.model.timelogs.DayRegistration;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeRegistration;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoadWeekRegistrationsTask extends BackendConnectionTask<ObservableList<TimeRegistration>> {
    private WeekModel weekModel;
    private Locale locale;

    public LoadWeekRegistrationsTask(BackendConnection connection, WeekModel weekModel, Locale locale) {
        super( connection );
        this.weekModel = weekModel;
        this.locale = locale;
    }

    @Override
    protected ObservableList<TimeRegistration> call() throws Exception {
        List<TimeRegistration> result = createDayRegistrations();

        return FXCollections.observableList(result);
    }

    private List<TimeLogModel> loadTimeLogs() {
        return connection.servicesFactory().newTimeLogsService().findByDates(weekModel.firstDate(), weekModel.lastDate());
    }

    private List<TimeRegistration> createDayRegistrations() {
        List<TimeRegistration> list = new ArrayList<>();

        for( LocalDate date = weekModel.firstDate(); !date.isAfter(weekModel.lastDate()); date = date.plusDays(1)) {
            DayRegistration dayRegistration = new DayRegistration(date, workingNorm(date), locale);
            list.add(dayRegistration);
        }

        return list;
    }

    private Double workingNorm(LocalDate date) {
        if (date.getDayOfWeek().getValue() < DayOfWeek.SATURDAY.getValue()) {
            return 5.0;
        }

        return null;
    }
}
