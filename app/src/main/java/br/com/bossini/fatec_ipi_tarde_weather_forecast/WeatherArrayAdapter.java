package br.com.bossini.fatec_ipi_tarde_weather_forecast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherArrayAdapter
                extends ArrayAdapter <Weather> {

    private Map<String, Bitmap> figuras =
                                new HashMap<>();

    public WeatherArrayAdapter (Context context,
                                List<Weather> previsoes){
        super (context, -1, previsoes);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Weather caraDaVez = getItem(position);
        LayoutInflater inflater =
                LayoutInflater.from(getContext());
        View caixa = inflater.inflate(R.layout.list_item, parent, false);
        TextView dayTextView = caixa.findViewById(R.id.dayTextView);
        TextView lowTextView = caixa.findViewById(R.id.lowTextView);
        TextView highTextView = caixa.findViewById(R.id.highTextView);
        TextView humidityTextView = caixa.findViewById(R.id.humidityTextView);
        ImageView conditionImageView
                = caixa.findViewById(R.id.conditionImageView);
        if (figuras.containsKey(caraDaVez.iconURL)){
            conditionImageView.
                    setImageBitmap(figuras.get(caraDaVez.iconURL));
        }
        else{
            new LoadImageTask(conditionImageView).
                    execute(caraDaVez.iconURL);
        }
        dayTextView.setText(
                getContext().
                        getString(R.string.day_description,
                                caraDaVez.dayOfWeek,
                                caraDaVez.description));
        lowTextView.setText(
                getContext().
                        getString(R.string.low_temp,
                                caraDaVez.minTemp));

        highTextView.setText(
                getContext().
                        getString(
                                R.string.high_temp,
                                caraDaVez.maxTemp));
        humidityTextView.setText(
                getContext().
                        getString(
                                R.string.humidity,
                                caraDaVez.humidity
                        )
        );
        return caixa;
    }

    private class LoadImageTask extends
            AsyncTask <String, Void, Bitmap>{

        private ImageView view;
        public LoadImageTask (ImageView view){
            this.view = view;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String endereco = urls[0];
            try {
                URL url = new URL(endereco);
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();
               Bitmap figura = BitmapFactory.
                        decodeStream(connection.getInputStream());
               connection.disconnect();
               return figura;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
        }
    }
}
