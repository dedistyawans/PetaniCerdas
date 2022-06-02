package com.dekastudio.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import static android.os.Build.VERSION.SDK_INT;

//Runtime Permission Helper by DekaStudio :*
public class PermissionHelper {
    private static String permisi = "android.permission.";
    //Calendar group
    public static final String READ_CALENDAR = permisi + "READ_CALENDAR";
    public static final String WRITE_CALENDAR = permisi + "WRITE_CALENDAR";
    //Camera group
    public static final String CAMERA = permisi + "CAMERA";
    //Contact group
    public static final String READ_CONTACTS = permisi + "READ_CONTACTS";
    public static final String WRITE_CONTACTS = permisi + "WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = permisi + "GET_ACCOUNTS";
    //Location group
    public static final String ACCESS_FINE_LOCATION = permisi + "ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = permisi + "ACCESS_COARSE_LOCATION";
    //Microphone group
    public static final String RECORD_AUDIO = permisi + "RECORD_AUDIO";
    //Phone group
    public static final String READ_PHONE_STATE = permisi + "READ_PHONE_STATE";
    public static final String READ_PHONE_NUMBERS = permisi + "READ_PHONE_NUMBERS";
    public static final String CALL_PHONE = permisi + "CALL_PHONE";
    public static final String ANSWER_PHONE_CALLS = permisi + "ANSWER_PHONE_CALLS"; //must request at runtime
    public static final String READ_CALL_LOG = permisi + "READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = permisi + "WRITE_CALL_LOG";
    public static final String ADD_VOICEMAIL = permisi + "ADD_VOICEMAIL";
    public static final String USE_SIP = permisi + "USE_SIP";
    public static final String PROCESS_OUTGOING_CALLS = permisi + "PROCESS_OUTGOING_CALLS";
    //Sensors group
    public static final String BODY_SENSORS = permisi + "BODY_SENSORS";
    //SMS group
    public static final String SEND_SMS = permisi + "SEND_SMS";
    public static final String RECEIVE_SMS = permisi + "RECEIVE_SMS";
    public static final String READ_SMS = permisi + "READ_SMS";
    public static final String RECEIVE_WAP_PUSH = permisi + "RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = permisi + "RECEIVE_MMS";
    //Storage group
    public static final String READ_EXTERNAL_STORAGE = permisi + "READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = permisi + "WRITE_EXTERNAL_STORAGE";

    private Activity activity;
    private PermissionListener permissionListener;
    private int kodePermissions = 102;
    private int kodeGoToSettingPermissions = 103;
    private boolean sentToSettings = false;
    private String[] permissions;


    public void setOnActivityResult(int requestCode){
        if(permissionListener == null) throw new IllegalStateException("PermissionListener masih bernilai null");
        if(requestCode == kodeGoToSettingPermissions) {
            if (sentToSettings) {
                if (!apakahBelumDiIjinkan()) {
                    //Got Permission
                    permissionListener.onPermissionGranted();
                } else {
                    permissionListener.onPermissionDenied();
                }
            }
        }
    }

    public void setOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       if(permissionListener == null) throw new IllegalStateException("PermissionListener masih bernilai null");
       if(requestCode == kodePermissions){
            if(apakahSemuaDiIjinkan(grantResults)){
                permissionListener.onPermissionGranted();
            }else {
                permissionListener.onPermissionDenied();
            }
       }
    }

    public PermissionHelper(Activity activity, String... permissions) {
        this.activity = activity;
        this.permissions = permissions;
    }

    public void setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public void requestPermission(){
        if(permissionListener == null) throw new IllegalStateException("PermissionListener masih bernilai null");
        if (SDK_INT >= Build.VERSION_CODES.M) {
            if(apakahBelumDiIjinkan()){
                String preferenceKey = "preferenceKey";
                if(apakahSebaiknyaMenampilkanDialogPermissions()){
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage(dapatkanPesanDialogPermissions());
                    builder.setPositiveButton("Grant", (dialog, which) -> {
                        dialog.dismiss();
                        //request permissions
                        ActivityCompat.requestPermissions(activity, permissions, kodePermissions);
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                        //request permissions ditolak
                        tampilkanPeringatanPermissionsDibutuhkan();
                    });
                    builder.show();
                } else if (PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(preferenceKey, false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage(dapatkanPesanDialogPermissions());
                    builder.setPositiveButton("Grant", (dialog, which) -> {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, kodeGoToSettingPermissions);
                        Toast.makeText(activity, "Go to 'Permissions' to Grant Requested Permissions", Toast.LENGTH_LONG).show();
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                        //request permissions ditolak
                        tampilkanPeringatanPermissionsDibutuhkan();
                    });
                    builder.show();
                }  else {
                    //just request the permission
                    ActivityCompat.requestPermissions(activity, permissions, kodePermissions);
                }

                PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean(preferenceKey, true).apply();

            } else {
                //You already have the permission, just go ahead.
                permissionListener.onPermissionGranted();
            }
        }else {
            //No need to request permissions
            permissionListener.onPermissionGranted();
        }

    }

    private boolean apakahSemuaDiIjinkan(int[] hasilRequest){
        for(int hasil : hasilRequest){
            if(hasil != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private String dapatkanPesanDialogPermissions(){
        StringBuilder pesan = new StringBuilder("This app needs some permissions, " +
                "if one or more permissions is not granted, the app cannot run normally. So Please grant all requested permissions listed bellow: \n");
        for(int i=0; i<permissions.length; i++){
            pesan.append(String.valueOf(i + 1))
                    .append(". ")
                    .append(permissions[i])
                    .append("\n");
        }
        return pesan.toString();
    }

    private boolean apakahBelumDiIjinkan(){
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                return true;
        }
        return false;
    }

    private boolean apakahSebaiknyaMenampilkanDialogPermissions(){
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return true;
        }
        return false;
    }

    private void tampilkanPeringatanPermissionsDibutuhkan(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Permissions Required!");
        builder.setMessage("Without required permissions this app cannot start normally");
        builder.setPositiveButton("Grant Permissions", (dialog, which) -> {
            dialog.dismiss();
            requestPermission();
        });
        builder.setNegativeButton("Close App", (dialog, which) -> {
            dialog.dismiss();
            //request permissions ditolak
            activity.finish();

        });
        builder.show();
    }

    public interface PermissionListener{
        void onPermissionGranted();
        void onPermissionDenied();
    }

}
