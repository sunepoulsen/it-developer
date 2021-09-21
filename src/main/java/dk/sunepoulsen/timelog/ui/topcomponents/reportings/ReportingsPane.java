package dk.sunepoulsen.timelog.ui.topcomponents.reportings;

import dk.sunepoulsen.timelog.ui.control.WeekNavigationPane;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class ReportingsPane extends BorderPane implements Initializable {
    @FXML
    private WeekNavigationPane navigationPane;

    @FXML
    private TimeRegistrationReportingsPane timeRegistrationsPane;

    @FXML
    private TimeSpendReportingsPane timeSpendPane;

    public ReportingsPane() {
        FXMLUtils.initFxmlWithNoBundle(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeRegistrationsPane.getCurrentWeekProperty().bind(navigationPane.getSelectedProperty());
        timeSpendPane.getCurrentWeekProperty().bind(navigationPane.getSelectedProperty());
    }
}
