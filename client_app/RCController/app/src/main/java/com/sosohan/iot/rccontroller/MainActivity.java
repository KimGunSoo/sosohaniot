package com.sosohan.iot.rccontroller;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class MainActivity extends Activity{

    private static final String TAG = "MainActivity";

    private EditText ipEt;
    private EditText portEt;
    private EditText connectStateEt;
    private String ipString;
    private String portString;
    private Button initBtn;
    private Button deinitBtn;
    private Button goBtn;
    private Button backBtn;

    private static final Integer STATE_DEINITIALIZED = 0;
    private static final Integer STATE_INITIALIZED = 1;
    private static final Integer STATE_UNKNOWN = 2;

    private static final Integer CONTROL_ID_INIT = 0;
    private static final Integer CONTROL_ID_DEINIT = 1;
    private static final Integer CONTROL_ID_GO = 2;
    private static final Integer CONTROL_ID_BACK = 3;
    private static final Integer CONTROL_ID_GETSTATE = 4;

    RaspControlTask raspTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipEt = (EditText)findViewById(R.id.ip_editText);
        portEt = (EditText)findViewById(R.id.port_editText);
        connectStateEt = (EditText)findViewById(R.id.connect_state_editText);
        initBtn = (Button)findViewById(R.id.init_button);
        deinitBtn = (Button)findViewById(R.id.deinit_button);
        goBtn = (Button)findViewById(R.id.go_button);
        backBtn = (Button)findViewById(R.id.back_button);

        goBtn.setEnabled(false);
        backBtn.setEnabled(false);
        deinitBtn.setEnabled(false);

        RaspState.setRaspState(STATE_DEINITIALIZED);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: query the raspState to remote.
        // 1.send getstate request
        //raspTask = new RaspControlTask();
        //raspTask.execute(new RaspCommand(CONTROL_ID_GETSTATE));
        // 2. update RaspState
        // RaspState.setRaspState(state);

        Log.d(TAG, "Enter onResume(), raspState="+RaspState.getRaspState()+"("+RaspState.getRaspStateStr()+")");
        updateUI();
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

    public void onClickedInitBtn(View view) {
        ipString = ipEt.getText().toString();
        portString = portEt.getText().toString();

        initBtn.setEnabled(false);
        ipEt.setEnabled(false);
        portEt.setEnabled(false);
        connectStateEt.setText("initializing...");

        Log.d(TAG, "onClickedInitBtn");
        Log.d(TAG, "rasp URL = " + ipString + ":" + portString);

        /* send init request */
        raspTask = new RaspControlTask();
        raspTask.execute(new RaspCommand(CONTROL_ID_INIT));
    }

    public void onClickedDeinitBtn(View view) {
        Log.d(TAG, "onClickedDeinitBtn");
        /* send go request */
        raspTask = new RaspControlTask();
        raspTask.execute(new RaspCommand(CONTROL_ID_DEINIT));
    }

    public void onClickedGoBtn(View view) {
        Log.d(TAG, "onClickedGoBtn");
        /* send go request */
        raspTask = new RaspControlTask();
        raspTask.execute(new RaspCommand(CONTROL_ID_GO));
    }

    public void onClickedBackBtn(View view) {
        Log.d(TAG, "onClickedBackBtn");
        /* send back request */
        raspTask = new RaspControlTask();
        raspTask.execute(new RaspCommand(CONTROL_ID_BACK));
    }

    private void updateUI() {
        connectStateEt.setText(RaspState.getRaspStateStr());
        if (RaspState.getRaspState() == STATE_INITIALIZED) {
            initBtn.setEnabled(false);
            deinitBtn.setEnabled(true);
            goBtn.setEnabled(true);
            backBtn.setEnabled(true);
            ipEt.setEnabled(false);
            portEt.setEnabled(false);
        } else {
            initBtn.setEnabled(true);
            deinitBtn.setEnabled(false);
            goBtn.setEnabled(false);
            backBtn.setEnabled(false);
            ipEt.setEnabled(true);
            portEt.setEnabled(true);
        }
    }

    public class RaspControlTask extends AsyncTask<RaspCommand, Integer, Integer> {
        private static final String TAG = "RaspControlTask";

        @Override
        protected Integer doInBackground(RaspCommand... params) {
            RaspCommand cmd = params[0];
            Log.d(TAG, "RaspControlTask : RaspCommand = " + cmd.getRaspCmd() +"(" +cmd.getRaspCmdStr()+")");

            if (cmd.getRaspCmdStr() == null) {
                Log.d(TAG, "Uknown controlID = " + cmd.getRaspCmd());
                return 0;
            }

            Integer respCode = requestRestCmd(cmd.getRaspCmdStr());
            publishProgress(cmd.getRaspCmd(), respCode);

            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, "onProgressUpdate controlID = " + values[0] +", response code = " + values[1]);
            if (values[1] == 200) {
                RaspState.setRaspState(STATE_INITIALIZED);
                updateUI();
            } else {
                RaspState.setRaspState(STATE_DEINITIALIZED);
                updateUI();
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        private Integer requestRestCmd(String cmd) {
            Log.d(TAG, "requestRestCmd : request = " + cmd);

            Integer respCode = -1;
            String restCmdUrlString = "http://" + ipString + ":" + portString + "/" +cmd;

            Log.d(TAG, "restCmdUrlString = " + restCmdUrlString);

            try {
                URL url = new URL(restCmdUrlString);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                httpCon.setConnectTimeout(3000);
                httpCon.setReadTimeout(3000);

                httpCon.connect();

                respCode = httpCon.getResponseCode();

                InputStreamReader isr = new InputStreamReader(httpCon.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    builder.append(str + "\n");
                }
                String resultStr = builder.toString();

                Log.d(TAG, "requestRestCmd : http response code = " + respCode + ", resultStr = " + resultStr);
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return respCode;
            }
            return respCode;
        }
    }

    public class RaspCommand {
        private Integer raspCmd = -1;
        private String raspCmdStr = null;

        public RaspCommand(Integer cmd) {
            raspCmd = cmd;
            if (raspCmd == CONTROL_ID_INIT) {
                raspCmdStr = "rasp_cmd_init";
            } else if (raspCmd == CONTROL_ID_DEINIT) {
                raspCmdStr = "rasp_cmd_deinit";
            } else if (raspCmd == CONTROL_ID_GO) {
                raspCmdStr = "rasp_cmd_go";
            } else if (raspCmd == CONTROL_ID_BACK) {
                raspCmdStr = "rasp_cmd_back";
            }
        }
        public Integer getRaspCmd() {
            return raspCmd;
        }
        public String getRaspCmdStr() {
            return raspCmdStr;
        }
    }

    public static class RaspState {
        private static Integer raspState = -1;
        private static String raspStateStr = null;

        private static final String STATE_DEINITIALIZED_STR = "Deinitialized";
        private static final String STATE_INITIALIZED_STR = "Initialized";
        private static final String STATE_UNKNOWN_STR = "Unknown State";

        public static void setRaspState(Integer state) {
            raspState = state;
            if (raspState == STATE_DEINITIALIZED) {
                raspStateStr = STATE_DEINITIALIZED_STR;
            } else if (raspState == STATE_INITIALIZED) {
                raspStateStr = STATE_INITIALIZED_STR;
            } else if (raspState == STATE_UNKNOWN) {
                raspStateStr = STATE_UNKNOWN_STR;
            }
        }
        public static Integer getRaspState() {
            return raspState;
        }
        public static String getRaspStateStr() {
            return raspStateStr;
        }
    }
}
