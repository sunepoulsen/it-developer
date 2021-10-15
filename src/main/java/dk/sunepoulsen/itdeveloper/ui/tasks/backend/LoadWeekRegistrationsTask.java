package dk.sunepoulsen.itdeveloper.ui.tasks.backend;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.DayRegistration;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogModel;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeLogRegistration;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.TimeRegistration;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.WeekModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class LoadWeekRegistrationsTask extends BackendConnectionTask<ObservableList<TimeRegistration>> {
    private final WeekModel weekModel;
    private final Locale locale;
    private AgreementModel cachedAgreement = null;

    public LoadWeekRegistrationsTask(BackendConnection connection, WeekModel weekModel, Locale locale) {
        super(connection, "week.load.task");
        this.weekModel = weekModel;
        this.locale = locale;
    }

    @Override
    protected ObservableList<TimeRegistration> call() throws Exception {
        log.info("Loading week registrations for week {}.{}", weekModel.weekNumber(), weekModel.year());

        try {
            List<TimeRegistration> result = createDayRegistrations();

            List<TimeLogModel> timelogs = loadTimeLogs();
            timelogs.forEach(timeLogModel -> {
                Optional<DayRegistration> dayRegistration = findDayRegistration(result, timeLogModel.getDate());

                if (dayRegistration.isPresent()) {
                    dayRegistration.get().getRegistrations().add(new TimeLogRegistration(timeLogModel));
                } else {
                    log.info("Unable to find DayRegistration for date {}. This timelog will be ignored. TimeLog: {}", timeLogModel.getDate(), timeLogModel);
                }
            });

            log.info("Loaded week registrations for week {}.{} -> OK", weekModel.weekNumber(), weekModel.year());
            return FXCollections.observableList(result);
        } catch (Exception ex) {
            log.info("Unable to load week registrations for week {}.{} -> {}", weekModel.weekNumber(), weekModel.year(), ex.getMessage());
            log.debug("Exception", ex);
            failed();
            return FXCollections.emptyObservableList();
        }
    }

    private List<TimeLogModel> loadTimeLogs() {
        return connection.servicesFactory().newTimeLogsService().findByDates(weekModel.firstDate(), weekModel.lastDate());
    }

    private List<TimeRegistration> createDayRegistrations() {
        List<TimeRegistration> list = new ArrayList<>();

        log.debug("Create TimeRegistration for each day of the used week");
        for (LocalDate date = weekModel.firstDate(); !date.isAfter(weekModel.lastDate()); date = date.plusDays(1)) {
            DayRegistration dayRegistration = new DayRegistration(date, workingNorm(date), locale);
            list.add(dayRegistration);
        }

        return list;
    }

    private Optional<DayRegistration> findDayRegistration(List<TimeRegistration> timeRegistrations, LocalDate date) {
        return timeRegistrations.stream()
            .map(DayRegistration.class::cast)
            .filter(dayRegistration -> dayRegistration.getDate().isEqual(date))
            .findFirst();
    }

    private Double workingNorm(LocalDate date) {
        log.info("Calculating working norm");

        Optional<AgreementModel> agreement = findAgreement(date);
        if (agreement.isPresent()) {
            log.debug("Select norm based on day of week");
            switch (date.getDayOfWeek()) {
                case MONDAY -> {
                    return agreement.get().getMondayNorm();
                }
                case TUESDAY -> {
                    return agreement.get().getTuesdayNorm();
                }
                case WEDNESDAY -> {
                    return agreement.get().getWednesdayNorm();
                }
                case THURSDAY -> {
                    return agreement.get().getThursdayNorm();
                }
                case FRIDAY -> {
                    return agreement.get().getFridayNorm();
                }
                case SATURDAY -> {
                    return agreement.get().getSaturdayNorm();
                }
                case SUNDAY -> {
                    return agreement.get().getSundayNorm();
                }
            }
        }

        return null;
    }

    private Optional<AgreementModel> findAgreement(LocalDate date) {
        // Check if we can reuse the cached agreement that we used the last time.
        if (cachedAgreement != null &&
            // cachedAgreement.getStartDate() <= date
            (cachedAgreement.getStartDate().isEqual(date) || cachedAgreement.getStartDate().isBefore(date)) &&
            // cachedAgreement.getEndDate() == null | date <= cachedAgreement.getEndDate()
            (cachedAgreement.getEndDate() == null || cachedAgreement.getEndDate().isEqual(date) || cachedAgreement.getEndDate().isAfter(date))) {
            return Optional.of(cachedAgreement);
        }

        log.info("Lookup agreement");
        Optional<AgreementModel> result = connection.servicesFactory().newAgreementsService().findByDate(date).stream().findFirst();
        cachedAgreement = result.orElse(null);

        return result;
    }
}
