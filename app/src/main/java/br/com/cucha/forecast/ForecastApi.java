package br.com.cucha.forecast;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public interface ForecastApi {

    @GET("/data/2.5/find")
    Observable<ForecastResponse> getForecastNearPlaces(@Query("lat") double lat,
                                                       @Query("lon") double lon,
                                                       @Query("cnt") int cnt,
                                                       @Query("units") String units,
                                                       @Query("appid") String appid);
}
