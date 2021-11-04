package dk.sunepoulsen.itdeveloper.services;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.backend.services.AgreementsService;
import dk.sunepoulsen.itdeveloper.ui.model.AgreementModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class AgreementService {
    private final AgreementsService agreementsService;
    private AgreementModel lastAgreementModel;

    public AgreementService(BackendConnection connection) {
        this.agreementsService = connection.servicesFactory().newAgreementsService();
    }

    public Optional<AgreementModel> findAgreement(LocalDate date) {
        if (lastAgreementModel != null && agreementCoversDate(lastAgreementModel, date)) {
            return Optional.of(lastAgreementModel);
        }

        Optional<AgreementModel> result = agreementsService.findAll().stream()
            .filter(agreement -> agreementCoversDate(agreement, date))
            .findFirst();

        lastAgreementModel = null;
        result.ifPresent(agreementModel -> lastAgreementModel = agreementModel);
        return result;
    }

    public Double workingNorm(LocalDate startDate, LocalDate endDate) {
        Double result = 0.0;
        for( LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Optional<AgreementModel> agreement = findAgreement(date);
            if (agreement.isEmpty()) {
                continue;
            }

            Map<DayOfWeek, Double> norms = createWorkingNormMap(agreement.get());

            if (norms.get(date.getDayOfWeek()) != null) {
                result += norms.get(date.getDayOfWeek());
            }
        }

        return result;
    }

    public Double workingNorm(AgreementModel agreement, LocalDate startDate, LocalDate endDate) {
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
        Map<DayOfWeek, Double> norms = new EnumMap<>(DayOfWeek.class);

        norms.put(DayOfWeek.MONDAY, agreement.getMondayNorm());
        norms.put(DayOfWeek.TUESDAY, agreement.getTuesdayNorm());
        norms.put(DayOfWeek.WEDNESDAY, agreement.getWednesdayNorm());
        norms.put(DayOfWeek.THURSDAY, agreement.getThursdayNorm());
        norms.put(DayOfWeek.FRIDAY, agreement.getFridayNorm());
        norms.put(DayOfWeek.SATURDAY, agreement.getSaturdayNorm());
        norms.put(DayOfWeek.SUNDAY, agreement.getSundayNorm());

        return norms;
    }

    private boolean agreementCoversDate(AgreementModel agreement, LocalDate date) {
        if (date.isBefore(agreement.getStartDate())) {
            return false;
        }

        return agreement.getEndDate() == null || !date.isAfter(agreement.getEndDate());
    }
}
