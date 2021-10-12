package dk.sunepoulsen.timelog.ui.tasks.backend;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.ui.model.AgreementModel;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekChartModel;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekModel;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class LoadTimeLogsChartDataTask extends BackendConnectionTask<WeekChartModel>{
    private final WeekModel weekModel;

    public LoadTimeLogsChartDataTask(BackendConnection connection, WeekModel weekModel) {
        super( connection, "timelogs.chart.data.load.task" );
        this.weekModel = weekModel;
    }

    @Override
    protected WeekChartModel call() throws Exception {
        WeekChartModel weekChartModel = new WeekChartModel();

        try {
            for (LocalDate date = weekModel.firstDate(); !date.isAfter(weekModel.lastDate()); date = date.plusDays(1)) {
                List<AgreementModel> agreements = connection.servicesFactory().newAgreementsService().findByDate(date);
                if (agreements.size() > 1) {
                    log.warn("Found more than one agreement for date {}. Number of agreements found: {}", date, agreements.size());
                    updateMessage("Found more than one agreement for date " + date);
                    failed();
                }

                weekChartModel.putWorkingNorm(date.getDayOfWeek(), agreements.get(0).weekDayNorm(date.getDayOfWeek()));

                Double workedHours = connection.servicesFactory().newTimeLogsService().workingTimeByDates(date, date);
                if (workedHours != null) {
                    weekChartModel.putWorkedHours(date.getDayOfWeek(), workedHours);
                }
            }

            return weekChartModel;
        }
        catch( Exception ex) {
            log.info("Unable to execute task {}: {}", getClass().getName(), ex.getMessage());
            log.debug("Exception", ex);
            throw ex;
        }
    }
}
