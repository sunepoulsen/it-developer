package dk.sunepoulsen.itdeveloper.ui.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import lombok.Setter;

@Setter
public class NodeNavigationModel implements TreeNavigatorModel {
    private String displayText;
    private Node node;
    private Pos alignment;
    private Insets margin;

    public NodeNavigationModel( String displayText, Node node ) {
        this( displayText, node, null );
    }

    public NodeNavigationModel( String displayText, Node node, Pos alignment ) {
        this( displayText, node, alignment, null );
    }

    public NodeNavigationModel( String displayText, Node node, Pos alignment, Insets margin ) {
        this.displayText = displayText;
        this.node = node;
        this.alignment = alignment;
        this.margin = margin;
    }

    @Override
    public String getDisplayText() {
        return displayText;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public Pos getAlignment() {
        return alignment;
    }

    @Override
    public Insets getMargin() {
        return margin;
    }
}
