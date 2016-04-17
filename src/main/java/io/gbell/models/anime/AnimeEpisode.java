package io.gbell.models.anime;


public class AnimeEpisode {

    private int id;
    private int showId;
    private String title;
    private String synopsis;
    private String airdate;
    private Integer seasonNumber;
    private int number;

    public AnimeEpisode() {}

    public int getId() {
        return id;
    }

    public int getShowId() {
        return showId;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getAirdate() {
        return airdate;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }
}
