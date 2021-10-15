package dk.sunepoulsen.itdeveloper.ui.topcomponents.reportings;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.ui.control.cell.DoubleTableCell;
import dk.sunepoulsen.itdeveloper.ui.control.cell.TableValueFactory;
import dk.sunepoulsen.itdeveloper.ui.model.reporting.TimeSpendReporting;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadTimeSpendReportingTask;
import dk.sunepoulsen.itdeveloper.utils.CalendarUtils;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ResourceBundle;

@Slf4j
public class TimeSpendReportingsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<TimeSpendReporting> viewer;

    @FXML
    private TableColumn<TimeSpendReporting, String> accountNumberTableColumn;

    @FXML
    private TableColumn<TimeSpendReporting, String> descriptionTableColumn;

    @Getter
    private SimpleObjectProperty<WeekModel> currentWeekProperty;

    public TimeSpendReportingsPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.bundle = registry.getBundle( getClass() );
        this.currentWeekProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(this.bundle, this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        currentWeekProperty.addListener((observable, oldValue, newValue) -> reload());

        accountNumberTableColumn.setCellValueFactory(new TableValueFactory<>(timeSpendReporting -> timeSpendReporting.getProjectAccount().getAccountNumber()));
        descriptionTableColumn.setCellValueFactory(new TableValueFactory<>(timeSpendReporting -> timeSpendReporting.getProjectAccount().getDescription()));

        for( LocalDate date = WeekModel.now().firstDate(); !date.isAfter(WeekModel.now().lastDate()); date = date.plusDays(1)) {
            final LocalDate columnDate = date;

            TableColumn<TimeSpendReporting, Double> column = new TableColumn<>();
            column.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, registry.getLocale()));
            column.setPrefWidth(110.0);
            column.setCellValueFactory(new TableValueFactory<>(timeSpendReporting -> {
                LocalDate lookupDate = CalendarUtils.findSameWeekDay(timeSpendReporting.getDates().keySet(), columnDate);
                return timeSpendReporting.getDates().get(lookupDate);
            }));
            column.setCellFactory(param -> {
                DoubleTableCell<TimeSpendReporting> cell = new DoubleTableCell<>(this.registry.getLocale());
                cell.setPositiveColor(Color.BLACK);

                return cell;
            });

            viewer.getColumns().add(column);
        }

        viewer.getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
    }

    private void reload() {
        LoadTimeSpendReportingTask task = new LoadTimeSpendReportingTask( backendConnection, currentWeekProperty.getValue() );

        task.setOnSucceeded( event -> {
            ObservableList<TimeSpendReporting> reportings = task.getValue();

            log.info( "Viewing {} time spend reportings", reportings.size() );
            viewer.setItems( reportings );
        } );

        log.info( "Loading week time reportings" );
        registry.getUiRegistry().getTaskExecutorService().submit( task );
    }
}
