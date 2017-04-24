package br.com.cucha.forecast;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eduardocucharro on 19/04/17.
 */

public class ForecastViewHolder extends RecyclerView.ViewHolder {

    private final TemperatureUtil tempUtil;
    @BindView(R.id.text_place_name_forecast)
    TextView textPlaceName;

    @BindView(R.id.text_description_forecast)
    TextView textDescription;

    @BindView(R.id.text_current_temp_forecast)
    TextView textCurrentTemp;

    @BindView(R.id.text_max_temp_forecast)
    TextView textMaxTemp;

    @BindView(R.id.text_min_temp_forecast)
    TextView textMinTemp;

    @BindView(R.id.text_distance_forecast)
    TextView textDistance;

    @BindView(R.id.image_icon_forecast)
    ImageView imageIcon;

    public ForecastViewHolder(View itemView) {
        super(itemView);

        tempUtil = Injector.provideTemperatureUtil();

        ButterKnife.bind(this, itemView);
    }

    public void bind(Forecast forecast, boolean showAsCelsius) {

        textPlaceName.setText(forecast.getName());

        String format = "%.1f°";
        textCurrentTemp.setText(String.format(format, showAsCelsius ? tempUtil.toCelsius(forecast.getTemp()) : forecast.getTemp()));
        textMinTemp.setText(String.format(format, showAsCelsius ? tempUtil.toCelsius(forecast.getMin()) : forecast.getMin()));
        textMaxTemp.setText(String.format(format, showAsCelsius ? tempUtil.toCelsius(forecast.getMax()) : forecast.getMax()));
        textDistance.setText(String.format("%.1f Km", forecast.getDistance()));

        switch (forecast.getDescription().toLowerCase()) {
            case "clear":
                imageIcon.setImageResource(R.drawable.ic_wb_sunny_black_24dp);
                textDescription.setText(textDescription.getContext().getString(R.string.clean_sky));
                break;
            case "rain":
                imageIcon.setImageResource(R.drawable.ic_weather_lightning_rainy);
                textDescription.setText(textDescription.getContext().getString(R.string.rain));
                break;
            default:
                imageIcon.setImageResource(R.drawable.ic_wb_cloudy_black_24dp);
                textDescription.setText(textDescription.getContext().getString(R.string.clouds));
                break;
        }
    }


}
