package com.wanderers.redneck.hikingdiary.model;

import java.io.Serializable;

/**
 * Created by Ari Iivari on 24.2.2015.
 */
public class Point implements Serializable{

    public static final String TAG = "Point";
    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mPointName;
    private String mPointDate;
    private String mPointDesc;
    private String mPointLAT;
    private String mPointLON;
    private String mPointImage;
    private Route mRoute;
    private String mWeather_sunrise, mWeather_sunset, mWeather_main, mWeather_desc,
            mWeather_temp, mWeather_humidity, mWeather_pressure, mWeather_temp_min, mWeather_temp_max,
            mWeather_windspeed, mWeather_windgust, mWeather_winddeg, mWeather_location_name,
            mWeather_location_country, mWeather_visibility;
    private String weather_desc_en, weather_temp_f, weather_temp_min_f, weather_temp_max_f, weather_windspeed_miles, weather_windgust_miles,
            weather_winddir16Point, weather_cloud_cover, weather_feels_like_c, weather_feels_like_f, weather_precip_mm, weather_moonrise,
            weather_moonset, weather_wind_chill_c, weather_wind_chill_f, weather_uv_index;
    private byte[] mWeather_icon;

    public Point() {

    }

    public Point(String pointName, String pointDate, String pointDesc, String pointLAT, String pointLON, String pointImage, String weather_sunrise,
                 String weather_sunset, String weather_main, String weather_desc, byte[] weather_icon,
                 String weather_temp, String weather_humidity, String weather_pressure, String weather_temp_min,
                 String weather_temp_max, String weather_windspeed, String weather_windgust, String weather_winddeg,
                 String weather_location_name, String weather_location_country, String mWeather_visibility,
                 String weather_desc_en, String weather_temp_f, String weather_temp_min_f, String weather_temp_max_f, String weather_windspeed_miles,
                 String weather_windgust_miles, String weather_winddir16Point, String weather_cloud_cover, String weather_feels_like_c, String weather_feels_like_f,
                 String weather_precip_mm, String weather_moonrise, String weather_moonset, String weather_wind_chill_c, String weather_wind_chill_f, String weather_uv_index) {
        this.mPointName = pointName;
        this.mPointDate = pointDate;
        this.mPointDesc = pointDesc;
        this.mPointLAT = pointLAT;
        this.mPointLON = pointLON;
        this.mPointImage = pointImage;
        this.mWeather_sunrise = weather_sunrise;
        this.mWeather_sunset= weather_sunset;
        this.mWeather_main= weather_main;
        this.mWeather_desc= weather_desc;
        this.mWeather_icon= weather_icon;
        this.mWeather_temp= weather_temp;
        this.mWeather_humidity= weather_humidity;
        this.mWeather_pressure= weather_pressure;
        this.mWeather_temp_min= weather_temp_min;
        this.mWeather_temp_max= weather_temp_max;
        this.mWeather_windspeed= weather_windspeed;
        this.mWeather_windgust= weather_windgust;
        this.mWeather_winddeg= weather_winddeg;
        this.mWeather_location_name= weather_location_name;
        this.mWeather_location_country= weather_location_country;
        this.mWeather_visibility = mWeather_visibility;
        this.weather_desc_en = weather_desc_en;
        this.weather_temp_f = weather_temp_f;
        this.weather_temp_min_f = weather_temp_min_f;
        this.weather_temp_max_f = weather_temp_max_f;
        this.weather_windspeed_miles = weather_windspeed_miles;
        this.weather_windgust_miles = weather_windgust_miles;
        this.weather_winddir16Point = weather_winddir16Point;
        this.weather_cloud_cover = weather_cloud_cover;
        this.weather_feels_like_c = weather_feels_like_c;
        this.weather_feels_like_f = weather_feels_like_f;
        this.weather_precip_mm = weather_precip_mm;
        this.weather_moonrise = weather_moonrise;
        this.weather_moonset = weather_moonset;
        this.weather_wind_chill_c = weather_wind_chill_c;
        this.weather_wind_chill_f = weather_wind_chill_f;
        this.weather_uv_index = weather_uv_index;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getPointName() {
        return mPointName;
    }

    public void setPointName(String mPointName) {
        this.mPointName = mPointName;
    }

    public String getPointDate() {
        return mPointDate;
    }

    public void setPointDate(String mPointDate) {
        this.mPointDate = mPointDate;
    }

    public String getPointDesc() {
        return mPointDesc;
    }

    public void setPointDesc(String mPointDesc) {
        this.mPointDesc = mPointDesc;
    }

    public String getPointLAT() {
        return mPointLAT;
    }

    public void setPointLAT(String mPointLAT) {
        this.mPointLAT = mPointLAT;
    }

    public String getPointLON() {
        return mPointLON;
    }

    public void setPointLON(String mPointLON) {
        this.mPointLON = mPointLON;
    }

    public String getPointImage() {
        return mPointImage;
    }

    public void setPointImage(String mPointImage) {
        this.mPointImage = mPointImage;
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route mRoute) {
        this.mRoute = mRoute;
    }


    public String getSunrise() {return mWeather_sunrise; }

    public void setSunrise (String mWeather_sunrise){
        this.mWeather_sunrise = mWeather_sunrise;
    }

    public String getSunset(){return mWeather_sunset;}
    public void setSunset(String mWeather_sunset){
        this.mWeather_sunset= mWeather_sunset;
    }
    public String getWeatherMain(){return mWeather_main;}
    public void setWeatherMain(String mWeather_main){this.mWeather_main=mWeather_main;}

    public String getWeatherDesc(){return mWeather_desc;}
    public void setWeatherDesc(String mWeather_desc){this.mWeather_desc= mWeather_desc;}

    public byte[] getWeatherIcon(){return mWeather_icon;}
    public void setWeatherIcon(byte[] mWeather_icon){this.mWeather_icon= mWeather_icon;}

    public String getWeatherTemp(){return mWeather_temp;}
    public void setWeatherTemp(String mWeather_temp){this.mWeather_temp= mWeather_temp;}
    public String getWeatherHum(){return mWeather_humidity;}
    public void setWeatherHum(String mWeather_humidity){this.mWeather_humidity=mWeather_humidity;}
    public String getWeatherPres(){return mWeather_pressure;}
    public void setWeatherPres(String mWeather_pressure){this.mWeather_pressure= mWeather_pressure;}
    public String getWeatherMinTemp(){return mWeather_temp_min;}
    public void setWeatherMinTemp(String mWeather_temp_min){this.mWeather_temp_min= mWeather_temp_min;}
    public String getWeatherMaxTemp(){return mWeather_temp_max;}
    public void setWeatherMaxTemp(String mWeather_temp_max){this.mWeather_temp_max=mWeather_temp_max; }
    public String getWeatherWindspeed(){return mWeather_windspeed;}
    public void setWeatherWindspeed(String mWeather_windspeed){this.mWeather_windspeed= mWeather_windspeed;}
    public String getWeatherWindgust(){return mWeather_windgust;}
    public void setWeatherWindgust(String mWeather_windgust){this.mWeather_windgust=mWeather_windgust;}
    public String getWeatherWindDeg(){return mWeather_winddeg;}
    public void setWeatherWindDeg(String mWeather_winddeg){this.mWeather_winddeg= mWeather_winddeg;}
    public String getWeatherLocationName(){return mWeather_location_name;}
    public void setWeatherLocationName(String mWeather_location_name){this.mWeather_location_name= mWeather_location_name;}
    public String getWeatherLocationCountry(){return mWeather_location_country;}
    public void setWeatherLocationCountry(String mWeather_location_country){this.mWeather_location_country=mWeather_location_country; }

    public String getWeatherVisibility(){return mWeather_visibility;}
    public void setWeatherVisibility(String mWeather_visibility){this.mWeather_visibility = mWeather_visibility; }

    public String getWeatherDescEn(){return weather_desc_en;}
    public void setWeatherDescEn(String weather_desc_en){this.weather_desc_en = weather_desc_en; }
    public String getWeatherTempF(){return weather_temp_f;}
    public void setWeatherTempF(String weather_temp_f){this.weather_temp_f = weather_temp_f; }
    public String getWeatherTempMinF(){return weather_temp_min_f;}
    public void setWeatherTempMinF(String weather_temp_min_f){this.weather_temp_min_f = weather_temp_min_f; }
    public String getWeatherTempMaxF(){return weather_temp_max_f;}
    public void setWeatherTempMaxF(String weather_temp_max_f){this.weather_temp_max_f = weather_temp_max_f; }
    public String getWeatherWindspeedMiles(){return weather_windspeed_miles;}
    public void setWeatherWindspeedMiles(String weather_windspeed_miles){this.weather_windspeed_miles= weather_windspeed_miles;}
    public String getWeatherWindgustmiles(){return weather_windgust_miles;}
    public void setWeatherWindgustMiles(String weather_windgust_miles){this.weather_windgust_miles=weather_windgust_miles;}
    public String getWeatherWindDir19Point(){return weather_winddir16Point;}
    public void setWeatherWindDir19Point(String weather_winddir16Point){this.weather_winddir16Point=weather_winddir16Point;}
    public String getWeatherCloudCover(){return weather_cloud_cover;}
    public void setWeatherCloudCover(String weather_cloud_cover){this.weather_cloud_cover=weather_cloud_cover;}
    public String getWeatherFeelsLikeC(){return weather_feels_like_c;}
    public void setWeatherFeelsLikeC(String weather_feels_like_c){this.weather_feels_like_c=weather_feels_like_c;}
    public String getWeatherFeelsLikeF(){return weather_feels_like_f;}
    public void setWeatherFeelsLikeF(String weather_feels_like_f){this.weather_feels_like_f=weather_feels_like_f;}

    public String getWeatherPrecip(){return weather_precip_mm;}
    public void setWeatherPrecip(String weather_precip_mm){this.weather_precip_mm=weather_precip_mm;}
    public String getWeatherMoonrise(){return weather_moonrise;}
    public void setWeatherMoonrise(String weather_moonrise){this.weather_moonrise=weather_moonrise;}
    public String getWeatherMoonset(){return weather_moonset;}
    public void setWeatherMoonset(String weather_moonset){this.weather_moonset=weather_moonset;}
    public String getWeatherWindChillC(){return weather_wind_chill_c;}
    public void setWeatherWindChillC(String weather_wind_chill_c){this.weather_wind_chill_c=weather_wind_chill_c;}
    public String getWeatherWindChillF(){return weather_wind_chill_f;}
    public void setWeatherWindChillF(String weather_wind_chill_f){this.weather_wind_chill_f=weather_wind_chill_f;}
    public String getWeatherUVIndex(){return weather_uv_index;}
    public void setWeatherUVIndex(String weather_uv_index){this.weather_uv_index=weather_uv_index;}

}