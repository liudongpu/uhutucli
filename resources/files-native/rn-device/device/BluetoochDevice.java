package com.[@config:appReact.workName].device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srnpr on 2017/6/16.
 */

public class BluetoochDevice implements ICallBack
{

        private BluetoothChatService mChatService;

    private static final String TAG = "log.device.BD";

    private CallBack call;

    List<String> lstDevices = new ArrayList<String>();
    BluetoothAdapter btAdapt;
    boolean bFlagInit=false;

    ReactContext reactContext;


    private String deviceType;

    public void connect(String sType,ContextWrapper context) {



        deviceType=sType;

        Log.d(TAG, "connect: deviceType:"+deviceType+"   bFlagInit:"+String.valueOf(bFlagInit));
        if(!bFlagInit){

            btAdapt = BluetoothAdapter.getDefaultAdapter();

            bFlagInit=true;


        }
        else{

            mChatService.stop();
        }

            //Log.d(TAG, "connect: bFlagInit:"+String.valueOf(bFlagInit));
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

            IMt imt=null;


        switch (sType){
            case "SpO208":
                imt=new MtBufSp();
                break;
            case "TEMP":
                imt=new MtBufTemp();
                break;
            case "BG01":
                imt=new MtBufBg();
                break;
            case "BC01":
                imt=new MtBufBc();
                break;
            case "NIBP04":
                imt=new MtBufBp();
                break;
            case "PULMO01":
                imt=new MtBufPu();
                break;
        }


            imt.inCallBack(this);

        call = new CallBack(imt, this);

        mChatService=new BluetoothChatService(context,call,imt.upCommand());

        context.registerReceiver(searchDevices, intent);

            reactContext=(ReactContext)context;


        lstDevices.clear();
        btAdapt.startDiscovery();
    }

        private BroadcastReceiver searchDevices = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {


                String action = intent.getAction();
                Bundle b = intent.getExtras();
                Object[] lstName = b.keySet().toArray();

                // ??????????????????????
                for (int i = 0; i < lstName.length; i++) {
                    String keyName = lstName[i].toString();
                    //Log.e(keyName, String.valueOf(b.get(keyName)));
                    Log.i(TAG, "onReceive: "+keyName+String.valueOf(b.get(keyName)));
                }

                Log.i(TAG, "action: "+action);

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    Log.i(TAG, "device: "+device.getName()+" address:"+device.getAddress());
                    if (device.getName()==null) {
                        return;
                    }
                    if (device.getName().contains(deviceType)) {
                        Log.i(TAG, deviceType);
                        if (mChatService != null)
                            mChatService.stop();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        mChatService.start();
                        mChatService.connect(device);
                    }
                }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                }
            }
        };

    @Override

        public void call(String sMessage) {

            /*
            Vector<Integer> _ver = MtBufTemp.m_buf;
            for (int i = 0; i < _ver.size(); i++) {
                Log.i("............", Integer.toHexString(_ver.get(i)&0xFF));

            }
            */

        Log.d(TAG, "call: srnpr---------------js"+sMessage);
        //reactContext.unregisterReceiver(searchDevices);

        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(

                "deviceDataCallBack",
                sMessage);


        }



}
