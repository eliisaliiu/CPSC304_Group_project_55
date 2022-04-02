package ca.ubc.cs304.model;


/**
 * The intent for this class is to update/store information about a single invoice
 */
public class InvoiceModel {
    private final int invoiceNumber;
    private final double invoiceAmount;
    private final String invoiceDate;
    private final int customerID;
    private final int hotelID;
    private final int serviceID;
    private final int facilityID;
    private final int paymentNumber;

    public InvoiceModel(int invoiceNumber,
                        double invoiceAmount,
                        String invoiceDate,
                        int customerID,
                        int hotelID,
                        int serviceID,
                        int facilityID,
                        int paymentNumber
                        ){

        this.invoiceNumber = invoiceNumber;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
        this.customerID = customerID;
        this.hotelID = hotelID;
        this.serviceID = serviceID;
        this.facilityID = facilityID;
        this.paymentNumber = paymentNumber;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public int getHotelID() {
        return hotelID;
    }

    public int getServiceID() {
        return serviceID;
    }

    @Override
    public String toString() {
        return "InvoiceModel{" +
                "invoiceNumber=" + invoiceNumber +
                ", invoiceAmount=" + invoiceAmount +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", customerID=" + customerID +
                ", hotelID=" + hotelID +
                ", serviceID=" + serviceID +
                ", facilityID=" + facilityID +
                ", paymentNumber=" + paymentNumber +
                '}';
    }
}
