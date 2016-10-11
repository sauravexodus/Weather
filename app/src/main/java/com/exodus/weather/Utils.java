package com.exodus.weather;


public class Utils {

    public static String OPEN_WEATHER_MAP_API_KEY = "d88ddda4323307a5801219b1386acd7d";
    public static String OPEN_WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";

    public static int getCityImageDrawable(int type) {
        int prefix = type / 100;

        if (type / 100 == 8 && type % 100 != 0) {
            return R.drawable.weather_1;
        }

        switch (prefix) {
            case 2:
                return R.drawable.weather_4;
            case 3:
            case 5:
                return R.drawable.weather_5;
            case 6:
                return R.drawable.weather_3;
            case 7:
                return R.drawable.weather_2;
            case 8:
                return R.drawable.weather_7;
            default:
                return R.drawable.weather_6;
        }
    }

}
