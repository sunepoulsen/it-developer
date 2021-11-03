package dk.sunepoulsen.itdeveloper.ui.tasks.backend;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.backend.services.TimeLogsService;
import dk.sunepoulsen.itdeveloper.services.AgreementService;
import dk.sunepoulsen.itdeveloper.services.FlexBalanceService;
import dk.sunepoulsen.itdeveloper.ui.model.overview.FlexOverviewModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.WeekModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LoadFlexOverviewTask extends BackendConnectionTask<ObservableList<FlexOverviewModel>> {
    private Integer year;
    private FlexBalanceService flexBalanceService;
    private AgreementService agreementService;
    private TimeLogsService timeLogsService;

    public LoadFlexOverviewTask(BackendConnection connection, Integer year) {
        super(connection, "calc.flex.overview.task");
        this.year = year;
        this.flexBalanceService = new FlexBalanceService(connection);
        this.agreementService = new AgreementService(connection);
        this.timeLogsService = connection.servicesFactory().newTimeLogsService();
    }

    @Override
    protected ObservableList<FlexOverviewModel> call() throws Exception {
        try {
            WeekModel currentWeekModel = WeekModel.of(1, year);
            Double openingBalance = flexBalanceService.flexBalance(currentWeekModel.firstDate().minusDays(1));

            List<FlexOverviewModel> result = new ArrayList<>();

            while (currentWeekModel.firstDate().getYear() <= year) {
                log.debug("Calculate flex overview for week: {}", String.format("%2s.%s", currentWeekModel.weekNumber(), currentWeekModel.year()));

                FlexOverviewModel model = new FlexOverviewModel();
                model.setWeek(currentWeekModel);
                model.setOpeningBalance(openingBalance);
                model.setWorkingNorm(agreementService.workingNorm(currentWeekModel.firstDate(), currentWeekModel.lastDate()));
                model.setWorkedHours(timeLogsService.workingTimeByDates(currentWeekModel.firstDate(), currentWeekModel.lastDate()));
                if (model.getWorkedHours() == null ) {
                    model.setWorkingNorm(0.0);
                    model.setWorkedHours(0.0);
                }
                result.add(model);

                currentWeekModel = currentWeekModel.nextWeek();
                openingBalance = openingBalance + model.getWorkedHours() - model.getWorkingNorm();
            }

            return FXCollections.observableList(result);
        } catch (Exception ex) {
            log.info("Unable to collect flex overview: {}", ex.getMessage(), ex.getMessage());
            log.debug("Exception", ex);
            failed();
            return FXCollections.emptyObservableList();
        }
    }
}
