package algonquin.cst2335.afinal.TicketMaster;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * This class handles interaction with the local database
 */
@Database(entities = {EventObject.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventObjectDAO evDAO();
}
