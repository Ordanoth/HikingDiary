package com.wanderers.redneck.hikingdiary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wanderers.redneck.hikingdiary.model.Point;
import com.wanderers.redneck.hikingdiary.model.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ari Iivari on 26.2.2015.
 */
public class PointDAO implements Serializable{

    public static final String TAG = "HikingDiary";
    private static final long serialVersionUID = -7406082437623008161L;

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_POINT_ID, DBHelper.COLUMN_POINT_NAME,
            DBHelper.COLUMN_POINT_DATE, DBHelper.COLUMN_POINT_DESC, DBHelper.COLUMN_POINT_LAT,
            DBHelper.COLUMN_POINT_LON,DBHelper.COLUMN_POINT_IMAGE,
            DBHelper.COLUMN_POINT_WEATHER_SUNRISE,
            DBHelper.COLUMN_POINT_WEATHER_SUNSET,
            DBHelper.COLUMN_POINT_WEATHER_MAIN,
            DBHelper.COLUMN_POINT_WEATHER_DESC,
            DBHelper.COLUMN_POINT_WEATHER_DESC_EN,
            DBHelper.COLUMN_POINT_WEATHER_ICON,
            DBHelper.COLUMN_POINT_WEATHER_TEMP,
            DBHelper.COLUMN_POINT_WEATHER_TEMP_F,
            DBHelper.COLUMN_POINT_WEATHER_HUMI,
            DBHelper.COLUMN_POINT_WEATHER_PRESSURE,
            DBHelper.COLUMN_POINT_WEATHER_TEMP_MIN,
            DBHelper.COLUMN_POINT_WEATHER_TEMP_MIN_F,
            DBHelper.COLUMN_POINT_WEATHER_TEMP_MAX,
            DBHelper.COLUMN_POINT_WEATHER_TEMP_MAX_F,
            DBHelper.COLUMN_POINT_WEATHER_VISIBILITY,
            DBHelper.COLUMN_POINT_WEATHER_WINDSPEED,
            DBHelper.COLUMN_POINT_WEATHER_WINDSPEED_MILES,
            DBHelper.COLUMN_POINT_WEATHER_WINDGUST,
            DBHelper.COLUMN_POINT_WEATHER_WINDGUST_MILES,
            DBHelper.COLUMN_POINT_WEATHER_WINDDEG,
            DBHelper.COLUMN_POINT_WEATHER_WINDDIR16_POINT,
            DBHelper.COLUMN_POINT_WEATHER_LOCATION_NAME,
            DBHelper.COLUMN_POINT_WEATHER_LOCATION_COUNTRY,
            DBHelper.COLUMN_POINT_WEATHER_CLOUD_COVER,
            DBHelper.COLUMN_POINT_WEATHER_FEELS_LIKE_C,
            DBHelper.COLUMN_POINT_WEATHER_FEELS_LIKE_F,
            DBHelper.COLUMN_POINT_WEATHER_PRECIP_MM,
            DBHelper.COLUMN_POINT_WEATHER_MOONRISE,
            DBHelper.COLUMN_POINT_WEATHER_MOONSET,
            DBHelper.COLUMN_POINT_WEATHER_WIND_CHILL_C,
            DBHelper.COLUMN_POINT_WEATHER_WIND_CHILL_F,
            DBHelper.COLUMN_POINT_WEATHER_UV_INDEX,
            DBHelper.COLUMN_POINT_ROUTE_ID};

    public PointDAO(Context context) {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        try {
            open();
        }
        catch(SQLException e) {
            Log.e(TAG, "SQLException " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Point createPoint(String pointName, String pointDate, String pointDesc,
                             String pointLAT, String pointLON, String pointImage, String weather_sunrise,
                             String weather_sunset, String weather_main, String weather_desc, String weather_desc_en, byte[] weather_icon,
                             float weather_temp, float weather_temp_f, float weather_humidity, float weather_pressure, float weather_temp_min, float weather_temp_min_f,
                             float weather_temp_max, float weather_temp_max_f, int weather_visibility, float weather_windspeed, float weather_windspeed_miles, float weather_windgust, float weather_windgust_miles, float weather_winddeg,
                             String weather_winddir16Point, String weather_location_name, String weather_location_country, int weather_cloud_cover,int weather_feels_like_c,
                             int weather_feels_like_f, float weather_precip_mm, String weather_moonrise, String weather_moonset, String weather_wind_chill_c, String weather_wind_chill_f, int weather_uv_index,
                             long routeId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_POINT_NAME, pointName);
        values.put(DBHelper.COLUMN_POINT_DATE, pointDate);
        values.put(DBHelper.COLUMN_POINT_DESC, pointDesc);
        values.put(DBHelper.COLUMN_POINT_LAT, pointLAT);
        values.put(DBHelper.COLUMN_POINT_LON, pointLON);
        values.put(DBHelper.COLUMN_POINT_IMAGE, pointImage);

        values.put(DBHelper.COLUMN_POINT_WEATHER_SUNRISE, weather_sunrise);
        values.put(DBHelper.COLUMN_POINT_WEATHER_SUNSET, weather_sunset);
        values.put(DBHelper.COLUMN_POINT_WEATHER_MAIN, weather_main);
        values.put(DBHelper.COLUMN_POINT_WEATHER_DESC, weather_desc);
        values.put(DBHelper.COLUMN_POINT_WEATHER_DESC_EN, weather_desc_en);
        values.put(DBHelper.COLUMN_POINT_WEATHER_ICON, weather_icon);
        values.put(DBHelper.COLUMN_POINT_WEATHER_TEMP, weather_temp);
        values.put(DBHelper.COLUMN_POINT_WEATHER_TEMP_F, weather_temp_f);
        values.put(DBHelper.COLUMN_POINT_WEATHER_HUMI, weather_humidity);
        values.put(DBHelper.COLUMN_POINT_WEATHER_PRESSURE, weather_pressure);
        values.put(DBHelper.COLUMN_POINT_WEATHER_TEMP_MIN, weather_temp_min);
        values.put(DBHelper.COLUMN_POINT_WEATHER_TEMP_MIN_F, weather_temp_min_f);
        values.put(DBHelper.COLUMN_POINT_WEATHER_TEMP_MAX, weather_temp_max);
        values.put(DBHelper.COLUMN_POINT_WEATHER_TEMP_MAX_F, weather_temp_max_f);
        values.put(DBHelper.COLUMN_POINT_WEATHER_VISIBILITY, weather_visibility);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WINDSPEED, weather_windspeed);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WINDSPEED_MILES, weather_windspeed_miles);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WINDGUST, weather_windgust);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WINDGUST_MILES, weather_windgust_miles);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WINDDEG, weather_winddeg);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WINDDIR16_POINT, weather_winddir16Point);
        values.put(DBHelper.COLUMN_POINT_WEATHER_LOCATION_NAME, weather_location_name);
        values.put(DBHelper.COLUMN_POINT_WEATHER_LOCATION_COUNTRY, weather_location_country);
        values.put(DBHelper.COLUMN_POINT_WEATHER_CLOUD_COVER, weather_cloud_cover);
        values.put(DBHelper.COLUMN_POINT_WEATHER_FEELS_LIKE_C, weather_feels_like_c);
        values.put(DBHelper.COLUMN_POINT_WEATHER_FEELS_LIKE_F, weather_feels_like_f);
        values.put(DBHelper.COLUMN_POINT_WEATHER_PRECIP_MM, weather_precip_mm);
        values.put(DBHelper.COLUMN_POINT_WEATHER_MOONRISE, weather_moonrise);
        values.put(DBHelper.COLUMN_POINT_WEATHER_MOONSET, weather_moonset);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WIND_CHILL_C, weather_wind_chill_c);
        values.put(DBHelper.COLUMN_POINT_WEATHER_WIND_CHILL_F, weather_wind_chill_f);
        values.put(DBHelper.COLUMN_POINT_WEATHER_UV_INDEX, weather_uv_index);

        values.put(DBHelper.COLUMN_POINT_ROUTE_ID, routeId);

        long insertId = mDatabase.insert(DBHelper.TABLE_POINTS, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_POINTS,
                mAllColumns, DBHelper.COLUMN_POINT_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Point newPoint = cursorToPoint(cursor);
        cursor.close();
        return newPoint;
    }

    public int updatePoint(String pointName, String pointDate, String pointDesc, String pointImage, long pointId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_POINT_NAME, pointName);
        values.put(DBHelper.COLUMN_POINT_DATE, pointDate);
        values.put(DBHelper.COLUMN_POINT_DESC, pointDesc);
        values.put(DBHelper.COLUMN_POINT_IMAGE, pointImage);
        return mDatabase.update(DBHelper.TABLE_POINTS, values, "_id =" + Long.toString(pointId), null );
    }

    public void deletePoint(Point point) {
        long id = point.getId();
        mDatabase.delete(DBHelper.TABLE_POINTS, DBHelper.COLUMN_POINT_ID + " = " + id, null);
    }

    public List<Point> getPointsOfRoute(long routeId) {
        List<Point> listPoints = new ArrayList<Point>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_POINTS, mAllColumns
                , DBHelper.COLUMN_POINT_ROUTE_ID + " = ?",
                new String[] { String.valueOf(routeId) }, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Point point = cursorToPoint(cursor);
            listPoints.add(point);
            cursor.moveToNext();
        }
        cursor.close();
        return listPoints;
    }

    private Point cursorToPoint(Cursor cursor) {
        Point point = new Point();
        point.setId(cursor.getLong(0));
        point.setPointName(cursor.getString(1));
        point.setPointDate(cursor.getString(2));
        point.setPointDesc(cursor.getString(3));
        point.setPointLAT(cursor.getString(4));
        point.setPointLON(cursor.getString(5));
        point.setPointImage(cursor.getString(6));
        point.setSunrise(cursor.getString(7));
        point.setSunset(cursor.getString(8));
        point.setWeatherMain(cursor.getString(9));
        point.setWeatherDesc(cursor.getString(10));
        point.setWeatherDescEn(cursor.getString(11));
        point.setWeatherIcon(cursor.getBlob(12));
        point.setWeatherTemp(cursor.getString(13));
        point.setWeatherTempF(cursor.getString(14));
        point.setWeatherHum(cursor.getString(15));
        point.setWeatherPres(cursor.getString(16));
        point.setWeatherMinTemp(cursor.getString(17));
        point.setWeatherTempMinF(cursor.getString(18));
        point.setWeatherMaxTemp(cursor.getString(19));
        point.setWeatherTempMaxF(cursor.getString(20));
        point.setWeatherVisibility(cursor.getString(21));
        point.setWeatherWindspeed(cursor.getString(22));
        point.setWeatherWindspeedMiles(cursor.getString(23));
        point.setWeatherWindgust(cursor.getString(24));
        point.setWeatherWindgustMiles(cursor.getString(25));
        point.setWeatherWindDeg(cursor.getString(26));
        point.setWeatherWindDir19Point(cursor.getString(27));
        point.setWeatherLocationName(cursor.getString(28));
        point.setWeatherLocationCountry(cursor.getString(29));

        point.setWeatherCloudCover(cursor.getString(30));
        point.setWeatherFeelsLikeC(cursor.getString(31));
        point.setWeatherFeelsLikeF(cursor.getString(32));
        point.setWeatherPrecip(cursor.getString(33));
        point.setWeatherMoonrise(cursor.getString(34));
        point.setWeatherMoonset(cursor.getString(35));
        point.setWeatherWindChillC(cursor.getString(36));
        point.setWeatherWindChillF(cursor.getString(37));
        point.setWeatherUVIndex(cursor.getString(38));

        long routeId = cursor.getLong(39);
        RouteDAO dao = new RouteDAO(mContext);
        Route route = dao.getRouteById(routeId);
        if(route != null) {
            point.setRoute(route);
        }
        dao.close();
        return point;
    }

    public Point getPointByPointId(long id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_POINTS, mAllColumns, DBHelper.COLUMN_POINT_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Point point = cursorToPoint(cursor);
        return point;
    }
}
