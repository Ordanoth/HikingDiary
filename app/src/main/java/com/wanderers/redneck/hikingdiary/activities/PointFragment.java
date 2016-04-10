package com.wanderers.redneck.hikingdiary.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.dao.PointDAO;
import com.wanderers.redneck.hikingdiary.model.Point;

import java.io.File;

/**
 * Created by Ari Iivari on 20.3.2015.
 */
public class PointFragment extends Fragment implements OnClickListener{
    public static final String TAG = "HikingDiary";
    private static final String KEY_POSITION="position";
    public static final int REQUEST_CODE_ADD_POINT = 50;

    private ImageView mPointImage;
    private TextView mTxtPointDetailsName, mTxtPointDetailsDate, mTxtPointDetailsLat, mTxtPointDetailsLon, mTxtPointDetailsDesc;
    private ImageButton btnOpenPointLocation;

    private TextView mCondDescr, mTemp, mHum, mPress, mWindSpeed, mWindDeg, mMoreWeatherDetails;
    private ImageView mImgView;

    private Point mPoint;

    public static PointFragment newInstance(Point point){
        PointFragment pFrag = new PointFragment();
        Bundle args = new Bundle();

        args.putSerializable(KEY_POSITION, point);
        pFrag.setArguments(args);
        return(pFrag);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_point_details, container, false);
        mPoint = (Point) getArguments().getSerializable(KEY_POSITION);
        setHasOptionsMenu(true);

        initViews(view);
        populateView(mPoint);
        return view;
    }

    private void initViews(View v){
        this.mTxtPointDetailsName = (TextView) v.findViewById(R.id.txt_point_details_name);
        this.mTxtPointDetailsDate = (TextView) v.findViewById(R.id.txt_point_details_date);
        this.mPointImage = (ImageView) v.findViewById(R.id.image_point_details);
        this.mTxtPointDetailsLat = (TextView) v.findViewById(R.id.edit_txt_lat_text);
        this.mTxtPointDetailsLon = (TextView) v.findViewById(R.id.edit_txt_lon_text);
        this.mTxtPointDetailsDesc = (TextView) v.findViewById(R.id.edit_txt_point_desc);
        this.btnOpenPointLocation = (ImageButton) v.findViewById(R.id.btn_get_location);
        this.btnOpenPointLocation.setOnClickListener(this);
        this.mPointImage.setOnClickListener(this);

        //Weather
//        this.mCityText = (TextView) v.findViewById(R.id.stationText);
        this.mCondDescr = (TextView) v.findViewById(R.id.condDescr);
        this.mTemp = (TextView) v.findViewById(R.id.temp);
        this.mHum = (TextView) v.findViewById(R.id.hum);
        this.mPress = (TextView) v.findViewById(R.id.press);
        this.mWindSpeed = (TextView) v.findViewById(R.id.windSpeed);
        this.mWindDeg = (TextView) v.findViewById(R.id.windDeg);
        this.mImgView = (ImageView) v.findViewById(R.id.condIcon);
        this.mMoreWeatherDetails = (TextView) v.findViewById(R.id.txt_more_weather);
        this.mMoreWeatherDetails.setOnClickListener(this);
    }

    private void populateView(Point mPoint) {
        this.mTxtPointDetailsName.setText(mPoint.getPointName());
        this.mTxtPointDetailsDate.setText(mPoint.getPointDate());
        File imgFile = new  File(mPoint.getPointImage());

        if(imgFile.exists()){
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), btmapOptions);
            int nh = (int) ( myBitmap.getHeight() * (512.0 / myBitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 512, nh, true);
            this.mPointImage.setImageBitmap(scaled);
        }
        this.mTxtPointDetailsLat.setText(mPoint.getPointLAT());
        this.mTxtPointDetailsLon.setText(mPoint.getPointLON());
        this.mTxtPointDetailsDesc.setText(mPoint.getPointDesc());
        this.mTxtPointDetailsDesc.setMovementMethod(new ScrollingMovementMethod());

//        this.mCityText.setText("");
        this.mCondDescr.setText(mPoint.getWeatherDesc());
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
    }

    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Point mPoint = (Point) getArguments().getSerializable(KEY_POSITION);
        switch (v.getId()) {
            case R.id.btn_get_location:
                Uri location = Uri.parse("geo:"+mPoint.getPointLAT()+","+mPoint.getPointLON()+"?z14");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
                break;
            case R.id.image_point_details:
                Intent i = new Intent(getActivity(), ActivityImageview.class);
                i.putExtra("image_url", mPoint.getPointImage());
                startActivity(i);
                break;
            case R.id.txt_more_weather:
                Intent intent = new Intent(getActivity(), ActivityWeatherDetails.class);
                intent.putExtra("weather_point", mPoint.getId());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(
        Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.point_details_actionbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                showAbout();
                break;
            case R.id.action_edit:
                Point mPoint = (Point) getArguments().getSerializable(KEY_POSITION);
                Intent intent = new Intent(getActivity(), ActivityEditPoint.class);
                intent.putExtra(ActivityEditPoint.EXTRA_SELECTED_POINT_ID, mPoint.getId());
                startActivityForResult(intent, REQUEST_CODE_ADD_POINT);
                break;
            default:
                break;
        }
        return true;
    }

    protected void showAbout() {
        // Inflate the about message contents
        View messageView = getActivity().getLayoutInflater().inflate(R.layout.about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.app_icon);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ADD_POINT) {
            PointDAO mPointDao = new PointDAO(getActivity());
            mPoint = mPointDao.getPointByPointId(mPoint.getId());
            populateView(mPoint);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
