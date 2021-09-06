package dk.sunepoulsen.timelog.ui.model.timelogs;

import dk.sunepoulsen.timelog.ui.model.ProjectAccountModel;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public abstract class TimeLogModel {
    String displayText;
    LocalTime startTime;
    LocalTime endTime;

    public abstract List<ProjectAccountModel> projectAccounts();
    public abstract Double workedTime();
}
