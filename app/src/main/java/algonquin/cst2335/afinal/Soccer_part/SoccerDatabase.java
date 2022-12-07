package algonquin.cst2335.afinal.Soccer_part;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Soccer database creation
 * Uses Room Database
 */

@Database(entities = {SoccerGame.class},version = 1)
public abstract class SoccerDatabase extends RoomDatabase {
    public abstract SoccerDAO soccerDAO();


}
