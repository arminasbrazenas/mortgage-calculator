package mortgagecalculator.mortgages;

import javafx.collections.transformation.FilteredList;

public class LinearMortgage extends Mortgage {
    public LinearMortgage(double amount, int termInYears, double interestRate) {
        super(amount, termInYears, interestRate);
    }

    @Override
    public FilteredList<MortgageRecord> calculate() {
        if (this.getRecords().size() > 0) {
            return this.getFilteredRecords();
        }

        final double redeemed = this.getAmount() / this.getTermInMonths();
        double amountLeft = this.getAmount();

        for (int month = 1; month <= this.getTermInMonths(); ++month) {
            final double interest = amountLeft * this.getMonthlyInterestRate();
            final double payment = redeemed + interest;
            MortgageRecord record = new MortgageRecord(month, amountLeft, payment, interest, redeemed, false);
            this.records.add(record);

            amountLeft -= redeemed;
        }

        return this.getFilteredRecords();
    }
}
