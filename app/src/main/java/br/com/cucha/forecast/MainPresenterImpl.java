package br.com.cucha.forecast;

/**
 * Created by eduardocucharro on 18/04/17.
 */

public class MainPresenterImpl implements MainPresenter {

    private final MainView view;

    public MainPresenterImpl(MainView view) {
        this.view = view;
    }

    @Override
    public void presentForecast() {

        if (!view.isLocationServicesEnabled()) {
            view.showEnabledGPSDialog();
            return;
        }

        if(!view.hasGPSPermission()) {

            if(view.needsRationale()) {
                view.showRationaleDialog();
                return;
            }

            view.showPermissionDialog();
        }

        if(view.getCurrentLocation() == null) {
            view.showFindingYourPosition();
            return;
        }

        if(view.isMapEnabled()) {
            view.showForecastMap();
            return;
        }

        view.showForecastList();
    }
}
