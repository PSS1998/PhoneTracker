package com.example.phonetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import java.util.List;

public class SmsLocationReporter {

    private Context context;

    private String receiver;

    public SmsLocationReporter(Context context, String receiver) {
        this.context = context;
        this.receiver = receiver;
    }

    @SuppressLint("StringFormatMatches")
    public void report() {
        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        String usedProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(usedProvider);


        if (location == null) {
            String result = "";
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            List<CellInfo> cellInfoList = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cellInfoList = telephonyManager.getAllCellInfo();
            }
            for (int i = 0; i < cellInfoList.size(); i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (cellInfoList.get(i) instanceof CellInfoWcdma) {
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfoList.get(i);
                        String a = "WCDMA";
                        String b = Integer.toString(cellInfoWcdma.getCellSignalStrength().getDbm());
                        String c = Integer.toString(cellInfoWcdma.getCellIdentity().getCid());
                        String d = Integer.toString(cellInfoWcdma.getCellIdentity().getMcc());
                        String e = Integer.toString(cellInfoWcdma.getCellIdentity().getMnc());
                        String f = Integer.toString(cellInfoWcdma.getCellIdentity().getLac());
                        if(!c.equals("2147483647") || !d.equals("2147483647") || !e.equals("2147483647") || !f.equals("2147483647"))
                            result += a+":"+b+": "+String.format("http://mobile.maps.yandex.net/cellid_location/?&cellid=%s&operatorid=%s&countrycode=%s&lac=%s", c, e, d, f)+"\n";
                    } else if (cellInfoList.get(i) instanceof CellInfoGsm) {
                        CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfoList.get(i);
                        String a = "GSM";
                        String b = Integer.toString(cellInfoGsm.getCellSignalStrength().getDbm());
                        String c = Integer.toString(cellInfoGsm.getCellIdentity().getCid());
                        String d = Integer.toString(cellInfoGsm.getCellIdentity().getMcc());
                        String e = Integer.toString(cellInfoGsm.getCellIdentity().getMnc());
                        String f = Integer.toString(cellInfoGsm.getCellIdentity().getLac());
                        if(!c.equals("2147483647") || !d.equals("2147483647") || !e.equals("2147483647") || !f.equals("2147483647"))
                            result += a+":"+b+": "+String.format("http://mobile.maps.yandex.net/cellid_location/?&cellid=%s&operatorid=%s&countrycode=%s&lac=%s", c, e, d, f)+"\n";
                    } else if (cellInfoList.get(i) instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfoList.get(i);
                        String a = "LTE";
                        String b = Integer.toString(cellInfoLte.getCellSignalStrength().getDbm());
                        String c = Integer.toString(cellInfoLte.getCellIdentity().getCi());
                        String d = Integer.toString(cellInfoLte.getCellIdentity().getMcc());
                        String e = Integer.toString(cellInfoLte.getCellIdentity().getMnc());
                        String f = Integer.toString(cellInfoLte.getCellIdentity().getTac());
                        if(!c.equals("2147483647") || !d.equals("2147483647") || !e.equals("2147483647") || !f.equals("2147483647"))
                            result += a+":"+b+": "+String.format("http://mobile.maps.yandex.net/cellid_location/?&cellid=%s&operatorid=%s&countrycode=%s&lac=%s", c, e, d, f)+"\n";
                    }
                }
            }
            TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(receiver, mPhoneNumber, result, null, null);

            return;
        }

        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        String message = String.format("https://maps.google.com/?q=%f,%f", location.getLatitude(), location.getLongitude());
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(receiver, mPhoneNumber, message, null, null);
    }
}