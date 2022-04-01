package ca.ubc.cs304.model;


/**
 * The intent for this class is to update/store information about a single event
 */
public class EventModel {
    private final int eventID;
    private final double eventQuota;
    private final String eventType;
    private final int numberOfGuests;
    private final String dateAvailable;

    public EventModel(int eventID,
                      double eventQuota,
                      String eventType,
                      int numberOfGuests,
                      String dateAvailable){

        this.eventID = eventID;
        this.eventQuota = eventQuota;
        this.eventType = eventType;
        this.numberOfGuests = numberOfGuests;
        this.dateAvailable = dateAvailable;
    }

    public int getEventID() {
        return eventID;
    }

    public double getEventQuota() {
        return eventQuota;
    }

    public String getEventType() {
        return eventType;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public String getDateAvailable() {
        return dateAvailable;
    }
}
