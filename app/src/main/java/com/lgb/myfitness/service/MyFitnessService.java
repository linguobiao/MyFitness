package com.lgb.myfitness.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.lgb.myfitness.global.Global;
import com.lgb.myfitness.helper.ParserHelper;

public class MyFitnessService extends Service {

	private String TAG = "MyFitnessService";

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;

	private String mBluetoothDeviceAddress;

	public String getDeviceAddress() {
		return mBluetoothDeviceAddress;
	}

	private int mConnectionState = STATE_DISCONNECTED;

	public int getConnectionState() {
		return mConnectionState;
	}

	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "myfitness.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_CONNECTED_FAIL = "myfitness.ACTION_GATT_CONNECTED_FAIL";
	public final static String ACTION_GATT_DISCONNECTED = "myfitness.ACTION_GATT_DISCONNECTED";
	
	public final static String ACTION_DEVICE_FOUND = "myfitness.ACTION_DEVICE_FOUND";
	
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "myfitness.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_GATT_SERVICES_DISCOVER_FAIL = "myfitness.ACTION_GATT_SERVICES_DISCOVER_FAIL";
	
	public final static String ACTION_WRITE_DESCRIPTOR_SUCCESS = "myfitness.ACTION_WRITE_DESCRIPTOR_SUCCESS";
	public final static String ACTION_WRITE_DESCRIPTOR_FAIL = "myfitness.ACTION_WRITE_DESCRIPTOR_FAIL";
	public final static String ACTION_WRITE_CHARACTERISTIC_SUCCESS = "myfitness.ACTION_WRITE_CHARACTERISTIC_SUCCESS";
	public final static String ACTION_WRITE_CHARACTERISTIC_FAIL = "myfitness.ACTION_WRITE_CHARACTERISTIC_FAIL";
	
	public final static String ACTION_READ_REMOVE_RSSI = "myfitness.READ_REMOVE_RSSI";
	public final static String ACTION_DATA_AVAILABLE = "myfitness.ACTION_DATA_AVAILABLE";
//	public final static String ACTION_RECEIVE_SCALES = "myfitness.ACTION_RECEIVE_SCALES"; 
	public final static String ACTION_RETURN_SPECIAL_KEY = "myfitness.ACTION_RETURN_SPECIAL_KEY";
	
	public final static String KEY_NOTIFY_DATA = "myfitness.UPADTEDATA";
	public final static String KEY_RSSI_VALUE = "myfitness.KEY_RSSI_VALUE";
	

	public class LocalBinder extends Binder {
		public MyFitnessService getService() {
			return MyFitnessService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		close();
		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();

	
	@Override
	public void onCreate() {

		initialize();
	}
	
	
	/**
	 * 蓝牙回调函数
	 */
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				
				if (status == BluetoothGatt.GATT_SUCCESS) {
					Log.i(TAG, "Connected to GATT server.");
					mConnectionState = STATE_CONNECTED;
					broadcastUpdate(ACTION_GATT_CONNECTED);

					// Attempts to discover services after successful connection.
					if (mBluetoothGatt != null) {
						Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
					}
				} else {
					mConnectionState = STATE_DISCONNECTED;
					disconnect();
					
					broadcastUpdate(ACTION_GATT_CONNECTED_FAIL);
					close();
				}
				
				
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.i(TAG, "Disconnected from GATT server.");
				mConnectionState = STATE_DISCONNECTED;	
				mBluetoothDeviceAddress = null;
				
				broadcastUpdate(ACTION_GATT_DISCONNECTED);
				close();
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.i(TAG, "onServicesDiscovered received: " + status);
			
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
				
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVER_FAIL);
			}
		}	
		

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			UUID uuid = characteristic.getUuid();
			if (uuid.equals(Global.UUID_CHARACTERISTIC_COMMUNICATION)) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_SUCCESS);
					
				} else {
					broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_FAIL);
				}
				
			} else if (uuid.equals(Global.UUID_CHARACTERISTIC_WRITE_BPM)) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_SUCCESS);
					
				} else {
					broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_FAIL);
				}
			}
			
		};

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			Log.i(TAG, "*****广播数据 ***** " + Arrays.toString(characteristic.getValue()));
//			Log.i(TAG, Arrays.toString(characteristic.getValue()));
			boolean specialKey = ParserHelper.isSpecialKey(characteristic.getValue());
			if (specialKey) {
				broadcastUpdate(ACTION_RETURN_SPECIAL_KEY);
			} else {
				broadcastUpdate(ACTION_DATA_AVAILABLE, KEY_NOTIFY_DATA, characteristic.getValue());
			}
			
		}
		
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_WRITE_DESCRIPTOR_SUCCESS);
				
			} else {
				broadcastUpdate(ACTION_WRITE_DESCRIPTOR_FAIL);
			}
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			Log.i(TAG, "rssi value: " + rssi);
			broadcastRSSI(rssi);
		}
	};
	
	
	/**
	 * 发送广播
	 * @param action
	 */
	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}
	
	
	private void broadcastUpdate(String action, String key, byte[] value) {

		Intent intent = new Intent(action);
		intent.putExtra(key, value);
		sendBroadcast(intent);
	}
	
	
	private void broadcastRSSI(int rssi) {
		Intent intent = new Intent(ACTION_READ_REMOVE_RSSI);
		intent.putExtra(KEY_RSSI_VALUE, rssi);
		sendBroadcast(intent);
	}

	

	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address, boolean is) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
//		if (mBluetoothDeviceAddress != null
//				&& address.equals(mBluetoothDeviceAddress)
//				&& mBluetoothGatt != null) {
//			Log.d(TAG,
//					"Trying to use an existing mBluetoothGatt for connection.");
//			if (mBluetoothGatt.connect()) {
//				Log.i(TAG, "connection attempt was initiated successfully");
//				mConnectionState = STATE_CONNECTING;
//				return true;
//			} else {
//				Log.i(TAG, "connection attempt was initiated fail");
//				return false;
//			}
//		}

		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}


	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}
	
	/**
	 * Read the RSSI for a connected remote device.
	 * */
	public boolean getRssiVal() {
		if (mBluetoothGatt == null)
			return false;

		return mBluetoothGatt.readRemoteRssi();
	}

	
	/**
	 * 写特征值
	 * @param characteristic
	 */
	public void wirteCharacteristic(BluetoothGattCharacteristic characteristic) {

		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.writeCharacteristic(characteristic);

	}
	
	
	public void writeCharacteristic(UUID uuid_service, UUID uuid_character, byte[] value) {
		// service
		BluetoothGattService mBluetoothGattService = getBluetoothGattService(mBluetoothGatt, uuid_service);
		// characteristic
		BluetoothGattCharacteristic mBluetoothGattCharacteristic = getBluetoothGattCharacteristic(mBluetoothGattService, uuid_character);

		if (mBluetoothGatt != null && mBluetoothGattCharacteristic != null) {
			mBluetoothGattCharacteristic.setValue(value);
			mBluetoothGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
			mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic);
			

		} else if (mBluetoothGatt == null) {
			Log.i(TAG, "mBluetoothGatt is null");
		} else if (mBluetoothGattCharacteristic == null) {
			Log.i(TAG, "mBluetoothGattCharacteristic is null");
		}		
	}
	
	
	public void writeDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
	}
	
	
	/**
	 * 读特征值
	 * @param characteristic
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//		if (descriptor != null) {
//			System.out.println("write descriptor");
//			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//			mBluetoothGatt.writeDescriptor(descriptor);
//		}
		/*
		 * // This is specific to Heart Rate Measurement. if
		 * (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
		 * System
		 * .out.println("characteristic.getUuid() == "+characteristic.getUuid
		 * ()+", "); BluetoothGattDescriptor descriptor =
		 * characteristic.getDescriptor
		 * (UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
		 * descriptor
		 * .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		 * mBluetoothGatt.writeDescriptor(descriptor); }
		 */
	}
	
	
	/**
	 * 设置通知属性为true，并写上descriptor
	 * 
	 * @param uuid_service
	 * @param uuid_characteristic
	 * @param uuid_descriptor
	 */
	public void setCharactoristicNotifyAndWriteDescriptor(UUID uuid_service, UUID uuid_characteristic, UUID uuid_descriptor) {
		// service
		BluetoothGattService mBluetoothGattService = getBluetoothGattService(mBluetoothGatt, uuid_service);
		// characteristic
		BluetoothGattCharacteristic mBluetoothGattCharacteristic = getBluetoothGattCharacteristic(mBluetoothGattService, uuid_characteristic);

		if (mBluetoothGatt != null && mBluetoothGattCharacteristic != null) {
			// 设置
			mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristic, true);

			BluetoothGattDescriptor bluetoothGattDescriptor = mBluetoothGattCharacteristic.getDescriptor(uuid_descriptor);
			if (bluetoothGattDescriptor != null) {
				bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				mBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
			}

		} else if (mBluetoothGatt == null) {
			Log.i(TAG, "mBluetoothGatt is null");
		} else if (mBluetoothGattCharacteristic == null) {
			Log.i(TAG, "mBluetoothGattCharacteristic is null");
		}
	}
	
	
	/**
	 * 设置通知属性为true，并写上descriptor
	 * 
	 * @param uuid_service
	 * @param uuid_characteristic
	 * @param uuid_descriptor
	 */
	public void setCharactoristicNotify(UUID uuid_service, UUID uuid_characteristic) {
		// service
		BluetoothGattService mBluetoothGattService = getBluetoothGattService(mBluetoothGatt, uuid_service);
		// characteristic
		BluetoothGattCharacteristic mBluetoothGattCharacteristic = getBluetoothGattCharacteristic(mBluetoothGattService, uuid_characteristic);

		if (mBluetoothGatt != null && mBluetoothGattCharacteristic != null) {
			// 设置
			mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristic, true);
		} else if (mBluetoothGatt == null) {
			Log.i(TAG, "mBluetoothGatt is null");
		} else if (mBluetoothGattCharacteristic == null) {
			Log.i(TAG, "mBluetoothGattCharacteristic is null");
		}
	}
	
	
	/**
	 * 获取bluetoothGattService
	 * 
	 * @param mBluetoothGatt
	 * @param UUID_SERVICE
	 * @return
	 */
	private BluetoothGattService getBluetoothGattService(BluetoothGatt mBluetoothGatt, UUID UUID_SERVICE) {
		if (mBluetoothGatt != null) {
			BluetoothGattService mBluetoothGattServer = mBluetoothGatt.getService(UUID_SERVICE);
			if (mBluetoothGattServer != null) {
				return mBluetoothGattServer;
			} else {
				Log.i(TAG, "getBluetoothGattService, bluetoothgatt get service uuid:" + UUID_SERVICE + " is null");
			}
		} else {
			Log.i(TAG, "mBluetoothGatt is null");
		}

		return null;
	}

	/**
	 * 获取bluetoothGattCharacteristic
	 * 
	 * @param mBluetoothGattService
	 * @param UUID_CHARACTERISTIC
	 * @return
	 */
	private BluetoothGattCharacteristic getBluetoothGattCharacteristic(BluetoothGattService mBluetoothGattService, UUID UUID_CHARACTERISTIC) {
		if (mBluetoothGattService != null)
		{
			BluetoothGattCharacteristic mBluetoothGattCharacteristic = mBluetoothGattService.getCharacteristic(UUID_CHARACTERISTIC);

			if (mBluetoothGattCharacteristic != null) {
				return mBluetoothGattCharacteristic;
			} else {
				Log.i(TAG, "getBluetoothGattCharacteristic, bluetoothGattServer get characteristic uuid:" + UUID_CHARACTERISTIC + " is null");
			}
		} else {
			Log.i(TAG, "mBluetoothGattServer is null");
		}

		return null;
	}
	

	/**
	 * 扫描设备
	 * @param start
	 */
	public void scan(boolean start) {
		if (mBluetoothAdapter != null) {
			if (start) {
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			} else {
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}
		} else {
			Log.i(TAG, "bluetoothadapter is null");
		}
	}

	/**
	 * 扫描设备的回调方法
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			Bundle mBundle = new Bundle();
			mBundle.putParcelable(BluetoothDevice.EXTRA_DEVICE, device);
			mBundle.putInt(BluetoothDevice.EXTRA_RSSI, rssi);

			Intent intent = new Intent();
			intent.setAction(ACTION_DEVICE_FOUND);
			intent.putExtras(mBundle);
			sendBroadcast(intent);
		}
	};

	
	public void setBandNotifyTrue() {
		setCharactoristicNotifyAndWriteDescriptor(
				Global.UUID_SERVICE, 
				Global.UUID_CHARACTERISTIC_COMMUNICATION, 
				Global.UUID_DESCRIPTOR_CONFIGURATION);
	}
	
	
	public void setScaleNotifyTrue() {
		setCharactoristicNotifyAndWriteDescriptor(
				Global.UUID_SERVICE, 
				Global.UUID_CHARACTERISTIC_COMMUNICATION_SCALE, 
				Global.UUID_DESCRIPTOR_CONFIGURATION);
	}

	
	public void requestAllBLEData(Context context) {
		byte[] value = ParserHelper.requestAllData(context, Global.TYPE_COMMUNICATE);
		
		writeCharacteristic(
				Global.UUID_SERVICE, 
				Global.UUID_CHARACTERISTIC_COMMUNICATION, 
				value);
	}
	
	
	public void request1HourBLEData(int hourLabel) {
		byte[] value = ParserHelper.request1HourBleData(hourLabel);
		
		writeCharacteristic(
				Global.UUID_SERVICE, 
				Global.UUID_CHARACTERISTIC_COMMUNICATION, 
				value);
	}
	
	
	public void requestScreenData() {
		byte[] value = ParserHelper.requestScreenData();
		writeCharacteristic(
				Global.UUID_SERVICE, 
				Global.UUID_CHARACTERISTIC_COMMUNICATION, 
				value);
	}
	
	
	public void setUnitData(Context context) {
		byte[] value = ParserHelper.setUnitData(context);
		writeCharacteristic(
				Global.UUID_SERVICE, 
				Global.UUID_CHARACTERISTIC_COMMUNICATION, 
				value);
	}
	
	
	public void setLostModeRSSI(int averageRSSI, int lostRSSI) {
		byte[] value = ParserHelper.setLostModeData(averageRSSI, lostRSSI);
		writeCharacteristic(
				Global.UUID_SERVICE, 
				Global.UUID_CHARACTERISTIC_LOST_MODE, 
				value);
	}
	
	
	public void setBPMTestStart() {
		int order = 0xa1;
		int xor = 0x3c;
		byte[] value = ParserHelper.setBpmWriteData(order, xor);
		
		writeCharacteristic(
				Global.UUID_SERVICE_BPM, 
				Global.UUID_CHARACTERISTIC_WRITE_BPM, 
				value);
	}
	
	
	public void setBPMTestStop() {
		int order = 0xa2;
		int xor = 0x3f;
		byte[] value = ParserHelper.setBpmWriteData(order, xor);
		
		writeCharacteristic(
				Global.UUID_SERVICE_BPM, 
				Global.UUID_CHARACTERISTIC_WRITE_BPM, 
				value);
	}
	
	
	public void setBPMMusicClose() {
		int order = 0xa3;
		int xor = 0x3e;
		byte[] value = ParserHelper.setBpmWriteData(order, xor);
		
		writeCharacteristic(
				Global.UUID_SERVICE_BPM, 
				Global.UUID_CHARACTERISTIC_WRITE_BPM, 
				value);
	}
	
	
	public void setBPMMusicOpen() {
		int order = 0xa4;
		int xor = 0x39;
		byte[] value = ParserHelper.setBpmWriteData(order, xor);
		
		writeCharacteristic(
				Global.UUID_SERVICE_BPM, 
				Global.UUID_CHARACTERISTIC_WRITE_BPM, 
				value);
	}
	
	
	public void setBPMNotifyTrue() {
		setCharactoristicNotifyAndWriteDescriptor(
				Global.UUID_SERVICE_BPM, 
				Global.UUID_CHARACTERISTIC_NOTIFY_BPM, 
				Global.UUID_DESCRIPTOR_CONFIGURATION);
	}
	
	
	public void setBPMLanguage(int languageMark) {
		byte[] value = new byte[7];
		value[0] = 0x02;
		value[1] = 0x40;
		value[2] = (byte) 0xdc;
		value[3] = 0x02;
		value[4] = (byte) 0xa6;
		value[5] = (byte) languageMark;
		value[6] = (byte) (value[1] ^ value[2] ^ value[3] ^ value[4] ^ value[5]);
		
//		int i1 = 0;
//		int i2 = 0;
//		int i3 = 0;
//		int i4 = 0;
//		int i5 = 0;
//		if (value[1] < 0) {
//			i1 = 256 + value[1];
//		}
//		if (value[2] < 0) {
//			i2 = 256 + value[2];
//		}
//		if (value[3] < 0) {
//			i3 = 256 + value[3];
//		}
//		if (value[4] < 0) {
//			i4 = 256 + value[4];
//		}
//		if (value[5] < 0) {
//			i5 = 256 + value[5];
//		}
//		
//		int value1 = (byte) (i1 ^ i2 ^ i3 ^ i4 ^ i5);
//		int value2 = 0x40 ^ 0xdc ^ 0x02 ^ 0xa6 ^ 0x02;
		
		
		Log.i(TAG, "set bpm language write data:" + Arrays.toString(value));
//		System.out.println("value1:" + value1 + ", value2:" + value2);
		writeCharacteristic(
				Global.UUID_SERVICE_BPM, 
				Global.UUID_CHARACTERISTIC_WRITE_BPM, 
				value);
	}
}
