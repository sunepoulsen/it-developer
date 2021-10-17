package dk.sunepoulsen.itdeveloper.ui.topcomponents.overview;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.ResourceBundle;

@Slf4j
public class FlexOverviewChartPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private LineChart<String, Number> chart;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis hoursAxis;

    @Getter
    private SimpleObjectProperty<Integer> currentYearProperty;

    public FlexOverviewChartPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle(getClass());
        this.currentYearProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info("Initializing {} custom control", getClass().getSimpleName());

        currentYearProperty.addListener((observable, oldValue, newValue) -> reload());

        categoryAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(
            this.bundle.getString("label.monday"),
            this.bundle.getString("label.tuesday"),
            this.bundle.getString("label.wednesday"),
            this.bundle.getString("label.thursday"),
            this.bundle.getString("label.friday"),
            this.bundle.getString("label.saturday"),
            this.bundle.getString("label.sunday")
        )));
    }

    private void reload() {
        chart.getData().clear();
    }

}
