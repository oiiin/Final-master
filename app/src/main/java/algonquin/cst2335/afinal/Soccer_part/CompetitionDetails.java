package algonquin.cst2335.afinal.Soccer_part;

import android.widget.ImageView;

import java.util.Date;
import java.util.List;

public class CompetitionDetails {
    private String title;
    private String url;
    private String thumbnail;
    private String date;
    private String team1;
    private String team2;
    private String competitionName;
    private String videoTitle;
    private String videoUrl;


    /**
     * no argument constructor
     */
    public CompetitionDetails(){}

    /**
     *  Getters and Setters for game details.
     * @param title
     * @param url
     * @param thumbnail
     * @param date
     * @param team1
     * @param team2
     * @param competitionName
     * @param videoTitle
     * @param videoUrl
     */
    public CompetitionDetails(String title, String url, String thumbnail, String date, String team1, String team2, String competitionName,
                              String videoTitle, String videoUrl) {

        this.title =title;
        this.url =url;
        this.thumbnail =thumbnail;
        this.date =date;
        this.team1 =team1;
        this.team2 =team2;
        this.competitionName =competitionName;
        this.videoTitle =videoTitle;
        this.videoUrl =videoUrl;


    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
