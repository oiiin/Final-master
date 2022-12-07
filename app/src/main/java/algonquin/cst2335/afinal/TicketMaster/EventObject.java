package algonquin.cst2335.afinal.TicketMaster;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class used to Create Event objects to store in the database
 */
@Entity
public class EventObject {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name="id")
    private String id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="ticketsURL")
    private String ticketsURL;

    @ColumnInfo(name="date")
    private String date;

    @ColumnInfo(name="minPrice")
    private float minPrice;

    @ColumnInfo(name="maxPrice")
    private float maxPrice;

    @ColumnInfo(name="imgLink")
    private String imgLink;

    @ColumnInfo(name="isFav")
    private boolean isFav;

    /**
     * Constructor used to create an event object
     * @param id The event's ID
     * @param name The name of the event
     * @param ticketsURL The URL to the ticket website
     * @param date The date of the event
     * @param minPrice The minimum ticket price
     * @param maxPrice The maximum ticket price
     * @param imgLink The link to the display image
     * @param isFav If the event is favorited
     */
    public EventObject(String id, String name, String ticketsURL, String date, float minPrice, float maxPrice, String imgLink, boolean isFav) {
        this.id = id;
        this.name = name;
        this.ticketsURL = ticketsURL;
        this.date = date;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.imgLink = imgLink;
        this.isFav = isFav;
    }

    /**
     * Method to get the event ID
     * @return Event ID
     */
    public String getId() {
        return id;
    }

    /**
     * Method to set the event ID
     * @param id Event ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method to get the event's name
     * @return Event Name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the event's name
     * @param name Event Name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getTicketsURL() {
        return ticketsURL;
    }

    public void setTicketsURL(String ticketsURL) {
        this.ticketsURL = ticketsURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
