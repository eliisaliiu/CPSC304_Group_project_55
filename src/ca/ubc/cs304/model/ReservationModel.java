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
    private final RoomModel roomModel;
    private final CustomerModel customerModel;
    private final HotelModel hotelModel;
    private final InvoiceModel invoiceModel;
    private final EventModel eventModel;
    private final FacilityModel facilityModel;

    public ReservationModel(int reservationID,
                            String reservationDate,
                            String checkInDate,
                            String checkOutDate,
                            RoomModel roomModel,
                            CustomerModel customerModel,
                            HotelModel hotelModel,
                            InvoiceModel invoiceModel,
                            FacilityModel facilityModel,
                            EventModel eventModel){

        //RoomModel roomModel = new RoomModel();

        this.reservationID = reservationID;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomModel = roomModel;
        this.customerModel = customerModel;
        this.hotelModel = hotelModel;
        this.invoiceModel = invoiceModel;
        this.eventModel = eventModel;
        this.facilityModel = facilityModel;
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

    public RoomModel getRoomModel() {
        return roomModel;
    }

    public HotelModel getHotelModel() {
        return hotelModel;
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public InvoiceModel getInvoiceModel() {
        return invoiceModel;
    }

    public EventModel getEventModel() {
        return eventModel;
    }

    public FacilityModel getFacilityModel() {
        return facilityModel;
    }
}
