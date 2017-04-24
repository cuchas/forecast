package br.com.cucha.forecast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static br.com.cucha.forecast.EXTRAS.EXTRA_LOCATION;
import static br.com.cucha.forecast.EXTRAS.EXTRA_TEMP;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastListFragment extends Fragment implements ForecastView, SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener listener;

    public static final String TAG = ForecastListFragment.class.getName();
    private Location location;
    private ForecastPresenter presenter;

    @BindView(R.id.recycler_forecast)
    RecyclerView recyclerView;

    @BindView(R.id.swiperefresh_forecast)
    SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;
    private ForecastListAdapter adapter;
    private BroadcastReceiver receiver;
    private boolean celsius;

    public ForecastListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener)context;
        }
    }

    public static ForecastListFragment newInstance(Location location) {

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_LOCATION, location);

        ForecastListFragment fragment = new ForecastListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = Injector.provideForecastPresenter(this);
        receiver = createBroadcastReceiver();
    }

    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(ACTIONS.ACTION_TOGGLE_TEMPERATURE)) {
                    celsius = intent.getBooleanExtra(EXTRAS.EXTRA_TEMP, false);
                    adapter.setCelsius(celsius);
                }
            }
        };
    }

    private void setStateInfo(Bundle bundle) {
        location = bundle.getParcelable(EXTRA_LOCATION);
        celsius = bundle.getBoolean(EXTRA_TEMP);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_LOCATION, location);
        outState.putBoolean(EXTRA_TEMP, celsius);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        setStateInfo(savedInstanceState == null ? bundle : savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_forecast_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {

        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new ForecastListAdapter(getContext(), celsius);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(ACTIONS.ACTION_TOGGLE_TEMPERATURE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);

        presenter.listForecast(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        presenter.stop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showForecast(Forecast forecast) {
        adapter.addForecast(forecast);
    }

    @Override
    public void showFail() {
        if(isAdded())
            Toast.makeText(getContext(), getString(R.string.fail_forecast_swipe_again), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void adjust() {
        swipeRefreshLayout.setRefreshing(false);
        listener.onReadyForInteraction(true);
    }

    @Override
    public void cleanUI() {
        adapter.clearData();
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
        listener.onReadyForInteraction(false);
    }

    @Override
    public void onRefresh() {
        presenter.refresh(location.getLatitude(), location.getLongitude());
    }

    interface OnFragmentInteractionListener {
        void onReadyForInteraction(boolean b);
    }
}
