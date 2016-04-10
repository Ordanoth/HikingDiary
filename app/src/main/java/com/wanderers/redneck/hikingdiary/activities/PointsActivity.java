package com.wanderers.redneck.hikingdiary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.adapter.ListPointsAdapter;
import com.wanderers.redneck.hikingdiary.dao.PointDAO;
import com.wanderers.redneck.hikingdiary.dao.RouteDAO;
import com.wanderers.redneck.hikingdiary.model.Point;
import com.wanderers.redneck.hikingdiary.model.Route;

import java.util.ArrayList;

/**
 * Created by Ari Iivari on 23.3.2015.
 */
public class PointsActivity extends Activity implements OnItemLongClickListener, OnItemClickListener {

    public static final String TAG = "HikingDiary";

    public static final int REQUEST_CODE_ADD_POINT = 41;
    public static final String EXTRA_SELECTED_ROUTE_ID = "extra_key_selected_route_id";

    private ListView mListviewPoints;
    private TextView mTxtEmptyListPoints, mTxtPointNameTitle, mTxtPointDateTitle;

    private ListPointsAdapter mAdapter;
    private ArrayList<Point> mListPoints;
    private PointDAO mPointDao;
    private RouteDAO mRouteDao;

    private long mRouteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poits_listview);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle(R.string.pointlist_activity);
        initViews();
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
                Intent intent = new Intent(this, ActivityAddPoint.class);
                intent.putExtra(ActivityAddPoint.EXTRA_SELECTED_ROUTE_ID, mRouteId);
                startActivityForResult(intent, REQUEST_CODE_ADD_POINT);
                break;
            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.action_info:
                showAbout();
                break;
            default:
                break;
        }

        return true;
    }

    private void openPointsList() {
        Intent intent  = getIntent();
        if(intent != null) {
            this.mRouteId = intent.getLongExtra(EXTRA_SELECTED_ROUTE_ID, -1);
        }
        if(mRouteDao == null){
            mRouteDao = new RouteDAO(this);
        }
        if(mPointDao == null){
            mPointDao = new PointDAO(this);
        }
        Route r = mRouteDao.getRouteById(mRouteId);
        mTxtPointNameTitle.setText(r.getName());
        mTxtPointDateTitle.setText(r.getDate());
        mRouteDao.close();
        mRouteDao = null;
        if(mRouteId != -1) {
            mListPoints = (ArrayList<Point>) mPointDao.getPointsOfRoute(mRouteId);
            if(mListPoints != null && !mListPoints.isEmpty()) {
                mAdapter = new ListPointsAdapter(this, mListPoints);
                mListviewPoints.setAdapter(mAdapter);
            } else {
                mTxtEmptyListPoints.setVisibility(View.VISIBLE);
                mListviewPoints.setVisibility(View.GONE);
            }
        }
    }

    private void initViews() {
        this.mListviewPoints = (ListView) findViewById(R.id.lvPoints);
        this.mTxtEmptyListPoints = (TextView) findViewById(R.id.txt_empty_list_points);
        this.mTxtPointNameTitle = (TextView) findViewById(R.id.pointsHeaderTitle);
        this.mTxtPointDateTitle = (TextView) findViewById(R.id.pointsHeaderDate);
        this.mListviewPoints.setOnItemClickListener(this);
        this.mListviewPoints.setOnItemLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ADD_POINT) {
            if(resultCode == RESULT_OK) {
                if(mListPoints == null || !mListPoints.isEmpty()) {
                    mListPoints = new ArrayList<Point>();
                }

                if(mPointDao == null){
                    mPointDao = new PointDAO(this);
                }
                mListPoints = (ArrayList<Point>) mPointDao.getPointsOfRoute(mRouteId);

                if(mListPoints != null && !mListPoints.isEmpty() &&
                        mListviewPoints.getVisibility() != View.VISIBLE) {
                    mTxtEmptyListPoints.setVisibility(View.GONE);
                    mListviewPoints.setVisibility(View.VISIBLE);
                }

                if(mAdapter == null) {
                    mAdapter = new ListPointsAdapter(this, mListPoints);
                    mListviewPoints.setAdapter(mAdapter);
                } else {
                    mAdapter.setItems(mListPoints);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PointsDetailActivity.class);
        intent.putExtra("SWIPEACTIVITY", mListPoints);
        intent.putExtra("SWIPEACTIVITYID", position);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Point clickedPoint = mAdapter.getItem(position);
        showDeleteDialogConfirmation(clickedPoint);
        return true;
    }

    private void showDeleteDialogConfirmation(final Point Point) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(getString(R.string.common_delete));
        alertDialogBuilder
                .setMessage(getString(R.string.delete_point_confirm) +" \""
                        + Point.getPointName() + " "
                        + Point.getPointDate() + "\"");

        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPointDao != null) {
                    mPointDao.deletePoint(Point);

                    mListPoints.remove(Point);
                    if (mListPoints.isEmpty()) {
                        mListviewPoints.setVisibility(View.GONE);
                        mTxtEmptyListPoints.setVisibility(View.VISIBLE);
                    }

                    mAdapter.setItems(mListPoints);
                    mAdapter.notifyDataSetChanged();
                }

                dialog.dismiss();
                Toast.makeText(PointsActivity.this, R.string.point_deleted_successfully, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPointDao.close();
        if(mRouteDao != null){
            mRouteDao.close();
        }
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
        openPointsList();
    }
}