package br.com.cucha.forecast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public class Injector {

    private static String FORECAST_API_URL = "http://api.openweathermap.org";
    private static ForecastApi forecastApi;
    private static Retrofit retrofit;
    private static TemperatureUtil tempUtil;
    private static ForecastCache forecastCache;

    static MainPresenter provideMainPresenter(MainView view) {
        return new MainPresenterImpl(view);
    }

    static ForecastService provideForecastService() {
        return new ForecastServiceImpl(provideForecastApi());
    }

    static ForecastApi provideForecastApi() {

        if (retrofit == null) {

            Gson gson = new GsonBuilder().setLenient().create();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(loggingInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(FORECAST_API_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client.build())
                    .build();
        }

        if(forecastApi == null) {
            forecastApi = retrofit.create(ForecastApi.class);
        }

        return forecastApi;
    }

    public static ForecastPresenter provideForecastPresenter(ForecastView view) {
        return new ForecastPresenterImpl(view, provideForecastService(), provideCacheManager());
    }

    public static TemperatureUtil provideTemperatureUtil() {
        if(tempUtil == null) {
            tempUtil = new TemperatureUtil();
        }
        return tempUtil;
    }

    public static ForecastCache provideCacheManager() {
        if(forecastCache == null) {
            forecastCache = new CacheImpl();
        }
        return forecastCache;
    }
}
