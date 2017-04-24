package br.com.cucha.forecast;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public interface ForecastView {
    void showForecast(Forecast forecast);

    void showFail();

    void adjust();

    void cleanUI();

    void showProgress();
}
