package ca.ubc.cs304.database;

import ca.ubc.cs304.model.*;
import ca.ubc.cs304.util.PrintablePreparedStatement;
import ca.ubc.cs304.DBTablePrinter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	// Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
	// Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";

	private Connection connection = null;

	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void insertReservation(ReservationModel model) {
		try {
			String query = "INSERT INTO Reservation VALUES (?,?,?,?,?,?,?,?,?,?)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setInt(1, model.getReservationID());
			ps.setString(2, model.getReservationDate());
			ps.setString(3, model.getCheckInDate());
			ps.setString(4, model.getCheckOutDate());
			if (model.getRoomNo() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getRoomNo());
			}
			ps.setInt(6,model.getCustomerID());
			ps.setInt(7,model.getHotelID());
			ps.setInt(8,model.getInvoiceNumber());
			if (model.getEventID() == 0) {
				ps.setNull(9, java.sql.Types.INTEGER);
			} else {
				ps.setInt(9, model.getEventID());
			}
			if (model.getFacilityID() == 0) {
				ps.setNull(10, java.sql.Types.INTEGER);
			} else {
				ps.setInt(10, model.getFacilityID());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}


	public void deleteCustomer(int customer_ID) {
		try {
			String query = "DELETE FROM CUSTOMER WHERE CUSTOMERID = ?";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setInt(1, customer_ID);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Customer " + customer_ID + " does not exist!");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}


	public InvoiceModel[]  aggregateMostExpensiveInvoice(){
			ArrayList<InvoiceModel> result = new ArrayList<InvoiceModel>();
		try{
			String query = "SELECT * FROM INVOICE WHERE INVOICEAMOUNT = (SELECT  MAX(INVOICEAMOUNT) FROM  INVOICE)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query,false);
			ResultSet rs = ps.executeQuery();
			DBTablePrinter.printResultSet(rs);
			while(rs.next()){
				InvoiceModel model = new InvoiceModel(
						rs.getInt("invoiceNumber"),
						rs.getDouble("invoiceAmount"),
						rs.getString("invoiceDate"),
						rs.getInt("customerID"),
						rs.getInt("hotelID"),
						rs.getInt("serviceID"),
						rs.getInt("facilityID"),
						rs.getInt("paymentNumber")

				);
				result.add(model);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		 return result.toArray(new InvoiceModel[result.size()]);
	}

	//return no table makes sense string[]
	public String[] joinMailsOfCustomersMoreThanOneWeek(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			String query = "SELECT DISTINCT s.CUSTOMEREMAIL  " +
							"FROM  CUSTOMER s, RESERVATION r " +
							"WHERE (r.CHECKOUTDATE-r.CHECKINDATE) > 7  	" +
							"AND s.CUSTOMERID =r.CUSTOMERID";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query,false);
			ResultSet rs = ps.executeQuery();
			DBTablePrinter.printResultSet(rs);
			while(rs.next()){
				String email  = rs.getString("customerEmail");
				result.add(email);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		return result.toArray(new String[result.size()]);
	}

	public InvoiceModel[] selectionInvoice(int value, String operator, String columnName) {
		// operator: (bigger, less, equal)
		ArrayList<InvoiceModel> result = new ArrayList<InvoiceModel>();
		try {
			String query;
			if(operator.equals("less")){
				query = "SELECT * FROM INVOICE WHERE " + columnName + " < " + value;
			}else if(operator.equals("bigger")){
				query = "SELECT * FROM INVOICE WHERE " + columnName + " > " + value;
			}else
				query = "SELECT * FROM INVOICE WHERE " + columnName + " = " + value;

			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();
			DBTablePrinter.printResultSet(rs);

			while(rs.next()){
				InvoiceModel model = new InvoiceModel(
						rs.getInt("invoiceNumber"),
						rs.getDouble("invoiceAmount"),
						rs.getString("invoiceDate"),
						rs.getInt("customerID"),
						rs.getInt("hotelID"),
						rs.getInt("serviceID"),
						rs.getInt("facilityID"),
						rs.getInt("paymentNumber")
				);
				result.add(model);

			}

			ps.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		return result.toArray(new InvoiceModel[result.size()]);
	}

	//return no table makes sense string[]
	public String[] projectionReservation(String columnName){
		//Columnname: one of the columns of Customer table
		// we need selection box not string box
		ArrayList<String> result = new ArrayList<String>();
		try {
			String query = "SELECT " + columnName + " FROM RESERVATION";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();
			//DBTablePrinter.printResultSet(rs);
			while(rs.next()){
				result.add(rs.getString(columnName));
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		return result.toArray(new String[result.size()]);
	}


	//return no table makes sense string[]
	public String[] divisionCustomersUsingAllServices(){
		ArrayList<String> result = new ArrayList<String>();
		try{

			String query = "SELECT c.CUSTOMERID FROM CUSTOMER c WHERE NOT EXISTS((SELECT s.serviceID FROM HOTELSERVICES s) MINUS (SELECT a.serviceID FROM INVOICE a WHERE a.CUSTOMERID = c.CUSTOMERID and a.SERVICEID IS NOT NULL))";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query,false);
			ResultSet rs = ps.executeQuery();
			DBTablePrinter.printResultSet(rs);
			while(rs.next()){
			result.add(String.valueOf(rs.getInt("customerID")));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
		return result.toArray(new String[result.size()]);
	}

	public void databaseSetup() {
		dropBranchTableIfExists();
		try {
			//Path file = Paths.get("src/ca/ubc/cs304","sql/scripts/databaseSetup.sql");
			//String query = Files.readString(file);
			String query = "CREATE TABLE Reservation (reservationID INTEGER NOT NULL, reservationDate DATE NOT NULL, checkInDate DATE NOT NULL, checkOutDate DATE NOT NULL, roomNo INTEGER, customerID INTEGER NOT NULL, hotelID INTEGER NOT NULL, invoiceNumber INTEGER NOT NULL, eventID INTEGER, facilityID INTEGER, PRIMARY KEY(reservationID),UNIQUE (customerID, hotelID, invoiceNumber), FOREIGN KEY (customerID) REFERENCES Customer, FOREIGN KEY (hotelID) REFERENCES Hotel, FOREIGN KEY (invoiceNumber) REFERENCES Invoice, FOREIGN KEY (eventID) REFERENCES Events, FOREIGN KEY (facilityID) REFERENCES Facility )";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		ReservationModel reservation1 = new ReservationModel(123,"TO_DATE('2022-10-30','YYYY-MM-DD')","DATE('2022-10-30')","TO_DATE('2022-10-30','YYYY-MM-DD')",123,54321,12345,98765,5678,1234);
		insertReservation(reservation1);

		ReservationModel reservation2 = new ReservationModel(1234,"TO_DATE('2022-10-30','YYYY-MM-DD')","TO_DATE('2022-10-30','YYYY-MM-DD')","TO_DATE('2022-10-30','YYYY-MM-DD')",0,543213,123456,98766,0,0);
		insertReservation(reservation1);



	}
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	private void dropBranchTableIfExists() {
		try {
			String query = "select table_name from user_tables";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				if(rs.getString(1).equalsIgnoreCase("Reservation")) {
					ps.execute("DROP TABLE Reservation");
					break;
				}
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	//TODO: MODIFY PRINT FUNCTION
	//TODO: SQL FILE IF EXISTS PART BROKEN
	public void showInvoiceBranch(InvoiceModel[] models) {
		for (int i = 0; i < models.length; i++) {
			InvoiceModel model = models[i];
			// simplified output formatting; truncation may occur
			System.out.printf("%-10.10s", model.getInvoiceNumber());
			System.out.printf("%-20.20s", model.getPaymentNumber());
			System.out.println();
		}
	}

/*
	public static void main(String args[]) {
		DatabaseConnectionHandler dbhandler = new DatabaseConnectionHandler();
		dbhandler.login("ora_emres", "a62827258");

		dbhandler.joinMailsofCustomersMoreThanOneWeek();
		dbhandler.aggregateMostExpensiveInvoice();
		dbhandler.showInvoiceBranch(dbhandler.selectionInvoice(13, "equal","HOTELID"));
		for(String part: dbhandler.divisionCustomersUsingAllServices()) {
			System.out.println(part);
		}
		dbhandler.projectionReservation("reservationID");


	}
 */
}

