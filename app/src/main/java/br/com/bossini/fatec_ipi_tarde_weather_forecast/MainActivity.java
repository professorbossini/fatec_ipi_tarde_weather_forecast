package br.com.bossini.fatec_ipi_tarde_weather_forecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Weather> weatherList =
            new ArrayList<>();
    private ArrayAdapter <Weather> weatherArrayAdapter;
    private ListView weatherListView;
    private EditText locationEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationEditText = findViewById(R.id.locationEditText);
        weatherListView =
                findViewById(R.id.weatherListView);
        weatherArrayAdapter =
                new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((v)->{
            String cidade =
                    locationEditText.
                        getEditableText().toString();
            try {
                URL url = montarURL(cidade);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(this,
                        getString(R.string.read_error),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class GetWeatherTask extends
            AsyncTask <URL, Void, Void>{
        @Override
        protected Void doInBackground(URL... urls) {
            URL endereco = urls[0];
            try {
                HttpURLConnection conn =
                        (HttpURLConnection)
                                endereco.openConnection();
                try(BufferedReader reader =
                        new BufferedReader
                                (new InputStreamReader(
                                        conn.getInputStream()));){
                    String linha = null;
                    StringBuilder resultado = new StringBuilder("");
                    while ((linha = reader.readLine()) != null){
                        resultado.append(linha);
                    }
                    JSONObject resultadoPrincipal =
                            new JSONObject(resultado.toString());
                    JSONArray list =
                            resultadoPrincipal.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++){
                        JSONObject iesimo = list.getJSONObject(i);
                        long dt = iesimo.getLong("dt");
                        JSONObject main =
                                iesimo.getJSONObject("main");
                        double temp_min =
                                main.getDouble("temp_min");
                        double temp_max =
                                main.getDouble("temp_max");
                        int humidity = main.getInt("humidity");
                        JSONArray weather =
                                iesimo.getJSONArray("weather");
                        JSONObject unicoNoWeather =
                                weather.getJSONObject(0);
                        String description =
                                unicoNoWeather.getString("description");
                        String icon =
                                unicoNoWeather.getString("icon");
                        Weather w =
                                new Weather(dt, temp_min, temp_max,
                                        humidity, description, icon);
                        weatherList.add(w);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private URL montarURL (String cidade)
                        throws MalformedURLException {
        String endereco =
                getString(
                        R.string.web_service_url,
                        cidade,
                        getString(R.string.api_key)
                );
        return new URL(endereco);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
