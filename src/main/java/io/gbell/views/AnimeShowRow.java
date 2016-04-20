package io.gbell.views;

import io.gbell.models.anime.AnimeShow;
import io.gbell.utils.TextUtils;

public class AnimeShowRow extends ShowRow {

    public AnimeShowRow(final AnimeShow show) {
        super(show.getPosterImage(),
                TextUtils.getTitle(show),
                TextUtils.getAlternateTitle(show),
                show.getShowType(),
                String.format("%s seasons", show.getSeasonCount()),
                String.format("%s eps", show.getEpisodeCount()),
                String.format("%d min", show.getEpisodeLength()),
                getAiring(show),
                show.getSynopsis());
    }

    private static String getAiring(AnimeShow show) {
        String startDate = show.getStartedAiringDate();
        String endDate = show.getFinishedAiringDate();
        if (TextUtils.isBlank(startDate)) {
            return null;
        } else {
            if (TextUtils.isBlank(endDate)) {
                return String.format("Started airing %s.", startDate);
            } else {
                return String.format("Aired from %s to %s.", startDate, endDate);
            }
        }
    }
}
