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
            transaction.setupDatabase(this);
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
    public void databaseSetup() {
        dbHandler.databaseSetup();
    }

    @Override
    public void deleteReservation(int reservationID) {

    }

    @Override
    public void insertReservation(ReservationModel reservationModel) {

    }

    @Override
    public void showReservation() {

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
    public void showInvoiceBranch(InvoiceModel[] models) {
        for (int i = 0; i < models.length; i++) {
            InvoiceModel model = models[i];
            // simplified output formatting; truncation may occur
            System.out.printf("%-10.10s", model.getInvoiceNumber());
            System.out.printf("%-20.20s", model.getPaymentNumber());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        HotelManagement hotelManagement = new HotelManagement();
        hotelManagement.start();
    }
}
