package ca.ubc.cs304.model;


/**
 * The intent for this class is to update/store information about a single reservation
 */
public class ReservationModel {

    //String reservationDate = new Date();
    //SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YY HH:mm");

    private final int reservationID;
    private final String reservationDate;
    private final String checkInDate;
    private final String checkOutDate;
    private final int roomNo;
    private final int customerID;
    private final int hotelID;
    private final int invoiceNumber;
    private final int eventID;
    private final int facilityID;

    public ReservationModel(int reservationID,
                            String reservationDate,
                            String checkInDate,
                            String checkOutDate,
                            int roomNo,
                            int customerID,
                            int hotelID,
                            int invoiceNumber,
                            int facilityID,
                            int eventID){

        //RoomModel roomModel = new RoomModel();

        this.reservationID = reservationID;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomNo = roomNo;
        this.customerID = customerID;
        this.hotelID = hotelID;
        this.invoiceNumber = invoiceNumber;
        this.eventID = eventID;
        this.facilityID = facilityID;
    }

    public int getReservationID() {
        return reservationID;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getHotelID() {
        return hotelID;
    }

    public int getEventID() {
        return eventID;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public int getRoomNo() {
        return roomNo;
    }
}