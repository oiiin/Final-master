package algonquin.cst2335.afinal.Movie_part;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MovieInfo {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name="title")
    public String title;
    @ColumnInfo(name="year")
    public String year;
    @ColumnInfo(name="actors")
    public String actors;
    @ColumnInfo(name="director")
    public String director;
    @ColumnInfo(name="rated")
    public String rated;
    @ColumnInfo(name="runtime")
    public String runtime;
    @ColumnInfo(name="plot")
    public String plot;
    @ColumnInfo(name="image")
    public String image;

    public MovieInfo(){
    }

    public MovieInfo(String t,String y,String a,String d,
                     String l,String c,String p,String i){
        title = t;
        year = y;
        actors = a;
        director = d;
        rated = l;
        runtime = c;
        plot = p;
        image = i;

    }

    public String getTitle(){
        return title;
    }


}
