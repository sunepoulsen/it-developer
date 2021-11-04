package dk.sunepoulsen.itdeveloper.ui.control;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class YearNavigationPane extends AnchorPane {
    @Getter
    private SimpleObjectProperty<Integer> selectedProperty;

    @FXML
    private TextField periodField;

    public YearNavigationPane() {
        this.selectedProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(Registry.getDefault().getBundle(getClass()), this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        periodField.setEditable(false);
        updateYear(LocalDate.now().getYear());
    }

    @FXML
    private void previousButtonClicked( final ActionEvent event ) {
        updateYear(selectedProperty.getValue() - 1);
    }

    @FXML
    private void nextButtonClicked( final ActionEvent event ) {
        updateYear(selectedProperty.getValue() + 1);
    }

    private void updateYear(Integer year) {
        selectedProperty.setValue(year);
        toControls();
    }

    private void toControls() {
        periodField.setText(selectedProperty.getValue().toString());
    }
}
