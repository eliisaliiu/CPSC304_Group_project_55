package ca.ubc.cs304.model;

import java.text.SimpleDateFormat;
import java.util.Date;


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
    private final int roomID;
    private final int customerID;
    private final int hotelID;
    private final int invoiceID;
    private final int eventID;
    private final int facilityID;

    public ReservationModel(int reservationID,
                            String reservationDate,
                            String checkInDate,
                            String checkOutDate,
                            int roomID,
                            int customerID,
                            int hotelID,
                            int invoiceID,
                            int facilityID,
                            int eventID){

        //RoomModel roomModel = new RoomModel();

        this.reservationID = reservationID;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomID = roomID;
        this.customerID = customerID;
        this.hotelID = hotelID;
        this.invoiceID = invoiceID;
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

    public int getInvoiceID() {
        return invoiceID;
    }

    public int getRoomID() {
        return roomID;
    }
}
