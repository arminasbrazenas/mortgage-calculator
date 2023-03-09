package mortgagecalculator.controllers;

import mortgagecalculator.mortgages.Mortgage;
import mortgagecalculator.mortgages.MortgageRecord;
import mortgagecalculator.utils.Localization;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class TableController implements Initializable {
    @FXML private TableView<MortgageRecord> table;
    @FXML private TableColumn<MortgageRecord, Integer> monthColumn;
    @FXML private TableColumn<MortgageRecord, Double> amountColumn;
    @FXML private TableColumn<MortgageRecord, Double> paymentColumn;
    @FXML private TableColumn<MortgageRecord, Double> interestColumn;
    @FXML private TableColumn<MortgageRecord, Double> redeemedColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initializeColumn(monthColumn, "month");
        this.initializeCurrencyColumn(amountColumn, "amountLeft");
        this.initializeCurrencyColumn(paymentColumn, "payment");
        this.initializeCurrencyColumn(interestColumn, "interest");
        this.initializeCurrencyColumn(redeemedColumn, "redeemed");
    }

    public void fill(Mortgage mortgage) {
        this.table.setItems(mortgage.getFilteredRecords());
        this.autoSize();
    }

    public void setIsVisible(boolean isVisible) {
        this.table.setVisible(isVisible);
    }

    public TableView getTable() {
        return this.table;
    }

    private void autoSize() {
        // Subtracting 4px for scroll-bar
        double columnWidth = this.table.getWidth() / this.table.getColumns().size() - 4;
        for (TableColumn<MortgageRecord, ?> column : this.table.getColumns()) {
            column.setPrefWidth(columnWidth);
        }
    }

    private void initializeCurrencyColumn(TableColumn<MortgageRecord, Double> column, String property) {
        this.initializeColumn(column, property);
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean isEmpty) {
                super.updateItem(amount, isEmpty);
                setText(isEmpty ? null : Localization.getFormattedMoney(amount));
            }
        });
    }

    private <S, T> void initializeColumn(TableColumn<S, T> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }
}
