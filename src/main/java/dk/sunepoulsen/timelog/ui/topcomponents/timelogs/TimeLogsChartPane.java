package dk.sunepoulsen.timelog.ui.topcomponents.timelogs;

import dk.sunepoulsen.timelog.backend.BackendConnection;
import dk.sunepoulsen.timelog.registry.Registry;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekChartModel;
import dk.sunepoulsen.timelog.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.timelog.ui.tasks.backend.LoadTimeLogsChartDataTask;
import dk.sunepoulsen.timelog.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
public class TimeLogsChartPane extends BorderPane {
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
    private SimpleObjectProperty<WeekModel> currentWeekProperty;

    public TimeLogsChartPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle(getClass());
        this.currentWeekProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info("Initializing {} custom control", getClass().getSimpleName());

        currentWeekProperty.addListener((observable, oldValue, newValue) -> loadTimeLogs());

        categoryAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(
            this.bundle.getString("label.monday"),
            this.bundle.getString("label.tuesday"),
            this.bundle.getString("label.wednesday"),
            this.bundle.getString("label.thursday"),
            this.bundle.getString("label.friday"),
            this.bundle.getString("label.saturday"),
            this.bundle.getString("label.sunday")
        )));

        loadTimeLogs();
    }

    private void loadTimeLogs() {
        chart.getData().clear();

        LoadTimeLogsChartDataTask task = new LoadTimeLogsChartDataTask(backendConnection, currentWeekProperty.getValue());
        task.setOnSucceeded(event -> {
            WeekChartModel weekChartModel = task.getValue();

            XYChart.Series<String, Number> normSeries = new XYChart.Series<>();
            normSeries.setName("Working norm");
            putSeriesData(normSeries.getData(), weekChartModel.accumulateWorkingNorms(), DayOfWeek.SUNDAY);
            chart.getData().add(normSeries);

            XYChart.Series<String, Number> workSeries = new XYChart.Series<>();
            workSeries.setName("Worked hours");

            DayOfWeek limitDayOfWeek = DayOfWeek.SUNDAY;
            if (currentWeekProperty.getValue().containsDate(LocalDate.now())) {
                limitDayOfWeek = LocalDate.now().getDayOfWeek();
            }

            putSeriesData(workSeries.getData(), weekChartModel.accumulateWorkedHours(), limitDayOfWeek);

            chart.getData().add(workSeries);
        });

        this.registry.getUiRegistry().getTaskExecutorService().submit(task);
    }

    private void putSeriesData(ObservableList<XYChart.Data<String, Number>> data, Map<DayOfWeek, Double> values, DayOfWeek limitWeekDay) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            data.add(new XYChart.Data<>(this.bundle.getString("label." + dayOfWeek.name().toLowerCase()), values.get(dayOfWeek)));

            if (dayOfWeek.equals(limitWeekDay)) {
                return;
            }
        }
    }

}
