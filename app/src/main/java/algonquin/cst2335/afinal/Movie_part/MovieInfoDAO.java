package algonquin.cst2335.afinal.Movie_part;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieInfoDAO {
    @Insert
    void insertMovie(MovieInfo m);
    @Delete
    void deleteMovie(MovieInfo m);
    @Query("Select * from MovieInfo")
    List<MovieInfo> getAllMovies();
}
