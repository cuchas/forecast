package br.com.cucha.forecast;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public class MainPresenterTest {

    @Mock
    MainView view;

    MainPresenter presenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        presenter = new MainPresenterImpl(view);
    }

    @Test
    public void presentForecast_showPermissionDialog_when_noPermissionGranted() {
        when(view.isLocationServicesEnabled()).thenReturn(true);
        when(view.hasGPSPermission()).thenReturn(false);

        presenter.presentForecast();

        verify(view).showPermissionDialog();
    }

    @Test
    public void presentForecast_showExplanationDialog_when_requestRationale() {
        when(view.isLocationServicesEnabled()).thenReturn(true);
        when(view.hasGPSPermission()).thenReturn(false);

        when(view.needsRationale()).thenReturn(true);

        presenter.presentForecast();

        verify(view).showRationaleDialog();
    }

    @Test
    public void presentForecast_showFindingPosition_when_noLocation() {
        when(view.isLocationServicesEnabled()).thenReturn(true);
        when(view.hasGPSPermission()).thenReturn(true);

        presenter.presentForecast();

        verify(view).showFindingYourPosition();
    }

    @Test
    public void presentForecast_showForecastList_when_locationAndMapDisabled() {
        when(view.isLocationServicesEnabled()).thenReturn(true);
        Location location = mock(Location.class);

        when(view.hasGPSPermission()).thenReturn(true);
        when(view.getCurrentLocation()).thenReturn(location);

        presenter.presentForecast();

        verify(view).showForecastList();
    }

    @Test
    public void presentForecast_showForecastMap_when_locationAndMapDisabled() {
        when(view.isLocationServicesEnabled()).thenReturn(true);
        Location location = mock(Location.class);

        when(view.hasGPSPermission()).thenReturn(true);
        when(view.getCurrentLocation()).thenReturn(location);
        when(view.isMapEnabled()).thenReturn(true);

        presenter.presentForecast();

        verify(view).showForecastMap();
    }

    @Test
    public void presentForecast_showEnabledGPSDialog_when_LocationServicesDisabled() {
        when(view.isLocationServicesEnabled()).thenReturn(false);

        presenter.presentForecast();

        verify(view).showEnabledGPSDialog();
    }
}
