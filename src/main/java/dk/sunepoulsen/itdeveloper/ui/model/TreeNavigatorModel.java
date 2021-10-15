package dk.sunepoulsen.itdeveloper.ui.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;

/**
 * Defines a model item for each item in the navigation tree.
 * <p>
 *     The model has the responsibility to create a Node that will be shown in the client area when
 *     the model is selected in the navigation tree.
 * </p>
 * <p>
 *     The Node is inserted into a StackPane in the client area.
 * </p>
 */
public interface TreeNavigatorModel {
    /**
     * Returns the text to display next to the icon in a TreeItem for this model
     */
    String getDisplayText();

    /**
     * Returns the Node to be visible in the client area when this model is selected in
     * the navigation tree.
     * <p>
     *     It is up to the implementation to ensure that the Node is cached so it not created
     *     again on every call to getNode()
     * </p>
     */
    Node getNode();

    /**
     * Returns how the Node of this model should be aligned in the client area.
     */
    Pos getAlignment();

    /**
     * Returns the margin around the Node in the client area.
     */
    Insets getMargin();
}
