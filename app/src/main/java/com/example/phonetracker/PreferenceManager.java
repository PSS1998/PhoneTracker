package com.example.phonetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PreferenceManager {

    private static final String PREFS_NAME = "settings";

    private static final String KEY_START_PASSWORD = "start_password";

    private static final String KEY_STOP_PASSWORD = "stop_password";

    private static final String KEY_REPORT_RECEIVER_NUMBER = "report_receiver";

    private Context context;

    private final SharedPreferences prefs;

    public PreferenceManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setStartPassword(String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_START_PASSWORD, generateHash(password));
        editor.apply();
    }

    public void setStopPassword (String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_STOP_PASSWORD, generateHash(password));
        editor.apply();
    }

    public boolean isStartPasswordCorrect(String password) {
        String hashed = prefs.getString(KEY_START_PASSWORD, null);
        if (hashed == null) {
            return false;
        }

        return hashed.equals(generateHash(password));
    }

    public boolean isStopPasswordCorrect(String password) {
        String hashed = prefs.getString(KEY_STOP_PASSWORD, null);
        if (hashed == null) {
            return false;
        }

        return hashed.equals(generateHash(password));
    }

    public void setReportReceiver(String number) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REPORT_RECEIVER_NUMBER, number);
        editor.apply();
    }

    public String getReportReceiver() {
        return prefs.getString(KEY_REPORT_RECEIVER_NUMBER, null);
    }

    private String generateHash(String password) {
        String salt = Build.SERIAL;
        if (Build.SERIAL == null) {
            salt = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            digest.update((password + salt).getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);

                while (h.length() < 2) {
                    h = "0" + h;
                }

                hexString.append(h);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
