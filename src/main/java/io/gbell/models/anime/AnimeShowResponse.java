package io.gbell.models.anime;

import java.util.List;

public class AnimeShowResponse {

    private AnimeShow anime;
    private Linked linked;

    public AnimeShowResponse() {}

    public AnimeShow getAnime() {
        return anime;
    }

    public List<AnimeEpisode> getEpisodes() {
        return linked.episodes;
    }

    private class Linked {
        List<AnimeEpisode> episodes;
    }
}
