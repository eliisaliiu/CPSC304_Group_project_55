package ca.ubc.cs304.model;


/**
 * The intent for this class is to update/store information about a single hotel
 */
public class HotelModel {
    private final int hotelID;
    private final String hotelType;
    private final String hotelName;

    public HotelModel(int hotelID,
                      String hotelType,
                      String hotelName){
        this.hotelID = hotelID;
        this.hotelType = hotelType;
        this.hotelName = hotelName;
    }

    public int getHotelID() {
        return hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getHotelType() {
        return hotelType;
    }
}
