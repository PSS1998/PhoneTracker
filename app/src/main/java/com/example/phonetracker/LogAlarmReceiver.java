package com.example.phonetracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class LogAlarmReceiver extends BroadcastReceiver {

    private static final String ACTION_TRIGGER_ALARM = "com.example.phonetracker.TRIGGER_ALARM";

    private static final String EXTRA_PHONE_NUMBER = "PHONE_NUMBER";

    private static PendingIntent pendingTrigger;

    private static int ALARM_TIME = 60 * 5 * 1000;

    public static synchronized void startAlarm(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(ACTION_TRIGGER_ALARM);
        intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);

        SmsLocationReporter sender = new SmsLocationReporter(context,
                intent.getStringExtra(EXTRA_PHONE_NUMBER));
        sender.report();

//        context.sendBroadcast(intent);
//        enqueueAlarm(context, phoneNumber);
    }

    public static synchronized void enqueueAlarm(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(ACTION_TRIGGER_ALARM);
        intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);

        pendingTrigger = PendingIntent.getBroadcast(context, 768, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_TIME, pendingTrigger);
    }

    public static synchronized void stopAlarm(Context context) {
        if (pendingTrigger == null) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingTrigger);

        pendingTrigger.cancel();
        pendingTrigger = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !ACTION_TRIGGER_ALARM.equals(intent.getAction())) {
            return;
        }

        if (intent.getStringExtra(EXTRA_PHONE_NUMBER) == null
                || intent.getStringExtra(EXTRA_PHONE_NUMBER).length() == 0) {
            return;
        }

        SmsLocationReporter sender = new SmsLocationReporter(context,
                intent.getStringExtra(EXTRA_PHONE_NUMBER));

        sender.report();

        // reschedule the alarm after the SMS has been sent
        enqueueAlarm(context, intent.getStringExtra(EXTRA_PHONE_NUMBER));
    }
}
