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
			String query = "INSERT INTO Reservation VALUES (?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),?,?,?,?,?,?)";
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

	public void insertEvent(EventModel model) {
		try {
			String query = "INSERT INTO EVENTS VALUES (?,?,?,?,TO_DATE(?,'YYYY-MM-DD'))";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setInt(1, model.getEventID());
			ps.setDouble(2, model.getEventQuota());
			ps.setString(3,model.getEventType());
			ps.setInt(4,model.getNumberOfGuests());
			ps.setString(5, model.getDateAvailable());

			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void deleteReservation(int reservationID) {

		try {
			String query = "DELETE FROM RESERVATION WHERE RESERVATIONID = ?";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setInt(1, reservationID);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Reservation with ID " + reservationID + " does not exist!");
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

	// maximum average invoice comes from a particular customer
	public void nestedAggregateInvoice(){
		try {
			String query = "SELECT MAX(avg) " +
					"FROM " +
					"(SELECT AVG(INVOICEAMOUNT) AS avg FROM INVOICE GROUP BY CUSTOMERID)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();
			DBTablePrinter.printResultSet(rs);
			ps.close();
			rs.close();
	} catch (SQLException e) {
		System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		rollbackConnection();
	}
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


	//TODO print statement change to reservation or add return string
	public void updateHotel(int id, String hotelType, String hotelName) {
		try {
			String query = "UPDATE HOTEL SET HOTELTYPE = \'" + hotelType + "\',HOTELNAME = \'" + hotelName + "\' WHERE HOTELID = " + id;
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Hotel with ID:  " + id + " does not exist!");
			}
			connection.commit();
			ps.close();

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	private void executeHelper(String query) throws SQLException {
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();
	}

	public void reservationSetUp() throws SQLException {
		String query = "CREATE TABLE Reservation (reservationID INTEGER NOT NULL, reservationDate DATE NOT NULL, checkInDate DATE NOT NULL, checkOutDate DATE NOT NULL, roomNo INTEGER, customerID INTEGER NOT NULL, hotelID INTEGER NOT NULL, invoiceNumber INTEGER NOT NULL, eventID INTEGER, facilityID INTEGER, PRIMARY KEY(reservationID),UNIQUE (customerID, hotelID, invoiceNumber), FOREIGN KEY (customerID) REFERENCES Customer, FOREIGN KEY (hotelID) REFERENCES Hotel, FOREIGN KEY (invoiceNumber) REFERENCES Invoice, FOREIGN KEY (eventID) REFERENCES Events, FOREIGN KEY (facilityID) REFERENCES Facility )";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO Reservation VALUES (1111111111, TO_DATE('2020-10-20','YYYY-MM-DD'), TO_DATE('2020-11-19','YYYY-MM-DD'), TO_DATE('2020-11-20','YYYY-MM-DD'),300,73648,13,92847563, null,null)");
		executeHelper("INSERT INTO Reservation VALUES (2222222222, TO_DATE('2020-02-10','YYYY-MM-DD'), TO_DATE('2020-03-11','YYYY-MM-DD'), TO_DATE('2020-03-14','YYYY-MM-DD'),100,92837,13,10293846, 23453,50)");
		executeHelper("INSERT INTO Reservation VALUES (3333333333, TO_DATE('2020-03-15','YYYY-MM-DD'), TO_DATE('2020-03-16','YYYY-MM-DD'), TO_DATE('2020-03-25','YYYY-MM-DD'),123,46352,24,38273957,null, null)");
		executeHelper("INSERT INTO Reservation VALUES (4444444444, TO_DATE('2020-01-20','YYYY-MM-DD'), TO_DATE('2020-02-21','YYYY-MM-DD'), TO_DATE('2020-03-01','YYYY-MM-DD'),200,85769,12,38264061,null,null )");
		executeHelper("INSERT INTO Reservation VALUES (5555555555, TO_DATE('2020-08-01','YYYY-MM-DD'), TO_DATE('2020-09-02','YYYY-MM-DD'), TO_DATE('2020-09-02','YYYY-MM-DD'),150,36252,22,27364951,null,null )");

	}

	public void invoiceSetUp() throws SQLException {
		String query = "CREATE TABLE Invoice (invoiceNumber INT NOT NULL, invoiceDate DATE, customerID INT NOT NULL, hotelID INT NOT NULL, invoiceAmount DEC NOT NULL, serviceID INT, facilityID INT, paymentNumber INT NOT NULL, PRIMARY KEY (invoiceNumber), FOREIGN KEY (customerID) REFERENCES Customer, FOREIGN KEY (hotelID) REFERENCES Hotel, FOREIGN KEY (serviceID) REFERENCES HotelServices, FOREIGN KEY (facilityID) REFERENCES Facility, FOREIGN KEY (paymentNumber) REFERENCES Payment)";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO Invoice VALUES (92847563,TO_DATE('2020-10-21','YYYY-MM-DD'),73648,13,3049.00,2938,null,192837)");
		executeHelper("INSERT INTO Invoice VALUES (10293846,TO_DATE('2020-02-11','YYYY-MM-DD'),92837,13,39473.00,3948,null,394856)");
		executeHelper("INSERT INTO Invoice VALUES (38273957,TO_DATE('2020-03-16','YYYY-MM-DD'),46352,24,3748.00,null,50,485739)");
		executeHelper("INSERT INTO Invoice VALUES (38264061,TO_DATE('2020-01-21','YYYY-MM-DD'),85769,12,2937.00,null,null,384759)");
		executeHelper("INSERT INTO Invoice VALUES (27364951,TO_DATE('2020-08-02','YYYY-MM-DD'),36252,22,3847.00,null,null,183754)");
		executeHelper("INSERT INTO Invoice VALUES (87384958,TO_DATE('2020-08-03','YYYY-MM-DD'),73648,22,4000.00,3948,null,488758)");
		executeHelper("INSERT INTO Invoice VALUES (40543821,TO_DATE('2020-08-21','YYYY-MM-DD'),92837,13,473.00,2938,null,224145)");

	}

	public void customerSetUp() throws SQLException {
		String query = "CREATE TABLE Customer (customerID INT NOT NULL,customerName CHAR(20) NOT NULL,customerPhone INT NOT NULL,customerEmail CHAR(20) NOT NULL,PRIMARY KEY (customerID),UNIQUE (customerEmail) )";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper( "INSERT INTO Customer VALUES (73648,'Shania Twain',9283756504,'shania@gmail.com')");
		executeHelper( "INSERT INTO Customer VALUES (92837,'Justin Bieber',8372915485,'jb@gmail.com')");
		executeHelper( "INSERT INTO Customer VALUES (46352,'Prince Philips',2735485960,'pp@gmail.com')");
		executeHelper( "INSERT INTO Customer VALUES (85769,'Elisa Liu',1246584910,'el@gmail.com')");
		executeHelper( "INSERT INTO Customer VALUES (36252,'Brad Pitt',4193520673,'bp@gmail.com')");
	}

	public void hotelServicesSetUp() throws SQLException {
		String query = "CREATE TABLE HotelServices (serviceID INT  NOT NULL,serviceName CHAR(20),serviceType CHAR(20),servicePrice DEC,PRIMARY KEY (serviceID))";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO HotelServices VALUES (2938, 'Room Service', 'Room', 100.00)");
		executeHelper("INSERT INTO HotelServices VALUES (3948, 'Massage', 'Spa', 10.00)");
	}

	public void roomsSetUp() throws SQLException {
		String query = "CREATE TABLE Rooms (roomNo INT NOT NULL, floorNo INT, roomType CHAR(20), numberOfBeds INT, hotelID INT NOT NULL, typeOfView   CHAR(20), roomPrice    INT NOT NULL, typeOfBed    CHAR(20), PRIMARY KEY (roomNo), FOREIGN KEY (hotelID) REFERENCES Hotel)";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO Rooms VALUES (300,3, 'Suite',null,13,'Ocean',20.00,null)");
		executeHelper("INSERT INTO Rooms VALUES (100,1, 'Multiple',3,13,null,1020.00,null )");
		executeHelper("INSERT INTO Rooms VALUES (123,1, 'Single',null,24,null,100.00,'Queen')");
		executeHelper("INSERT INTO Rooms VALUES (200,2, 'Suite',null,12,'Garden',1.00,null)");
		executeHelper("INSERT INTO Rooms VALUES (150,1, 'Suite',null,22,'Ocean',150.00,null  )");


	}

	public void eventSetUp() throws SQLException {
		String query = "CREATE TABLE Events (eventID INT NOT NULL, numberofGuests INT NOT NULL, eventType CHAR(20), eventQuota DEC, dateAvailable DATE, PRIMARY KEY (eventID))";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO Events VALUES (23453,300,'Wedding', 5010.00, TO_DATE('2020-03-11','YYYY-MM-DD'))");
	}

	public void paymentSetUp() throws SQLException {
		String query = "CREATE TABLE Payment (paymentNumber INT NOT NULL, paymentAmount DEC NOT NULL, paymentMethod CHAR(20) NOT NULL, paymentDate DATE, PRIMARY KEY (paymentNumber))";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO Payment VALUES (192837,3049.00, 'Credit', TO_DATE('2020-10-21','YYYY-MM-DD'))");
		executeHelper("INSERT INTO Payment VALUES (394856,39473.00, 'Debit', TO_DATE('2020-02-11','YYYY-MM-DD'))");
		executeHelper("INSERT INTO Payment VALUES (485739,3748.00, 'Debit', TO_DATE('2020-03-16','YYYY-MM-DD'))");
		executeHelper("INSERT INTO Payment VALUES (384759,2937.00, 'Credit', TO_DATE('2020-01-21','YYYY-MM-DD'))");
		executeHelper("INSERT INTO Payment VALUES (183754,3847.00, 'Credit', TO_DATE('2020-08-02','YYYY-MM-DD'))");
		executeHelper("INSERT INTO Payment VALUES (488758,4000.00, 'Debit', TO_DATE('2020-08-03','YYYY-MM-DD'))");
		executeHelper("INSERT INTO Payment VALUES (224145,473.00, 'Debit', TO_DATE('2020-08-21','YYYY-MM-DD'))");

	}

	public void hotelSetUp() throws SQLException {
		String query = "CREATE TABLE Hotel ( hotelID INT NOT NULL, hotelType CHAR(20), hotelName CHAR(20) NOT NULL, PRIMARY KEY (hotelID))";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO HotelServices VALUES (2938, 'Room Service', 'Room', 100.00)");
		executeHelper("INSERT INTO HotelServices VALUES (3948, 'Massage', 'Spa', 10.00)");
	}

	public void facilitySetUp() throws SQLException {
		String query = "CREATE TABLE Facility(facilityID INT NOT NULL, facilityType CHAR(20), facilityAmenities CHAR(20), facilityCapacity INT NOT NULL, dateAvailable DATE, PRIMARY KEY (facilityID))";
		PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
		ps.executeUpdate();
		ps.close();

		executeHelper("INSERT INTO Facility VALUES (50,'Aquatic Centre', 'Pool',500, TO_DATE('2020-03-11','YYYY-MM-DD'))");
	}

	public void databaseSetup () {
		dropBranchTableIfExists();
		try {
			//Path file = Paths.get("src/ca/ubc/cs304","sql/scripts/databaseSetup.sql");
			//String query = Files.readString(file);
			facilitySetUp();
			hotelSetUp();
			paymentSetUp();
			eventSetUp();
			roomsSetUp();
			hotelServicesSetUp();
			customerSetUp();
			invoiceSetUp();
			reservationSetUp();
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
				if (rs.getString(1).equalsIgnoreCase("Invoice")) {
					ps.execute("DROP TABLE Invoice");
					break;
				}
				if (rs.getString(1).equalsIgnoreCase("Customer")) {
					ps.execute("DROP TABLE Customer");
					break;
				}
				if (rs.getString(1).equalsIgnoreCase("HotelServices")) {
					ps.execute("DROP TABLE HotelServices");
					break;
				}
				if (rs.getString(1).equalsIgnoreCase("Rooms")) {
					ps.execute("DROP TABLE Rooms");
					break;
				}
				if (rs.getString(1).equalsIgnoreCase("Events")) {
					ps.execute("DROP TABLE Events");
					break;
				}
				if (rs.getString(1).equalsIgnoreCase("Payment")) {
					ps.execute("DROP TABLE Payment");
					break;
				}
				if (rs.getString(1).equalsIgnoreCase("Hotel")) {
					ps.execute("DROP TABLE Hotel");
					break;
				}
				if (rs.getString(1).equalsIgnoreCase("Facility")) {
					ps.execute("DROP TABLE Facility");
					break;
				}

			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


		public boolean login (String username, String password){
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
			try {
				connection.rollback();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		}
	}
/*

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


	public static void main(String args[]) {
		DatabaseConnectionHandler dbhandler = new DatabaseConnectionHandler();
		dbhandler.login("ora_emres", "a62827258");
		ReservationModel reservation1 = new ReservationModel(1111111111,"2020-10-20","2020-11-19","2020-11-20",300,73648,13,92847563,50,23453);
		dbhandler.insertReservation(reservation1);
		//dbhandler.deleteReservation(1111111111);



	}
/*
		DatabaseConnectionHandler dbhandler = new DatabaseConnectionHandler();
		dbhandler.login("ora_emres", "a62827258");

		dbhandler.joinMailsofCustomersMoreThanOneWeek();
		dbhandler.aggregateMostExpensiveInvoice();
		dbhandler.showInvoiceBranch(dbhandler.selectionInvoice(13, "equal","HOTELID"));
		for(String part: dbhandler.divisionCustomersUsingAllServices()) {
			System.out.println(part);
		}
		dbhandler.projectionReservation("reservationID");

		EventModel e = new EventModel(1223, 245.00, "Party", 145, "2020-03-11");
		dbhandler.insertEvent(e);

		dbhandler.deleteEvent(123);
		dbhandler.nestedAggregateInvoice();


	}
*/


