package br.com.cucha.forecast;

import java.util.Set;

import rx.Observable;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public interface ForecastService {

    Observable<Forecast> getForecast(double lat, double lon, int cnt);
}
