package br.com.cucha.forecast;

import android.location.Location;

import java.util.AbstractSet;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by eduardocucharro on 18/04/17.
 */

class ForecastPresenterImpl implements ForecastPresenter {
    private final ForecastView view;
    private final ForecastService service;
    private static int CNT = 50;
    private static int KILOMETERS = 1000;
    private final ForecastCache cache;
    private Subscription subscription;

    public ForecastPresenterImpl(ForecastView view, ForecastService forecastService, ForecastCache cache) {
        this.view = view;
        this.service = forecastService;
        this.cache = cache;
    }

    @Override
    public void listForecast(final double lat, final double lon) {

        AbstractSet<Forecast> forecastCache = this.cache.getForecast();

        if(forecastCache.size() > 0) {

            view.cleanUI();

            for(Forecast f : forecastCache) {
                view.showForecast(f);
            }

            view.adjust();

            return;
        }

        refresh(lat, lon);
    }

    @Override
    public void refresh(final double lat, final double lon) {
        cache.clear();
        view.cleanUI();
        view.showProgress();

        subscription = service.getForecast(lat, lon, CNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Forecast>() {
                    @Override
                    public void call(Forecast forecast) {
                        calcAndSetDistance(forecast, lat, lon);

                        cache.add(forecast);

                        view.showForecast(forecast);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.adjust();
                        view.showFail();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        view.adjust();
                    }
                });
    }

    @Override
    public void more(final double baselat, final double baselon, final double lat, final double lon) {
        view.showProgress();

        subscription = service.getForecast(lat, lon, CNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Forecast>() {
                    @Override
                    public void call(Forecast forecast) {
                        calcAndSetDistance(forecast, baselat, baselon);

                        cache.add(forecast);

                        view.showForecast(forecast);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.adjust();
                        view.showFail();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        view.adjust();
                    }
                });
    }

    private void calcAndSetDistance(Forecast forecast, double baselat, double baselon) {
        float[] results = new float[1];
        Location.distanceBetween(baselat, baselon, forecast.getLat(), forecast.getLon(), results);

        forecast.setDistance(results[0] / KILOMETERS);
    }

    private float calcDistance(double baseLat, double baseLon, double lat, double lon) {
        float[] results = new float[1];
        Location.distanceBetween(baseLat, baseLon, lat, lon, results);

        return results[0] / KILOMETERS;
    }

    @Override
    public Forecast pickDetails(String id) {
        return cache.find(id);
    }

    @Override
    public void stop() {
        if(subscription != null) subscription.unsubscribe();
    }
}
