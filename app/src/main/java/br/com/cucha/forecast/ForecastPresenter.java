package br.com.cucha.forecast;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public interface ForecastPresenter {
    void listForecast(double lat, double lon);

    void refresh(double lat, double lon);

    void more(double baseLat, double baseLon, double lat, double lon);

    Forecast pickDetails(String id);

    void stop();
}
