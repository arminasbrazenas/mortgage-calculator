package mortgagecalculator.controllers;

import mortgagecalculator.mortgages.Mortgage;
import mortgagecalculator.mortgages.MortgageRecord;
import mortgagecalculator.utils.CustomTextField;
import mortgagecalculator.utils.Localization;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SettingsController implements Initializable {
    private static final int MIN_DEFERRAL_IN_MONTHS = 0;
    private static final int MAX_DEFERRAL_IN_MONTHS = 24;

    private static final double MIN_DEFERRAL_INTEREST_RATE = 0;
    private static final double MAX_DEFERRAL_INTEREST_RATE = 100;

    private final SimpleDoubleProperty interestSum;
    private final SimpleDoubleProperty paymentSum;
    private Mortgage mortgage;
    private MainViewController mainController;

    @FXML private Label interestSumLabel;
    @FXML private Label paymentSumLabel;
    @FXML private GridPane gridPane;
    @FXML private ComboBox<Integer> monthStartFilterComboBox;
    @FXML private ComboBox<Integer> monthEndFilterComboBox;
    @FXML private ComboBox<Integer> deferralStartMonthComboBox;
    @FXML private TextField deferralInMonthsTextField;
    @FXML private TextField deferralInterestRateTextField;
    @FXML private Button exportButton;
    @FXML private Button deferButton;

    public SettingsController() {
        this.interestSum = new SimpleDoubleProperty();
        this.paymentSum = new SimpleDoubleProperty();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomTextField.initializeNumbered(deferralInMonthsTextField, MIN_DEFERRAL_IN_MONTHS, MAX_DEFERRAL_IN_MONTHS);
        CustomTextField.initializeNumbered(deferralInterestRateTextField, MIN_DEFERRAL_INTEREST_RATE, MAX_DEFERRAL_INTEREST_RATE);

        this.interestSumLabel.textProperty().bind(Bindings.createStringBinding(
                () -> Localization.getFormattedMoney(this.interestSum.get()),
                this.interestSum
        ));
        this.paymentSumLabel.textProperty().bind(Bindings.createStringBinding(
                () -> Localization.getFormattedMoney(this.paymentSum.get()),
                this.paymentSum
        ));

        this.monthStartFilterComboBox.setValue(1);
        this.monthEndFilterComboBox.setValue(1);
        this.deferralStartMonthComboBox.setValue(1);

        this.addMonthFilterValueChangeListener(this.monthStartFilterComboBox);
        this.addMonthFilterValueChangeListener(this.monthEndFilterComboBox);

        this.deferButton.setOnAction(event -> onDefer());
        this.exportButton.setOnAction(event -> onExport());
    }

    public void fill(Mortgage mortgage) {
        this.mortgage = mortgage;

        ObservableList<Integer> monthRange = createObservableIntegerRange(this.mortgage.getRecords().size());
        this.deferralStartMonthComboBox.setItems(monthRange);

        this.resetMonthFilters(monthRange);
        this.mortgage.getRecords().addListener((ListChangeListener<? super MortgageRecord>) change -> {
            ObservableList<Integer> filteredMonthRange = createObservableIntegerRange(this.mortgage.getRecords().size());
            this.resetMonthFilters(filteredMonthRange);
        });

        this.computeSums();
        this.mortgage.getFilteredRecords().addListener((ListChangeListener<? super MortgageRecord>) change -> this.computeSums());
    }

    public void setIsVisible(boolean isVisible) {
        this.gridPane.setVisible(isVisible);
    }

    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }

    private void onDefer() {
        int deferralStartMonth = this.deferralStartMonthComboBox.getValue();
        int deferralInMonths = Integer.parseInt(this.deferralInMonthsTextField.getText());
        double deferralInterestRate = Double.parseDouble(this.deferralInterestRateTextField.getText()) / 100;

        this.mainController.deferMortgage(deferralStartMonth, deferralInMonths, deferralInterestRate);
    }

    private void onExport() {
        this.mainController.exportMortgageData();
    }

    private void resetMonthFilters(ObservableList<Integer> monthRange) {
        this.monthStartFilterComboBox.setValue(1);
        this.monthStartFilterComboBox.setItems(monthRange);
        this.monthEndFilterComboBox.setItems(monthRange);
        this.monthEndFilterComboBox.setValue(monthRange.size());
    }

    private void computeSums() {
        this.interestSum.setValue(0);
        this.paymentSum.setValue(0);

        for (MortgageRecord record : this.mortgage.getFilteredRecords()) {
            this.interestSum.setValue(this.interestSum.add(record.getInterest()).doubleValue());
            this.paymentSum.setValue(this.paymentSum.add(record.getPayment()).doubleValue());
        }
    }

    private ObservableList<Integer> createObservableIntegerRange(int size) {
        ObservableList<Integer> months = IntStream.rangeClosed(1, size)
                .boxed()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        return months;
    }

    private void addMonthFilterValueChangeListener(ComboBox<Integer> comboBox) {
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.mortgage.filter(this.monthStartFilterComboBox.getValue(), this.monthEndFilterComboBox.getValue());
            }
        });
    }
}
