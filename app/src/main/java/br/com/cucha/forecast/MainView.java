package br.com.cucha.forecast;

import android.location.Location;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public interface MainView {
    void showPermissionDialog();

    boolean hasGPSPermission();

    boolean needsRationale();

    void showRationaleDialog();

    boolean isMapEnabled();

    void showForecastList();

    void showForecastMap();

    Location getCurrentLocation();

    void showFindingYourPosition();

    boolean isLocationServicesEnabled();

    void showEnabledGPSDialog();
}
