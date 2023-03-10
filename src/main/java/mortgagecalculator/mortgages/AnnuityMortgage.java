package mortgagecalculator.mortgages;

import javafx.collections.transformation.FilteredList;

public class AnnuityMortgage extends Mortgage {
    public AnnuityMortgage(double amount, int termInYears, double interestRate) {
        super(amount, termInYears, interestRate);
    }

    @Override
    public void calculate() {
        if (this.getRecords().size() > 0) {
            return;
        }

        final double annuity = this.getCoefficient() * this.getAmount();
        double amountLeft = this.getAmount();

        for (int month = 1; month <= this.getTermInMonths(); ++month) {
            final double interest = amountLeft * this.getMonthlyInterestRate();
            final double redeemed = annuity - interest;
            MortgageRecord record = new MortgageRecord(month, amountLeft, annuity, interest, redeemed, false);
            this.records.add(record);

            amountLeft -= redeemed;
        }
    }

    private double getCoefficient() {
        final double interest = this.getMonthlyInterestRate();
        if (Double.compare(interest, 0) == 0) {
            return 1f / this.getTermInMonths();
        }

        final int months = this.getTermInMonths();
        return (interest * Math.pow(1 + interest, months)) / (Math.pow(1 + interest, months) - 1);
    }
}
