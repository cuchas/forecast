package br.com.cucha.forecast;

/**
 * Created by eduardocucharro on 22/04/17.
 */

public class TemperatureUtil {
    float toCelsius(float fahrenheit) {
        return (float) ((fahrenheit - 32) / 1.8000);
    }
}
