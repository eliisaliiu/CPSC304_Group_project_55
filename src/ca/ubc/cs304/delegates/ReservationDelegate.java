package ca.ubc.cs304.delegates;

import ca.ubc.cs304.DBTablePrinter;
import ca.ubc.cs304.model.InvoiceModel;
import ca.ubc.cs304.model.ReservationModel;
import ca.ubc.cs304.util.PrintablePreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ReservationDelegate {

    public void deleteReservation(int reservationID);
    public void insertReservation(ReservationModel reservationModel);
    public void  showInvoiceTable(InvoiceModel[] models);

    public void updateReservation(int reservationID, String reservationDate);
    public void reservationFinished();

    //new queries
    public String[] joinMailsofCustomersMoreThanOneWeek();
    public InvoiceModel[] aggregateMostExpensiveInvoice();
    public InvoiceModel[] selectionInvoice(int value, String operator, String columnName);
    public String[] projectionReservation(String columnName);
    public String[] divisionCustomersUsingAllServices();
}

