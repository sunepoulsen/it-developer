package dk.sunepoulsen.itdeveloper.ui.topcomponents.navigator;

import dk.sunepoulsen.itdeveloper.registry.Registry;
import dk.sunepoulsen.itdeveloper.ui.model.NodeNavigationModel;
import dk.sunepoulsen.itdeveloper.ui.model.TreeNavigatorModel;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.AgreementsPane;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.ProjectAccountsPane;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.overview.FlexOverviewPane;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.registration.types.RegistrationTypesPane;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.reportings.ReportingsPane;
import dk.sunepoulsen.itdeveloper.ui.topcomponents.timelogs.TimeLogsPane;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

import java.util.ResourceBundle;

public class RootNode extends TreeItem<TreeNavigatorModel> {
    public RootNode() {
        super();

        ResourceBundle bundle = Registry.getDefault().getBundle(TreeNavigator.class);

        TreeItem<TreeNavigatorModel> timeRegistrationItem = createParentItem(bundle.getString("navigator.time.registration.group.label"));
        createAndAddChildItem(timeRegistrationItem, new NodeNavigationModel(bundle.getString("navigator.time.registration.timelogs.label"), new TimeLogsPane()));
        createAndAddChildItem(timeRegistrationItem, new NodeNavigationModel(bundle.getString("navigator.time.registration.reporting.label"), new ReportingsPane()));
        createAndAddChildItem(timeRegistrationItem, new NodeNavigationModel(bundle.getString("navigator.time.registration.flex.overview.label"), new FlexOverviewPane()));
        addRootItem(timeRegistrationItem);

        TreeItem<TreeNavigatorModel> adminItem = createParentItem(bundle.getString("navigator.admin.group.label"));
        createAndAddChildItem(adminItem, new NodeNavigationModel( bundle.getString("navigator.admin.agreements.label"), new AgreementsPane() ));
        createAndAddChildItem(adminItem, new NodeNavigationModel( bundle.getString("navigator.admin.registration.types.label"), new RegistrationTypesPane() ));
        createAndAddChildItem(adminItem, new NodeNavigationModel( bundle.getString("navigator.admin.project.accounts.label"), new ProjectAccountsPane() ));
        addRootItem(adminItem);
    }

    private void addRootItem(TreeItem<TreeNavigatorModel> treeItem ) {
        this.getChildren().add( treeItem );
        treeItem.setExpanded(true);
    }

    private TreeItem<TreeNavigatorModel> createParentItem(String displayText) {
        return new TreeItem<>(new NodeNavigationModel( displayText, new Label()) );
    }

    private void createAndAddChildItem(TreeItem<TreeNavigatorModel> parent, TreeNavigatorModel model ) {
        parent.getChildren().add( new TreeItem<>( model ) );
    }
}
