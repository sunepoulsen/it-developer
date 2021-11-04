package dk.sunepoulsen.itdeveloper.services;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.backend.services.AgreementsService;
import dk.sunepoulsen.itdeveloper.backend.services.TimeLogsService;
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FlexBalanceService {
    private final AgreementService agreementService;
    private final AgreementsService agreementsService;
    private final TimeLogsService timeLogsService;

    public FlexBalanceService(BackendConnection connection) {
        this.agreementService = new AgreementService(connection);
        this.agreementsService = connection.servicesFactory().newAgreementsService();
        this.timeLogsService = connection.servicesFactory().newTimeLogsService();
    }

    public Double flexBalance(LocalDate date) {
        List<AgreementModel> agreements = agreementsService.findAll().stream()
            .filter(agreement -> !agreement.getStartDate().isAfter(date))
            .collect(Collectors.toList());

        LocalDate balanceStartDate = startDate(agreements);
        if (balanceStartDate == null || balanceStartDate.isAfter(date)) {
            return 0.0;
        }

        Double workedTime = timeLogsService.workingTimeByDates(startDate(agreements), date);
        Double totalWorkingNorm = agreements.stream()
            .mapToDouble(agreement -> {
                LocalDate from = agreement.getStartDate();
                if (agreement.getStartDate().isBefore(balanceStartDate)) {
                    from = balanceStartDate;
                }

                LocalDate to = agreement.getEndDate();
                if (agreement.getEndDate() == null || agreement.getEndDate().isAfter(date)) {
                    to = date;
                }

                return agreementService.workingNorm(agreement, from, to);
            })
            .sum();

        return workedTime - totalWorkingNorm;
    }

    private LocalDate startDate(List<AgreementModel> agreements) {
        return agreements.stream()
            .min(Comparator.comparing(AgreementModel::getStartDate))
            .map(AgreementModel::getStartDate)
            .orElse(null);
    }
}
