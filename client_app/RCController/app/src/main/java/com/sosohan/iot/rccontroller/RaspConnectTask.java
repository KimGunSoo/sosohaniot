package com.sosohan.iot.rccontroller;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by KimGunSoo on 2015-06-05.
 */
public class RaspConnectTask extends AsyncTask<String, Integer, String>{
    private String ipString;
    private String portString;

    private static final String TAG = "RaspConnectTask";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        ipString = params[0];
        portString = params[1];

        Log.d(TAG, "Enter AsyncTask to connect remote server (" + ipString + ":" + portString + ")");

        URL url = null;

        try {
            url = new URL(ipString + ":" +portString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Unavailable internet address.";
        }

        return "Complete connections";
    }
}
