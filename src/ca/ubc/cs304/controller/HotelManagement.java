package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.ReservationDelegate;
import ca.ubc.cs304.model.InvoiceModel;
import ca.ubc.cs304.model.ReservationModel;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;



public class HotelManagement  implements LoginWindowDelegate, ReservationDelegate {

    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;

    public HotelManagement() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
    }


    @Override
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();

            TerminalTransactions transaction = new TerminalTransactions();
            transaction.showMainMenu(this);
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    @Override
    public void deleteReservation(int reservationID) {

    }
    @Override
    public void insertReservation(ReservationModel reservationModel) {

    }

    @Override
    public void updateReservation(int reservationID, String reservationDate) {

    }

    @Override
    public void reservationFinished() {
      dbHandler.close();
      dbHandler = null;
      System.exit(0);
    }

    @Override
    public String[] joinMailsofCustomersMoreThanOneWeek() {
       return dbHandler.joinMailsofCustomersMoreThanOneWeek();
    }

    @Override
    public InvoiceModel[] aggregateMostExpensiveInvoice() {
        return dbHandler.aggregateMostExpensiveInvoice();
    }


    @Override
    public void showInvoiceTable(InvoiceModel[] models) {
        for (int i = 0; i < models.length; i++) {
            InvoiceModel model = models[i];
            System.out.printf("%-10.10s", model.getInvoiceNumber());
            System.out.printf("PaymentNumber%-20.20s", model.getPaymentNumber());
            System.out.printf("CustomerID%-20.20s", model.getCustomerID());
            System.out.println();
        }
    }

    @Override
    public InvoiceModel[] selectionInvoice(int value, String operator, String columnName) {
        return dbHandler.selectionInvoice(value, operator, columnName);
    }

    @Override
    public String[] projectionReservation(String columnName) {
        return dbHandler.projectionReservation(columnName);
    }

    @Override
    public String[] divisionCustomersUsingAllServices() {
        return dbHandler.divisionCustomersUsingAllServices();
    }


    public static void main(String[] args) {
        HotelManagement hotelManagement = new HotelManagement();
        hotelManagement.start();
    }
}
