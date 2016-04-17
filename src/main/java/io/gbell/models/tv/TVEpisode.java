package io.gbell.models.tv;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Episode", strict = false)
public class TVEpisode {

    @Element(name = "id")
    private int id;

    @Element(name = "seriesid")
    private int showId;

    @Element(name = "EpisodeName", required = false)
    private String title;

    @Element(name = "Overview", required = false)
    private String synopsis;

    @Element(name = "SeasonNumber")
    private int seasonNumber;

    @Element(name = "EpisodeNumber")
    private int episodeNumber;

    @Element(name = "absolute_number", required = false)
    private int absoluteNumber;

    @Element(name = "FirstAired", required = false)
    private String airdate;

    private String localPath;

    public TVEpisode() {}

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

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getAbsoluteNumber() {
        return absoluteNumber;
    }

    public String getAirdate() {
        return airdate;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
