package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.ReservationDelegate;
import ca.ubc.cs304.model.ReservationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotelManagementGUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    private ReservationDelegate delegate;

    private JPanel topPanel;
    private JPanel bottomPanel;
    private JList jlist;


    private JFrame frame;
    private JButton insertReservationButton;

    public HotelManagementGUI() {
        super("Hotel Management Database");
    }

    public void showFrame(ReservationDelegate delegate){
        this.delegate = delegate;
        frame = this;
        initializeFields();
        initializeGraphics();
        initializeUserInteraction();
    }

    private void initializeFields() {
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        insertReservationButton = new JButton("Insert Reservation");


    }

    private void initializeGraphics() {
        setLayout(new FlowLayout());
        setMinimumSize(new Dimension(WIDTH,HEIGHT));
        setBothPanels();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setTopPanel(){
        topPanel.add(insertReservationButton);
    }

    private void setBothPanels() {
        frame.setLayout(new FlowLayout());
        setTopPanel();
        frame.add(topPanel);
    }


    private void initializeUserInteraction() {
        initializedInsertButton();
    }



    private void initializedInsertButton(){
        insertReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reservationID = JOptionPane.showInputDialog(jlist,"Enter a reservationId: ");
//                String reservationDate = JOptionPane.showInputDialog(jlist,"Enter a reservation date: ");
//                String checkInDate = JOptionPane.showInputDialog(jlist,"Enter a check in date: ");
//                String checkOutDate = JOptionPane.showInputDialog(jlist,"Enter a check out date: ");
//                String roomNo = JOptionPane.showInputDialog(jlist,"Enter a roomNo: ");
//                String customerID = JOptionPane.showInputDialog(jlist,"Enter a customer ID: ");
//                String hotelID = JOptionPane.showInputDialog(jlist,"Enter a hotel ID: ");
//                String invoiceNumber = JOptionPane.showInputDialog(jlist,"Enter a invoice Number: ");
//                String eventID = JOptionPane.showInputDialog(jlist,"Enter a eventID: ");

                ReservationModel model = new ReservationModel(Integer.parseInt(reservationID),"2020-10-20","2020-11-25","2020-11-26",300,73648,13,92847563,0,0);
                delegate.insertReservation(model);

            }
        });
    }

    public static void main(String[] args) {
        new HotelManagementGUI();
    }
}
