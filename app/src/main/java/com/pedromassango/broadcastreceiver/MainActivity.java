package com.pedromassango.broadcastreceiver;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private ComponentName componentName;
    private PackageManager packageManager;
    private CustomBroadCastReceiver receiver = new CustomBroadCastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button show notification click
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showNotification();
            }
        });

        // Use LocalBroadcast to use it only on this app
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter(CustomBroadCastReceiver.SHOW_NOTIFICATION));
    }

    public void onStart() {
        super.onStart();

        componentName = new ComponentName(this, CustomBroadCastReceiver.class);
        packageManager = getPackageManager();

        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onStop() {
        super.onStop();

        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onDestroy() {

        // Unregister the receiver
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(receiver);

        super.onDestroy();
    }

    public void showNotification() {
        String message = ((EditText) findViewById(R.id.edt_message)).getText().toString();

        Intent i = new Intent(CustomBroadCastReceiver.SHOW_NOTIFICATION);
        i.putExtra("message", message);

        LocalBroadcastManager.getInstance(this).
                sendBroadcast(i);

    }
}
