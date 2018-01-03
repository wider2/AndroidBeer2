package beer.api.utils;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {

    //https://api.punkapi.com/v2/beers

    @GET("/v2/beers")
    Observable<String> getBeerList(@Query("page") int page, @Query("per_page") int per_page);

    @GET("/v2/beers/")
    Observable<String> getSingleBeer(@Query("key") String key);

}