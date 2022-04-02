package ca.ubc.cs304.database;

import ca.ubc.cs304.model.*;
import ca.ubc.cs304.util.PrintablePreparedStatement;
import ca.ubc.cs304.DBTablePrinter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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
	public String[] joinMailsofCustomersMoreThanOneWeek(){
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

	public void databaseSetup() {}
	/*
	public void insertInvoice(InvoiceModel model) {
		try {
			String query = "INSERT INTO INVOICE VALUES (?,?,?,?,?)";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getAddress());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();

		try {
			String query = "SELECT * FROM branch";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("branch_addr"),
						rs.getString("branch_city"),
						rs.getInt("branch_id"),
						rs.getString("branch_name"),
						rs.getInt("branch_phone"));
				result.add(model);
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result.toArray(new BranchModel[result.size()]);
	}

	public void updateBranch(int id, String name) {
		try {
			String query = "UPDATE branch SET branch_name = ? WHERE branch_id = ?";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ps.setString(1, name);
			ps.setInt(2, id);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
*/
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
////////////////

/*
	private void dropBranchTableIfExists() {
		try {
			String query = "select table_name from user_tables";
			PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				if(rs.getString(1).toLowerCase().equals("branch")) {
					ps.execute("DROP TABLE branch");
					break;
				}
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
 */

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

