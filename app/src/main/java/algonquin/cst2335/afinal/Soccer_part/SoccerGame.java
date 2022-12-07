package algonquin.cst2335.afinal.Soccer_part;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Entity attributes used for saving and retrieving games
 *
 */
@Entity
public class SoccerGame {

    @PrimaryKey(autoGenerate = true)
    public  int id;

    @ColumnInfo(name="title")
    protected String title;

    @ColumnInfo(name = "imageURL")
    protected String imageURL;

    @ColumnInfo(name = "competition name")
    protected String compname;


    public SoccerGame(String title, String imageURL, String compname){
        this.title = title;
        this.imageURL = imageURL;
        this.compname = compname;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getImageURL(){
        return imageURL;
    }


    public String getCompname() {
        return compname;
    }
}

