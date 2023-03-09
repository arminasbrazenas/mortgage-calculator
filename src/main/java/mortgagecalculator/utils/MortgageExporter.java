package mortgagecalculator.utils;

import mortgagecalculator.mortgages.AnnuityMortgage;
import mortgagecalculator.mortgages.Mortgage;
import mortgagecalculator.mortgages.MortgageRecord;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class MortgageExporter {
    public static void exportToSpreadsheet(Mortgage mortgage, TableView<MortgageRecord> table, Stage stage) {
        File file = openSaveFileAsDialog("Excel files (*.xlsx)", "*.xlsx", stage);
        if (file == null) {
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet(Localization.getMessage("mortgage"));

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat(Localization.getCurrencyFormat()));

        CellStyle percentageStyle = workbook.createCellStyle();
        percentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));

        AtomicInteger rowIndex = new AtomicInteger(0);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("mortgage"), null);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("amount"), mortgage.getAmount(), currencyStyle);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("termInYears"), mortgage.getTermInYears(), null);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("interestRate"), mortgage.getInterestRate(), percentageStyle);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("returnGraph"), Localization.getMessage(mortgage instanceof AnnuityMortgage ? "annuity" : "linear"), null);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("deferralInMonths"), mortgage.getDeferralInMonths(), null);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("deferralInterestRate"), mortgage.getDeferralInterestRate(), null);
        addMainDataRowToSpreadsheet(spreadsheet, rowIndex, Localization.getMessage("deferralStartMonth"), mortgage.getDeferralStartMonth(), null);
        rowIndex.getAndIncrement();
        addTableToSpreadsheet(spreadsheet, table, rowIndex, currencyStyle);

        try {
            FileOutputStream fileOut = new FileOutputStream(file.getPath());
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception ignored) {}
    }

    public static File openSaveFileAsDialog(String description, String extension, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(description, extension);
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showSaveDialog(stage);
    }

    private static void addMainDataRowToSpreadsheet(Sheet spreadsheet, AtomicInteger rowIndex, String title, int value, CellStyle cellStyle) {
        Row row = addMainDataRowToSpreadsheet(spreadsheet, rowIndex, title, cellStyle);
        row.getCell(1).setCellValue(value);
    }

    private static void addMainDataRowToSpreadsheet(Sheet spreadsheet, AtomicInteger rowIndex, String title, double value, CellStyle cellStyle) {
        Row row = addMainDataRowToSpreadsheet(spreadsheet, rowIndex, title, cellStyle);
        row.getCell(1).setCellValue(value);
    }

    private static void addMainDataRowToSpreadsheet(Sheet spreadsheet, AtomicInteger rowIndex, String title, String value, CellStyle cellStyle) {
        Row row = addMainDataRowToSpreadsheet(spreadsheet, rowIndex, title, cellStyle);
        row.getCell(1).setCellValue(value);
    }

    private static Row addMainDataRowToSpreadsheet(Sheet spreadsheet, AtomicInteger rowIndex, String title, CellStyle cellStyle) {
        Row amountRow = spreadsheet.createRow(rowIndex.getAndIncrement());
        amountRow.createCell(0).setCellValue(title);
        amountRow.createCell(1);
        amountRow.getCell(1).setCellStyle(cellStyle);
        return amountRow;
    }

    private static void addTableToSpreadsheet(Sheet spreadsheet, TableView<MortgageRecord> table, AtomicInteger rowIndex, CellStyle currencyStyle) {
        ObservableList<TableColumn<MortgageRecord, ?>> columns = table.getColumns();
        Row tableHeaderRow = spreadsheet.createRow(rowIndex.getAndIncrement());
        for (int i = 0; i < columns.size(); ++i) {
            tableHeaderRow.createCell(i).setCellValue(columns.get(i).getText());
        }

        for (int i = 0; i < table.getItems().size(); ++i) {
            Row dataRow = spreadsheet.createRow(rowIndex.getAndIncrement());
            for (int j = 0; j < columns.size(); ++j) {
                if (columns.get(j).getCellData(i) instanceof Integer) {
                    dataRow.createCell(j).setCellValue((int)columns.get(j).getCellData(i));
                } else if (columns.get(j).getCellData(i) instanceof Double) {
                    dataRow.createCell(j).setCellValue((double)columns.get(j).getCellData(i));
                    dataRow.getCell(j).setCellStyle(currencyStyle);
                }
            }
        }

        for (int i = 0; i < columns.size(); ++i) {
            spreadsheet.autoSizeColumn(i);
        }
    }
}
