package br.com.cucha.forecast;

import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * Created by eduardocucharro on 18/04/17.
 */

class Forecast implements Comparable {

    private String id;
    private String name;
    private float temp;
    private float min;
    private float max;
    private String description;
    private float distance;
    private double lat;
    private double lon;

    public Forecast(){}

    public Forecast(String id,
                    String name,
                    float temp,
                    float min,
                    float max,
                    String weatherDesc,
                    int distance,
                    double lat,
                    double lon) {

        this.id = id;
        this.name = name;
        this.temp = temp;
        this.min = min;
        this.max = max;
        this.description = weatherDesc;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public float getDistance() {
        return distance;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public float getTemp() {
        return temp;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = ((Forecast) obj).getId().equals(id);
        return equals;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (int)distance;
    }
}
