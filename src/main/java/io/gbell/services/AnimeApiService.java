package io.gbell.services;

import io.gbell.models.anime.AnimeShowResponse;
import io.gbell.models.anime.AnimeSearchResult;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;

public interface AnimeApiService {

    @GET("/api/v1/search/anime")
    @Headers("X-Client-Id: 90e4bd1afaf89bc68740")
    Observable<List<AnimeSearchResult>> search(@Query("query") String text);

    @GET("/api/v2/anime/{slug}")
    @Headers("X-Client-Id: 90e4bd1afaf89bc68740")
    Observable<AnimeShowResponse> getDetails(@Path("slug") String slug);

}
