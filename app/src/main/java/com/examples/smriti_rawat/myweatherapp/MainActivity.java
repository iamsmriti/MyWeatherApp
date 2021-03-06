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
import android.net.Uri;
import android.util.Log;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;


public class MainActivity extends AppCompatActivity implements ForecastAdapterOnClickHandler ,LoaderCallbacks<String[]>{

    private static final String TAG = MainActivity.class.getSimpleName();
//    private TextView mWeatherTextView;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private static final int FORECAST_LOADER_ID = 0;

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
//        loadWeatherData();
        int loaderId = FORECAST_LOADER_ID;
        LoaderCallbacks<String[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] mWeatherData = null;

            @Override
            protected void onStartLoading() {
                if (mWeatherData != null) {
                    deliverResult(mWeatherData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }
            @Override
            public String[] loadInBackground() {
                String[] geo=weatherpreferences.getLocationCoordinates(MainActivity.this);
                String lat=geo[0];
                String lon=geo[1];
                URL weatherRequestUrl=NetworkUtils.buildUrl(lat,lon);
                try {
                    String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                    return simpleJsonWeatherData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(String[] data) {
                mWeatherData = data;
                super.deliverResult(data);
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mForecastAdapter.setWeatherData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showWeatherDataView();
        }
    }
    @Override
    public void onLoaderReset(Loader<String[]> loader) {
    }
    private void invalidateData() {
        mForecastAdapter.setWeatherData(null);
    }

    @Override
    public void onClick(String weatherForDay) {
        Context context = this;
//        Toast.makeText(context, weatherForDay, Toast.LENGTH_SHORT).show();
        Class destinationClass= DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay);
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
//    public class FetchWeatherData extends AsyncTask<String ,Void ,String[]>{
//        @Override
//        protected String[] doInBackground(String... params){
//            if(params.length==0) return null;
//            String location=params[0];
//            String lat=params[0];
//            String lon=params[1];
//            URL weatherRequestUrl=NetworkUtils.buildUrl(lat,lon);
//            try{
//                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
////                String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
//                return OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
//            }catch (Exception e) { e.printStackTrace();return null;}
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
//        }
//        @Override
//        protected void onPostExecute(String[] weatherData){
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
//            if (weatherData != null) {
//                showWeatherDataView();
//                mForecastAdapter.setWeatherData(weatherData);
//            } else {showErrorMessage();}
//        }
//    }


    private void openLocationInMap() {
        String addressString = "Patel Nagar";
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
        }
    }
    private void openGithubLinkInMap(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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

        if (itemThatWasClickedId == R.id.action_refresh) {
            invalidateData();
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            return true;
        }
        if(itemThatWasClickedId==R.id.action_map){
            openLocationInMap();
            return true;
        }
        if(itemThatWasClickedId==R.id.action_about){
            String githuburl = "https://github.com/iamsmriti/MyWeatherApp?files=1";
            openGithubLinkInMap(githuburl);
            return true;
        }
        if(itemThatWasClickedId==R.id.action_aboutme){
            Context context = getApplicationContext();
            CharSequence text = "This is made by Smriti Rawat";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
