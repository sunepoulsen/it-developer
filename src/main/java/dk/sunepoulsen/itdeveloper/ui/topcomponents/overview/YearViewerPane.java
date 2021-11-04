package dk.sunepoulsen.itdeveloper.ui.topcomponents.overview;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.control.cell.DoubleTableCell;
import dk.sunepoulsen.itdeveloper.ui.control.cell.TableValueFactory;
import dk.sunepoulsen.itdeveloper.ui.model.overview.FlexOverviewModel;
import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadFlexOverviewTask;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YearViewerPane extends AnchorPane {
    @FXML
    private TableView<FlexOverviewModel> viewer;

    @FXML
    private TableColumn<FlexOverviewModel, Integer> weekTableColumn;

    @FXML
    private TableColumn<FlexOverviewModel, Double> openingBalanceTableColumn;

    @FXML
    private TableColumn<FlexOverviewModel, Double> workedHoursTableColumn;

    @FXML
    private TableColumn<FlexOverviewModel, Double> workingNormTableColumn;

    @FXML
    private TableColumn<FlexOverviewModel, Double> ultimateBalanceTableColumn;

    @Getter
    private SimpleObjectProperty<Integer> currentYearProperty;

    public YearViewerPane() {
        this.currentYearProperty = new SimpleObjectProperty<>();

        FXMLUtils.initFxml(Registry.getDefault().getBundle(getClass()), this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        currentYearProperty.addListener((observable, oldValue, newValue) -> reload());

        weekTableColumn.setCellValueFactory(new TableValueFactory<>(model -> model.getWeek().weekNumber()));
        openingBalanceTableColumn.setCellValueFactory(new TableValueFactory<>(FlexOverviewModel::getOpeningBalance));
        workedHoursTableColumn.setCellValueFactory(new TableValueFactory<>(FlexOverviewModel::getWorkedHours));
        workingNormTableColumn.setCellValueFactory(new TableValueFactory<>(FlexOverviewModel::getWorkingNorm));
        ultimateBalanceTableColumn.setCellValueFactory(new TableValueFactory<>(FlexOverviewModel::ultimateBalance));

        openingBalanceTableColumn.setCellFactory(param -> new DoubleTableCell<>(Registry.getDefault().getLocale()));
        workedHoursTableColumn.setCellFactory(param -> {
            DoubleTableCell<FlexOverviewModel> cell = new DoubleTableCell<>(Registry.getDefault().getLocale());
            cell.setPositiveValueStyleClass(cell.getZeroValueStyleClass());

            return cell;
        });
        workingNormTableColumn.setCellFactory(param -> {
            DoubleTableCell<FlexOverviewModel> cell = new DoubleTableCell<>(Registry.getDefault().getLocale());
            cell.setPositiveValueStyleClass(cell.getZeroValueStyleClass());

            return cell;
        });
        ultimateBalanceTableColumn.setCellFactory(param -> new DoubleTableCell<>(Registry.getDefault().getLocale()));
    }

    private void reload() {
        LoadFlexOverviewTask task = new LoadFlexOverviewTask(Registry.getDefault().getBackendConnection(), currentYearProperty.getValue());

        task.setOnSucceeded(event -> {
            viewer.setItems(task.getValue());
        });

        log.info("Loading flex overview");
        Registry.getDefault().getUiRegistry().getTaskExecutorService().submit(task);
    }
}
