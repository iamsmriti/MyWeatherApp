package com.examples.smriti_rawat.myweatherapp;
import com.examples.smriti_rawat.myweatherapp.utilities.NetworkUtils;
import com.examples.smriti_rawat.myweatherapp.data.weatherpreferences;
import com.examples.smriti_rawat.myweatherapp.utilities.OpenWeatherJsonUtils;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import java.net.URL;
public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);
    }
    private void loadWeatherData(){
        String location= weatherpreferences.getPreferredWeatherLocation(this);
        new FetchWeatherData().execute(location);
    }
    public class FetchWeatherData extends AsyncTask<String ,Void ,String[]>{
        @Override
        protected String[] doInBackground(String... params){
            if(params.length==0) return null;
            String location=params[0];
            URL weatherRequestUrl=NetworkUtils.buildUrl(location);
            try{
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);
                System.out.println("jsonWeatherResponse " + jsonWeatherResponse);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(String[] weatherData){
            if (weatherData != null) {
                for (String weatherString : weatherData) {
                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }
            }
        }
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemThatWasClickedId = item.getItemId();
        if(itemThatWasClickedId == R.id.action_settings){
            Toast.makeText(MainActivity.this, "This is settings menu", Toast.LENGTH_SHORT).show();
        }
        if (itemThatWasClickedId == R.id.action_aboutus) {
            Toast.makeText(MainActivity.this, "This is about us menu", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);


    }
}
