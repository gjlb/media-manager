package io.gbell.models.anime;

public class AnimeSearchResult {

    private int id;
    private String slug;
    private String title;
    private String alternateTitle;
    private int episodeCount;
    private int episodeLength;
    private String coverImage;
    private String showType;

    public AnimeSearchResult() {}

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getAlternateTitle() {
        return alternateTitle;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public int getEpisodeLength() {
        return episodeLength;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getShowType() {
        return showType;
    }
}
