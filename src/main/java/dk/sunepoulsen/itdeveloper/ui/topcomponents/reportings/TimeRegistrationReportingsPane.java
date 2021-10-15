package dk.sunepoulsen.itdeveloper.ui.topcomponents.reportings;

import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.ui.control.cell.RegistrationTypeTableCell;
import dk.sunepoulsen.itdeveloper.ui.control.cell.TableValueFactory;
import dk.sunepoulsen.itdeveloper.ui.control.cell.TimeReportingIntervalTableCell;
import dk.sunepoulsen.itdeveloper.ui.model.registration.types.RegistrationTypeModel;
import dk.sunepoulsen.itdeveloper.ui.model.reporting.TimeRegistrationReporting;
import dk.sunepoulsen.itdeveloper.ui.model.reporting.TimeRegistrationReportingInterval;
import dk.sunepoulsen.itdeveloper.ui.model.timelogs.WeekModel;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadWeekReportingTask;
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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ResourceBundle;

@Slf4j
public class TimeRegistrationReportingsPane extends BorderPane {
    private Registry registry;
    private BackendConnection backendConnection = null;
    private ResourceBundle bundle;

    @FXML
    private TableView<TimeRegistrationReporting> viewer;

    @FXML
    private TableColumn<TimeRegistrationReporting, RegistrationTypeModel> registrationTypeTableColumn;

    @Getter
    private SimpleObjectProperty<WeekModel> currentWeekProperty;

    public TimeRegistrationReportingsPane() {
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

        registrationTypeTableColumn.setCellValueFactory(new TableValueFactory<>(TimeRegistrationReporting::getRegistrationType));
        registrationTypeTableColumn.setCellFactory(param -> new RegistrationTypeTableCell<>());

        final DateTimeFormatter timeFormatter = registry.getSettings().getModel().getCalendar().shortTimeFormatter();
        for( LocalDate date = WeekModel.now().firstDate(); !date.isAfter(WeekModel.now().lastDate()); date = date.plusDays(1)) {
            final LocalDate columnDate = date;

            TableColumn<TimeRegistrationReporting, TimeRegistrationReportingInterval> column = new TableColumn<>();
            column.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, registry.getLocale()));
            column.setPrefWidth(110.0);
            column.setCellValueFactory(new TableValueFactory<>(timeRegistrationReporting -> {
                LocalDate lookupDate = CalendarUtils.findSameWeekDay(timeRegistrationReporting.getDates().keySet(), columnDate);
                return timeRegistrationReporting.getDates().get(lookupDate);
            } ));
            column.setCellFactory(param -> new TimeReportingIntervalTableCell<>(timeFormatter));

            viewer.getColumns().add(column);
        }

        viewer.getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
    }

    private void reload() {
        LoadWeekReportingTask task = new LoadWeekReportingTask( backendConnection, currentWeekProperty.getValue() );

        task.setOnSucceeded( event -> {
            ObservableList<TimeRegistrationReporting> movies = task.getValue();

            log.info( "Viewing {} TimeRegistrationReporting from week {}.{}: {}", movies.size(), currentWeekProperty.getValue().weekNumber(), currentWeekProperty.getValue().weekNumber(), movies );
            viewer.setItems( movies );
        } );

        log.info( "Loading week time reportings" );
        registry.getUiRegistry().getTaskExecutorService().submit( task );
    }
}
