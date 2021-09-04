package dk.sunepoulsen.timelog.ui.topcomponents.navigator;

import dk.sunepoulsen.timelog.ui.model.NodeNavigationModel;
import dk.sunepoulsen.timelog.ui.model.TreeNavigatorModel;
import dk.sunepoulsen.timelog.ui.topcomponents.accounts.AccountsPane;
import dk.sunepoulsen.timelog.ui.topcomponents.registration.systems.RegistrationSystemsPane;
import dk.sunepoulsen.timelog.ui.topcomponents.registration.types.RegistrationTypesPane;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;

/**
 * Created by sunepoulsen on 13/05/2017.
 */
public class RootNode extends TreeItem<TreeNavigatorModel> {
    public RootNode() {
        super();

        createAndAddRootItem( new NodeNavigationModel( "Accounts", new AccountsPane() ) );
        createAndAddRootItem( new NodeNavigationModel( "Registration Systems", new RegistrationSystemsPane() ) );

        TreeItem<TreeNavigatorModel> adminItem = createParentItem("Admin");
        createAndAddChildItem(adminItem, new NodeNavigationModel( "Registration Types", new RegistrationTypesPane() ));
        addRootItem(adminItem);
    }

    private void addRootItem(TreeItem<TreeNavigatorModel> treeItem ) {
        this.getChildren().add( treeItem );
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
