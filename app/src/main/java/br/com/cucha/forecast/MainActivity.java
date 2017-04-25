package br.com.cucha.forecast;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.cucha.forecast.EXTRAS.EXTRA_LOCATION;
import static br.com.cucha.forecast.EXTRAS.EXTRA_MAP_ENABLED;
import static br.com.cucha.forecast.EXTRAS.EXTRA_TEMP;

public class MainActivity extends AppCompatActivity implements
        MainView,
        ForecastListFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    static String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    static int REQUEST_CODE_FINE_LOCATION = 1000;
    private MainPresenter presenter;
    private AlertDialog rationaleDialog;
    private GoogleApiClient googleApiClient;
    private Location location;
    private boolean celsius = true;

    @BindView(R.id.frame_content_main)
    ViewGroup viewGroup;
    private String currentFrag;
    private boolean mapEnabled;
    private boolean lockedButton;
    private AlertDialog enableGPSDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInstanceState(savedInstanceState == null ? getIntent().getExtras() : savedInstanceState);

        ButterKnife.bind(this);

        presenter = Injector.provideMainPresenter(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.presentForecast();
    }

    private void setInstanceState(Bundle bundle) {
        if (bundle == null) return;
        location = bundle.getParcelable(EXTRA_LOCATION);
        mapEnabled = bundle.getBoolean(EXTRA_MAP_ENABLED);
        celsius = bundle.getBoolean(EXTRA_TEMP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.temp_main_menu)
                setupTempIcon(menu.getItem(i));
            else if (menu.getItem(i).getItemId() == R.id.toggleview_main_menu)
                setupNavIcon(menu.getItem(i));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(lockedButton) return true;

        if (item.getItemId() == R.id.temp_main_menu) {

            celsius = !celsius;

            setupTempIcon(item);

            broadcastToggleTemperature();

        } else if (item.getItemId() == R.id.toggleview_main_menu) {

            if (currentFrag.equals(ForecastListFragment.TAG)) {
                showForecastMap();
            } else {
                showForecastList();
            }

            setupNavIcon(item);
        }

        return true;
    }

    private void setupNavIcon(MenuItem item) {
        if (mapEnabled) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_black_24dp);
            item.setIcon(drawable);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_map_black_24dp);

            item.setIcon(drawable);
        }
    }

    private void setupTempIcon(MenuItem item) {
        if (!celsius) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_temperature_celsius);
            item.setIcon(drawable);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_temperature_fahrenheit);
            item.setIcon(drawable);
        }
    }

    private void broadcastToggleTemperature() {
        Intent intent = new Intent();
        intent.setAction(ACTIONS.ACTION_TOGGLE_TEMPERATURE);
        intent.putExtra(EXTRAS.EXTRA_TEMP, celsius);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_FINE_LOCATION) {

            for (int p : grantResults) {

                if (p == PackageManager.PERMISSION_DENIED) {
                    showRationaleDialog();
                    return;
                }

                presenter.presentForecast();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_LOCATION, location);
        outState.putBoolean(EXTRA_MAP_ENABLED, mapEnabled);
        outState.putBoolean(EXTRA_TEMP, celsius);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showPermissionDialog() {
        requestPermissions(permissions, REQUEST_CODE_FINE_LOCATION);
    }

    @Override
    public boolean hasGPSPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean needsRationale() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

    }

    public void initGoogleClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApiIfAvailable(LocationServices.API)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .build();
        }
    }

    @Override
    public void showRationaleDialog() {

        if (rationaleDialog == null) {
            rationaleDialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.gps_permission_is_needed_))
                    .setPositiveButton(getString(R.string.enable_gps), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showPermissionDialog();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
        }

        rationaleDialog.show();
    }

    @Override
    public boolean isMapEnabled() {
        return mapEnabled;
    }

    @Override
    public void showForecastList() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ForecastListFragment.TAG);

        if (fragment == null)
            fragment = ForecastListFragment.newInstance(location);

        fragment.setArguments(getStateBundle());

        addFragment(fragment);

        currentFrag = ForecastListFragment.TAG;
        mapEnabled = false;
    }

    @Override
    public void showForecastMap() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);

        if (fragment == null)
            fragment = MapFragment.newInstance(location);

        fragment.setArguments(getStateBundle());

        addFragment(fragment);

        currentFrag = MapFragment.TAG;
        mapEnabled = true;
    }

    private Bundle getStateBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_TEMP, celsius);
        bundle.putParcelable(EXTRA_LOCATION, location);

        return bundle;
    }

    @Override
    public Location getCurrentLocation() {
        return location;
    }

    @Override
    public void showFindingYourPosition() {
        initGoogleClient();

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FindingYourPositionFragment.TAG);

        if (fragment == null)
            fragment = FindingYourPositionFragment.newInstance();

        addFragment(fragment);

        currentFrag = FindingYourPositionFragment.TAG;
        mapEnabled = false;
    }

    @Override
    public boolean isLocationServicesEnabled() {
        LocationManager locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void showEnabledGPSDialog() {
        if(enableGPSDialog == null) {
            enableGPSDialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.gps_required))
                    .setMessage(getString(R.string.gps_must_be_enabled_))
                    .setPositiveButton(getString(R.string.enable_gps), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    })
                    .create();
        }

        enableGPSDialog.show();
    }

    private void addFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_content_main, fragment, FindingYourPositionFragment.TAG);
            transaction.commit();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        presenter.presentForecast();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onReadyForInteraction(boolean b) {
        lockedButton = !b;
    }
}
