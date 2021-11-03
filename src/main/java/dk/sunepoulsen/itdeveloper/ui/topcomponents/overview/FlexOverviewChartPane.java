package dk.sunepoulsen.itdeveloper.ui.topcomponents.overview;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.model.overview.FlexOverviewModel;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadFlexOverviewTask;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
    }

    private void reload() {
        LoadFlexOverviewTask task = new LoadFlexOverviewTask(backendConnection, currentYearProperty.getValue());

        task.setOnSucceeded(event -> {
            chart.getData().clear();

            final ObservableList<FlexOverviewModel> model = task.getValue();

            final XYChart.Series<String, Number> flexSeries = new XYChart.Series<>();
            flexSeries.setName("Flex balance");

            model.forEach(flexOverviewModel -> {
                flexSeries.getData().add(new XYChart.Data<>(flexOverviewModel.getWeek().weekNumber().toString(), flexOverviewModel.ultimateBalance()));
            });

            chart.getData().add(flexSeries);
        });

        log.info("Loading flex overview");
        Registry.getDefault().getUiRegistry().getTaskExecutorService().submit(task);
    }

}
