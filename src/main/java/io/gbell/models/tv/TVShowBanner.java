package io.gbell.models.tv;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Banner", strict = false)
public class TVShowBanner {

    @Element(name = "id")
    private int id;

    @Element(name = "BannerPath")
    private String path;

    @Element(name = "BannerType")
    private String type;

    @Element(name = "BannerType2")
    private String resolution;

    @Element(name = "Language")
    private String lang;

    @Element(name = "RatingCount")
    private int ratingCount;

    @Element(name = "Rating", required = false)
    private double rating;

    public TVShowBanner() {}

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getResolution() {
        return resolution;
    }

    public String getLang() {
        return lang;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public double getRating() {
        return rating;
    }
}
