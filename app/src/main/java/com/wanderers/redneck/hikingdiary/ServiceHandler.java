package com.wanderers.redneck.hikingdiary;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Ari Iivari on 6.4.2015.
 */
public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    private static String TAG = "HikingDiary";

    public ServiceHandler() {

    }

    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public byte[] getImage(String code) {
        HttpURLConnection con = null ;
        InputStream is = null;
        Log.e("HikingDiary", "Ikoni " + code);
        try {
            con = (HttpURLConnection) ( new URL(code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();

            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch(Throwable t) {Log.e(TAG, "" + t);}
            try {
                con.disconnect();
            } catch(Throwable t) {Log.e(TAG, "" + t);}
        }

        return null;

    }
}
