package algonquin.cst2335.afinal.Soccer_part;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


/**
 * Dao interface for interacting with database
 */
@Dao
public interface SoccerDAO {

    @Insert
     void insertGame(SoccerGame game);

    @Query("select * from SoccerGame")
    public List<SoccerGame> getAllGames();

    @Delete
     void deleteGame(SoccerGame game);


}
