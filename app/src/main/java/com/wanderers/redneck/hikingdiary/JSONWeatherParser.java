/**
 * This is a tutorial source code 
 * provided "as is" and without warranties.
 *
 * For any question please visit the web site
 * http://www.survivingwithandroid.com
 *
 * or write an email to
 * survivingwithandroid@gmail.com
 *
 */
package com.wanderers.redneck.hikingdiary;

import android.util.Log;

import com.wanderers.redneck.hikingdiary.model.WorldWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ari Iivari on 3.4.2015.
 */
public class JSONWeatherParser {

	static private String TAG = "HikingDiary";
	public static WorldWeather getWeather(String data) throws JSONException {
		WorldWeather ww = new WorldWeather();

		Log.e(TAG, data);
		JSONObject jsonObj = new JSONObject(data);

		JSONObject  jsonpost= jsonObj.getJSONObject("data");

		JSONArray worldweather = jsonpost.getJSONArray("current_condition");
		JSONObject c = worldweather.getJSONObject(0);

		ww.currentCondition.setTempCur(c.getInt("temp_C"));
		ww.currentCondition.setTempFeel(c.getInt("FeelsLikeC"));
		ww.currentCondition.setCloudCover(c.getInt("cloudcover"));
		ww.currentCondition.setHumidity(c.getInt("humidity"));
		ww.currentCondition.setPressure(c.getInt("pressure"));
		ww.currentCondition.setVisibility(c.getInt("visibility"));
		ww.currentCondition.setWeatherDesc(c.getJSONArray("lang_fi").getJSONObject(0).getString("value"));
		ww.currentCondition.setWindDir(c.getInt("winddirDegree"));
		ww.currentCondition.setWindDir16(c.getString("winddir16Point"));
		ww.currentCondition.setWindspeed(c.getInt("windspeedKmph"));
		ww.currentCondition.setIcon(c.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value"));
		ww.currentCondition.setWeatherPrecip(c.getInt("precipMM"));
		ww.currentCondition.setTempFeel(c.getInt("FeelsLikeC"));

		// Weather data
		worldweather = jsonpost.getJSONArray("weather");
		c = worldweather.getJSONObject(0);

		ww.weather.setMoonrise(c.getJSONArray("astronomy").getJSONObject(0).getString("moonrise"));
		ww.weather.setMoonset(c.getJSONArray("astronomy").getJSONObject(0).getString("moonset"));
		ww.weather.setSunrise(c.getJSONArray("astronomy").getJSONObject(0).getString("sunrise"));
		ww.weather.setSunset(c.getJSONArray("astronomy").getJSONObject(0).getString("sunset"));
		ww.weather.setUv(c.getInt("uvIndex"));
		ww.weather.setTempMax(c.getInt("maxtempC"));
		ww.weather.setTempMin(c.getInt("mintempC"));

		// Location data
		worldweather = jsonpost.getJSONArray("nearest_area");
		c = worldweather.getJSONObject(0);

		ww.area.setAreaName(c.getJSONArray("areaName").getJSONObject(0).getString("value"));
		ww.area.setCountry(c.getJSONArray("country").getJSONObject(0).getString("value"));
		ww.area.setRegion(c.getJSONArray("region").getJSONObject(0).getString("value"));
		return ww;
	}
}
