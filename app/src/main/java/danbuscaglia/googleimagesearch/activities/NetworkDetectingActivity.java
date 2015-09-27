package danbuscaglia.googleimagesearch.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import danbuscaglia.googleimagesearch.R;
import danbuscaglia.googleimagesearch.helpers.ConnectionHelper;

public class NetworkDetectingActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private boolean isPaused;

    //toolbar status bar for no internet connection message
    private RelativeLayout errorsBar;

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        if(findViewById(R.id.toolbar_sub_error_bar) != null) {
            errorsBar = (RelativeLayout)findViewById(R.id.toolbar_sub_error_bar);
            errorsBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideErrorsBar(true);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(CONNECTIVITY_CHANGE_ACTION);
        this.registerReceiver(mChangeConnectionReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChangeConnectionReceiver != null) {
            this.unregisterReceiver(mChangeConnectionReceiver);
        }
    }

    private final BroadcastReceiver mChangeConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (CONNECTIVITY_CHANGE_ACTION.equals(action) && !isPaused) {
                //check internet connection
                if (!ConnectionHelper.isConnectedOrConnecting(getApplicationContext())) {
                    if (context != null) {
                        boolean show = false;
                        if(ConnectionHelper.lastNoConnectionTs == -1) {//first time
                            show = true;
                            ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                        } else {
                            if(System.currentTimeMillis() - ConnectionHelper.lastNoConnectionTs > 1000) {
                                show = true;
                                ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                            }
                        }

                        if(show && ConnectionHelper.isOnline) {
                            hideErrorsBar(false);
                            ConnectionHelper.isOnline = false;
                        }
                    }
                } else {
                    hideErrorsBar(true);
                    ConnectionHelper.isOnline = true;
                }
            }
        }
    };

    public void hideErrorsBar(boolean hide) {
        try {
            //try to find alert bar view
            View errorBar = findViewById(R.id.toolbar_sub_error_bar);
            if (errorBar != null) {
                if (hide) {
                    errorBar.setVisibility(View.GONE);
                } else {
                    errorBar.setVisibility(View.VISIBLE);
                }
            }
        } catch(Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
        }
    }
}
