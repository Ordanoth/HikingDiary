package com.wanderers.redneck.hikingdiary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.wanderers.redneck.hikingdiary.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Locale;

/**
 * Created by Ari Iivari on 17.3.2015.
 */
public class ActivitySettings extends Activity implements OnClickListener {

    public static final String TAG = "HikingDiary";
    private static String DB_BU_PATH = "/HikingDiary/backups/";
    private static String DB_PATH = "/Android/data/com.wanderers.redneck.hikingdiary/databases/";
    private static final String DATABASE_NAME = "hikingdiary.db";

    private Spinner mSpinner;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prefs = this.getSharedPreferences("com.wanderers.redneck.hikingdiary", Context.MODE_PRIVATE);
        setTitle(getString(R.string.settings_label));
        initViews();

    }

    private void initViews() {
        File direct = new File(Environment.getExternalStorageDirectory().getPath() + DB_BU_PATH );

        if(!direct.exists())
        {
            if(direct.mkdirs())
            {
            }
        }
        Button mBtnImport = (Button) findViewById(R.id.btn_import_db);
        Button mBtnExport = (Button) findViewById(R.id.btn_export_db);
        mBtnExport.setOnClickListener(this);
        mBtnImport.setOnClickListener(this);

        this.mSpinner = (Spinner) findViewById(R.id.langSpinner);


        Button mSaveButton = (Button) findViewById(R.id.btn_change_language);
        mSaveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_export_db:
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    if (sd.canWrite()) {
                        String  currentDBPath= DB_PATH + DATABASE_NAME;
                        String backupDBPath  = DB_BU_PATH + DATABASE_NAME;
                        File currentDB = new File(sd, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Toast.makeText(this, "Database exported!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
                break;
            case R.id.btn_import_db:
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    if (sd.canWrite()) {
                        String  currentDBPath= DB_PATH + DATABASE_NAME;
                        String backupDBPath  = DB_BU_PATH + DATABASE_NAME;
                        File  backupDB= new File(sd, currentDBPath);
                        File currentDB  = new File(sd, backupDBPath);

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();

                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                        Toast.makeText(this, "Database imported!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                break;
            case R.id.btn_change_language:
//                Log.e(TAG, "Spinner 1 : " + String.valueOf(mSpinner.getSelectedItem()));
                String strLang = "en";
                String strValLang = String.valueOf(mSpinner.getSelectedItem());
                if(strValLang.equals("Suomi")){
                    strLang = "fi";
                }
                SharedPreferences.Editor edit  = prefs.edit();
                edit.putString("rw_language", strLang);
                edit.commit();
                Locale locale = new Locale(strLang);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Toast.makeText(this, "Kieli on vaihdettu " + strLang + "!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
