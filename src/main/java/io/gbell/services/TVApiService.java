package io.gbell.services;

import io.gbell.models.tv.TVBannerResults;
import io.gbell.models.tv.TVSearchResponse;
import io.gbell.models.tv.TVShowResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface TVApiService {

    @GET("GetSeries.php")
    Observable<TVSearchResponse> search(@Query("seriesname") String text);

    @GET("5E90EABF66950153/series/{series}/banners.xml")
    Observable<TVBannerResults> getBanners(@Path("series") String series);

    @GET("5E90EABF66950153/series/{series}/{lang}.xml")
    Observable<TVShowResponse> getDetails(@Path("series") int id, @Path("lang") String language);

    @GET("5E90EABF66950153/series/{series}/all/{lang}.xml")
    Observable<TVShowResponse> getFullDetails(@Path("series") int id, @Path("lang") String language);

}
