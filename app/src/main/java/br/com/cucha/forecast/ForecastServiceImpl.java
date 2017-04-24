package br.com.cucha.forecast;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public class ForecastServiceImpl implements ForecastService {

    private final ForecastApi api;
    private final static String API_KEY = "781b8263713d131646620af488f7659e";
    private final static String FAHRENHEIT = "imperial";

    public ForecastServiceImpl(ForecastApi forecastApi) {
        this.api = forecastApi;
    }

    @Override
    public Observable<Forecast> getForecast(double lat, double lon, int cnt) {

        return api.getForecastNearPlaces(lat, lon, cnt, FAHRENHEIT, API_KEY)
                .flatMap(new Func1<ForecastResponse, Observable<?>>() {
                    @Override
                    public Observable<?> call(ForecastResponse forecastResponse) {
                        return Observable.from(forecastResponse.getList());
                    }
                })
                .map(new ForecastMapFunc());
    }
}
