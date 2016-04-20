package io.gbell.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.gbell.controllers.AnimeSearchAreaController;
import io.gbell.controllers.LibraryController;
import io.gbell.controllers.TVSearchAreaController;
import io.gbell.providers.FirebaseProvider;
import io.gbell.services.AnimeApiService;
import io.gbell.services.TVApiService;
import io.gbell.views.AnimeShowTile;
import io.gbell.views.TVShowTile;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import javax.inject.Singleton;

@Module(library = false, complete = true, injects = {
        LibraryController.class,
        TVSearchAreaController.class,
        AnimeSearchAreaController.class,
        TVShowTile.class,
        AnimeShowTile.class,
})
public class MediaManagerModule {

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .setExclusionStrategies(new ExclusionStrategy() {
//                    @Override
//                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getDeclaringClass().equals(RealmObject.class);
//                    }
//
//                    @Override
//                    public boolean shouldSkipClass(Class<?> clazz) {
//                        return false;
//                    }
//                })
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    AnimeApiService providesAnimeApiService(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://hummingbird.me")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
                .create(AnimeApiService.class);
    }

    @Provides
    @Singleton
    TVApiService providesTVApiService(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://thetvdb.com/api/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
                .create(TVApiService.class);
    }

    @Provides
    @Singleton
    FirebaseProvider providesFirebaseProvider() {
        return new FirebaseProvider();
    }
}
