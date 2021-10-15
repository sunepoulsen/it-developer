package dk.sunepoulsen.itdeveloper.ui.control;

import dk.sunepoulsen.itdeveloper.ui.tasks.backend.LoadBackendServiceItemsTask;
import dk.sunepoulsen.itdeveloper.backend.BackendConnection;
import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.model.ProjectAccountModel;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SelectProjectAccountsPane extends AnchorPane {
    private final Registry registry;
    private final BackendConnection backendConnection;

    @FXML
    private TreeTableView<ProjectAccountModel> viewer;

    @FXML
    private TreeTableColumn<ProjectAccountModel, String> checkedColumn;

    @Getter
    private final ObservableList<ProjectAccountModel> selectedResultProperty;

    @Getter
    private boolean resultsChanged;

    private List<ProjectAccountModel> currentSelectedAccounts;

    public SelectProjectAccountsPane() {
        this.registry = Registry.getDefault();
        this.backendConnection = registry.getBackendConnection();
        this.selectedResultProperty = FXCollections.observableArrayList();
        this.resultsChanged = false;

        FXMLUtils.initFxml(registry.getBundle(getClass()), this);
    }

    @FXML
    public void initialize() {
        log.info("Initializing {} custom control", getClass().getSimpleName());

        checkedColumn.setCellFactory(param -> new CheckBoxTreeTableCell<>());
        checkedColumn.setEditable(true);

        viewer.setEditable(true);
        viewer.setShowRoot(false);
        viewer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void clearContent() {
        log.debug("Clear project accounts view");
        viewer.setRoot(null);
    }

    public void reload(List<ProjectAccountModel> projectAccounts) {
        LoadBackendServiceItemsTask<ProjectAccountModel> task = new LoadBackendServiceItemsTask<>(backendConnection, ProjectAccountModel.PERFORMANCE_LOAD_TAG,
            connection -> {
                log.debug("Load all project accounts");
                return connection.servicesFactory().newProjectAccountsService().findAll();
            }
        );

        task.setOnSucceeded(event -> {
            ObservableList<ProjectAccountModel> accounts = task.getValue();
            log.debug("Loaded {} project accounts successfully", accounts.size());

            this.currentSelectedAccounts = new ArrayList<>(projectAccounts);

            log.debug("Update selected state of load project accounts");
            accounts.forEach(projectAccountModel ->
                findProjectAccountById(projectAccounts, projectAccountModel.getId())
                    .ifPresent(foundProjectAccount -> projectAccountModel.selectedProperty().setValue(true))
            );

            final CheckBoxTreeItem<ProjectAccountModel> rootItem = new CheckBoxTreeItem<>();
            accounts.forEach(projectAccountModel -> {
                CheckBoxTreeItem<ProjectAccountModel> treeItem = new CheckBoxTreeItem<>(projectAccountModel);

                projectAccountModel.selectedProperty().addListener((observable, oldValue, newValue) -> updateSelectedResults(projectAccountModel, newValue));
                rootItem.getChildren().add(treeItem);
            });

            log.info("Viewing {} project accounts", accounts.size());
            viewer.setRoot(rootItem);
            this.resultsChanged = false;
        });

        log.info("Loading project accounts");
        registry.getUiRegistry().getTaskExecutorService().submit(task);
    }

    private void updateSelectedResults(ProjectAccountModel model, Boolean selected) {
        if (selected) {
            if (this.currentSelectedAccounts.stream().noneMatch(projectAccountModel -> hasSameId(projectAccountModel, model))) {
                log.debug("Add project account {} to selected result", model.getAccountNumber());
                this.currentSelectedAccounts.add(model);
            }
        }
        else {
            log.debug("Removed project account {} from selected result", model.getAccountNumber());
            this.currentSelectedAccounts.removeIf(projectAccountModel -> hasSameId(projectAccountModel, model));
        }

        this.selectedResultProperty.setAll(this.currentSelectedAccounts);
        this.resultsChanged = true;
    }

    private static Optional<ProjectAccountModel> findProjectAccountById(List<ProjectAccountModel> projectAccounts, Long id) {
        return projectAccounts.stream()
            .filter(projectAccountModel -> projectAccountModel.getId().equals(id))
            .findFirst();
    }

    private boolean hasSameId(ProjectAccountModel m1, ProjectAccountModel m2) {
        return m1.getId().equals(m2.getId());
    }
}
