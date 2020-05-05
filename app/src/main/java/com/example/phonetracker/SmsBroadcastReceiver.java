package com.example.phonetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("sms recived");
        if (intent == null
                || !Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            return;
        }


        Bundle extras = intent.getExtras();
        if (extras != null) {
            PreferenceManager prefs = new PreferenceManager(context);

            Object[] pdus = (Object[]) extras.get("pdus");

            boolean abort = false;
            for (Object pdu : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                String sender = message.getOriginatingAddress();
                String body = message.getMessageBody();

                if (prefs.isStartPasswordCorrect(body)) {
                    // we also enable GPS here as it takes some time to get a location fix
//                    GpsToggler.enableGps(context);
                    // hide activity to prevent changing passwords
                    MainActivity.disableActivity(context);
                    LogAlarmReceiver.startAlarm(context, sender);
                    prefs.setReportReceiver(sender);
                    abort = true;

                    break;
                } else if (prefs.isStopPasswordCorrect(body)) {
//                    GpsToggler.disableGps(context);
                    MainActivity.enableActivity(context);
//                    LogAlarmReceiver.stopAlarm(context);
                    abort = true;

                    break;
                }
            }

            if (abort) {
                // this is not working under KITKAT anymore
                abortBroadcast();

                // as a hack, disable sound and vibration to hide the incoming SMS notification from any possible thief
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    AudioManager audioManager = (AudioManager) context.getSystemService(
                            Context.AUDIO_SERVICE);

                    audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                            AudioManager.VIBRATE_SETTING_OFF);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                            AudioManager.VIBRATE_SETTING_OFF);
                }
            }
            else{
                clearAbortBroadcast();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    AudioManager audioManager = (AudioManager) context.getSystemService(
                            Context.AUDIO_SERVICE);

                    audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                            AudioManager.VIBRATE_SETTING_ON);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                            AudioManager.VIBRATE_SETTING_ON);
                }
            }
        }
    }
}
