package io.gbell.models.tv;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Series", strict = false)
public class TVShow implements Comparable<TVShow> {

    @Element(name = "id")
    private int id;

    @Element(name = "SeriesName")
    private String title;

    @Element(name = "Language")
    private String language;

    @Element(name = "Overview", required = false)
    private String synopsis;

    @Element(name = "FirstAired", required = false)
    private String startedAiringDate;

    @Element(name = "Network", required = false)
    private String network;

    private int seasonCount;

    private int episodeCount;

    @Element(name = "Runtime")
    private int episodeLength;

    @Element(name = "RatingCount")
    private int ratingCount;

    @Element(name = "Rating", required = false)
    private double rating;

    @Element(name = "poster", required = false)
    private String poster;

    @Element(name = "Actors")
    private String actors;

    private String localPath;

    public TVShow() {}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getStartedAiringDate() {
        return startedAiringDate;
    }

    public String getNetwork() {
        return network;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public int getEpisodeLength() {
        return episodeLength;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public double getRating() {
        return rating;
    }

    public String getPoster() {
        return poster;
    }

    public String getActors() {
        return actors;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TVShow tvShow = (TVShow) o;

        return id == tvShow.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(TVShow o) {
        return this.getTitle().compareTo(o.getTitle());
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
