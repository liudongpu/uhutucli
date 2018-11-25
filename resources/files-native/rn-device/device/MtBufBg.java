package com.[@config:appReact.workName].device;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import cn.com.contec.jar.cmssxt.DeviceCommand;
import cn.com.contec.jar.cmssxt.DevicePackManager;

/**
 * Created by srnpr on 2017/6/22.
 */

public class MtBufBg  extends AMt implements  IMt {

    @Override
    public byte[] upCommand() {
        return DeviceCommand.command_ReadID();
    }

    private static final String TAG = "device.BtBufBg";
    public static Vector<Integer> m_buf = null;
    private DevicePackManager m_DevicePackManager = new DevicePackManager();

    MtBufBg() {
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
        int _receiveNum = m_DevicePackManager.arrangeMessage(buf, count);
        Log.i(TAG, "jar包返回的信息：" + _receiveNum);
        switch (_receiveNum) {
            case 8:// 该设备是旧设备 发送不带秒的对时命令

                            pOutputStream.write(DeviceCommand.command_VerifyTime());// 发送请求数据的命令

                break;
            case 9:// 该设备是新设备 发送带秒的对时命令

                            pOutputStream.write(DeviceCommand
                                    .command_VerifyTimeSS());// 发送请求数据的命令

                break;
            case 1:// 成功接收数据

                JSONObject jsonResult=new JSONObject();
                JSONArray jsonDeviceData=new JSONArray();
                try
                {

                    int iLength=m_DevicePackManager.m_DeviceDatas.size();

                    for (int i = 0; i < iLength; i++) {

                        Log.d(TAG, String.valueOf(  m_DevicePackManager.m_DeviceDatas.get(i).m_data));

                        JSONObject data = new JSONObject();

                        data.put("BloodSugar", String.valueOf(m_DevicePackManager.m_DeviceDatas.get(i).m_data));

                        jsonDeviceData.put(data);

                    }


                    jsonResult.put("DataCount",iLength);
                    jsonResult.put("DeviceData",jsonDeviceData);





                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                callBack(jsonResult);




                m_DevicePackManager.m_DeviceDatas.clear();// 清空数据集合 避免重复保存
                pOutputStream.write(DeviceCommand.command_delData());// 发送删除数据的命令

                break;
            case 2:// 接收数据失败
                Log.i(TAG, "接收数据失败");
                break;
            case 3:// 对时成功;
                Log.i(TAG, "对时间成功");
                pOutputStream
                        .write(DeviceCommand.command_requestData());// 发送请求数据的命令

                break;
            case 4:// 对时失败;
                Log.i(TAG, "对时间失败");
                break;
            case 5: // 删除成功
                Log.i(TAG, "删除成功");
                pOutputStream.close();
                break;
            case 6:// 删除失败；
                Log.i(TAG, "删除失败");
                pOutputStream.close();
                break;
            case 7:// 设备无数据；
                Log.i(TAG, "设备无数据");
                break;
        }
    }

    /**
     * 接收到的数据存数到文件中
     *
     * @param pContent
     */
    public void saveAsString(String pContent) {
        String PATH_BASE = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/contec";
        File _file = new File(PATH_BASE);
        if (!_file.exists()) {
            _file.mkdirs();
        }
        try {
            OutputStreamWriter os = new OutputStreamWriter(
                    new FileOutputStream(_file + "/Sp10w_Data.txt", true));
            os.write(pContent);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized int read(int[] buf) {
        int len = 0;
        if (buf.length <= m_buf.size()) {
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (int) (m_buf.get(i));
            }
            len = buf.length;
            for (int j = 0; j < len; j++) {
                m_buf.remove(0);
            }

        } else if (buf.length > m_buf.size()) {
            for (int i = 0; i < m_buf.size(); i++) {
                buf[i] = m_buf.get(i);
            }
            len = m_buf.size();
            for (int j = 0; j < len; j++) {
                m_buf.remove(0);
            }

        }
        return len;
    }
}
