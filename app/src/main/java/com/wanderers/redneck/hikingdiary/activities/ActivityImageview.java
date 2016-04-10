package com.wanderers.redneck.hikingdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;

import com.wanderers.redneck.hikingdiary.R;

/**
 * Created by Ari Iivari on 31.3.2015.
 */
public class ActivityImageview extends Activity {

    public static final String TAG = "HikingDiary";

    private WebView mImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle(R.string.point_image_title);

        mImageView = (WebView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        String mImagePath = intent.getExtras().getString("image_url");
        Log.e(TAG, mImagePath);

        mImageView.getSettings().setLoadWithOverviewMode(true);
        mImageView.getSettings().setUseWideViewPort(true);
        mImageView.setBackgroundColor(16777216);
        mImageView.getSettings().setSupportZoom(true);
        mImageView.getSettings().setBuiltInZoomControls(true);
        mImageView.getSettings().setDisplayZoomControls(false);
        mImageView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mImageView.setScrollbarFadingEnabled(false);
//        mImageView.getSettings().setDefaultZoom(ZoomDensity.FAR);

        mImageView.loadUrl("file:///" + mImagePath);

//        Bitmap bm = BitmapFactory.decodeFile(mImagePath);
//
//        this.mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        this.mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        this.mImageView.setImageBitmap(bm);
    }
}
