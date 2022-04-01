package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single customer
 */
public class CustomerModel {
    private final int customerID;
    private final String customerName;
    private final String customerEmail;
    private final int customerPhone;

    public CustomerModel(int customerID,
                         String customerName,
                         String customerEmail,
                         int customerPhone){
        this.customerID = customerID;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getCustomerPhone() {
        return customerPhone;
    }
}
