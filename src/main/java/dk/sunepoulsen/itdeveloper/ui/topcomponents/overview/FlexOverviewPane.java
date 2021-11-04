package dk.sunepoulsen.itdeveloper.ui.topcomponents.overview;

import dk.sunepoulsen.itdeveloper.ui.control.YearNavigationPane;
import dk.sunepoulsen.itdeveloper.utils.FXMLUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class FlexOverviewPane extends BorderPane implements Initializable {
    @FXML
    private YearNavigationPane yearNavigationPane;

    @FXML
    private YearViewerPane yearViewerPane;

    @FXML
    private FlexOverviewChartPane flexOverviewChartPane;

    public FlexOverviewPane() {
        FXMLUtils.initFxmlWithNoBundle(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yearViewerPane.getCurrentYearProperty().bind(yearNavigationPane.getSelectedProperty());
        flexOverviewChartPane.getCurrentYearProperty().bind(yearNavigationPane.getSelectedProperty());
    }
}
