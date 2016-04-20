package io.gbell.views;

import com.firebase.client.Firebase;
import io.gbell.MediaManagerMain;
import io.gbell.models.tv.TVEpisode;
import io.gbell.models.tv.TVShow;
import io.gbell.services.TVApiService;
import io.gbell.utils.DialogUtils;
import io.gbell.utils.TextUtils;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static io.gbell.utils.FirebaseUtils.FIREBASE_TV_EPISODES;
import static io.gbell.utils.FirebaseUtils.FIREBASE_TV_SHOWS;

public class TVShowTile extends ShowTile {

    @Inject
    TVApiService tvApiService;

    public TVShowTile(TVShow show, final Firebase firebase) {
        super(getPoster(show),
                show.getTitle(),
                null,
                String.valueOf(TextUtils.parseTVDate(show.getStartedAiringDate()).getYear() + 1900),
                show.getNetwork(),
                String.format("%d min", show.getEpisodeLength()));

        MediaManagerMain.inject(this);

        if (firebase == null) {
            details.getChildren().remove(action);
            return;
        }

        action.setText("Add To Library");
        JavaFxObservable.fromActionEvents(action)
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .flatMap(actionEvent -> tvApiService.getFullDetails(show.getId(), show.getLanguage()))
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "An error occurred while trying to add show '" + show.getTitle() + "' to library.", e))
                .doOnNext(tvShowResponse -> {
                    TVShow tvShow = tvShowResponse.getShowDetails();
                    List<TVEpisode> episodes = tvShowResponse.getEpisodes();
                    if (episodes == null) {
                        episodes = Collections.emptyList();
                    }
                    int episodeCount = episodes.size();
                    tvShow.setEpisodeCount(episodeCount);
                    int seasonCount = episodes.stream().map(TVEpisode::getSeasonNumber).collect(Collectors.toCollection(HashSet::new)).size();
                    tvShow.setSeasonCount(seasonCount);
                    System.out.println("Got show: " + tvShow.getTitle() + " with " + seasonCount + " seasons and " + episodeCount + " episodes.");
                })
                .doOnNext(tvShowResponse -> {
                    TVShow tvShow = tvShowResponse.getShowDetails();
                    firebase.child(FIREBASE_TV_SHOWS).child(String.valueOf(tvShow.getId())).setValue(tvShow);
                })
                .flatMap(tvShowResponse -> Observable.from(tvShowResponse.getEpisodes()))
                .doOnNext(episode -> firebase.child(FIREBASE_TV_EPISODES).child(String.valueOf(episode.getId())).setValue(episode))
                .subscribe();
    }

    private static String getPoster(TVShow show) {
        return show.getPoster() == null ? null : "http://thetvdb.com/banners/" + show.getPoster();
    }
}
