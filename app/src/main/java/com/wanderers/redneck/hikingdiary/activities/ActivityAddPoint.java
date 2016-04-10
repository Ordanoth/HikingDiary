package com.wanderers.redneck.hikingdiary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.wanderers.redneck.hikingdiary.JSONWeatherParser;
import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.ServiceHandler;
import com.wanderers.redneck.hikingdiary.adapter.LocationAdapter;
import com.wanderers.redneck.hikingdiary.dao.PointDAO;
import com.wanderers.redneck.hikingdiary.dao.RouteDAO;
import com.wanderers.redneck.hikingdiary.model.Point;
import com.wanderers.redneck.hikingdiary.model.Route;
import com.wanderers.redneck.hikingdiary.model.WorldWeather;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.String.format;

/**
 * Created by Ari Iivari on 26.2.2015.
 */
public class ActivityAddPoint extends Activity implements OnClickListener {

    public static final String TAG = "HikingDiary";
    public static final String PHOTO_PATH = "/HikingDiary/photos";
    public static final String EXTRA_SELECTED_ROUTE_ID = "extra_key_selected_route_id";
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;

    private EditText mTxtPointName;
    private TextView mTxtPointDate;
    private EditText mTxtDesc;
    private EditText mTxtPointLAT;
    private EditText mTxtPointLON;
    private ImageView mTxtPointImage;
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    private TextView mCityText, mCondDescr, mTemp, mHum, mPress, mWindSpeed, mWindDeg, mWeatherTitle, mWeatherPressTitle, mWindTitle, mHumTitle;
    private ImageView mImgView;
    private Button mBtnGetWeather;
    private EditText mWorldWeatherEdit;

    private RouteDAO mRouteDao;
    private PointDAO mPointDao;

    private Route mSelectedRoute;
    private long mRouteId = -1;

    LocationAdapter mGetCoordinates;
    private WorldWeather mWeather;
    private ProgressDialog pDialog;
    String mWeatherkey;
    SharedPreferences prefs;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;
            updateDateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle(R.string.point_add);

         prefs = this.getSharedPreferences(
                "com.wanderers.redneck.hikingdiary", Context.MODE_PRIVATE);

        initViews();

//        mWeatherkey = prefs.getString("weatherkey", null);
        if(prefs.getString("weatherkey", null) == null){
            // Inflate the about message contents
            View messageView = getLayoutInflater().inflate(R.layout.world_weather_key_note, null, false);
            this.mWorldWeatherEdit = (EditText) messageView.findViewById(R.id.txt_world_weather_key);
            // When linking text, force to always use default color. This works
            // around a pressed color state bug.
            TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
//            mWeatherkey = "";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.app_icon);
            builder.setTitle(R.string.app_name);
            builder.setView(messageView);
            builder.setPositiveButton(getString(R.string.common_save), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mWeatherkey = mWorldWeatherEdit.getText().toString();
                    SharedPreferences.Editor edit  = prefs.edit();
                    edit.putString("weatherkey", mWorldWeatherEdit.getText().toString());
                    edit.commit();
                }
            });


            builder.setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            builder.create();
            final AlertDialog dialog = builder.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            mWorldWeatherEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.length()==0){
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            });
        }

        Intent intent  = getIntent();
        if(intent != null) {
            this.mRouteId = intent.getLongExtra(EXTRA_SELECTED_ROUTE_ID, -1);
        }

        this.mRouteDao = new RouteDAO(this);
        this.mPointDao = new PointDAO(this);

        mSelectedRoute = mRouteDao.getRouteById(mRouteId);
        mGetCoordinates = new LocationAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_point_actionbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                showAbout();
                break;
            case R.id.action_save:
                Bitmap photo = ((BitmapDrawable)mImgView.getDrawable()).getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
                String point_date = mTxtPointDate.getText().toString();
                String point_name = mTxtPointName.getText().toString();
                String pointDesc = mTxtDesc.getText().toString();
                String pointLAT = mTxtPointLAT.getText().toString();
                String pointLON = mTxtPointLON.getText().toString();
                String pointImage = mTxtPointImage.getTag().toString();
                SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
                SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm", Locale.GERMANY);
                String sunrise = mWeather.weather.getSunrise();
                String sunset = mWeather.weather.getSunset();
                String weather_sunrise = null;
                String weather_sunset = null;
                try {
                    weather_sunrise = outFormat.format(inFormat.parse(sunrise));
                    weather_sunset = outFormat.format(inFormat.parse(sunset));
                    Log.e(TAG, weather_sunrise + " " + weather_sunset );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String weather_main = "";
                String weather_desc = mWeather.currentCondition.getWeatherDesc();
                String weather_desc_en = mWeather.currentCondition.getWeatherDescEn();
                byte[] weather_icon = bos.toByteArray();
                int weather_temp = mWeather.currentCondition.getTempCur();
                int weather_temp_f = mWeather.currentCondition.getTempCur();
                int weather_humidity = mWeather.currentCondition.getHumidity();
                int weather_pressure = mWeather.currentCondition.getPressure();
                int weather_temp_min = mWeather.weather.getTempMin();
                int weather_temp_min_f = mWeather.weather.getTempMinF();
                int weather_temp_max = mWeather.weather.getTempMax();
                int weather_temp_max_f = mWeather.weather.getTempMaxF();
                int weather_visibility = mWeather.currentCondition.getVisibility();
                int weather_windspeed = mWeather.currentCondition.getWindspeed();
                int weather_windspeed_miles = mWeather.currentCondition.getWindspeedMiles();
                int weather_windgust =0;
                int weather_windgust_miles =0;
                float weather_winddeg =mWeather.currentCondition.getWindDir();
                String weather_location_name =mWeather.area.getAreaName()+", " + mWeather.area.getRegion();
                String weather_location_country = mWeather.area.getCountry();
                String weather_winddir16Point = mWeather.currentCondition.getWindDir16();
                int weather_cloud_cover = mWeather.currentCondition.getCloudCover();
                int weather_feels_like_c = mWeather.currentCondition.getTempFeel();
                int weather_feels_like_f = mWeather.currentCondition.getTempFeel();
                float weather_precip_mm = mWeather.currentCondition.getWeatherPrecip();
                String moonrise = mWeather.weather.getMoonrise();
                String moonset = mWeather.weather.getMoonset();
                String weather_moonrise = null;
                String weather_moonset = null;
                try {
                    weather_moonrise = outFormat.format(inFormat.parse(moonrise));
                    weather_moonset = outFormat.format(inFormat.parse(moonset));
                    Log.e(TAG, weather_moonrise + " " + weather_moonset);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String weather_wind_chill_c = "";
                String weather_wind_chill_f = "";
                int weather_uv_index = mWeather.weather.getUv();

                Log.e(TAG, point_name + " " + point_date + " " + pointDesc);

                if (!TextUtils.isEmpty(point_name) && !TextUtils.isEmpty(point_date)
                        && !TextUtils.isEmpty(pointDesc)) {

                    // add the point to database
                    Point createdPoint = mPointDao.createPoint(point_name, point_date, pointDesc, pointLAT, pointLON, pointImage, weather_sunrise,
                            weather_sunset, weather_main, weather_desc, weather_desc_en, weather_icon,
                            weather_temp, weather_temp_f, weather_humidity, weather_pressure, weather_temp_min, weather_temp_min_f,
                            weather_temp_max, weather_temp_max_f, weather_visibility, weather_windspeed, weather_windspeed_miles, weather_windgust, weather_windgust_miles, weather_winddeg,
                            weather_winddir16Point, weather_location_name, weather_location_country, weather_cloud_cover, weather_feels_like_c, weather_feels_like_f, weather_precip_mm,
                            weather_moonrise, weather_moonset, weather_wind_chill_c, weather_wind_chill_f, weather_uv_index, mSelectedRoute.getId());

                    /*
                    String pointName, String pointDate, String pointDesc,

                            String pointLAT, String pointLON, String pointImage, String weather_sunrise,
                            String weather_sunset, String weather_main, String weather_desc, String weather_desc_en, byte[] weather_icon,
                    float weather_temp, float weather_temp_f, float weather_humidity, float weather_pressure, float weather_temp_min, float weather_temp_min_f,
                    float weather_temp_max, float weather_temp_max_f, int weather_visibility, float weather_windspeed, float weather_windspeed_miles, float weather_windgust, float weather_windgust_miles, float weather_winddeg,
                    ,
                    */
                    Toast.makeText(this, R.string.point_created_successfully, Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    mPointDao.close();
                    finish();
                }
                else {
                    Toast.makeText(this, R.string.empty_fields_message, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

        return true;
    }

    private void initViews() {
        this.mTxtPointName = (EditText) findViewById(R.id.edit_txt_point_add_details_name);
        this.mTxtPointDate = (TextView) findViewById(R.id.txt_point_add_details_date);
        this.mTxtDesc = (EditText) findViewById(R.id.edit_txt_point_add_desc);
        this.mTxtPointLAT = (EditText) findViewById(R.id.edit_txt_point_add_lat_text);
        this.mTxtPointLON = (EditText) findViewById(R.id.edit_txt_point_add_lon_text);
        this.mTxtPointImage = (ImageView) findViewById(R.id.image_point_add);
        ImageButton mBtnGetCoord = (ImageButton) findViewById(R.id.btn_get_location);

        Calendar calendar = Calendar.getInstance();
        this.mSelectedYear = calendar.get(Calendar.YEAR);
        this.mSelectedMonth = calendar.get(Calendar.MONTH);
        this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateUI();

        mBtnGetCoord.setOnClickListener(this);
        this.mTxtPointDate.setOnClickListener(this);
        this.mTxtPointImage.setOnClickListener(this);

        //Weather
        this.mWeatherTitle = (TextView) findViewById(R.id.weatherTitle);
        this.mCityText = (TextView) findViewById(R.id.stationText);

        this.mCondDescr = (TextView) findViewById(R.id.condDescr);
        this.mTemp = (TextView) findViewById(R.id.temp);
        this.mHum = (TextView) findViewById(R.id.hum);
        this.mPress = (TextView) findViewById(R.id.press);
        this.mWindSpeed = (TextView) findViewById(R.id.windSpeed);
        this.mWindDeg = (TextView) findViewById(R.id.windDeg);
        this.mImgView = (ImageView) findViewById(R.id.condIcon);
        this.mBtnGetWeather = (Button) findViewById(R.id.btn_get_weather);
        this.mBtnGetWeather.setOnClickListener(this);
        this.mWeatherPressTitle = (TextView) findViewById(R.id.pressLab);
        this.mWindTitle = (TextView) findViewById(R.id.windLab);
        this.mHumTitle = (TextView) findViewById(R.id.humLab);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_point_add_details_date:
                showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);
                break;
            case R.id.btn_get_location:
                if(mGetCoordinates.canGetLocation()){
                    mTxtPointLAT.setText(Double.toString(mGetCoordinates.getLatitude()));
                    mTxtPointLON.setText(Double.toString(mGetCoordinates.getLongitude()));
                }else{
                    mGetCoordinates.showSettingsAlert();
                }
                break;
            case R.id.image_point_add:
                selectImage();
                break;
            case R.id.btn_get_weather:
                if(!TextUtils.isEmpty(mTxtPointLAT.getText().toString()) && !TextUtils.isEmpty(mTxtPointLON.getText().toString())){
                    String lat = mTxtPointLAT.getText().toString();
                    String lon = mTxtPointLON.getText().toString();
                    GetWeather task = new GetWeather();
                    task.execute(new String[]{lat, lon});

                    mWeatherTitle.setVisibility(View.VISIBLE);

                    this.mBtnGetWeather.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, R.string.empty_fields_coord, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
                    int nh = (int) ( bm.getHeight() * (512.0 / bm.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, 512, nh, true);

                    this.mTxtPointImage.setImageBitmap(scaled);

                    File folder = new File(Environment.getExternalStorageDirectory().getPath() + PHOTO_PATH );

                    if(!folder.exists())
                    {
                        if(folder.mkdirs())
                        {
                        }
                    }

                    String path = android.os.Environment.getExternalStorageDirectory() + PHOTO_PATH;

                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    this.mTxtPointImage.setTag(String.valueOf(file));
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, ActivityAddPoint.this);

                File imgFile = new  File(tempPath);

                if(imgFile.exists()){
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), btmapOptions);
                    int nh = (int) ( myBitmap.getHeight() * (512.0 / myBitmap.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 512, nh, true);
                    this.mTxtPointImage.setTag(imgFile);
                    this.mTxtPointImage.setImageBitmap(scaled);
                }
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void updateDateUI() {
        String month = ((mSelectedMonth+1) > 9) ? ""+(mSelectedMonth+1): "0"+(mSelectedMonth+1) ;
        String day = ((mSelectedDay) < 10) ? "0"+mSelectedDay: ""+mSelectedDay ;
        mTxtPointDate.setText(day + "." + month + "." + mSelectedYear);
    }

    private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, initialYear, initialMonth, initialDay);
        dialog.show();
        return dialog;
    }

    private void selectImage() {
        final CharSequence[] items = { getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.common_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddPoint.this);
        builder.setTitle(getString(R.string.change_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getString(R.string.choose_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)), SELECT_FILE);
                } else if (items[item].equals(getString(R.string.common_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.app_icon);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteDao.close();
        mPointDao.close();
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

    private class GetWeather extends AsyncTask<String, Void, WorldWeather> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityAddPoint.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected WorldWeather doInBackground(String... params) {
            WorldWeather worldweather = new WorldWeather();
            ServiceHandler sh = new ServiceHandler();

            String location = format("&lat=%s&lon=%s",params[0], params[1]);
            String BASE_URL = "http://api.worldweatheronline.com/free/v2/weather.ashx?";
            String KEYID = "key=" + prefs.getString("weatherkey", null);
            String LANGID = "&lang=fi";
            String FORMATID = "&format=json";
            String TPID = "&tp=0";
            String LOCDATA = "&includeLocation=yes";
            String data = BASE_URL + KEYID + location + LANGID + TPID + FORMATID + LOCDATA;
            Log.d("Linkki: ", "> " + data);
            String jsonStr = sh.makeServiceCall(data, ServiceHandler.GET);

//            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    worldweather = JSONWeatherParser.getWeather(jsonStr);

                    worldweather.iconData = ((new ServiceHandler()).getImage(worldweather.currentCondition.getIcon()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, getString(R.string.could_get_weather));
            }
            return worldweather;
        }

        @Override
        protected void onPostExecute(WorldWeather worldweather) {
            super.onPostExecute(worldweather);
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            mWeather = worldweather;

            if (worldweather.iconData != null && worldweather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(worldweather.iconData, 0, worldweather.iconData.length);
                mImgView.setImageBitmap(img);
            }

//            mCityText.setText(worldweather.location.getCity() + "," + worldweather.location.getCountry());
            mCondDescr.setText(worldweather.currentCondition.getWeatherDesc());
            mTemp.setText(worldweather.currentCondition.getTempCur() + getString(R.string.common_celsius));
            mHum.setText("" + worldweather.currentCondition.getHumidity() + getString(R.string.common_percent));
            mPress.setText("" + worldweather.currentCondition.getPressure() + " " + getString(R.string.common_hpa));
            int iWindSpeed = worldweather.currentCondition.getWindspeed() * 1000/ 3600;
            mWindSpeed.setText(iWindSpeed + " " + getString(R.string.common_speed));
            mWindDeg.setText("" + worldweather.currentCondition.getWindDir() + getString(R.string.common_deg));

//            mCityText.setVisibility(View.VISIBLE);
            mCondDescr.setVisibility(View.VISIBLE);
            mTemp.setVisibility(View.VISIBLE);
            mHum.setVisibility(View.VISIBLE);
            mPress.setVisibility(View.VISIBLE);
            mWindSpeed.setVisibility(View.VISIBLE);
            mWindDeg.setVisibility(View.VISIBLE);

            mImgView.setVisibility(View.VISIBLE);
            mWeatherPressTitle.setVisibility(View.VISIBLE);
            mWindTitle.setVisibility(View.VISIBLE);
            mHumTitle.setVisibility(View.VISIBLE);

            Log.e(TAG, "" + worldweather.area.getAreaName());
            Log.e(TAG, "" + worldweather.area.getCountry());
            Log.e(TAG, "" + worldweather.area.getRegion());
         }
    }
}