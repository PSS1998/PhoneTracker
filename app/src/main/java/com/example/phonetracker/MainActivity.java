package com.example.phonetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.enterStartPassword).setOnClickListener(this);
        findViewById(R.id.enterStopPassword).setOnClickListener(this);
    }

    /**
     * Re-show activity in the user's launcher.
     * @param context
     */
    public static void enableActivity(Context context) {
        ComponentName componentName = new ComponentName(context, MainActivity.class);

        context.getPackageManager().setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Disable activity and hide it from the launcher.
     *
     * @param context
     */
    public static void disableActivity(Context context) {
        ComponentName componentName = new ComponentName(context, MainActivity.class);

        context.getPackageManager().setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static boolean isEnabled(Context context) {
        ComponentName componentName = new ComponentName(context, MainActivity.class);

        return context.getPackageManager().getComponentEnabledSetting(componentName)
                != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enterStartPassword) {
            setStartPassword();
        } else if (v.getId() == R.id.enterStopPassword) {
            setStopPassword();
        }
    }

    private void setStopPassword() {
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_enter_password, null);

        new AlertDialog.Builder(this)
                .setTitle(R.string.enter_password_stop)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText passwordEdit = (EditText) dialogView.findViewById(R.id.password);

                        PreferenceManager prefs = new PreferenceManager(MainActivity.this);
                        prefs.setStopPassword(passwordEdit.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void setStartPassword() {
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_enter_password, null);

        new AlertDialog.Builder(this)
                .setTitle(R.string.enter_password_start)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText passwordEdit = (EditText) dialogView.findViewById(R.id.password);

                        PreferenceManager prefs = new PreferenceManager(MainActivity.this);
                        prefs.setStartPassword(passwordEdit.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
