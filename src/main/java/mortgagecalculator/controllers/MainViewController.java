package mortgagecalculator.controllers;

import mortgagecalculator.mortgages.Mortgage;
import mortgagecalculator.utils.MortgageExporter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    public Stage stage;

    @FXML private CalculatorController calculatorController;
    @FXML private SettingsController settingsController;
    @FXML private TableController tableController;
    @FXML private GraphController graphController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.calculatorController.setMainController(this);
        this.settingsController.setMainController(this);
        this.setAreChildControllersVisible(false);
    }

    public void visualizeMortgage(Mortgage mortgage) {
        this.tableController.fill(mortgage);
        this.graphController.fill(mortgage);
        this.settingsController.fill(mortgage);

        this.setAreChildControllersVisible(true);
    }

    public void deferMortgage(int deferralStartMonth, int deferralInMonths, double deferralInterestRate) {
        this.calculatorController.defer(deferralStartMonth, deferralInMonths, deferralInterestRate);
    }

    public void exportMortgageData() {
        MortgageExporter.exportToSpreadsheet(this.calculatorController.getMortgage(), this.tableController.getTable(), stage);
    }

    private void setAreChildControllersVisible(boolean isVisible) {
        this.settingsController.setIsVisible(isVisible);
        this.tableController.setIsVisible(isVisible);
        this.graphController.setIsVisible(isVisible);
    }
}
