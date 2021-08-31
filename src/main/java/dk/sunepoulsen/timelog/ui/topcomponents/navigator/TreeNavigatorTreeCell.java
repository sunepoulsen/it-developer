package dk.sunepoulsen.timelog.ui.topcomponents.navigator;

import dk.sunepoulsen.timelog.ui.model.TreeNavigatorModel;
import javafx.scene.control.TreeCell;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TreeNavigatorTreeCell extends TreeCell<TreeNavigatorModel> {
    public TreeNavigatorTreeCell() {
    }

    @Override
    protected void updateItem( final TreeNavigatorModel item, final boolean empty ) {
        super.updateItem( item, empty );

        if( empty || item == null ) {
            setText( null );
            setGraphic( null );
        }
        else {
            setText( item.getDisplayText() );
        }
    }
}
