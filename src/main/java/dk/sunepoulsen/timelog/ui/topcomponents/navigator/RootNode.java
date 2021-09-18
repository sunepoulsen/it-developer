package dk.sunepoulsen.timelog.ui.topcomponents.navigator;

import dk.sunepoulsen.timelog.ui.model.NodeNavigationModel;
import dk.sunepoulsen.timelog.ui.model.TreeNavigatorModel;
import dk.sunepoulsen.timelog.ui.topcomponents.AgreementsPane;
import dk.sunepoulsen.timelog.ui.topcomponents.ProjectAccountsPane;
import dk.sunepoulsen.timelog.ui.topcomponents.registration.types.RegistrationTypesPane;
import dk.sunepoulsen.timelog.ui.topcomponents.reportings.ReportingsPane;
import dk.sunepoulsen.timelog.ui.topcomponents.timelogs.TimeLogsPane;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

/**
 * Created by sunepoulsen on 13/05/2017.
 */
public class RootNode extends TreeItem<TreeNavigatorModel> {
    public RootNode() {
        super();

        createAndAddRootItem(new NodeNavigationModel("Time Logs", new TimeLogsPane()));
        createAndAddRootItem(new NodeNavigationModel("Reportings", new ReportingsPane()));

        TreeItem<TreeNavigatorModel> adminItem = createParentItem("Admin");
        createAndAddChildItem(adminItem, new NodeNavigationModel( "Agreements", new AgreementsPane() ));
        createAndAddChildItem(adminItem, new NodeNavigationModel( "Registration Types", new RegistrationTypesPane() ));
        createAndAddChildItem(adminItem, new NodeNavigationModel( "Project Accounts", new ProjectAccountsPane() ));
        addRootItem(adminItem);
    }

    private void addRootItem(TreeItem<TreeNavigatorModel> treeItem ) {
        this.getChildren().add( treeItem );
        treeItem.setExpanded(true);
    }

    private void createAndAddRootItem(TreeNavigatorModel model ) {
        addRootItem(new TreeItem<>( model ) );
    }

    private TreeItem<TreeNavigatorModel> createParentItem(String displayText) {
        return new TreeItem<>(new NodeNavigationModel( displayText, new Label()) );
    }

    private void createAndAddChildItem(TreeItem<TreeNavigatorModel> parent, TreeNavigatorModel model ) {
        parent.getChildren().add( new TreeItem<>( model ) );
    }
}
