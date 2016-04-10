package com.wanderers.redneck.hikingdiary.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * Created by Ari Iivari on 26.2.2015.
 */
public class DBHelper  extends SQLiteOpenHelper {

    public static final String TAG = "HikingDiary";
    private Context context;

    private static String DB_PATH = "/Android/data/com.wanderers.redneck.hikingdiary/databases/";
    // columns of the companies table
    public static final String TABLE_ROUTES = "routes";
    public static final String COLUMN_ROUTE_ID = "_id";
    public static final String COLUMN_ROUTE_NAME = "route_name";
    public static final String COLUMN_ROUTE_DATE = "route_date";

    // columns of the employees table
    public static final String TABLE_POINTS = "points";
    public static final String COLUMN_POINT_ID = COLUMN_ROUTE_ID;
    public static final String COLUMN_POINT_NAME = "point_name";
    public static final String COLUMN_POINT_DATE = "point_date";
    public static final String COLUMN_POINT_DESC = "point_desc";
    public static final String COLUMN_POINT_LAT = "point_lat";
    public static final String COLUMN_POINT_LON = "point_lon";
    public static final String COLUMN_POINT_IMAGE = "point_image";
    public static final String COLUMN_POINT_ROUTE_ID = "route_id";
    public static final String COLUMN_POINT_WEATHER_SUNRISE = "weather_sunrise"; //:1427514459,
    public static final String COLUMN_POINT_WEATHER_SUNSET = "weather_sunset"; //:1427561895},
    public static final String COLUMN_POINT_WEATHER_MAIN = "weather_main"; //"Rain",
    public static final String COLUMN_POINT_WEATHER_DESC = "weather_desc"; //"light rain",
    public static final String COLUMN_POINT_WEATHER_DESC_EN = "weather_desc_en"; //"light rain",
    public static final String COLUMN_POINT_WEATHER_ICON = "weather_icon"; //"10n"
    public static final String COLUMN_POINT_WEATHER_TEMP = "weather_temp"; //:273.15,
    public static final String COLUMN_POINT_WEATHER_TEMP_F = "weather_temp_f"; //:273.15,
    public static final String COLUMN_POINT_WEATHER_HUMI = "weather_humidity"; //:92,
    public static final String COLUMN_POINT_WEATHER_PRESSURE = "weather_pressure"; //:1007,
    public static final String COLUMN_POINT_WEATHER_TEMP_MIN = "weather_temp_min"; //:273.15,
    public static final String COLUMN_POINT_WEATHER_TEMP_MIN_F = "weather_temp_min_f"; //:273.15,
    public static final String COLUMN_POINT_WEATHER_TEMP_MAX = "weather_temp_max"; //:273.15},
    public static final String COLUMN_POINT_WEATHER_TEMP_MAX_F = "weather_temp_max_f"; //:273.15},
    public static final String COLUMN_POINT_WEATHER_VISIBILITY = "weather_visibility";
    public static final String COLUMN_POINT_WEATHER_WINDSPEED = "weather_windspeed"; //:3.08,
    public static final String COLUMN_POINT_WEATHER_WINDSPEED_MILES = "weather_windspeed_miles"; //:3.08,
    public static final String COLUMN_POINT_WEATHER_WINDGUST = "weather_windgust"; //:0,
    public static final String COLUMN_POINT_WEATHER_WINDGUST_MILES = "weather_windgust_miles"; //:0,
    public static final String COLUMN_POINT_WEATHER_WINDDEG = "weather_winddeg"; //:230},
    public static final String COLUMN_POINT_WEATHER_WINDDIR16_POINT = "weather_winddir16Point"; //:230},
    public static final String COLUMN_POINT_WEATHER_LOCATION_NAME = "weather_location_name"; //:"Jylkynkangas"
    public static final String COLUMN_POINT_WEATHER_LOCATION_COUNTRY = "weather_location_country"; //":"FI",
    public static final String COLUMN_POINT_WEATHER_CLOUD_COVER = "weather_cloud_cover";
    public static final String COLUMN_POINT_WEATHER_FEELS_LIKE_C = "weather_feels_like_c";
    public static final String COLUMN_POINT_WEATHER_FEELS_LIKE_F = "weather_feels_like_f";
    public static final String COLUMN_POINT_WEATHER_PRECIP_MM = "weather_precip_mm";
    public static final String COLUMN_POINT_WEATHER_MOONRISE = "weather_moonrise";
    public static final String COLUMN_POINT_WEATHER_MOONSET = "weather_moonset";
    public static final String COLUMN_POINT_WEATHER_WIND_CHILL_C = "weather_wind_chill_c";
    public static final String COLUMN_POINT_WEATHER_WIND_CHILL_F = "weather_wind_chill_f";
    public static final String COLUMN_POINT_WEATHER_UV_INDEX = "weather_uv_index";


    private static final String DATABASE_NAME = "hikingdiary.db";

    private static final int DATABASE_VERSION = 8;

    private static final String SQL_CREATE_TABLE_POINTS = "CREATE TABLE " + TABLE_POINTS + "("
            + COLUMN_POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_POINT_NAME + " TEXT NOT NULL, "
            + COLUMN_POINT_DATE + " TEXT NOT NULL, "
            + COLUMN_POINT_DESC + " TEXT, "
            + COLUMN_POINT_LAT + " TEXT, "
            + COLUMN_POINT_LON + " TEXT, "
            + COLUMN_POINT_IMAGE + " TEXT, "
            + COLUMN_POINT_ROUTE_ID + " INTEGER NOT NULL, "
            + COLUMN_POINT_WEATHER_SUNRISE + " INTEGER, "
            + COLUMN_POINT_WEATHER_SUNSET  + " INTEGER, "
            + COLUMN_POINT_WEATHER_MAIN + " TEXT, "
            + COLUMN_POINT_WEATHER_DESC + " TEXT, "
            + COLUMN_POINT_WEATHER_DESC_EN + " TEXT, "
            + COLUMN_POINT_WEATHER_ICON + " BLOB, "
            + COLUMN_POINT_WEATHER_TEMP + " REAL, "
            + COLUMN_POINT_WEATHER_TEMP_F + " REAL, "
            + COLUMN_POINT_WEATHER_HUMI + " REAL, "
            + COLUMN_POINT_WEATHER_PRESSURE + " REAL, "
            + COLUMN_POINT_WEATHER_TEMP_MIN + " REAL, "
            + COLUMN_POINT_WEATHER_TEMP_MIN_F + " REAL, "
            + COLUMN_POINT_WEATHER_TEMP_MAX + " REAL, "
            + COLUMN_POINT_WEATHER_TEMP_MAX_F + " REAL, "
            + COLUMN_POINT_WEATHER_VISIBILITY + " INTEGER, "
            + COLUMN_POINT_WEATHER_WINDSPEED + " REAL, "
            + COLUMN_POINT_WEATHER_WINDSPEED_MILES + " REAL, "
            + COLUMN_POINT_WEATHER_WINDGUST + " REAL, "
            + COLUMN_POINT_WEATHER_WINDGUST_MILES + " REAL, "
            + COLUMN_POINT_WEATHER_WINDDEG + " REAL, "
            + COLUMN_POINT_WEATHER_WINDDIR16_POINT + " TEXT, "
            + COLUMN_POINT_WEATHER_LOCATION_NAME + " TEXT, "
            + COLUMN_POINT_WEATHER_LOCATION_COUNTRY + " TEXT, "
            + COLUMN_POINT_WEATHER_CLOUD_COVER + " INTEGER, "
            + COLUMN_POINT_WEATHER_FEELS_LIKE_C + " REAL, "
            + COLUMN_POINT_WEATHER_FEELS_LIKE_F + " REAL, "
            + COLUMN_POINT_WEATHER_PRECIP_MM + " INTEGER, "
            + COLUMN_POINT_WEATHER_MOONRISE + " INTEGER, "
            + COLUMN_POINT_WEATHER_MOONSET + " INTEGER, "
            + COLUMN_POINT_WEATHER_WIND_CHILL_C + " REAL, "
            + COLUMN_POINT_WEATHER_WIND_CHILL_F + " REAL, "
            + COLUMN_POINT_WEATHER_UV_INDEX + " INTEGER "
            +");";

    private static final String SQL_CREATE_TABLE_ROUTES = "CREATE TABLE " + TABLE_ROUTES + "("
            + COLUMN_ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ROUTE_NAME + " TEXT NOT NULL, "
            + COLUMN_ROUTE_DATE + " TEXT NOT NULL "
            +");";

    public DBHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory().getPath() + DB_PATH + DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try{
            database.execSQL(SQL_CREATE_TABLE_ROUTES);
            database.execSQL(SQL_CREATE_TABLE_POINTS);
        } catch (SQLException e){
            Log.e(TAG, ""+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
            onCreate(db);
        } catch (SQLException e){
            Log.e(TAG, ""+e);
        }

    }
}
