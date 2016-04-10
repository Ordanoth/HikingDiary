package com.wanderers.redneck.hikingdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.dao.PointDAO;
import com.wanderers.redneck.hikingdiary.model.Point;

/**
 * Created by Ari Iivari on 1.4.2015.
 */
public class ActivityWeatherDetails extends Activity {

    public static final String TAG = "HikingDiary";

    private TextView mLocation, mTemp, mDescrWeather, mTempUnit, mTempMin, mTempMax, mWindSpeed, mWindDeg, mHumidity, mPressure, mPressureStat, mVisibility,
            mSunrise, mSunset, mCloudCover, mMoonrise, mMoonset, mUv, mPrecip, mWind16Deg, mFeelsLike;
    private ImageView mWeatherImage;

    private Point mPoint;
    private PointDAO mPointDao;
    private long mPointId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle(R.string.weather_information_header);

        initViews();

        Intent intent = getIntent();
        if(intent != null) {
            this.mPointId = intent.getLongExtra("weather_point", mPointId);
            Log.e(TAG, "mPointId " + mPointId);
        }
        mPointDao = new PointDAO(this);
        mPoint = mPointDao.getPointByPointId(mPointId);
        Log.e(TAG,"" + mPoint.getPointName() + " " + mPoint.getId());
        populateView(mPoint);
    }

    private void populateView(Point mPoint) {
        byte[] blob = mPoint.getWeatherIcon();
        this.mWeatherImage.setImageBitmap(getPhoto(blob));
        this.mLocation.setText(mPoint.getPointName());
        float fTemp = Float.parseFloat(mPoint.getWeatherTemp());
        this.mTemp.setText("" + (int)fTemp);
        this.mDescrWeather.setText(mPoint.getWeatherDesc());
        fTemp = Float.parseFloat(mPoint.getWeatherMinTemp());
        this.mTempMin.setText(""+ (int)fTemp + getString(R.string.common_celsius));
        fTemp = Float.parseFloat(mPoint.getWeatherMaxTemp());
        this.mTempMax.setText(""+ (int)fTemp + getString(R.string.common_celsius));
        this.mWindSpeed.setText(mPoint.getWeatherWindspeed() + getString(R.string.common_speed));
        float fDeg = Float.parseFloat(mPoint.getWeatherWindDeg());
        this.mWindDeg.setText(""+ (int)fDeg + getString(R.string.common_deg));
        this.mWind16Deg.setText(mPoint.getWeatherWindDir19Point());
        this.mHumidity.setText(mPoint.getWeatherHum() + getString(R.string.common_percent));
        this.mPressure.setText(mPoint.getWeatherPres() + " " + getString(R.string.common_hpa));
        this.mPressureStat.setText("");
        this.mVisibility.setText(mPoint.getWeatherVisibility()+ "km");
        this.mSunrise.setText(mPoint.getSunrise());
        this.mSunset.setText(mPoint.getSunset());
        this.mCloudCover.setText(mPoint.getWeatherCloudCover() + getString(R.string.common_percent));
        this.mMoonrise.setText(mPoint.getWeatherMoonrise());
        this.mMoonset.setText(mPoint.getWeatherMoonset());
        this.mUv.setText(mPoint.getWeatherUVIndex());
        float precip = Float.parseFloat(mPoint.getWeatherPrecip());
        this.mPrecip.setText("" + precip);
        this.mFeelsLike.setText(mPoint.getWeatherFeelsLikeC()+ getString(R.string.common_celsius));
    }

    private void initViews() {
        this.mLocation = (TextView) findViewById(R.id.location);
        this.mTemp = (TextView) findViewById(R.id.temp);
        this.mTempUnit = (TextView) findViewById(R.id.tempUnit);
        this.mDescrWeather = (TextView) findViewById(R.id.descrWeather);
        this.mTempMin = (TextView) findViewById(R.id.tempMin);
        this.mTempMax = (TextView) findViewById(R.id.tempMax);
        this.mWindSpeed = (TextView) findViewById(R.id.windSpeed);
        this.mWindDeg = (TextView) findViewById(R.id.windDeg);
        this.mHumidity = (TextView) findViewById(R.id.humidity);
        this.mPressure = (TextView) findViewById(R.id.pressure);
        this.mPressureStat = (TextView) findViewById(R.id.pressureStat);
        this.mVisibility = (TextView) findViewById(R.id.visibility);
        this.mSunrise = (TextView) findViewById(R.id.sunrise);
        this.mSunset = (TextView) findViewById(R.id.sunset);
        this.mCloudCover = (TextView) findViewById(R.id.cloudCover);
        this.mMoonrise = (TextView) findViewById(R.id.moonrise);
        this.mMoonset = (TextView) findViewById(R.id.moonset);
        this.mUv = (TextView) findViewById(R.id.uvIndex);
        this.mPrecip = (TextView) findViewById(R.id.precip);
        this.mWind16Deg = (TextView) findViewById(R.id.wind16Deg);
        this.mFeelsLike = (TextView) findViewById(R.id.tempFeelsLike);

        this.mWeatherImage = (ImageView) findViewById(R.id.imgWeather);

    }

    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void onStart() {
        super.onStart();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPointDao.close();
    }
}
