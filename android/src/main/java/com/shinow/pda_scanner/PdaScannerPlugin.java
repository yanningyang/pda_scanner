package com.shinow.pda_scanner;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Set;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry;

public class PdaScannerPlugin implements EventChannel.StreamHandler {
    private static final String CHANNEL = "com.shinow.pda_scanner/plugin";
    private static final String ZEBRA_SCAN_ACTION = "com.symbol.scanconfig.SCANDEMO";
    private static final String ZEBRA_SCAN_CATEGORY = "com.symbol.category.DEFAULT";
    

    private static EventChannel.EventSink eventSink;

    private static final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contentEquals(ZEBRA_SCAN_ACTION)) {
                eventSink.success(intent.getStringExtra("com.symbol.scanconfig.decode_data"));
            } else {
                Log.i("PdaScannerPlugin", "NoSuchAction");
            }
        }
    };

    private PdaScannerPlugin(Context context) {
        IntentFilter zebraIntentFilter = new IntentFilter();
        zebraIntentFilter.addAction(ZEBRA_SCAN_ACTION);
        zebraIntentFilter.addCategory(ZEBRA_SCAN_CATEGORY);
        zebraIntentFilter.setPriority(Integer.MAX_VALUE);
        context.registerReceiver(scanReceiver, zebraIntentFilter);
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        EventChannel channel = new EventChannel(registrar.messenger(), CHANNEL);
        PdaScannerPlugin plugin = new PdaScannerPlugin(registrar.context());
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
