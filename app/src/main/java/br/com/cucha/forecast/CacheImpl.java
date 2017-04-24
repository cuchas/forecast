package br.com.cucha.forecast;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by eduardocucharro on 22/04/17.
 */

public class CacheImpl implements ForecastCache {

    static ConcurrentSkipListSet<Forecast> list = new ConcurrentSkipListSet<>(new Comparator<Forecast>() {
        @Override
        public int compare(Forecast o1, Forecast o2) {
            boolean b = o1.getId().equals(o2.getId());

            return b ? 0 : (int) o1.getDistance() > (int) o2.getDistance() ? 1 : -1;
        }
    });

    @Override
    public ConcurrentSkipListSet<Forecast> getForecast() {
        return list;

    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void add(Forecast forecast) {
        list.add(forecast);
    }

    @Override
    public Forecast find(String id) {
        Forecast forecast = new Forecast();
        forecast.setId(id);

        Forecast ceiling = list.ceiling(forecast);

        return ceiling;
    }
}
