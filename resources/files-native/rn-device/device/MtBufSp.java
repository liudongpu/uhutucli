package com.[@config:appReact.workName].device;

import android.os.Environment;
import android.util.Log;

import com.contec.cms50dj_jar.DeviceCommand;
import com.contec.cms50dj_jar.DeviceData50DJ_Jar;
import com.contec.cms50dj_jar.DevicePackManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class MtBufSp extends AMt implements  IMt{
	private static final String TAG = "device.MtBufSp";
	public static Vector<Integer> m_buf = null;
	private DevicePackManager m_DevicePackManager = new DevicePackManager();

	MtBufSp() {
		m_buf = new Vector<Integer>();
	}

	public synchronized int Count() {
		return m_buf.size();
	}

	int mSettimeCount = 0;

	public synchronized void write(byte[] buf, int count,
			final OutputStream pOutputStream) throws Exception {
		int _receiveNum = m_DevicePackManager.arrangeMessage(buf, count);
		switch (_receiveNum) {
		case 1:// 得到设备号 发送校时命令
				// DeviceManager.m_DeviceBean.mProgress = 5;
				// App_phms.getInstance().mEventBusPostOnBackGround
				// .postInMainThread(DeviceManager.m_DeviceBean);
			/*
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						pOutputStream.write(DeviceCommand.correctionDateTime());
						Log.i(TAG, "得到设备号 发送校时命令");
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			*/
			pOutputStream.write(DeviceCommand.correctionDateTime());
			Log.i(TAG, "得到设备号 发送校时命令");
			break;
		case 2:// 对时成功
				// DeviceManager.m_DeviceBean.mProgress = 10;
				// App_phms.getInstance().mEventBusPostOnBackGround
				// .postInMainThread(DeviceManager.m_DeviceBean);
			/*
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
						pOutputStream.write(DeviceCommand.setPedometerInfo(
								"175", "75", 0, 24, 10000,
								1));
						Log.i(TAG, "对时成功  设置计步器信息");
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			*/

			pOutputStream.write(DeviceCommand.getDataFromDevice());

			break;
		case 3:// 对时失败 关闭socket
			Log.i(TAG, "对时间失败");
			break;
		case 4:// 无新数据;
			new Thread() {
				public void run() {
					try {
						Thread.sleep(500);
//						pOutputStream.write(DeviceCommand
//								.dayPedometerDataCommand());
						Log.i(TAG, "血氧无新数据  请求以天为单位的 计步器数据");
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();

			break;
		case 5: // 整个数据接收完成
			Log.i(TAG, " 整个血氧数据接收完成  发送以天为单位请求数据命令");


			JSONArray jsonDeviceData1=new JSONArray();

			DeviceData50DJ_Jar _djData = m_DevicePackManager.getDeviceData50dj();

			JSONObject jsonResult=new JSONObject();

			try
			{

				int iLength=_djData.getmSp02DataList().size();

			for (int i = 0; i < iLength; i++) {
				byte[] _data = _djData.getmSp02DataList().get(i);
				Log.d(TAG, "year:" + _data[0] + " month:" +_data[1] + "  day:" +_data[2] + " hour:" + _data[3] + "  min:" + _data[4] + "  second:" + _data[5] + "  spo:" + _data[6] + "  pluse:" + _data[7]);

				JSONObject data = new JSONObject();

				data.put("Oxygen", String.valueOf( _data[6]));
				data.put("PulseRate",String.valueOf(_data[7]));
				jsonDeviceData1.put(data);

			}
			JSONObject jsonPulse=new JSONObject();

				jsonPulse.put("DataCount1",iLength);
				jsonPulse.put("DeviceData1",jsonDeviceData1);


				jsonResult.put("OxygenPulse",jsonPulse);


			}
			catch (JSONException e){
				e.printStackTrace();
			}
			callBack(jsonResult);





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
		case 6:// 一包数据接收完毕 ；
			Log.i(TAG, "一包血氧数据接收完毕 ");
			pOutputStream.write(DeviceCommand.dataUploadSuccessCommand ());
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.dataUploadSuccessCommand());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
			break;
		case 7:// 接收失败
			Log.i(TAG, "血氧数据接收失败  请求以天为单位的 计步器数据");
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.dayPedometerDataCommand());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

			break;
		case 8:// 8:设置计步器 成功
				// DeviceManager.m_DeviceBean.mProgress = 20;
				// App_phms.getInstance().mEventBusPostOnBackGround
				// .postInMainThread(DeviceManager.m_DeviceBean);
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand.getDataFromDevice());
//						Log.i(TAG, "设置计步器 成功  发送 请求血氧数据命令");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

			break;
		case 9:// 9: 设置计步器失败
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand.getDataFromDevice());
//						Log.i(TAG, "设置计步器 失败    发送 请求血氧数据命令 ");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
			break;
		case 10:// 以天为单位 计步器数据 一包上传完成
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.dayPedometerDataSuccessCommand());
//						Log.i(TAG, "以天为单位计步器 数据 一包上传完成  发送上传完成命令");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

			break;
		case 11:// 以天为单位计步器 数据上一包上传成功 请求下一包数据

//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.dayPedometerDataCommand());
//						Log.i(TAG, "以天为单位计步器 数据上一包上传成功  请求下一包数据 ");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

			break;
		case 12:// 以天为单位计步器 数据 全部 上传成功 请求以分为单位的数据
			// TODO 此处处理计步器一天为单位的数据
//			saveDaypedometerData();
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//						Log.i(TAG, "以天为单位计步器 数据  全部   上传成功  请求以分为单位的数据");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
			break;
		case 13:// 以天为单位计步器 数据上传失败 请求以分为单位的数据
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//						Log.i(TAG, " 以天为单位计步器 数据上传失败 请求以分为单位的数据");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

			break;
		case 14:// 以分为单位 计步器数据 一包上传完成
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataSuccessCommand());
//						Log.i(TAG, "以 分 为单位 计步器数据 一包上传完成 发送上传完成命令");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

			break;
		case 15:// 以分为单位 计步器数据 一包上传完成
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//						Log.i(TAG, "以分为单位计步器 数据 一包上传完成  发送请求下一包的命令");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
			break;
		case 16:
			// 以分为单位计步器 数据 全部 上传成功 关闭socket
			// TODO 存储以分为单位的数据
//			savePedometerMindata();
//			App_phms.getInstance().mEventBusPostOnBackGround
//					.postInMainThread(DeviceManager.m_DeviceBean);
			Log.i(TAG, " 以分为单位计步器 数据 全部 上传成功 关闭socket ");
			break;
		case 17:// 以天为单位 计步器无新数据
			Log.i(TAG, " 以天为单位 计步器无新数据    请求以分为单位的数据");
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						SendCommand.send(DeviceCommand
//								.minPedometerDataCommand());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();

			break;
		case 18:// 以分为单位 计步器无新数据
			Log.i(TAG, " 以分为单位 计步器无新数据   ");
			break;
		case 19:// 以分为单位 计步器数据上传失败
			Log.i(TAG, " 以分为单位 计步器数据上传失败   ");
			break;
		case 20:// 请求下一包血氧数据
//			new Thread() {
//				public void run() {
//					try {
//						Thread.sleep(500);
//						Log.i(TAG, " 请求下一包血氧数据   ");
//						SendCommand.send(DeviceCommand.getDataFromDevice());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
			break;
		}
	}

	@Override
	public byte[] upCommand() {
		return DeviceCommand.deviceConfirmCommand();
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
					new FileOutputStream(_file + "/Cmssxt_Data.txt", true));
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
