package ir.ac.ut.network;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ir.ac.ut.berim.BerimApplication;
import ir.ac.ut.berim.Constants;

/**
 * Created by Masood on 12/9/2015 AD.
 */
public abstract class ChatNetworkListner extends BroadcastReceiver {

    public abstract void onMessageReceived(JSONObject response);

    public abstract void onMessageErrorReceived(BerimNetworkException error);

    private boolean mRegistered;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Constants.ACTION_NETWORK_RESPONSE.equals(intent.getAction())) {
            try {
                String response = intent.getStringExtra(Constants.EXTRA_RESPONSE_BODY);
                if (BerimApplication.DEBUG) {
                    Log.e(NetworkReceiver.class.getSimpleName(),
                            "onReceive() :: SUCCESS :: response= " + response);
                }
                if (response != null) {
                    Class clazz = (Class) intent
                            .getSerializableExtra(Constants.EXTRA_RESPONSE_CLASS_TYPE);
                    try {
                        onMessageReceived(new JSONObject(response));
                    } catch (Exception e) {
                        e.printStackTrace();
                        onMessageErrorReceived(new BerimNetworkException());
                    }
                }
            } finally {
//                this.unregister();
            }

        } else if (Constants.ACTION_NETWORK_ERROR.equals(intent.getAction())) {
            try {
                int errorCode = intent.getIntExtra(Constants.EXTRA_ERROR_CODE, -1);
                String errors = intent.getStringExtra(Constants.EXTRA_ERROR_MESSAGES);
                BerimNetworkException exception = new BerimNetworkException(errorCode,
                        errors);
                if (BerimApplication.DEBUG) {
                    Log.e(getClass().getSimpleName(),
                            "onReceive() :: ERROR :: ex= " + exception);
                }
                onMessageErrorReceived(exception);
            } finally {
//                this.unregister();
            }
        }
    }

    public static void sendNetworkResponseBroadcast(Object response) {
        Intent netResponseBroadcast = new Intent();
        netResponseBroadcast.setAction(Constants.ACTION_NETWORK_RESPONSE);
        netResponseBroadcast.putExtra(Constants.EXTRA_RESPONSE_BODY, response.toString());
        LocalBroadcastManager.getInstance(BerimApplication.getInstance())
                .sendBroadcast(netResponseBroadcast);
    }

    public static void sendNetworkErrorBroadcast(BerimNetworkException error) {
        Intent netErrorBroadcast = new Intent();
        netErrorBroadcast.setAction(Constants.ACTION_NETWORK_ERROR);
        netErrorBroadcast.putExtra(Constants.EXTRA_ERROR_CODE, error.getErrorCode());
        netErrorBroadcast.putExtra(Constants.EXTRA_ERROR_MESSAGES, error.getMessage());
        LocalBroadcastManager.getInstance(BerimApplication.getInstance())
                .sendBroadcast(netErrorBroadcast);
    }

    public void register() {
        if (!mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.ACTION_NETWORK_RESPONSE);
            intentFilter.addAction(Constants.ACTION_NETWORK_ERROR);
            LocalBroadcastManager.getInstance(BerimApplication.getInstance())
                    .registerReceiver(this, intentFilter);
            mRegistered = true;
        }
    }

    public void unregister() {
        if (mRegistered) {
            LocalBroadcastManager.getInstance(BerimApplication.getInstance())
                    .unregisterReceiver(this);
            mRegistered = false;
        }
    }
}