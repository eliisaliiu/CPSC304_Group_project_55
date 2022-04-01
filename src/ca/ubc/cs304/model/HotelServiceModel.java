package ca.ubc.cs304.model;

public class HotelServiceModel {
    private final int serviceID;
    private final String serviceName;
    private final String serviceType;
    private final double servicePrice;

    public HotelServiceModel(int serviceID,
                             String serviceName,
                             String serviceType,
                             double servicePrice){

        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.servicePrice = servicePrice;
    }

    public int getServiceID() {
        return serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public double getServicePrice() {
        return servicePrice;
    }
}
