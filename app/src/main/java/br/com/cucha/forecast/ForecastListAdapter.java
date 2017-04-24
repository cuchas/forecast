package br.com.cucha.forecast;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eduardocucharro on 19/04/17.
 */

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastViewHolder> {

    private final Context context;
    SortedList<Forecast> list = new SortedList<>(Forecast.class, new SortedList.Callback<Forecast>() {
        @Override
        public int compare(Forecast o1, Forecast o2) {
            return (int)o1.getDistance() > (int)o2.getDistance() ? 1 : -1;
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Forecast oldItem, Forecast newItem) {
            return false;
        }

        @Override
        public boolean areItemsTheSame(Forecast item1, Forecast item2) {
            return item1.getId().equals(item2.getId());
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    private boolean celsius;

    public ForecastListAdapter(Context context, boolean showAsCelsius) {
        this.context = context;
        this.celsius = showAsCelsius;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_forecast, null, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        holder.bind(list.get(position), celsius);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setCelsius(boolean celsius) {
        this.celsius = celsius;
        notifyDataSetChanged();
    }

    void addForecast(Forecast forecast) {
        list.add(forecast);
    }

    public void clearData() {
        list.clear();
    }
}
