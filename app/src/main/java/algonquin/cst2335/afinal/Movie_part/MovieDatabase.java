package algonquin.cst2335.afinal.Movie_part;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MovieInfo.class}, version = 5)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieInfoDAO miDAO();
}
