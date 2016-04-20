package io.gbell.views;

import io.gbell.models.tv.TVShow;
import io.gbell.utils.TextUtils;

public class TVShowRow extends ShowRow {

    public TVShowRow(final TVShow show) {
        super(getPoster(show),
                show.getTitle(),
                null,
                show.getNetwork(),
                String.format("%s seasons", show.getSeasonCount()),
                String.format("%s eps", show.getEpisodeCount()),
                String.format("%d min", show.getEpisodeLength()),
                getAiring(show),
                show.getSynopsis());
    }

    private static String getPoster(TVShow show) {
        return show.getPoster() == null ? null : "http://thetvdb.com/banners/" + show.getPoster();
    }

    private static String getAiring(TVShow show) {
        String startDate = show.getStartedAiringDate();
        if (TextUtils.isBlank(startDate)) {
            return null;
        } else {
            return String.format("Started airing %s.", startDate);
        }
    }
}
