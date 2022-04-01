package ca.ubc.cs304.model;


/**
 * The intent for this class is to update/store information about a single payment
 */
public class PaymentModel {
    private final int paymentNumber;
    private final double paymentAmount;
    private final String paymentMethod;
    private final String paymentDate;

    public PaymentModel(int paymentNumber,
                        double paymentAmount,
                        String paymentMethod,
                        String paymentDate){
        this.paymentNumber  = paymentNumber;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
}
