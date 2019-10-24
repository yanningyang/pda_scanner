package com.shinow.pda_scanner;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry;

public class PdaScannerPlugin implements EventChannel.StreamHandler {
    private static final String CHANNEL = "com.shinow.pda_scanner/plugin";
    private static final String ZEBRA_SCAN_ACTION = "com.jac.mc36.SCANNER";
    

    private static EventChannel.EventSink eventSink;

    private static final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("PdaScannerPlugin", intent.getAction());
            if (intent.getAction().contentEquals(ZEBRA_SCAN_ACTION)) {
                eventSink.success(intent.getStringExtra("data"));
            } else if (intent.getAction().contentEquals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                Log.i(intent.getAction(), "" + status);
//                 if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
//                     eventSink.error("UNAVAILABLE", "Charging status unavailable", null);
//                 } else {
//                     boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                             status == BatteryManager.BATTERY_STATUS_FULL;
//                     eventSink.success(isCharging ? "charging" : "discharging");
//                 }
            } else if (intent.getAction().contentEquals(Intent.ACTION_TIME_TICK)) {
                eventSink.success("time");
            } else {
                Log.i("PdaScannerPlugin", "NoSuchAction");
            }
        }
    };

    private PdaScannerPlugin(Activity activity) {
        IntentFilter zebraIntentFilter = new IntentFilter();
        zebraIntentFilter.addAction(ZEBRA_SCAN_ACTION);
        zebraIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, zebraIntentFilter);
                
        IntentFilter batteryIntentFilter = new IntentFilter();
        batteryIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, batteryIntentFilter);
                        
        IntentFilter timeIntentFilter = new IntentFilter();
        timeIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        timeIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, timeIntentFilter);
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        EventChannel channel = new EventChannel(registrar.messenger(), CHANNEL);
        PdaScannerPlugin plugin = new PdaScannerPlugin(registrar.activity());
        channel.setStreamHandler(plugin);
        Log.i("PdaScannerPlugin", "PdaScannerPlugin:registerWith");
    }

    @Override
    public void onListen(Object o, final EventChannel.EventSink eventSink) {
        PdaScannerPlugin.eventSink = eventSink;
    }

    @Override
    public void onCancel(Object o) {
        Log.i("PdaScannerPlugin", "PdaScannerPlugin:onCancel");
    }
}
