package dk.sunepoulsen.timelog.ui.topcomponents.timelogs;

import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.utils.AlertUtils;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.stream.Collectors;

@Slf4j
public class TimeLogsNavigationPane extends AnchorPane {
    ValidationSupport validationSupport;

    @FXML
    private TextField periodField;

    public TimeLogsNavigationPane() {
        FXMLUtils.initFxml(Registry.getDefault().getBundle(getClass()), this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        LocalDate now = LocalDate.now();
        periodField.setText(now.get(WeekFields.ISO.weekOfWeekBasedYear()) + "." + now.getYear());
    }

    @FXML
    private void previousButtonClicked( final ActionEvent event ) {
        AlertUtils.notImplementedYet();
    }

    @FXML
    private void nextButtonClicked( final ActionEvent event ) {
        AlertUtils.notImplementedYet();
    }
}
