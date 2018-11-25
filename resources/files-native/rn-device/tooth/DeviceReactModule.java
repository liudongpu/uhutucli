package com.[@config:appReact.workName].tooth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.[@config:appReact.workName].device.BluetoochDevice;

/**
 * Created by srnpr on 2017/6/15.
 */

public class DeviceReactModule  extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static final String TAG = "log.device.DRM";
    public DeviceReactModule(ReactApplicationContext reactContext) {
        super(reactContext);

        reactContext.addActivityEventListener(this);

    }





    @Override
    public String getName() {
        return "DeviceReact";
    }



    @ReactMethod
    public void initDevice(String sInit){

    }


    private final  static BluetoochDevice blueToochDevice=new BluetoochDevice();

    @ReactMethod
    public void commandFine(String sType){

        Log.d(TAG, "commandFine: "+sType);
        blueToochDevice.connect(sType,this.getReactApplicationContext());

    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

}
