module mortgagecalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens mortgagecalculator.controllers to javafx.fxml;
    opens mortgagecalculator.mortgages to javafx.base;
    opens mortgagecalculator.utils to javafx.base;

    exports mortgagecalculator;
}