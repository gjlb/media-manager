package io.gbell.models.anime;

import io.gbell.utils.TextUtils;
import org.jetbrains.annotations.NotNull;

public class AnimeShow implements Comparable<AnimeShow> {

    private int id;
    private String slug;
    private Titles titles;
    private String synopsis;
    private int episodeCount;
    private int seasonCount;
    private int episodeLength;
    private String posterImage;
    private String showType;
    private String startedAiringDate;
    private String finishedAiringDate;
    private String localPath;

    public AnimeShow() {}

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public Titles getTitles() {
        return titles;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public int getEpisodeLength() {
        return episodeLength;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getShowType() {
        return showType;
    }

    public String getStartedAiringDate() {
        return startedAiringDate;
    }

    public String getFinishedAiringDate() {
        return finishedAiringDate;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimeShow animeShow = (AnimeShow) o;

        return id == animeShow.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(@NotNull AnimeShow o) {
        return TextUtils.getTitle(this).compareTo(TextUtils.getTitle(o));
    }

    @Override
    public String toString() {
        return "AnimeShow{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                '}';
    }

    public class Titles {

        private String canonical;
        private String english;
        private String romaji;
        private String japanese;

        public Titles() {}

        public String getCanonical() {
            return canonical;
        }

        public String getEnglish() {
            return english;
        }

        public String getRomaji() {
            return romaji;
        }

        public String getJapanese() {
            return japanese;
        }
    }
}
