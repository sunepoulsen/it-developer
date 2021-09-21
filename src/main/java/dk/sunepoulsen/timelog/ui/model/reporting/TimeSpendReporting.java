package dk.sunepoulsen.timelog.ui.model.reporting;

import dk.sunepoulsen.timelog.ui.model.ProjectAccountModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class TimeSpendReporting {
    private ProjectAccountModel projectAccount;
    private Map<LocalDate, Double> dates;

    public TimeSpendReporting(ProjectAccountModel projectAccount) {
        this.projectAccount = projectAccount;
        this.dates = new HashMap<>();
    }
}
