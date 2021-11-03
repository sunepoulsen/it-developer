package dk.sunepoulsen.itdeveloper.ui.model.overview;

import dk.sunepoulsen.itdeveloper.ui.model.timelogs.WeekModel;
import lombok.Data;

@Data
public class FlexOverviewModel {
    private WeekModel week;
    private Double openingBalance;
    private Double workingNorm;
    private Double workedHours;

    public Double ultimateBalance() {
        Double result = null;

        if (openingBalance != null) {
            result = openingBalance;
        }

        if (result != null && workedHours != null) {
            result += workedHours;
        }

        if (result != null && workingNorm != null) {
            result -= workingNorm;
        }

        return result;
    }
}
