package mortgagecalculator.mortgages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public abstract class Mortgage {
    /** Mortgage records. */
    protected final ObservableList<MortgageRecord> records;

    /** Mortgage records filtered by month range. */
    protected final FilteredList<MortgageRecord> filteredRecords;

    /** Initial amount of the mortgage. */
    private final double amount;

    /** Yearly interest rate of the mortgage.  */
    private final double interestRate;

    /** Length of the mortgage expressed in years. */
    private final int termInYears;

    /** Temporary deferral period of payments for the mortgage expressed in months. */
    private int deferralInMonths;

    /** Start month of the temporary deferral period. */
    private int deferralStartMonth;

    /** Interest rate during the temporary deferral period. */
    private double deferralInterestRate;

    public Mortgage(double amount, int termInYears, double interestRate) {
        this.amount = amount;
        this.termInYears = termInYears;
        this.interestRate = interestRate;
        this.records = FXCollections.observableArrayList();
        this.filteredRecords = new FilteredList<>(this.records);
    }

    public int getTermInYears() { return this.termInYears; }
    public int getTermInMonths() { return this.getTermInYears() * 12; }
    public int getDeferralInMonths() { return this.deferralInMonths; }
    public int getDeferralStartMonth() { return this.deferralStartMonth; }
    public double getAmount() { return this.amount; }
    public double getInterestRate() { return this.interestRate; }
    public double getMonthlyInterestRate() { return this.interestRate / 12; }
    public double getDeferralInterestRate() { return this.deferralInterestRate; }
    public FilteredList<MortgageRecord> getFilteredRecords() { return this.filteredRecords; }
    public ObservableList<MortgageRecord> getRecords() { return this.records; }

    public abstract FilteredList<MortgageRecord> calculate();

    public FilteredList<MortgageRecord> defer(int deferralInMonths, int deferralStartMonth, double deferralInterestRate) {
        this.removeDeferredRecords();

        this.deferralInMonths = deferralInMonths;
        this.deferralStartMonth = deferralStartMonth;
        this.deferralInterestRate = deferralInterestRate;

        double principalLeft = this.records.get(this.getDeferralStartMonth() - 1).getAmountLeft();
        double interestRate = this.getDeferralInterestRate() / 12;

        for (int month = 0; month < deferralInMonths; ++month) {
            double interest = principalLeft * interestRate;
            MortgageRecord record = new MortgageRecord(deferralStartMonth + month, principalLeft, interest, interest, 0, true);
            this.records.add(deferralStartMonth + month - 1, record);
        }

        this.reassignMonths();
        return this.filteredRecords;
    }

    public void filter(int monthStart, int monthEnd) {
        this.filteredRecords.setPredicate(record -> (record.getMonth() >= monthStart && record.getMonth() <= monthEnd));
    }

    private void removeDeferredRecords() {
        for (int i = 0; i < this.records.size(); ++i) {
            if (this.records.get(i).getIsDeferred()) {
                this.records.remove(i);
                --i;
            }
        }
    }

    private void reassignMonths() {
        for (int i = 0; i < this.records.size(); ++i) {
            this.records.get(i).setMonth(i + 1);
        }
    }
}
