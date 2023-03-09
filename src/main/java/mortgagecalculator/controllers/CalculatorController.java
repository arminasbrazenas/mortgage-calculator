package mortgagecalculator.controllers;

import mortgagecalculator.mortgages.AnnuityMortgage;
import mortgagecalculator.mortgages.LinearMortgage;
import mortgagecalculator.mortgages.Mortgage;
import mortgagecalculator.utils.CustomTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class CalculatorController implements Initializable {
    private static final double MIN_AMOUNT = 1;
    private static final double MAX_AMOUNT = 1_000_000_000;

    private static final int MIN_TERM_IN_YEARS = 1;
    private static final int MAX_TERM_IN_YEARS = 50;

    private static final double MIN_INTEREST_RATE = 0;
    private static final double MAX_INTEREST_RATE = 100;

    private MainViewController mainController;
    private Mortgage mortgage;

    @FXML private TextField amountTextField;
    @FXML private TextField maturityInYearsTextField;
    @FXML private TextField interestRateTextField;
    @FXML private RadioButton annuityRadioButton;
    @FXML private RadioButton linearRadioButton;
    @FXML private Button confirmButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomTextField.initializeNumbered(this.amountTextField, MIN_AMOUNT, MAX_AMOUNT);
        CustomTextField.initializeNumbered(this.maturityInYearsTextField, MIN_TERM_IN_YEARS, MAX_TERM_IN_YEARS);
        CustomTextField.initializeNumbered(this.interestRateTextField, MIN_INTEREST_RATE, MAX_INTEREST_RATE);

        this.annuityRadioButton.setSelected(true);

        this.confirmButton.setOnAction(event -> onConfirm());
    }

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    public Mortgage getMortgage() {
        return this.mortgage;
    }

    public void defer(int deferralStartMonth, int deferralInMonths, double deferralInterestRate) {
        this.mortgage.defer(deferralInMonths, deferralStartMonth, deferralInterestRate);
        this.mortgage.filter(1, this.mortgage.getRecords().size());
    }

    private void onConfirm() {
        double amount = Double.parseDouble(this.amountTextField.getText());
        int maturityInYears = Integer.parseInt(this.maturityInYearsTextField.getText());
        double interestRate = Double.parseDouble(this.interestRateTextField.getText()) / 100;

        if (this.annuityRadioButton.isSelected()) {
            this.mortgage = new AnnuityMortgage(amount, maturityInYears, interestRate);
        } else if (linearRadioButton.isSelected()) {
            this.mortgage = new LinearMortgage(amount, maturityInYears, interestRate);
        } else {
            throw new IllegalStateException("Unsupported mortgage graph type selected.");
        }

        this.mortgage.calculate();
        this.mainController.visualizeMortgage(mortgage);
    }
}
