package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.InvoiceModel;
import ca.ubc.cs304.model.ReservationModel;


public interface ReservationDelegate {

    public void nestedAggregationInvoice();
    public void deleteReservation(int reservationID);
    public void insertReservation(ReservationModel reservationModel);
    public void  showInvoiceTable(InvoiceModel[] models);
    public void reservationFinished();
    public void updateHotel(int id, String type, String name);
    public String[] joinMailsofCustomersMoreThanOneWeek();
    public InvoiceModel[] aggregateMostExpensiveInvoice();
    public InvoiceModel[] selectionInvoice(int value, String operator, String columnName);
    public String[] projectionReservation(String columnName);
    public String[] divisionCustomersUsingAllServices();
}

