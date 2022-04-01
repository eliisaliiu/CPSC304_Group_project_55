package ca.ubc.cs304.model;

/**
 * The intent for this class is to update/store information about a single reservation
 */
public class RoomModel {
    public final int roomNo;
    private final int floorNo;
    private final String roomType;
    private final HotelModel hotelModel;
    private final double roomPrice;
    private final int numberOfBeds;
    private final String typeOfView;
    private final String typeOfBed;

    public RoomModel(int roomNo, int floorNo, String roomType, HotelModel hotelModel, double roomPrice,
                     int numberOfBeds,
                     String typeOfView, String typeOfBed){
        this.roomNo = roomNo;
        this.floorNo = floorNo;
        this.roomType = roomType;
        this.hotelModel = hotelModel;
        this.roomPrice = roomPrice;
        this.numberOfBeds = numberOfBeds;
        this.typeOfView = typeOfView;
        this.typeOfBed = typeOfBed;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public int getFloorNo() {
        return floorNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public HotelModel getHotelModel() {
        return hotelModel;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public String getTypeOfView() {
        return typeOfView;
    }

    public String getTypeOfBed() {
        return typeOfBed;
    }
}
