package ca.ubc.cs304.model;


/**
 * The intent for this class is to update/store information about a single facility
 */
public class FacilityModel {
    private final int facilityID;
    private final double facilityQuota;
    private final String facilityAmenities;
    private final int facilityCapacity;
    private final String facilityType;
    private final String dateAvailable;

    public FacilityModel(int facilityID,
                         double facilityQuota,
                         String facilityAmenities,
                         int facilityCapacity,
                         String facilityType,
                         String dateAvailable){

        this.facilityID = facilityID;
        this.facilityQuota = facilityQuota;
        this.facilityAmenities = facilityAmenities;
        this.facilityCapacity = facilityCapacity;
        this.facilityType = facilityType;
        this.dateAvailable = dateAvailable;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public double getFacilityQuota() {
        return facilityQuota;
    }

    public String getFacilityAmenities() {
        return facilityAmenities;
    }

    public int getFacilityCapacity() {
        return facilityCapacity;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public String getDateAvailable() {
        return dateAvailable;
    }
}
