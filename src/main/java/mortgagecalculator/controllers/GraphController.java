package mortgagecalculator.controllers;

import mortgagecalculator.mortgages.Mortgage;
import mortgagecalculator.mortgages.MortgageRecord;
import mortgagecalculator.utils.Localization;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements Initializable {
    @FXML private LineChart<Number, Number> lineChart;
    @FXML private XYChart.Series<Number, Number> paymentSeries;
    @FXML private XYChart.Series<Number, Number> interestSeries;
    @FXML private XYChart.Series<Number, Number> redeemedSeries;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.lineChart.setAnimated(false);
        this.lineChart.setCreateSymbols(false);

        NumberAxis axis = (NumberAxis)this.lineChart.getXAxis();
        axis.setAutoRanging(false);
        axis.setLowerBound(1);

        this.paymentSeries = new XYChart.Series<>();
        initializeSeries(paymentSeries, Localization.getMessage("payment"));

        this.interestSeries = new XYChart.Series<>();
        initializeSeries(interestSeries, Localization.getMessage("interest"));

        this.redeemedSeries = new XYChart.Series<>();
        initializeSeries(redeemedSeries, Localization.getMessage("redeemed"));
    }

    public void fill(Mortgage mortgage) {
        mortgage.getFilteredRecords().addListener((ListChangeListener<? super MortgageRecord>) change -> {
            this.clear();
            this.setData(mortgage.getFilteredRecords());
            this.setXAxisBounds(mortgage.getFilteredRecords());
        });
    }

    public void setIsVisible(boolean isVisible) {
        this.lineChart.setVisible(isVisible);
    }

    private void initializeSeries(XYChart.Series<Number, Number> series, String name) {
        series.setName(name);
        this.lineChart.getData().add(series);
    }

    private void clear() {
        for (XYChart.Series<Number, Number> series : this.lineChart.getData()) {
            series.getData().clear();
        }
    }

    private void setData(FilteredList<MortgageRecord> records) {
        for (MortgageRecord record : records) {
            this.paymentSeries.getData().add(new XYChart.Data<>(record.getMonth(), record.getPayment()));
            this.interestSeries.getData().add(new XYChart.Data<>(record.getMonth(), record.getInterest()));
            this.redeemedSeries.getData().add(new XYChart.Data<>(record.getMonth(), record.getRedeemed()));
        }
    }

    private void setXAxisBounds(FilteredList<MortgageRecord> records) {
        if (records.size() < 1) {
            return;
        }

        NumberAxis axis = (NumberAxis)this.lineChart.getXAxis();
        int lowerBound = records.get(0).getMonth();
        int upperBound = records.get(records.size() - 1).getMonth();
        axis.setLowerBound(lowerBound);
        axis.setUpperBound(upperBound);
        axis.setTickUnit((lowerBound + upperBound) / 12);
    }
}
