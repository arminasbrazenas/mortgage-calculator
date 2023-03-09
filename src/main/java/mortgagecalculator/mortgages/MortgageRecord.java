package mortgagecalculator.mortgages;

public class MortgageRecord {
    /** The month in which the mortgage payment is being made. */
    private int month;

    /** Remaining amount of the mortgage at the beginning of the month. */
    private final double amountLeft;

    /** Total payment made towards the mortgage in the record. */
    private final double payment;

    /** The interest portion of the mortgage payment made in the record. */
    private final double interest;

    /** The portion of the mortgage payment made towards reducing the total amount in the record. */
    private final double redeemed;

    /** True if the given record is part of the mortgage deferral.  */
    private final boolean isDeferred;

    public MortgageRecord(int month, double amountLeft, double payment, double interest, double redeemed, boolean isDeferred) {
        this.month = month;
        this.amountLeft = amountLeft;
        this.payment = payment;
        this.interest = interest;
        this.redeemed = redeemed;
        this.isDeferred = isDeferred;
    }

    public int getMonth() { return this.month; }
    public double getAmountLeft() { return this.amountLeft; }
    public double getPayment() { return this.payment; }
    public double getInterest() { return this.interest; }
    public double getRedeemed() { return this.redeemed; }
    public boolean getIsDeferred() { return this.isDeferred; }

    public void setMonth(int month) {
        if (month > 0) {
            this.month = month;
        }
    }
}
