package ca.ubc.cs304.model;


/**
 * The intent for this class is to update/store information about a single invoice
 */
public class InvoiceModel {
    private final int invoiceNumber;
    private final double invoiceAmount;
    private final String invoiceDate;
    private final ReservationModel reservationModel;
    private final HotelServiceModel hotelServiceModel;
    private final PaymentModel paymentModel;

    public InvoiceModel(int invoiceNumber,
                        double invoiceAmount,
                        String invoiceDate,
                        ReservationModel reservationModel,
                        HotelServiceModel hotelServiceModel,
                        PaymentModel paymentModel){

        this.invoiceNumber = invoiceNumber;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
        this.hotelServiceModel = hotelServiceModel;
        this.reservationModel = reservationModel;
        this.paymentModel = paymentModel;
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

    public ReservationModel getReservationModel() {
        return reservationModel;
    }

    public HotelServiceModel getHotelServiceModel() {
        return hotelServiceModel;
    }

    public PaymentModel getPaymentModel() {
        return paymentModel;
    }
}
