package dk.sunepoulsen.itdeveloper.ui.tasks.backend;

import dk.sunepoulsen.itdeveloper.ui.model.reporting.TimeSpendReporting;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LoadTimeSpendReportingTask extends BackendConnectionTask<ObservableList<TimeSpendReporting>>{
    private final WeekModel weekModel;

    public LoadTimeSpendReportingTask(BackendConnection connection, WeekModel weekModel) {
        super( connection, "week.spend.reporting.load.task" );
        this.weekModel = weekModel;
    }

    @Override
    protected ObservableList<TimeSpendReporting> call() throws Exception {
        try {
            List<TimeSpendReporting> reportings = new ArrayList<>();

            loadTimeLogs().forEach(timeLogModel -> addTimeLog(reportings, timeLogModel));

            return FXCollections.observableList(reportings.stream()
                .sorted(Comparator.comparing(timeSpendReporting -> timeSpendReporting.getProjectAccount().getAccountNumber()))
                .collect(Collectors.toList())
            );
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

    private void addTimeLog(List<TimeSpendReporting> reportings, TimeLogModel timeLogModel) {
        if (timeLogModel.getProjectAccounts().isEmpty()) {
            return;
        }

        double timeRatio = 1.0 / timeLogModel.getProjectAccounts().size();
        double projectAccountTimeSpend = timeSpend(timeLogModel.getStartTime(), timeLogModel.getEndTime()) * timeRatio;

        timeLogModel.getProjectAccounts()
            .forEach(projectAccountModel -> this.addProjectAccount(reportings, timeLogModel, projectAccountModel, projectAccountTimeSpend));
    }

    private void addProjectAccount(List<TimeSpendReporting> reportings, TimeLogModel timeLogModel, ProjectAccountModel projectAccountModel, double projectAccountTimeSpend) {
        List<TimeSpendReporting> list = findReportings(reportings, projectAccountModel);

        boolean mergedProjectAccount = false;

        // Iterate over each TimeReporting
        for (TimeSpendReporting report : list ) {
            if (!report.getDates().containsKey(timeLogModel.getDate())) {
                // No interval for the correct day.

                report.getDates().put(timeLogModel.getDate(), projectAccountTimeSpend);
                mergedProjectAccount = true;
                break;
            }
            else {
                // We found a TimeReporting that can be extended to include the timelog because the timelog is before the TimeReporting interval

                report.getDates().put(timeLogModel.getDate(), report.getDates().get(timeLogModel.getDate()) + projectAccountTimeSpend);
                mergedProjectAccount = true;
                break;
            }
        }

        if (!mergedProjectAccount) {
            TimeSpendReporting newReporting = new TimeSpendReporting(projectAccountModel);
            newReporting.getDates().put(timeLogModel.getDate(), projectAccountTimeSpend);

            reportings.add(newReporting);
        }
    }

    private List<TimeSpendReporting> findReportings(List<TimeSpendReporting> reportings, ProjectAccountModel projectAccount) {
        return reportings.stream()
            .filter(timeSpendReporting -> timeSpendReporting.getProjectAccount().getId().equals(projectAccount.getId()))
            .collect(Collectors.toList());
    }

    private double timeSpend(LocalTime lt1, LocalTime lt2) {
        return ChronoUnit.MINUTES.between(lt1, lt2) / 60.0;
    }
}
