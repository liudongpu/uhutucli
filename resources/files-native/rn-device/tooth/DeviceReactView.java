package com.[@config:appReact.workName].tooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import serial.jni.BluetoothConnect;
import serial.jni.DataUtils;
import serial.jni.GLView;
import serial.jni.NativeCallBack;
import serial.jni.UsbHidConnect;

/**
 * Created by srnpr on 2017/6/26.
 */

public class DeviceReactView  extends SimpleViewManager<GLView> {

    private static final String TAG = "log.device.DRV";

    private DataUtils data;

    private GLView glView;
    private static final int MESSAGE_UPDATE_HR = 0;

    // Member fields
    private BluetoothAdapter mBtAdapter = null;



    private String execString="";


    private String fileString="";

    ThemedReactContext tContext;
    String sAddress;

    @Override
    public String getName() {
        return "DeviceReactView";
    }


    private void sendEvent(
            DataJson dataJson) {

        Gson gson=new Gson();


        tContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(

                "deviceViewCallBack",
                gson.toJson(dataJson));

    }



    @Override
    protected GLView createViewInstance(ThemedReactContext reactContext) {

        Log.i(TAG, "createViewInstance: "+"");

        tContext=reactContext;
        //data.gatherStart(new nativeMsg());

        glView=new GLView(reactContext,null);
        glView.setBackground(Color.TRANSPARENT, Color.rgb(111, 110, 110));
        //glView.setGather(data);
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_UPDATE_HR:
                        Log.d(TAG,msg.obj.toString());

                        break;
                    case BluetoothConnect.MESSAGE_CONNECT_SUCCESS:
                        Log.i(TAG,"BluetoothConnect.MESSAGE_CONNECT_SUCCESS");
                        break;
                    case BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED:

                        Log.i(TAG,"BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED");
                        // Intent interrupt = new Intent(GLActivity.this,
                        // DeviceListActivity.class);
                        // startActivity(interrupt);

                        break;
                    case BluetoothConnect.MESSAGE_CONNECT_FAILED:
                        Log.i(TAG,"BluetoothConnect.MESSAGE_CONNECT_FAILED");


                        break;
                    case UsbHidConnect.MESSAGE_USB_CONNECT_FAILED:

                        break;
                    case UsbHidConnect.MESSAGE_USB_CONNECT_START:

                        break;
                    case UsbHidConnect.MESSAGE_USB_CONNECT_SUCCESS:

                        break;
                    case UsbHidConnect.MESSAGE_USB_CONNECT_ERROR_OPEN_DEVICE:

                        break;
                    case UsbHidConnect.MESSAGE_USB_CONNECT_INTERRUPTED:

                        break;
                    case UsbHidConnect.MESSAGE_USB_CONNECT_REMOVE_DEVICE:

                        break;

                }
            }
        };
        glView.setMsg(mHandler);
        glView.setZOrderOnTop(true);
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        //data.gatherStart(new nativeMsg());


        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //reactContext.registerReceiver(mReceiver, intent);
        reactContext.getCurrentActivity().registerReceiver(mReceiver, intent);




        //mBtAdapter.startDiscovery();

        fileString= UUID.randomUUID().toString();

        return glView;
    }


    @ReactProp(name = "commands")
    public void setCommands(GLView glView, @Nullable String sCommand){

        Log.i(TAG, "setCommand: "+sCommand);


        if(sCommand!=null&&sCommand!=""&&sCommand.length()>0){


            try {
                JSONObject jsonObject=new JSONObject(sCommand);



                String sExec=jsonObject.getString("exec");

                if(sExec!=null){

                    execString=sExec;





                    switch (execString){

                        case "found":
                        {

                            if(mBtAdapter!=null){
                                mBtAdapter.cancelDiscovery();
                            }

                            mBtAdapter = BluetoothAdapter.getDefaultAdapter();


                            boolean bFlagEnable= mBtAdapter.enable();

                            Log.i(TAG, "flagEnableBt: "+bFlagEnable);


                            mBtAdapter.startDiscovery();

                        }
                        break;

                        case "save":
                        {
                            if(data!=null){


                                data.saveCase(Environment.getExternalStorageDirectory() + "/",
                                        fileString, 20);// 存储文件 参数为路径，文件名，存储秒数
                            }
                        }
                        break;

                        case "xml":
                        {
                            this.saveXml();
                        }

                        break;
                    }








                }





            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


    private void saveXml(){
        if(data!=null) {

            String sFilePath=Environment.getExternalStorageDirectory() + "/" + fileString + ".xml";
            int ret = data.ecgDataToAECG(
                    Environment.getExternalStorageDirectory() + "/"
                            + fileString + ".c8k",
                    sFilePath);
            Log.i(TAG, "aecg ecgDataToAECG ret = " + ret + " " + fileString);


            DataJson dataJson=new DataJson();

            dataJson.setType("finish");

            dataJson.setCodeEct(fileString);
            dataJson.setFilePath(sFilePath);

            this.sendEvent(dataJson);
        }
    }








    @ReactProp(name = "src")
    public void setSrc(GLView glView, @Nullable String src) {

        Log.i(TAG, "setSrc: "+src);



        if(src.startsWith("save")){

            String strCase=src.substring(4);
            data.saveCase(Environment.getExternalStorageDirectory() + "/",
                    strCase, 20);// 存储文件 参数为路径，文件名，存储秒数


            Log.i(TAG, "save ret = " + strCase);
        }
        else if(src.startsWith("analyze")){

            String strCase=src.substring(7);



            // 分析数据文件，结果存储为xml
            int ret = data.ecgAnalyzeToXml(
                    Environment.getExternalStorageDirectory() + "/"
                            + strCase,
                    Environment.getExternalStorageDirectory()
                            + "/"+strCase+"aa.xml",
                    Environment.getExternalStorageDirectory()
                            + "/conclusion.cn");
            Log.i(TAG, "analyze ret = " + ret);
        }
        else if(src.startsWith("xml")){

            String strCase=src.substring(3);

            int ret = data.ecgDataToAECG(
                    Environment.getExternalStorageDirectory() + "/"
                            + strCase + ".c8k",
                    Environment.getExternalStorageDirectory() + "/"+strCase+".xml");
            Log.i(TAG,"aecg ecgDataToAECG ret = " + ret);




        }

//data.getGather().BGStart();

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: action:"+action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "onReceive: "+device.getName()+"address:"+device.getAddress());
                if (device.getName() != null&&device.getName().startsWith("ECGWS")) {

                    mBtAdapter.cancelDiscovery();

                    sAddress=device.getAddress();
                    Handler mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case MESSAGE_UPDATE_HR:
                                    Log.d(TAG,msg.obj.toString());

                                    break;
                                case BluetoothConnect.MESSAGE_CONNECT_SUCCESS:
                                    Log.i(TAG,"BluetoothConnect.MESSAGE_CONNECT_SUCCESS");
                                    break;
                                case BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED:

                                    Log.i(TAG,"BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED");
                                    // Intent interrupt = new Intent(GLActivity.this,
                                    // DeviceListActivity.class);
                                    // startActivity(interrupt);

                                    break;
                                case BluetoothConnect.MESSAGE_CONNECT_FAILED:
                                    Log.i(TAG,"BluetoothConnect.MESSAGE_CONNECT_FAILED");


                                    break;
                                case UsbHidConnect.MESSAGE_USB_CONNECT_FAILED:

                                    break;
                                case UsbHidConnect.MESSAGE_USB_CONNECT_START:

                                    break;
                                case UsbHidConnect.MESSAGE_USB_CONNECT_SUCCESS:

                                    break;
                                case UsbHidConnect.MESSAGE_USB_CONNECT_ERROR_OPEN_DEVICE:

                                    break;
                                case UsbHidConnect.MESSAGE_USB_CONNECT_INTERRUPTED:

                                    break;
                                case UsbHidConnect.MESSAGE_USB_CONNECT_REMOVE_DEVICE:

                                    break;

                            }
                        }
                    };
                    data = new DataUtils(tContext, sAddress, mHandler);

                    glView.setGather(data);

                    glView.onResume();
                    data.gatherStart(new nativeMsg());


                    /*
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {

                            data = new DataUtils(tContext, sAddress, mHandler);
                            glView.setGather(data);
                            data.gatherStart(new nativeMsg());

                        }
                    });
                    */
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            }
        }
    };







    class nativeMsg extends NativeCallBack {

        @Override
        public void callHRMsg(short hr) {// 心率
            // mHandler.obtainMessage(MESSAGE_UPDATE_HR, hr).sendToTarget();
        }

        @Override
        public void callLeadOffMsg(String flagOff) {// 导联脱落
            //Log.i(TAG,"LF"+flagOff);
        }

        @Override
        public void callProgressMsg(short progress) {// 文件存储进度百分比 progress%

            //Log.i(TAG,"progress"+ "" + progress);
        }

        @Override
        public void callCaseStateMsg(short state) {
            if (state == 0) {
                Log.i(TAG, " callCaseStateMsg start");// 开始存储文件
            } else {
                Log.i(TAG, "callCaseStateMsg end");// 存储完成
                saveXml();
            }
        }

        @Override
        public void callHBSMsg(short hbs) {// 心率 hbs = 1表示有心跳
            Log.i(TAG,"HeartBeat"+" Sound"+hbs);
        }

        @Override
        public void callBatteryMsg(short per) {// 采集盒电量
            //Log.i(TAG,"Battery"+""+per);
        }

        @Override
        public void callCountDownMsg(short per) {// 剩余存储时长
            //Log.i(TAG,"CountDown"+""+per);
        }

        @Override
        public void callWaveColorMsg(boolean flag) {
            Log.i(TAG,"WaveColor"+ "" + flag);
            if (flag) {
                /*
                // 波形稳定后颜色变为绿色
                glView.setRendererColor(0, 1.0f, 0, 0);
                // 以下操作可以实现自动开始保存文件
                data.saveCase(Environment.getExternalStorageDirectory() + "/",
                        strCase, 20);// 存储文件 参数为路径，文件名，存储秒数
               */
            }
        }
    }



    class DataJson{
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type="";

        public String getCodeEct() {
            return codeEct;
        }

        public void setCodeEct(String codeEct) {
            this.codeEct = codeEct;
        }

        private String codeEct="";

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        private String filePath="";
    }


}