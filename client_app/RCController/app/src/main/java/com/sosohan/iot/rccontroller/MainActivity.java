package com.sosohan.iot.rccontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity{

    //private static final String TAG = "MainActivity";
    private static final String TAG = "gunsoo";

    private EditText ipEt;
    private EditText portEt;
    private EditText connectStateEt;
    private String ipString;
    private String portString;
    private Button conBtn;

    private static final String STR_CON_STATE_CONNECTED = "Complete connections.";
    private static final String STR_CON_STATE_UNAVAILABLE_IP = "Unavailable internet address.";
    private static final Integer CON_STATE_DISCONNECTED = 0;
    private static final Integer CON_STATE_CONNECTED = 1;
    private static final Integer CON_STATE_UNAVAILABLE_IP = 2;
    private static Integer conState = CON_STATE_DISCONNECTED;

    RaspConnectTask conTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipEt = (EditText)findViewById(R.id.ip_editText);
        portEt = (EditText)findViewById(R.id.port_editText);
        connectStateEt = (EditText)findViewById(R.id.connect_state_editText);
        conBtn = (Button)findViewById(R.id.connect_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Enter onResume(), conState="+conState);
        if (conState == CON_STATE_CONNECTED) {
            connectStateEt.setText("connected");
        } else {
            connectStateEt.setText("disconnected");
            conBtn.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickedConnectBtn(View view) {
        ipString = ipEt.getText().toString();
        portString = portEt.getText().toString();
        conBtn.setEnabled(false);

        Log.d(TAG, "connect to " + ipString + ":" + portString + "  [" + connectStateEt.getText().toString() + "]");

        connectStateEt.setText("connecting...");
        conTask = new RaspConnectTask();
        conTask.execute();
    }

    public void updateConnectStateUI() {
        Log.d(TAG, "Enter onResume(), conState="+conState);
        if (conState == CON_STATE_CONNECTED) {
            connectStateEt.setText("connected");
        } else {
            connectStateEt.setText("disconnected");
            conBtn.setEnabled(true);
        }
    }

    public class RaspConnectTask extends AsyncTask<String, Integer, Integer> {
        //private static final String TAG = "RaspConnectTask";
        private static final String TAG = "gunsoo";

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //Intent intent = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(intent);
            updateConnectStateUI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url = null;
            String urlString = "http://" + ipString + ":" + portString;
            Log.d(TAG, "Enter AsyncTask to connect remote server (" + urlString + ")");

            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                conState = CON_STATE_UNAVAILABLE_IP;
                return null;
            }

            conState = CON_STATE_CONNECTED;
            return null;
        }
    }
}
