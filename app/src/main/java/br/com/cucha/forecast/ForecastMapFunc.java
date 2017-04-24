package br.com.cucha.forecast;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by eduardocucharro on 20/04/17.
 */

public class ForecastMapFunc implements Func1<Object, Forecast> {

    @Override
    public Forecast call(Object o) {

        ForecastResponse.ForecastBean forecastBean =
                (ForecastResponse.ForecastBean)o;

        float id = forecastBean.getId();
        String name = forecastBean.getName();
        float temp = (float) forecastBean.getMain().getTemp();
        float temp_min = (float) forecastBean.getMain().getTemp_min();
        float temp_max = (float) forecastBean.getMain().getTemp_max();

        List<ForecastResponse.ForecastBean.WeatherBean> weather = forecastBean.getWeather();
        String weatherDesc = weather.size() > 0 ? weather.get(0).getMain() : "";

        return new Forecast(
                String.valueOf(id),
                name,
                temp,
                temp_min,
                temp_max,
                weatherDesc,
                (int)id,
                forecastBean.getCoord().getLat(),
                forecastBean.getCoord().getLon());
    }
}
