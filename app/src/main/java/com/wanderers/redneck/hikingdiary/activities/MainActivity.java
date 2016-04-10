package com.wanderers.redneck.hikingdiary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.adapter.ListRoutesAdapter;
import com.wanderers.redneck.hikingdiary.dao.RouteDAO;
import com.wanderers.redneck.hikingdiary.model.Route;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ari Iivari on 1.3.2015.
 */
public class MainActivity extends Activity  implements OnItemLongClickListener, OnItemClickListener {

    public static final String TAG = "HikingDiary";

    public static final int REQUEST_CODE_ADD_ROUTE = 40;
    public static final int REQUEST_CODE_IMPORT_DB = 41;

    public static final String EXTRA_ADDED_ROUTE = "extra_key_added_route";

    private ListView mListViewRoutes;
    private TextView mTxtEmptyListRoutes;

    private ListRoutesAdapter mAdapter;
    private List<Route> mListRoutes;
    private RouteDAO mRouteDao;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prefs = this.getSharedPreferences("com.wanderers.redneck.hikingdiary", Context.MODE_PRIVATE);
        Log.e(TAG, "SP = " + prefs.getString("rw_language", null));
        if(prefs.getString("rw_language", null) == null){
            SharedPreferences.Editor edit  = prefs.edit();
            edit.putString("rw_language", "en");
            edit.commit();
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        } else {
            Locale locale = new Locale(prefs.getString("rw_language", null));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        initViews();
        setTitle(R.string.routelist_activity);

        String DB_FOLDER = "/Android/data/com.wanderers.redneck.hikingdiary/databases";
        File direct = new File(Environment.getExternalStorageDirectory().getPath() + DB_FOLDER);

        if(!direct.exists())
        {
            Log.e(TAG, DB_FOLDER + " ei ole olemassa");
            if(direct.mkdirs())
            {
                Log.d(TAG, DB_FOLDER + " LUOTU!!!");
            }
        }

        mRouteDao = new RouteDAO(this);
        mListRoutes = mRouteDao.getAllRoutes();
        if(mListRoutes != null && !mListRoutes.isEmpty()) {
            mAdapter = new ListRoutesAdapter(this, mListRoutes);
            mListViewRoutes.setAdapter(mAdapter);
        }
        else {
            mTxtEmptyListRoutes.setVisibility(View.VISIBLE);
            mListViewRoutes.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actionbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, ActivityAddRoute.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ROUTE);
                break;
            case R.id.action_info:
                showAbout();
                break;
            case R.id.action_tools:
                intent = new Intent(this, ActivitySettings.class);
                startActivityForResult(intent, REQUEST_CODE_IMPORT_DB);
                break;
            default:
                break;
        }

        return true;
    }


    private void initViews() {
        this.mListViewRoutes = (ListView) findViewById(R.id.lvTrips);
        this.mTxtEmptyListRoutes = (TextView) findViewById(R.id.txt_empty_list_routes);
        this.mListViewRoutes.setOnItemClickListener(this);
        this.mListViewRoutes.setOnItemLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ADD_ROUTE || requestCode == REQUEST_CODE_IMPORT_DB) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    Route createdRoute = (Route) data.getSerializableExtra(EXTRA_ADDED_ROUTE);
                    if(createdRoute != null) {
                        if(mListRoutes == null)
                            mListRoutes = new ArrayList<Route>();
                        mListRoutes.add(createdRoute);
                        if(mListViewRoutes.getVisibility() != View.VISIBLE) {
                            mListViewRoutes.setVisibility(View.VISIBLE);
                            mTxtEmptyListRoutes.setVisibility(View.GONE);
                        }
                        if(mAdapter == null) {
                            mAdapter = new ListRoutesAdapter(this, mListRoutes);
                            mListViewRoutes.setAdapter(mAdapter);
                        } else {
                            mAdapter.setItems(mListRoutes);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Route clickedRoute = mAdapter.getItem(position);
        Log.d(TAG, "clickedItem : " + clickedRoute.getName());
        Intent intent = new Intent(this, PointsActivity.class);
        intent.putExtra(PointsActivity.EXTRA_SELECTED_ROUTE_ID, clickedRoute.getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Route clickedRoute = mAdapter.getItem(position);
        Log.d(TAG, "longClickedItem : "+clickedRoute.getName());
        showDeleteDialogConfirmation(clickedRoute);
        return true;
    }

    private void showDeleteDialogConfirmation(final Route clickedRoute) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete the \"" + clickedRoute.getName() + "\" route ?");

        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mRouteDao != null) {
                    mRouteDao.deleteRoute(clickedRoute);
                    mListRoutes.remove(clickedRoute);

                    if (mListRoutes.isEmpty()) {
                        mListRoutes = null;
                        mListViewRoutes.setVisibility(View.GONE);
                        mTxtEmptyListRoutes.setVisibility(View.VISIBLE);
                    }
                    mAdapter.setItems(mListRoutes);
                    mAdapter.notifyDataSetChanged();
                }

                dialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.route_deleted_successfully, Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    protected void onDestroy() {
        super.onDestroy();
        mRouteDao.close();
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