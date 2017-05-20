package com.examples.smriti_rawat.myweatherapp;
import com.examples.smriti_rawat.myweatherapp.utilities.NetworkUtils;
import com.examples.smriti_rawat.myweatherapp.data.weatherpreferences;
import com.examples.smriti_rawat.myweatherapp.utilities.OpenWeatherJsonUtils;
import com.examples.smriti_rawat.myweatherapp.ForecastAdapter.ForecastAdapterOnClickHandler;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import java.net.URL;
import android.widget.ProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements ForecastAdapterOnClickHandler {

//    private TextView mWeatherTextView;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        LinearLayoutManager layoutManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        loadWeatherData();
    }
    private void loadWeatherData(){
        showWeatherDataView();
//        String location= weatherpreferences.getPreferredWeatherLocation(this);
        String[] geo=weatherpreferences.getLocationCoordinates(this);
        new FetchWeatherData().execute(geo);
    }
    @Override
    public void onClick(String weatherForDay) {
        Context context = this;
//        Toast.makeText(context, weatherForDay, Toast.LENGTH_SHORT).show();
        Class destinationClass= DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        startActivity(intentToStartDetailActivity);
    }
    private void showWeatherDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    public class FetchWeatherData extends AsyncTask<String ,Void ,String[]>{
        @Override
        protected String[] doInBackground(String... params){
            if(params.length==0) return null;
            String location=params[0];
            String lat=params[0];
            String lon=params[1];
            URL weatherRequestUrl=NetworkUtils.buildUrl(lat,lon);
            try{
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
//                String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                return OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
            }catch (Exception e) { e.printStackTrace();return null;}
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String[] weatherData){
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                mForecastAdapter.setWeatherData(weatherData);
            } else {showErrorMessage();}
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
        if (itemThatWasClickedId == R.id.action_refresh) {
            mForecastAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
