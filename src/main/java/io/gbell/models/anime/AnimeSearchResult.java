package io.gbell.models.anime;

import org.jetbrains.annotations.NotNull;

public class AnimeSearchResult implements Comparable<AnimeSearchResult> {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimeSearchResult that = (AnimeSearchResult) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(@NotNull AnimeSearchResult o) {
        return this.getTitle().compareTo(o.getTitle());
    }

    @Override
    public String toString() {
        return "AnimeSearchResult{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                '}';
    }
}
