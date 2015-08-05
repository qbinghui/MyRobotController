package bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import common.GlobalSetting;

/**
 * 蓝牙控制类
 * Created by zhuhp on 15/7/13.
 */
public enum BluetoothManager {
    INSTANCE;

    private BluetoothSPP mBlueSpp;
    private Context mContext;
    private IBlueConnectListener mBlueConnectListener;

    private String mBlueAddress;

    /**
     * 蓝牙初始化
     * @param context
     */
    public synchronized boolean init(Context context){
        mContext = context;
        if(mBlueSpp == null) {
            mBlueSpp = new BluetoothSPP(mContext);
        }
        mBlueSpp.setBluetoothConnectionListener(mConnectListener);
        mBlueSpp.setOnDataReceivedListener(mDataReceiverListener);
        return mBlueSpp.isBluetoothAvailable();
    }

    public void setConnectListener(IBlueConnectListener listener){
        mBlueConnectListener = listener;
    }

    /**
     * 启动蓝牙服务，链接的不是Android手机
     * @param context
     */
    public void startBlueService(Activity context){
        if (!mBlueSpp.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if(!mBlueSpp.isServiceAvailable()) {
                mBlueSpp.setupService();
                mBlueSpp.startService(GlobalSetting.BluetoothSetting.BLUE_TYPE);
            }
        }
    }

    public void stopBlueService(){
        if(mBlueSpp != null){
            mBlueSpp.stopService();
        }
    }

    public void enableBlue(){
        if(mBlueSpp == null){
            return;
        }
        mBlueSpp.setupService();
        mBlueSpp.startService(GlobalSetting.BluetoothSetting.BLUE_TYPE);
    }

    /**
     * 判断蓝牙是否可用
     * @return
     */
    public boolean isBlueAvailable(){
        if(mBlueSpp != null){
            return mBlueSpp.isBluetoothAvailable();
        }
        return false;
    }

    public void connect(String address){
        if(mBlueSpp == null){
            return;
        }
        mBlueSpp.connect(address);
    }

    public void connect(Intent data){
        if(mBlueSpp == null){
            return;
        }
        mBlueSpp.connect(data);
    }

    public void disconnect(){
        if(mBlueSpp != null) {
            mBlueSpp.disconnect();
        }
    }

    public void sendData(String cmd){
        if(mBlueSpp == null){
            return;
        }
        mBlueSpp.send(cmd,true);
    }

    private BluetoothSPP.BluetoothConnectionListener mConnectListener =
            new BluetoothSPP.BluetoothConnectionListener() {
        @Override
        public void onDeviceConnected(String name, String address) {
//            Toast.makeText(mContext
//                    , "Connected to " + name + "\n" + address
//                    , Toast.LENGTH_SHORT).show();
            if(mBlueConnectListener != null){
                mBlueConnectListener.onConnected(name,address);
            }
        }

        @Override
        public void onDeviceDisconnected() {
//            Toast.makeText(mContext
//                    , "Connection lost", Toast.LENGTH_SHORT).show();
            if(mBlueConnectListener != null){
                mBlueConnectListener.onDisconnected();
            }
        }

        @Override
        public void onDeviceConnectionFailed() {
//            Toast.makeText(mContext
//                    , "Unable to connect", Toast.LENGTH_SHORT).show();
            if(mBlueConnectListener != null){
                mBlueConnectListener.onConnectFailed();
            }
        }
    };

    private BluetoothSPP.OnDataReceivedListener mDataReceiverListener = new BluetoothSPP.OnDataReceivedListener() {
        @Override
        public void onDataReceived(byte[] data, String message) {
//            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            if(mBlueConnectListener != null) {
                mBlueConnectListener.onRecvMsg(message);

            }
        }
    };
}
