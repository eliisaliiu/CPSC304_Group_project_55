package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.ReservationDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.InvoiceModel;

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
	 * Sets up the database to have a branch table with two tuples so we can insert/update/delete from it.
	 * Refer to the databaseSetup.sql file to determine what tuples are going to be in the table.
	 */

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
			System.out.println("6. Quit");
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
					handleQuitOption();
					break;
				default:
					System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
					break;
				}
			}
		}		
	}
	/*
	private void handleDeleteOption() {
		int branchId = INVALID_INPUT;
		while (branchId == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to delete: ");
			branchId = readInteger(false);
			if (branchId != INVALID_INPUT) {
				delegate.deleteBranch(branchId);
			}
		}
	}
	
	private void handleInsertOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to insert: ");
			id = readInteger(false);
		}
		
		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to insert: ");
			name = readLine().trim();
		}
		
		// branch address is allowed to be null so we don't need to repeatedly ask for the address
		System.out.print("Please enter the branch address you wish to insert: ");
		String address = readLine().trim();
		if (address.length() == 0) {
			address = null;
		}
		
		String city = null;
		while (city == null || city.length() <= 0) {
			System.out.print("Please enter the branch city you wish to insert: ");
			city = readLine().trim();
		}
		
		int phoneNumber = INVALID_INPUT;
		while (phoneNumber == INVALID_INPUT) {
			System.out.print("Please enter the branch phone number you wish to insert: ");
			phoneNumber = readInteger(true);
		}
		
		BranchModel model = new BranchModel(address,
											city,
											id,
											name,
											phoneNumber);
		delegate.insertBranch(model);
	}
	*/
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
		String[] list = delegate.joinMailsofCustomersMoreThanOneWeek();
		//System.out.println("customerID");
		//System.out.println("--------------------------------------");
		//for(String value : list){
		//	System.out.println(value);
		//}

	}


 	/*
	private void handleUpdateOption() {
		int id = INVALID_INPUT;
		while (id == INVALID_INPUT) {
			System.out.print("Please enter the branch ID you wish to update: ");
			id = readInteger(false);
		}
		
		String name = null;
		while (name == null || name.length() <= 0) {
			System.out.print("Please enter the branch name you wish to update: ");
			name = readLine().trim();
		}

		delegate.updateBranch(id, name);
	}
	 */
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
