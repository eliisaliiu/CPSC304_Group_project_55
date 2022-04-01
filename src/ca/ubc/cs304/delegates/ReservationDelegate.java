package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.ReservationModel;

public interface ReservationDelegate {
    public void databaseSetup();

    public void deleteReservation(int reservationID);
    public void insertReservation(ReservationModel reservationModel);
    public void showReservation();
    public void updateReservation(int reservationID, String reservationDate);

    public void reservationFinished();
}
