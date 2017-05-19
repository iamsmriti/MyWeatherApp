package com.examples.smriti_rawat.myweatherapp.data;

/**
 * Created by smriti_rawat on 5/19/17.
 */

import android.content.Context;
public class weatherpreferences {

    public static final String PREF_CITY_NAME = "city_name";
    public static final String PREF_COORD_LAT = "coord_lat";
    public static final String PREF_COORD_LONG = "coord_long";
    private static final String DEFAULT_WEATHER_LOCATION = "560034,IN";
    private static final String[] DEFAULT_WEATHER_COORDINATES = {"12.98", "77.6"};
    private static final String DEFAULT_MAP_LOCATION =
            "1600 Amphitheatre Parkway, Mountain View, CA 94043";

    static public void setLocationDetails(Context c, String cityName, double lat, double lon) {
        /** This will be implemented in a future lesson **/
    }

    static public void setLocation(Context c, String locationSetting, double lat, double lon) {
        /** This will be implemented in a future lesson **/
    }


    static public void resetLocationCoordinates(Context c) {
        /** This will be implemented in a future lesson **/
    }

    public static String getPreferredWeatherLocation(Context context) {
        /** This will be implemented in a future lesson **/
        return getDefaultWeatherLocation();
    }

    public static boolean isMetric(Context context) {
        /** This will be implemented in a future lesson **/
        return true;
    }

    public static String[] getLocationCoordinates(Context context) {
        return getDefaultWeatherCoordinates();
    }


    public static boolean isLocationLatLonAvailable(Context context) {
        return false;
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION;
    }

    public static String[] getDefaultWeatherCoordinates() {
        return DEFAULT_WEATHER_COORDINATES;
    }
}
