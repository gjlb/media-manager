package io.gbell.views;

import com.firebase.client.Firebase;
import io.gbell.providers.FirebaseProvider;
import io.gbell.MediaManagerMain;
import io.gbell.models.anime.AnimeEpisode;
import io.gbell.models.anime.AnimeSearchResult;
import io.gbell.models.anime.AnimeShow;
import io.gbell.services.AnimeApiService;
import io.gbell.utils.DialogUtils;
import io.gbell.utils.TextUtils;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static io.gbell.utils.FirebaseUtils.FIREBASE_ANIME_EPISODES;
import static io.gbell.utils.FirebaseUtils.FIREBASE_ANIME_SHOWS;

public class AnimeShowTile extends ShowTile {

    @Inject
    AnimeApiService animeApiService;

    @Inject
    FirebaseProvider firebaseProvider;

    public AnimeShowTile(final AnimeSearchResult show, final Firebase firebase) {
        super(show.getCoverImage(),
                show.getTitle(),
                show.getAlternateTitle(),
                show.getShowType(),
                String.format("%s eps", show.getEpisodeCount()),
                String.format("%d min", show.getEpisodeLength()));

        MediaManagerMain.inject(this);

        action.setText("Add To Library");
        JavaFxObservable.fromActionEvents(action)
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .flatMap(actionEvent -> animeApiService.getDetails(show.getSlug()))
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "An error occurred while trying to add show '" + show.getTitle() + "' to library.", e))
                .doOnNext(animeShowResponse -> {
                    AnimeShow animeShow = animeShowResponse.getAnime();
                    List<AnimeEpisode> episodes = animeShowResponse.getEpisodes();
                    int seasonCount = (episodes == null ? 0 : episodes.stream().map(AnimeEpisode::getSeasonNumber).collect(Collectors.toCollection(HashSet::new)).size());
                    animeShow.setSeasonCount(seasonCount);
                    System.out.println("Got show: " + TextUtils.getTitle(animeShow) + " with " + seasonCount + " seasons.");
                })
                .doOnNext(animeShowResponse -> {
                    AnimeShow animeShow = animeShowResponse.getAnime();
                    firebase.child(FIREBASE_ANIME_SHOWS).child(String.valueOf(animeShow.getId())).setValue(animeShow);
                })
                .flatMap(animeShowResponse -> Observable.from(animeShowResponse.getEpisodes()))
                .doOnNext(episode -> firebase.child(FIREBASE_ANIME_EPISODES).child(String.valueOf(episode.getId())).setValue(episode))
                .subscribe();
    }
}
