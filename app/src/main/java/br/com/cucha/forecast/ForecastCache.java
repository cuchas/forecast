package br.com.cucha.forecast;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by eduardocucharro on 22/04/17.
 */

public interface ForecastCache {
    ConcurrentSkipListSet<Forecast> getForecast();
    void clear();
    void add(Forecast forecast);
    Forecast find(String id);
}
