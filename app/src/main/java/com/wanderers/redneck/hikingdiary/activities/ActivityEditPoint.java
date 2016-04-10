package com.wanderers.redneck.hikingdiary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.adapter.LocationAdapter;
import com.wanderers.redneck.hikingdiary.dao.PointDAO;
import com.wanderers.redneck.hikingdiary.dao.RouteDAO;
import com.wanderers.redneck.hikingdiary.model.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by Ari Iivari on 26.2.2015.
 */
public class ActivityEditPoint extends Activity implements OnClickListener {

    public static final String TAG = "HikingDiary";
    public static final String PHOTO_PATH = "/HikingDiary/photos";
    public static final String EXTRA_SELECTED_POINT_ID = "extra_key_selected_point_id";
    public static final int REQUEST_CODE_EDIT_POINT = 51;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;

    private EditText mTxtPointName;
    private TextView mTxtPointDate;
    private EditText mTxtDesc;
    private TextView mTxtPointLAT;
    private TextView mTxtPointLON;
    private ImageView mTxtPointImage;
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    private TextView mCondDescr, mTemp, mHum, mPress, mWindSpeed, mWindDeg, mWeatherTitle, mWeatherPressTitle, mWindTitle, mHumTitle;
    private ImageView mImgView;

    private RouteDAO mRouteDao;
    private PointDAO mPointDao;

    private Point mPoint;
    private long mPointId = -1;

    LocationAdapter mGetCoordinates;

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
        setContentView(R.layout.activity_edit_point);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViews();

        Intent intent  = getIntent();
        if(intent != null) {
            this.mPointId = intent.getLongExtra(EXTRA_SELECTED_POINT_ID, -1);
        }

        this.mRouteDao = new RouteDAO(this);
        this.mPointDao = new PointDAO(this);

        mPointDao = new PointDAO(this);
        mPoint = new Point();
        mPoint = mPointDao.getPointByPointId(mPointId);
        populateView(mPoint);

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
                String point_date = mTxtPointDate.getText().toString();
                String point_name = mTxtPointName.getText().toString();
                String pointDesc = mTxtDesc.getText().toString();
                String pointImage = mTxtPointImage.getTag().toString();
                Log.e(TAG, point_name + " " + point_date + " " + pointDesc);

                if (!TextUtils.isEmpty(point_name) && !TextUtils.isEmpty(point_date)
                        && !TextUtils.isEmpty(pointDesc)) {
                    mPointDao.updatePoint(point_name, point_date, pointDesc, pointImage, mPoint.getId());
                    setResult(RESULT_OK);

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
        this.mTxtPointLAT = (TextView) findViewById(R.id.edit_txt_point_add_lat_text);
        this.mTxtPointLON = (TextView) findViewById(R.id.edit_txt_point_add_lon_text);
        this.mTxtPointImage = (ImageView) findViewById(R.id.image_point_add);

        Calendar calendar = Calendar.getInstance();
        this.mSelectedYear = calendar.get(Calendar.YEAR);
        this.mSelectedMonth = calendar.get(Calendar.MONTH);
        this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateUI();

        this.mTxtPointDate.setOnClickListener(this);
        this.mTxtPointImage.setOnClickListener(this);

        //Weather
        this.mWeatherTitle = (TextView) findViewById(R.id.weatherTitle);
//        this.mCityText = (TextView) findViewById(R.id.stationText);
        this.mCondDescr = (TextView) findViewById(R.id.condDescr);
        this.mTemp = (TextView) findViewById(R.id.temp);
        this.mHum = (TextView) findViewById(R.id.hum);
        this.mPress = (TextView) findViewById(R.id.press);
        this.mWindSpeed = (TextView) findViewById(R.id.windSpeed);
        this.mWindDeg = (TextView) findViewById(R.id.windDeg);
        this.mImgView = (ImageView) findViewById(R.id.condIcon);
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
            case R.id.image_point_add:
                selectImage();
                break;
            default:
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.common_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditPoint.this);
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

    private void populateView(Point mPoint) {
        this.mTxtPointName.setText(mPoint.getPointName());
        this.mTxtPointDate.setText(mPoint.getPointDate());
        File imgFile = new  File(mPoint.getPointImage());

        if(imgFile.exists()){
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), btmapOptions);
            int nh = (int) ( myBitmap.getHeight() * (512.0 / myBitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 512, nh, true);
            this.mTxtPointImage.setTag(String.valueOf(imgFile));
            this.mTxtPointImage.setImageBitmap(scaled);
        }
        this.mTxtPointLAT.setText(mPoint.getPointLAT());
        this.mTxtPointLON.setText(mPoint.getPointLON());
        this.mTxtDesc.setText(mPoint.getPointDesc());

//        this.mCityText.setText(mPoint.getWeatherLocationName() +","+ mPoint.getWeatherLocationCountry());
        this.mCondDescr.setText(mPoint.getWeatherMain()+ "("+mPoint.getWeatherDesc()+")");
        float fTemp = Float.parseFloat(mPoint.getWeatherTemp());
        this.mTemp.setText(""+ Math.round(fTemp)+getString(R.string.common_celsius));
        this.mHum.setText(mPoint.getWeatherHum() + getString(R.string.common_percent));
        this.mPress.setText(mPoint.getWeatherPres() + " " + getString(R.string.common_hpa));
        this.mWindSpeed.setText(mPoint.getWeatherWindspeed() + getString(R.string.common_speed));
        float fDeg = Float.parseFloat(mPoint.getWeatherWindDeg());
        int iDeg = (int) fDeg;
        this.mWindDeg.setText(""+ iDeg + getString(R.string.common_deg));
        byte[] blob = mPoint.getWeatherIcon();
        this.mImgView.setImageBitmap(getPhoto(blob));

        this.mWeatherTitle.setVisibility(View.VISIBLE);
//        this.mCityText.setVisibility(View.VISIBLE);
        this.mCondDescr.setVisibility(View.VISIBLE);
        this.mTemp.setVisibility(View.VISIBLE);
        this.mHum.setVisibility(View.VISIBLE);
        this.mPress.setVisibility(View.VISIBLE);
        this.mWindSpeed.setVisibility(View.VISIBLE);
        this.mWindDeg.setVisibility(View.VISIBLE);
        this.mImgView.setVisibility(View.VISIBLE);
        this.mWeatherPressTitle.setVisibility(View.VISIBLE);
        this.mWindTitle.setVisibility(View.VISIBLE);
        this.mHumTitle.setVisibility(View.VISIBLE);
    }

    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_EDIT_POINT) {
            if (resultCode == RESULT_OK) {
            }
        }
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
                OutputStream fOut;
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

            String tempPath = getPath(selectedImageUri, ActivityEditPoint.this);

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

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void showAbout() {
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

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
}