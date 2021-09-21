package dk.sunepoulsen.timelog.ui.control.cell;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.Locale;

public class DoubleTableCell<S> extends TableCell<S, Double> {
    private final NumberFormat numberFormat;

    @Setter
    private Color positiveColor;

    @Setter
    private Color negativeColor;

    @Setter
    private Color zeroColor;

    public DoubleTableCell(Locale locale) {
        this(locale,2);
    }

    public DoubleTableCell(Locale locale, int fractionDigits) {
        this.numberFormat = NumberFormat.getInstance(locale);
        this.numberFormat.setMinimumFractionDigits(fractionDigits);
        this.numberFormat.setMaximumFractionDigits(fractionDigits);

        this.positiveColor = Color.GREEN;
        this.negativeColor = Color.RED;
        this.zeroColor = Color.BLACK;
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            setText(numberFormat.format(item));

            if (item == 0.0) {
                setTextFill(zeroColor);
            } else if (item < 0.0) {
                setTextFill(negativeColor);
            } else {
                setTextFill(positiveColor);
            }
        }
        else {
            setText(null);
        }

        setGraphic(null);
    }
}
