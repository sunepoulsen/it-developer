package dk.sunepoulsen.itdeveloper.ui.control.cell;

import javafx.scene.control.TreeTableCell;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.Locale;

import static dk.sunepoulsen.itdeveloper.ui.styles.StyleClasses.FLEX_NEGATIVE_VALUE_CLASS_NAME;
import static dk.sunepoulsen.itdeveloper.ui.styles.StyleClasses.FLEX_POSITIVE_VALUE_CLASS_NAME;
import static dk.sunepoulsen.itdeveloper.ui.styles.StyleClasses.FLEX_ZERO_VALUE_CLASS_NAME;

public class DoubleTreeTableCell<S> extends TreeTableCell<S, Double> {
    private final NumberFormat numberFormat;

    @Getter
    @Setter
    private String positiveValueStyleClass;

    @Getter
    @Setter
    private String negativeValueStyleClass;

    @Getter
    @Setter
    private String zeroValueStyleClass;

    public DoubleTreeTableCell(Locale locale) {
        this(locale,2);
    }

    public DoubleTreeTableCell(Locale locale, int fractionDigits) {
        this.numberFormat = NumberFormat.getInstance(locale);
        this.numberFormat.setMinimumFractionDigits(fractionDigits);
        this.numberFormat.setMaximumFractionDigits(fractionDigits);

        this.positiveValueStyleClass = FLEX_POSITIVE_VALUE_CLASS_NAME;
        this.negativeValueStyleClass = FLEX_NEGATIVE_VALUE_CLASS_NAME;
        this.zeroValueStyleClass = FLEX_ZERO_VALUE_CLASS_NAME;
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            setText(numberFormat.format(item));
            getStyleClass().clear();

            if (item == 0.0) {
                getStyleClass().add(zeroValueStyleClass);
            } else if (item < 0.0) {
                getStyleClass().add(negativeValueStyleClass);
            } else {
                getStyleClass().add(positiveValueStyleClass);
            }
        }
        else {
            setText(null);
        }

        setGraphic(null);
    }
}
