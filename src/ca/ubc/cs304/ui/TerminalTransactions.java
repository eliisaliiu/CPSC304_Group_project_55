package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.ReservationDelegate;
import ca.ubc.cs304.model.EventModel;
import ca.ubc.cs304.model.InvoiceModel;
import ca.ubc.cs304.model.ReservationModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * The class is only responsible for handling terminal text inputs. 
 */
public class TerminalTransactions {
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private static final int INVALID_INPUT = Integer.MIN_VALUE;
	private static final int EMPTY_INPUT = 0;
	
	private BufferedReader bufferedReader = null;
	private ReservationDelegate delegate = null;
	public TerminalTransactions() {
	}
	

	/**
	 * Displays simple text interface
	 */ 
	public void showMainMenu(ReservationDelegate delegate) {
		this.delegate = delegate;
		
	    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int choice = INVALID_INPUT;
		
		while (choice != 5) {
			System.out.println();
			System.out.println("1. Join");
			System.out.println("2. Aggregate");
			System.out.println("3. Division");
			System.out.println("4. Projection on Reservation");
			System.out.println("5. Selection on Invoice");
			System.out.println("6. Update on Hotel");
			System.out.println("7. Insert on Reservation");
			System.out.println("8. Delete on Reservation");
			System.out.println("9. nestedAggregation on Invoice");
			System.out.println("10. Quit");
			System.out.print("Please choose one of the above  options: ");

			choice = readInteger(false);

			System.out.println(" ");

			if (choice != INVALID_INPUT) {
				switch (choice) {
				case 1:
					handleJoin();
					break;
				case 2:  
					handleAggregate();
					break;
				case 3:
					handleDivision();
					break;
				case 4:
					handleProjection();
					break;
				case 5:
					handleSelection();
					break;
				case 6:
					handleUpdate();
					break;
				case 7:
					handleInsertReservation();
					break;
				case 8:
					handleDeleteReservation();
					break;
				case 9:
					handleNestedAggregate();
					break;
				case 10:
					handleQuitOption();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
					break;
				}
			}
		}		
	}

	private void handleDeleteReservation() {
		int reservationID = INVALID_INPUT;
		while (reservationID == INVALID_INPUT) {
			System.out.print("Please enter the event ID you wish to delete: ");
			reservationID = readInteger(false);
			if (reservationID != INVALID_INPUT) {
				delegate.deleteReservation(reservationID);
			}
		}
	}

	private void handleInsertReservation() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the Reservation ID you wish to insert: ");
			id = readInteger(false);
		}

		String resDate = null;
		while (resDate == null || resDate.length() <= 0) {
			System.out.print("Please enter the reservation date you wish to insert in YYYY-MM-DD format:");
			resDate = readLine().trim();
		}
		String checkInDate = null;
		while (checkInDate == null || checkInDate.length() <= 0) {
			System.out.print("Please enter the checkin date you wish to insert in YYYY-MM-DD format:");
			checkInDate = readLine().trim();
		}
		String checkOutDate = null;
		while (checkOutDate == null || checkOutDate.length() <= 0) {
			System.out.print("Please enter the checkout date you wish to insert in YYYY-MM-DD format:");
			checkOutDate = readLine().trim();
		}
		int roomNo = INVALID_INPUT;
		while (roomNo == INVALID_INPUT) {
			System.out.print("Please enter the roomNo you wish to insert: Hint 300 ");
			roomNo = readInteger(true);
		}
		int customerID = INVALID_INPUT;
		while (customerID == INVALID_INPUT) {
			System.out.print("Please enter the customerID number you wish to insert: Hint 73648 ");
			customerID = readInteger(true);
		}
		int hotelID = INVALID_INPUT;
		while (hotelID == INVALID_INPUT) {
			System.out.print("Please enter the hotelID you wish to insert: Hint 13  ");
			hotelID = readInteger(true);
		}
		int invoiceNumber = INVALID_INPUT;
		while (invoiceNumber == INVALID_INPUT) {
			System.out.print("Please enter the invoiceNumber you wish to insert: Hint 92847563 ");
			invoiceNumber = readInteger(true);
		}
		int eventID = INVALID_INPUT;
		while (eventID == INVALID_INPUT) {
			System.out.print("Please enter the eventID you wish to insert: Hint 23453 ");
			eventID = readInteger(true);
		}
		int facilityID = INVALID_INPUT;
		while (facilityID == INVALID_INPUT) {
			System.out.print("Please enter the facilityID you wish to insert: 50");
			facilityID = readInteger(true);
		}
		ReservationModel model = new ReservationModel(id,resDate,checkInDate,checkOutDate,roomNo,customerID,hotelID,invoiceNumber,facilityID,eventID);
		delegate.insertReservation(model);
	}

	private void handleQuitOption() {
		System.out.println("Good Bye!");
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("IOException!");
			}
		}
		
		delegate.reservationFinished();
	}

	private void handleJoin(){
		delegate.joinMailsofCustomersMoreThanOneWeek();
	}

	private void handleNestedAggregate(){
		delegate.nestedAggregationInvoice();
	}

	private void handleAggregate(){
		delegate.showInvoiceTable(delegate.aggregateMostExpensiveInvoice());
	}

	private void  handleSelection(){
		//TODO: Take inputs from GUI to pass this method
		int value = INVALID_INPUT;
		while (value == INVALID_INPUT) {
			System.out.print("Please enter value you wish to compare: ");
			value = readInteger(false);
		}
		String operator = null;
		while (operator == null || operator.length() <= 0) {
			System.out.print("Please enter the operator: ");
			operator = readLine().trim();
		}
		String columnName = null;
		while (columnName == null || columnName.length() <= 0) {
			System.out.print("Please enter the columnName: ");
			columnName = readLine().trim();
		}
		InvoiceModel[] models = delegate.selectionInvoice(value,operator,columnName);
		delegate.showInvoiceTable(models);
	}

	private void handleProjection(){
		String columnName = null;
		while (columnName == null || columnName.length() <= 0) {
			System.out.print("Please enter the columnName: ");
			columnName = readLine().trim();
		}

		String[] columnValues = delegate.projectionReservation(columnName);
		System.out.println(columnName);
		System.out.println("--------------------------------------");
		for(String value : columnValues){
			System.out.println(value);
		}
	}

	private void handleDivision(){
		String[] list = delegate.divisionCustomersUsingAllServices();
		//System.out.println("customerID");
		//System.out.println("--------------------------------------");
		//for(String value : list){
		//	System.out.println(value);
		//}

	}

	private void handleUpdate() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the hotel ID you wish to update: ");
			id = readInteger(false);
		}
		String type = null;
		while (type == null || type.length() <= 0) {
			System.out.print("Please enter the hotel name you wish to update: ");
			type = readLine().trim();
		}
		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the hotel name you wish to update: ");
			name = readLine().trim();
		}
		delegate.updateHotel(id, type, name);
	}

	private int readInteger(boolean allowEmpty) {
		String line = null;
		int input = INVALID_INPUT;
		try {
			line = bufferedReader.readLine();
			input = Integer.parseInt(line);
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		} catch (NumberFormatException e) {
			if (allowEmpty && line.length() == 0) {
				input = EMPTY_INPUT;
			} else {
				System.out.println(WARNING_TAG + " Your input was not an integer");
			}
		}
		return input;
	}
	
	private String readLine() {
		String result = null;
		try {
			result = bufferedReader.readLine();
		} catch (IOException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}

}
