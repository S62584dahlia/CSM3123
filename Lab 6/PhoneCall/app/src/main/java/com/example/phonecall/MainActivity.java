package com.example.phonecall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TelephonyManager mTelephonyManager;
    private MyPhoneCallListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a telephony manager.
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // Check to see if Telephony is enabled.
        if (isTelephonyEnabled()) {
            Log.d(TAG, getString(R.string.telephony_enabled));
            // Check for phone permission.
            checkForPhonePermission();
            // Register the PhoneStateListener to monitor phone activity.
            mListener = new MyPhoneCallListener();
            mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
        } else {
            Toast.makeText(this,
                    R.string.telephony_not_enabled, Toast.LENGTH_LONG).show();
            Log.d(TAG, getString(R.string.telephony_not_enabled));
            // Disable the call button.
            disableCallButton();
        }
    }

    private void disableCallButton() {
        Toast.makeText(this,
                "Phone calling disabled", Toast.LENGTH_LONG).show();
        ImageButton callButton = (ImageButton) findViewById(R.id.phone_icon);
        callButton.setVisibility(View.INVISIBLE);
        if (isTelephonyEnabled()) {
            Button retryButton = (Button) findViewById(R.id.button_retry);
            retryButton.setVisibility(View.VISIBLE);
        }
    }

    private void enableCallButton() {
        ImageButton callButton = (ImageButton) findViewById(R.id.phone_icon);
        callButton.setVisibility(View.VISIBLE);
    }

    public void retryApp(View view) {
        enableCallButton();
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());
        startActivity(intent);
    }

    public void callNumber(View view) {
        EditText editText = (EditText) findViewById(R.id.editText_main);
        String phoneNumber = String.format("tel: %s",
                editText.getText().toString());

        Log.d(TAG, "Phone Status: DIALING: " + phoneNumber);
        Toast.makeText(this,
                "Phone Status: DIALING: " + phoneNumber,
                Toast.LENGTH_LONG).show();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));

        if (callIntent.resolveActivity(getPackageManager()) != null) {
            checkForPhonePermission();
            startActivity(callIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
        }
    }

    private boolean isTelephonyEnabled() {
        if (mTelephonyManager != null) {
            if (mTelephonyManager.getSimState() ==
                    TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }

    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, String.valueOf(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Permissions already granted. Enable the call button.
            enableCallButton();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.CALL_PHONE)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                } else {
                    // Permission denied.
                    Log.d(TAG, String.valueOf(R.string.failure_permission));
                    Toast.makeText(this,
                            R.string.failure_permission,
                            Toast.LENGTH_LONG).show();
                    // Disable the call button
                    disableCallButton();
                }
            }
        }
    }

    private class MyPhoneCallListener extends PhoneStateListener {
        private boolean returningFromOffHook = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            String message = getString(R.string.phone_status);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    message = message +
                            getString(R.string.ringing) + incomingNumber;
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    message = message + getString(R.string.offhook);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    returningFromOffHook = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    message = message + getString(R.string.idle);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    if (returningFromOffHook) {
                        // No need to do anything if >= version KitKat.
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            Log.i(TAG, getString(R.string.restarting_app));
                            Intent intent = getPackageManager()
                                    .getLaunchIntentForPackage(getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                    break;
                default:
                    message = message + "Phone off";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isTelephonyEnabled()) {
            mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
        }
    }
}