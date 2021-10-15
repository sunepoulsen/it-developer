package dk.sunepoulsen.itdeveloper.ui.tasks.backend;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.backend.services.AgreementsService;
import dk.sunepoulsen.itdeveloper.backend.services.TimeLogsService;
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CalculateFlexBalanceTask extends BackendConnectionTask<Double> {
    private final LocalDate balanceDate;
    private final AgreementsService agreementsService;
    private final TimeLogsService timeLogsService;
    private List<AgreementModel> agreements;

    public CalculateFlexBalanceTask(BackendConnection connection, LocalDate balanceDate) {
        super(connection, "calc.balance.task");
        this.balanceDate = balanceDate;
        this.agreementsService = connection.servicesFactory().newAgreementsService();
        this.timeLogsService = connection.servicesFactory().newTimeLogsService();
    }

    @Override
    protected Double call() throws Exception {
        try {
            this.agreements = agreementsService.findAll().stream()
                .filter(agreement -> !agreement.getStartDate().isAfter(balanceDate))
                .collect(Collectors.toList());

            LocalDate balanceStartDate = startDate();
            if (balanceStartDate == null || balanceStartDate.isAfter(balanceDate)) {
                return 0.0;
            }

            Double workedTime = timeLogsService.workingTimeByDates(startDate(), balanceDate);
            Double totalWorkingNorm = agreements.stream()
                .mapToDouble(agreement -> {
                    LocalDate from = agreement.getStartDate();
                    if (agreement.getStartDate().isBefore(balanceStartDate)) {
                        from = balanceStartDate;
                    }

                    LocalDate to = agreement.getEndDate();
                    if (agreement.getEndDate() == null || agreement.getEndDate().isAfter(balanceDate)) {
                        to = balanceDate;
                    }

                    return workingNorm(agreement, from, to);
                })
                .sum();

            return workedTime - totalWorkingNorm;
        } catch (Exception ex) {
            log.info("Unable to calculate flex balance for date {} -> {}", balanceDate, ex.getMessage());
            log.debug("Exception", ex);
            failed();
            return 0.0;
        }
    }

    private LocalDate startDate() {
        return agreements.stream()
            .min(Comparator.comparing(AgreementModel::getStartDate))
            .map(AgreementModel::getStartDate)
            .orElse(null);
    }

    private Double workingNorm(AgreementModel agreement, LocalDate startDate, LocalDate endDate) {
        Map<DayOfWeek, Double> norms = createWorkingNormMap(agreement);

        Double result = 0.0;
        for( LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (norms.get(date.getDayOfWeek()) != null) {
                result += norms.get(date.getDayOfWeek());
            }
        }

        return result;
    }

    private Map<DayOfWeek, Double> createWorkingNormMap(AgreementModel agreement) {
        Map<DayOfWeek, Double> norms = new HashMap<>();

        norms.put(DayOfWeek.MONDAY, agreement.getMondayNorm());
        norms.put(DayOfWeek.TUESDAY, agreement.getTuesdayNorm());
        norms.put(DayOfWeek.WEDNESDAY, agreement.getWednesdayNorm());
        norms.put(DayOfWeek.THURSDAY, agreement.getThursdayNorm());
        norms.put(DayOfWeek.FRIDAY, agreement.getFridayNorm());
        norms.put(DayOfWeek.SATURDAY, agreement.getSaturdayNorm());
        norms.put(DayOfWeek.SUNDAY, agreement.getSundayNorm());

        return norms;
    }
}
