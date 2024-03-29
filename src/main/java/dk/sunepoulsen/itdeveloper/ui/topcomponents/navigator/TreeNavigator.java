package dk.sunepoulsen.itdeveloper.ui.topcomponents.navigator;

import dk.sunepoulsen.itdeveloper.ui.model.TreeNavigatorModel;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by sunepoulsen on 09/05/2017.
 */
@Slf4j
public class TreeNavigator extends AnchorPane {
    @FXML
    private TreeView<TreeNavigatorModel> treeView = null;

    @Getter
    private SimpleObjectProperty<TreeNavigatorModel> selectedProperty;

    public TreeNavigator() {
        FXMLUtils.initFxmlWithNoBundle(this);
    }

    @FXML
    public void initialize() {
        log.info( "Initializing {} custom control", getClass().getSimpleName() );

        treeView.setCellFactory( view -> new TreeNavigatorTreeCell() );
        treeView.setShowRoot( false );
        treeView.setEditable( false );
        treeView.getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
        treeView.getSelectionModel().getSelectedItems().addListener( (ListChangeListener<TreeItem<TreeNavigatorModel>>) c -> {
            ObservableList<? extends TreeItem<TreeNavigatorModel>> list = c.getList();
            if( list.isEmpty() ) {
                selectedProperty.setValue( null );
            }
            else {
                TreeItem<TreeNavigatorModel> item = list.get( 0 );
                selectedProperty.setValue( item.getValue() );
            }
        } );

        selectedProperty = new SimpleObjectProperty<>();

        reload();
    }

    public void reload() {
        treeView.setRoot( new RootNode() );
    }
}
