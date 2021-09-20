package dk.sunepoulsen.timelog.ui.tasks.backend;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.timelog.ui.model.reporting.TimeReporting;
import dk.sunepoulsen.timelog.ui.model.reporting.TimeReportingInterval;
import dk.sunepoulsen.timelog.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LoadWeekReportingTask extends BackendConnectionTask<ObservableList<TimeReporting>>{
    private final WeekModel weekModel;

    public LoadWeekReportingTask(BackendConnection connection, WeekModel weekModel) {
        super( connection, "week.reporting.load.task" );
        this.weekModel = weekModel;
    }

    @Override
    protected ObservableList<TimeReporting> call() throws Exception {
        try {
            List<TimeReporting> reportings = new ArrayList<>();

            loadTimeLogs().forEach(timeLogModel -> addTimeLog(reportings, timeLogModel));

            return FXCollections.observableList(reportings);
        }
        catch( Exception ex) {
            log.info("Unable to execute task {}: {}", getClass().getName(), ex.getMessage());
            log.debug("Exception", ex);
            throw ex;
        }
    }

    private List<TimeLogModel> loadTimeLogs() {
        return connection.servicesFactory().newTimeLogsService().findByDates(weekModel.firstDate(), weekModel.lastDate());
    }

    private void addTimeLog(List<TimeReporting> reportings, TimeLogModel timeLogModel) {
        List<TimeReporting> list = findReportings(reportings, timeLogModel.getRegistrationType());

        boolean mergedTimeLog = false;

        // Iterate over each TimeReporting
        for (TimeReporting report : list ) {
            if (!report.getDates().containsKey(timeLogModel.getDate())) {
                // No interval for the correct day.

                report.getDates().put(timeLogModel.getDate(), new TimeReportingInterval(timeLogModel.getStartTime(), timeLogModel.getEndTime()));
                mergedTimeLog = true;
                break;
            }
            else if (timeLogModel.getEndTime().equals(report.getDates().get(timeLogModel.getDate()).getStartTime())) {
                // We found a TimeReporting that can be extended to include the timelog because the timelog is before the TimeReporting interval

                report.getDates().get(timeLogModel.getDate()).setStartTime(timeLogModel.getStartTime());
                mergedTimeLog = true;
                break;
            }
            else if (timeLogModel.getStartTime().equals(report.getDates().get(timeLogModel.getDate()).getEndTime())) {
                // We found a TimeReporting that can be extended to include the timelog because the timelog is after the TimeReporting interval

                report.getDates().get(timeLogModel.getDate()).setEndTime(timeLogModel.getEndTime());
                mergedTimeLog = true;
                break;
            }
        }

        if (!mergedTimeLog) {
            TimeReporting newReporting = new TimeReporting(timeLogModel.getRegistrationType());
            newReporting.getDates().put(timeLogModel.getDate(), new TimeReportingInterval(timeLogModel.getStartTime(), timeLogModel.getEndTime()));

            reportings.add(newReporting);
        }
    }

    private List<TimeReporting> findReportings(List<TimeReporting> reportings, RegistrationTypeModel registrationType) {
        return reportings.stream()
            .filter(timeReporting -> timeReporting.getRegistrationType().getId().equals(registrationType.getId()))
            .collect(Collectors.toList());
    }
}
