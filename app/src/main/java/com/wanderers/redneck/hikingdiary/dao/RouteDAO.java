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
public class RouteDAO implements Serializable {
    public static final String TAG = "HikingDiary";
    private static final long serialVersionUID = -7406082437623008161L;

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { DBHelper.COLUMN_ROUTE_ID,
            DBHelper.COLUMN_ROUTE_NAME, DBHelper.COLUMN_ROUTE_DATE};

    public RouteDAO(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        try {
            open();
        } catch (SQLException e) {
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

    public Route createRoute(String name, String date) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ROUTE_NAME, name);
        values.put(DBHelper.COLUMN_ROUTE_DATE, date);
        long insertId = mDatabase
                .insert(DBHelper.TABLE_ROUTES, null, values);
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ROUTES, mAllColumns,
                DBHelper.COLUMN_ROUTE_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Route newRoute = cursorToRoute(cursor);
        cursor.close();
        return newRoute;
    }

    public void deleteRoute(Route route) {
        long id = route.getId();
        PointDAO pointDao = new PointDAO(mContext);
        List<Point> listPoints = pointDao.getPointsOfRoute(id);
        if (listPoints != null && !listPoints.isEmpty()) {
            for (Point e : listPoints) {
                pointDao.deletePoint(e);
            }
        }
        pointDao.close();
        mDatabase.delete(DBHelper.TABLE_ROUTES, DBHelper.COLUMN_ROUTE_ID + " = " + id, null);
    }

    public List<Route> getAllRoutes() {
        List<Route> listRoutes = new ArrayList<Route>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_ROUTES, mAllColumns,  null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Route route = cursorToRoute(cursor);
                listRoutes.add(route);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listRoutes;
    }

    public Route getRouteById(long id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_ROUTES, mAllColumns, DBHelper.COLUMN_ROUTE_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursorToRoute(cursor);
    }

    protected Route cursorToRoute(Cursor cursor) {
        Route route = new Route();
        route.setId(cursor.getLong(0));
        route.setName(cursor.getString(1));
        route.setDate(cursor.getString(2));
        return route;
    }
}
