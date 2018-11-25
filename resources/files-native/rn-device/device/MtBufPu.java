package com.[@config:appReact.workName].device;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

import cn.com.contec.jar.sp10w.DeviceCommand;
import cn.com.contec.jar.sp10w.DeviceDataJar;
import cn.com.contec.jar.sp10w.DevicePackManager;

/**
 * Created by srnpr on 2017/7/24.
 */

public class MtBufPu extends AMt implements  IMt {

    private static final String TAG = "device.MtBufPu";
    public static Vector<Integer> m_buf = null;
    private DevicePackManager m_DevicePackManager = new DevicePackManager();

    MtBufPu() {
        m_buf = new Vector<Integer>();
    }

    public synchronized int Count() {
        return m_buf.size();
    }

    int mSettimeCount = 0;

    public synchronized void write(byte[] buf, int count,
                                   final OutputStream pOutputStream) throws Exception {
        int _receiveNum = m_DevicePackManager.arrangeMessage(buf, count);

        Log.i(TAG, "_receiveNum: "+_receiveNum);

        switch (_receiveNum) {

            case 3:// 对时失败 关闭socket
                Log.i(TAG, "请求数据");
                pOutputStream.write(DeviceCommand.command_requestData());

                break;
            case 4:// 无新数据;


                break;
            case 1: // 整个数据接收完成





                JSONArray jsonDeviceData1=new JSONArray();

                ArrayList<DeviceDataJar> _djData = m_DevicePackManager.mDeviceDataJarsList;

                JSONObject jsonResult=new JSONObject();
                JSONArray jsonDeviceData=new JSONArray();

                try
                {

                    int iLength=_djData.size();

                    for (int i = 0; i < iLength; i++) {

                        DeviceDataJar device=_djData.get(i);

                        JSONObject data = new JSONObject();

                        data.put("FVC", String.valueOf(device.mParamInfo.mFVC));
                        data.put("FEV1", String.valueOf(device.mParamInfo.mFEV1));
                        data.put("PEF", String.valueOf(device.mParamInfo.mPEF));
                        data.put("FEF25", String.valueOf(device.mParamInfo.mFEF25));
                        data.put("FEF75", String.valueOf(device.mParamInfo.mFEF75));
                        data.put("FEF2575", String.valueOf(device.mParamInfo.mFEF2575));
                        data.put("FEV1Per", String.valueOf(device.mParamInfo.mFEV1Per));
                        data.put("FEF50", String.valueOf(device.mParamInfo.mFEF50));
                        data.put("FEV05", String.valueOf(device.mParamInfo.mFEV05));


                        JSONObject testResult=new JSONObject();
                        testResult.put("TestResult",data);


                        jsonDeviceData.put(testResult);

                    }
                    JSONObject jsonPulse=new JSONObject();

                    jsonResult.put("DataCount",iLength);
                    jsonResult.put("DeviceData",jsonDeviceData);


                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                callBack(jsonResult);


                pOutputStream.write(DeviceCommand.command_delData());




                // 保存数据
//			saveSpo2Data();
			/*
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						pOutputStream.write(DeviceCommand
								.dayPedometerDataCommand());
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			*/
                break;

            case 7:// 对时成功

                pOutputStream.write(DeviceCommand.command_Time());


                break;

        }
    }

    @Override
    public byte[] upCommand() {
        return DeviceCommand.command_Date();
    }





}


