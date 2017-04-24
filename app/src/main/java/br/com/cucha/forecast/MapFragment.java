package br.com.cucha.forecast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static br.com.cucha.forecast.EXTRAS.EXTRA_LOCATION;
import static br.com.cucha.forecast.EXTRAS.EXTRA_TEMP;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        ForecastView,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, GoogleMap.InfoWindowAdapter {

    public static final String TAG = MapFragment.class.getName();
    private GoogleMap map;
    private ForecastPresenter presenter;
    private LatLng focusLatLng;
    private BroadcastReceiver receiver;
    private Location location;
    private boolean celsius;
    @BindView(R.id.progress_map_forecast)
    ProgressBar progressBar;
    private Unbinder unbinder;
    List<Marker> markerList = new LinkedList<>();
    private TemperatureUtil tempUtil;
    private static String format = "%.1f°";

    public static MapFragment newInstance(Location location) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(ACTIONS.ACTION_TOGGLE_TEMPERATURE)) {
                    celsius = intent.getBooleanExtra(EXTRA_TEMP, false);
                }
            }
        };
    }

    @NonNull
    private float castTemp(float t) {
        return celsius ? tempUtil.toCelsius(t) : t;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = createBroadcastReceiver();
        presenter = Injector.provideForecastPresenter(this);
        tempUtil = Injector.provideTemperatureUtil();
    }

    private void setStateInfo(Bundle bundle) {
        location = bundle.getParcelable(EXTRA_LOCATION);
        celsius = bundle.getBoolean(EXTRA_TEMP);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_LOCATION, location);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        setStateInfo(savedInstanceState == null ? bundle : savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        unbinder = ButterKnife.bind(this, view);

        setupProgress();

        return view;
    }

    private void setupProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(ACTIONS.ACTION_TOGGLE_TEMPERATURE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        presenter.stop();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnCameraMoveListener(this);
        map.setInfoWindowAdapter(this);

        presenter.listForecast(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void showForecast(Forecast forecast) {

        LatLng city = new LatLng(forecast.getLat(), forecast.getLon());

        if(focusLatLng == null) {
            focusLatLng = city;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(city)
                .title(forecast.getId());

        Marker marker = map.addMarker(markerOptions);
        markerList.add(marker);
    }

    @Override
    public void showFail() {
        if(isAdded())
            Toast.makeText(getContext(), getString(R.string.fail_forecast_swipe_again), Toast.LENGTH_LONG).show();
    }

    @Override
    public void adjust() {

        if(isAdded()) {

            progressBar.setVisibility(View.GONE);

            if(focusLatLng != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(focusLatLng, 11f));
                focusLatLng = null;
                map.setOnCameraIdleListener(null);
            }
        }
    }

    @Override
    public void cleanUI() {
        for(Marker m : markerList) {
            m.remove();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCameraMove() {
        map.setOnCameraIdleListener(this);
    }

    @Override
    public void onCameraIdle() {
        LatLng target = map.getCameraPosition().target;

        presenter.more(location.getLatitude(), location.getLongitude(), target.latitude, target.longitude);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        Forecast forecast = presenter.pickDetails(marker.getTitle());

        if(forecast != null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.view_map_tip, null);

            TextView text = (TextView)view.findViewById(R.id.text_temp_map_tip);
            ImageView imageView = (ImageView)view.findViewById(R.id.image_icon_map_tip);

            float temp = castTemp(forecast.getTemp());
            text.setText(String.format(format, temp));

            switch (forecast.getDescription().toLowerCase()) {
                case "clear":
                    imageView.setImageResource(R.drawable.ic_wb_sunny_black_24dp);
                    break;
                case "rain":
                    imageView.setImageResource(R.drawable.ic_weather_lightning_rainy);
                    break;
                default:
                    imageView.setImageResource(R.drawable.ic_wb_cloudy_black_24dp);
                    break;
            }

            return view;
        }

        return null;
    }
}
