package com.example.phonetracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

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
        System.out.println("sms send");
        LocationManager locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        System.out.println("sms send2");
        String usedProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
        System.out.println(usedProvider);
        System.out.println("sms send3");
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(usedProvider);
        System.out.println("sms send4");


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
                        String a = "wcdma";
                        String b = Integer.toString(cellInfoWcdma.getCellSignalStrength().getDbm());
                        String c = Integer.toString(cellInfoWcdma.getCellIdentity().getCid());
                        String d = Integer.toString(cellInfoWcdma.getCellIdentity().getMcc());
                        String e = Integer.toString(cellInfoWcdma.getCellIdentity().getMnc());
                        String f = Integer.toString(cellInfoWcdma.getCellIdentity().getLac());
                        if(!c.equals("2147483647") || !d.equals("2147483647") || !e.equals("2147483647") || !f.equals("2147483647"))
                            result += a+":"+b+":"+c+":"+d+":"+e+":"+f+"\n";
                    } else if (cellInfoList.get(i) instanceof CellInfoGsm) {
                        CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfoList.get(i);
                        String a = "gsm";
                        String b = Integer.toString(cellInfoGsm.getCellSignalStrength().getDbm());
                        String c = Integer.toString(cellInfoGsm.getCellIdentity().getCid());
                        String d = Integer.toString(cellInfoGsm.getCellIdentity().getMcc());
                        String e = Integer.toString(cellInfoGsm.getCellIdentity().getMnc());
                        String f = Integer.toString(cellInfoGsm.getCellIdentity().getLac());
                        if(!c.equals("2147483647") || !d.equals("2147483647") || !e.equals("2147483647") || !f.equals("2147483647"))
                            result += a+":"+b+":"+c+":"+d+":"+e+":"+f+"\n";
                    } else if (cellInfoList.get(i) instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfoList.get(i);
                        String a = "lte";
                        String b = Integer.toString(cellInfoLte.getCellSignalStrength().getDbm());
                        String c = Integer.toString(cellInfoLte.getCellIdentity().getCi());
                        String d = Integer.toString(cellInfoLte.getCellIdentity().getMcc());
                        String e = Integer.toString(cellInfoLte.getCellIdentity().getMnc());
                        String f = Integer.toString(cellInfoLte.getCellIdentity().getTac());
                        if(!c.equals("2147483647") || !d.equals("2147483647") || !e.equals("2147483647") || !f.equals("2147483647"))
                            result += a+":"+b+":"+c+":"+d+":"+e+":"+f+"\n";
                    } else if (cellInfoList.get(i) instanceof CellInfoCdma) {
                        CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfoList.get(i);
                        String a = "cdma";
                        String b = Integer.toString(cellInfoCdma.getCellSignalStrength().getDbm());
                        result += a+":"+b+"\n";
                    }
                }
                System.out.println(result);
            }
            System.out.println(result);
            System.out.println("sms send7");
            TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
            System.out.println("sms send8");
            System.out.println(mPhoneNumber);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(receiver, mPhoneNumber, result, null, null);
            System.out.println("sms send9");

            return;
        }
        System.out.println("sms send5");
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        System.out.println(mPhoneNumber);
        System.out.println("sms send6");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(receiver, mPhoneNumber, context.getString(R.string.report_text,
                location.getLatitude(), location.getLongitude()), null, null);
    }
}