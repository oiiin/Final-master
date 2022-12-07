package algonquin.cst2335.afinal.TicketMaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This class handles all the database queries
 */
@Dao
public interface EventObjectDAO {

    /**
     * Query to insert an event to the database
     * @param event Event chosen by the user
     */
    @Insert
    void insertEvent(EventObject event);

    /**
     * Query to get all the saved events
     * @return List of the saved events
     */
    @Query("SELECT * FROM EventObject")
    List<EventObject> getFavorites();

    /**
     * Query to delete a saved event from the database
     * @param event Event chosen by the user
     */
    @Delete
    void removeEvent(EventObject event);
}
