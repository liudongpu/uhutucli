package com.[@config:appReact.workName].device;

import android.util.Log;

import com.contec.jar.contec08a.DeviceCommand;
import com.contec.jar.contec08a.DevicePackManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by srnpr on 2017/7/24.
 */

public class MtBufBp extends AMt implements  IMt{

    @Override
    public byte[] upCommand() {


        byte[] _cmd =DeviceCommand.REQUEST_HANDSHAKE() ;
        byte[] _commd_Handshake = {(byte) 0x42, (byte) 0x8f, (byte) 0xFF, (byte) 0xFE,
                (byte) 0xFD, (byte) 0xFC
        };
        return _commd_Handshake;
    }

    private static final String TAG = "device.MtBufBp";
    public static Vector<Integer> m_buf = null;
    private DevicePackManager m_DevicePackManager = new DevicePackManager();

    public static final int e_pack_pressure_back = 0x46;


    DevicePackManager mPackManager = new DevicePackManager();

    public static final int e_pack_hand_back = 0x48;
    public static final int e_pack_oxygen_back = 0x47;
    private int mType = 0;

    MtBufBp() {
        mPacks = new byte[1024];
        mCount = 0;
        m_buf = new Vector<Integer>();
    }

    public synchronized int Count() {
        return m_buf.size();
    }

    int mSettimeCount = 0;
    private byte[] mPacks;
    private int mCount;

    public synchronized void write(byte[] buf, int count,
                                   final OutputStream pOutputStream) throws Exception {

        int state = mPackManager.arrangeMessage(buf, count, mType);

        Log.i(TAG, "mType"+mType+"  mDeviceData"+mPackManager.mDeviceData.mData_blood.size()+" mDeviceDatas "+mPackManager.mDeviceDatas.size() +"  state: "+state);

        int x = DevicePackManager.Flag_User;
        switch (state) {
            case e_pack_hand_back:
                switch (mType) {
                    case 9:
                        mType = 5;
                        pOutputStream.write(DeviceCommand.DELETE_BP());
                        break;
                    case 0:
                        mType = 3;
                        pOutputStream.write(DeviceCommand.correct_time_notice);
                        break;
//			case 1:
//				mType = 2;
//				pOutputStream.write(DeviceCommand.REQUEST_OXYGEN());
//				break;
//			case 7:
//				mType = 8;
//				pOutputStream.write(DeviceCommand.REQUEST_OXYGEN());
//				break;
//			case 2:
//				mType = 5;
//				pOutputStream.write(DeviceCommand.DELETE_OXYGEN());
//				break;
//			case 8:
//				mType = 5;
//				pOutputStream.write(DeviceCommand.DELETE_OXYGEN());
//				break;
                    case 3:
                        mType = 1;

                        if (x == 0x11) {
                            mType = 7;// 三个用户
                        } else {
                            mType = 1;// 单用户
                        }

                        pOutputStream.write(DeviceCommand.REQUEST_BLOOD_PRESSURE());
                        break;
                }
                break;
            case 0x30:// 确认校正时间正确
                pOutputStream.write(DeviceCommand.Correct_Time());
                break;
            case 0x40:// 校正时间正确
                pOutputStream.write(DeviceCommand.REQUEST_HANDSHAKE());
                break;
            case e_pack_pressure_back:
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {////防止最后一条命令血压设备接收不到
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ArrayList<byte[]> _dataList = mPackManager.mDeviceData.mData_blood;
                int _size = _dataList.size();


                JSONObject jsonResult=new JSONObject();
                JSONArray jsonDeviceData=new JSONArray();

                try
                {

                for (int i = 0; i < _size; i++) {

                    byte[] _byte = _dataList.get(i);




                    JSONObject data = new JSONObject();

                    data.put("HighPressure", String.valueOf(_byte[1]& 0xff));
                    data.put("LowPressure", String.valueOf(_byte[2]& 0xff));
                    data.put("PulseRate", String.valueOf(_byte[3]& 0xff));

                    jsonDeviceData.put(data);
                }
                    jsonResult.put("DataCount",_size);
                    jsonResult.put("DeviceData",jsonDeviceData);

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                callBack(jsonResult);


                if (_size == 0) {
                    pOutputStream.write(DeviceCommand.REPLAY_CONTEC08A());

                    Log.e(TAG, "-------Pressure-------");


                }else{
                    pOutputStream.write(DeviceCommand.REPLAY_CONTEC08A());

                }


                break;
        }
    }



}
