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

import cn.com.contec.jar.eartemperture.DeviceCommand;
import cn.com.contec.jar.eartemperture.DevicePackManager;
//import cn.com.contec.jar.util.PrintBytes;

public class MtBufTemp extends AMt implements  IMt{
	private static final String TAG = "device.MtBufTemp";
	public static Vector<Integer> m_buf = null;




	MtBufTemp() {
		m_buf = new Vector<Integer>();
	}

	public synchronized int Count() {
		return m_buf.size();
	}

	DevicePackManager mDevicePackManager = new DevicePackManager();
	private int mType = 0;



	public synchronized void write(byte[] buf, int count,
								   OutputStream pOutputStream) throws Exception {

		//PrintBytes.printData(buf, count);
		int state = mDevicePackManager.arrangeMessage(buf, count);
		switch (state) {
		case 1:// 接收成功
			if (mDevicePackManager.m_DeviceDatas.size() > 0) {
				int _saveNum = 0;


				JSONObject jsonResult=new JSONObject();
				JSONArray jsonDeviceData=new JSONArray();
				try
				{

					int iLength=mDevicePackManager.m_DeviceDatas.size();

					for (int i = 0; i < iLength; i++) {

						Log.d(TAG, String.valueOf(  mDevicePackManager.m_DeviceDatas.get(i).m_data));

						JSONObject data = new JSONObject();

						data.put("temperature", String.valueOf(mDevicePackManager.m_DeviceDatas.get(i).m_data));

						jsonDeviceData.put(data);

					}


					jsonResult.put("DataCount",iLength);
					jsonResult.put("DeviceData",jsonDeviceData);





				}
				catch (JSONException e){
					e.printStackTrace();
				}
				callBack(jsonResult);


			} else {}

			pOutputStream.write(DeviceCommand.command_delData());
			break;
		case 2:// 接收失败
			break;
		case 3:// set time success
						pOutputStream.write(DeviceCommand.command_queryDataNum());
			break;
		case 4:// set time failed
			break;
		case 5://del data success
			Log.e(TAG, "删除成功");
			break;
		case 6://del data failed
			break;
		case 7:
			break;
		case 8:// 进行设备校
			pOutputStream.write(DeviceCommand.command_VerifyTime());
			break;
		case 9:// 设备有数据 发送请求所有数据的命令
			pOutputStream.write(DeviceCommand.command_requestAllData());
			break;
		}
	}

	@Override
	public byte[] upCommand() {
		return DeviceCommand.commandConfirmEquipment();
	}

	String PATH_BASE = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/contec";

	/**
	 * 接收到的数据存数到文件中
	 * 
	 * @param pContent
	 */
	public void saveAsString(String pContent) {
		File _file = new File(PATH_BASE);
		if (!_file.exists()) {
			_file.mkdirs();
		}
		try {
			// OutputStreamWriter os=new FileOutputStream(_file+"trend.txt");
			OutputStreamWriter os = new OutputStreamWriter(
					new FileOutputStream(_file + "/EW_Data.txt"));
			os.write(pContent);
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
