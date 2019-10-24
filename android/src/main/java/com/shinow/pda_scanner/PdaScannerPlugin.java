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
    private static final String XM_SCAN_ACTION = "com.android.server.scannerservice.broadcast";
    private static final String IDATA_SCAN_ACTION = "android.intent.action.SCANRESULT";
    private static final String YBX_SCAN_ACTION = "android.intent.ACTION_DECODE_DATA";
    private static final String BARCODE_DATA_ACTION = "com.ehsy.warehouse.action.BARCODE_DATA";
    private static final String ZEBRA_SCAN_ACTION = "com.symbol.scanconfig.SCANDEMO";
    

    private static EventChannel.EventSink eventSink;

    private static final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contentEquals(XM_SCAN_ACTION)) {
                eventSink.success(intent.getStringExtra("scannerdata"));
            } else if (intent.getAction().contentEquals(IDATA_SCAN_ACTION)) {
                eventSink.success(intent.getStringExtra("value"));
            } else if (intent.getAction().contentEquals(YBX_SCAN_ACTION)) {
                eventSink.success(intent.getStringExtra("barcode"));
            } else if (intent.getAction().contentEquals(BARCODE_DATA_ACTION)) {
                eventSink.success(intent.getStringExtra("data"));
            } else if (intent.getAction().contentEquals(ZEBRA_SCAN_ACTION)) {
                eventSink.success(intent.getStringExtra("data"));
                Log.i("PdaScannerPlugin", intent.getAction());
            } else if (intent.getAction().contentEquals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                    eventSink.error("UNAVAILABLE", "Charging status unavailable", null);
                } else {
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL;
                    eventSink.success(isCharging ? "charging" : "discharging");
                }
            } else {
                Log.i("PdaScannerPlugin", "NoSuchAction");
            }
        }
    };

    private PdaScannerPlugin(Activity activity) {
        IntentFilter xmIntentFilter = new IntentFilter();
        xmIntentFilter.addAction(XM_SCAN_ACTION);
        xmIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, xmIntentFilter);

        IntentFilter iDataIntentFilter = new IntentFilter();
        iDataIntentFilter.addAction(IDATA_SCAN_ACTION);
        iDataIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, iDataIntentFilter);

        IntentFilter yBoXunIntentFilter = new IntentFilter();
        yBoXunIntentFilter.addAction(IDATA_SCAN_ACTION);
        yBoXunIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, yBoXunIntentFilter);

        IntentFilter honeyIntentFilter = new IntentFilter();
        honeyIntentFilter.addAction(BARCODE_DATA_ACTION);
        honeyIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, honeyIntentFilter);
        
        IntentFilter zebraIntentFilter = new IntentFilter();
        zebraIntentFilter.addAction(ZEBRA_SCAN_ACTION);
        zebraIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, zebraIntentFilter);
                
        IntentFilter batteryIntentFilter = new IntentFilter();
        batteryIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, batteryIntentFilter);
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
