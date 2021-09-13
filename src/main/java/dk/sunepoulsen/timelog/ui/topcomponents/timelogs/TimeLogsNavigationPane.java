package dk.sunepoulsen.timelog.ui.topcomponents.timelogs;

import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeLogsNavigationPane extends AnchorPane {
    private WeekModel weekModel;

    @Getter
    private SimpleObjectProperty<WeekModel> selectedProperty;

    @FXML
    private TextField periodField;

    public TimeLogsNavigationPane() {
        this.selectedProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(Registry.getDefault().getBundle(getClass()), this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        periodField.setEditable(false);
        updateWeek(WeekModel.now());
    }

    @FXML
    private void previousButtonClicked( final ActionEvent event ) {
        updateWeek(weekModel.previousWeek());
    }

    @FXML
    private void nextButtonClicked( final ActionEvent event ) {
        updateWeek(weekModel.nextWeek());
    }

    private void updateWeek(WeekModel weekModel) {
        this.weekModel = weekModel;
        toControls();

        selectedProperty.setValue(weekModel);
    }

    private void toControls() {
        periodField.setText(String.format("%02d.%04d", weekModel.weekNumber(), weekModel.year()));
    }
}
