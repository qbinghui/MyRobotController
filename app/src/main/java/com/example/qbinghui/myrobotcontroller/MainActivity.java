package com.example.qbinghui.myrobotcontroller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import bluetooth.BluetoothPresent;
import bluetooth.IBlueConnectListener;
import bluetooth.IBluetoothPresent;
import utils.FileUtils;


public class MainActivity extends Activity implements View.OnClickListener, IBlueConnectListener{
    private static final String TAG = "MainActivity";
    private SeekBar seekBar, seekBar2, seekBar3, seekBar4, seekBar5, seekBar6, seekBar7, seekBar8, seekBar9;
    private TextView tv, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;
    private Button btnConnect, btnRgb, btnRun;
    private Button btnAdd, btnDel, btnClean, btnSave, btnImport;
    private EditText etRed, etGreen, etBlue;
    private EditText etOrders;
    private TextView tvConsole;
    private ArrayList<String> orders;
    private IBluetoothPresent mBluePresent;
    private String speed = "5";
    private static boolean bRuning = false;

    private final int FILE_SELECT_CODE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        mBluePresent = new BluetoothPresent();
        if(!mBluePresent.initBluetoothService(this)){
            Log.e(TAG, "Init Bluetooth Service error!");
            finish();
        }
        orders = new ArrayList<String>(100);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluePresent.startBluetoothService(this);
        mBluePresent.setConnectListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluePresent.stopBluetoothService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case BluetoothState.REQUEST_CONNECT_DEVICE:
                if(resultCode == RESULT_OK) {
                    mBluePresent.resolveBlueResult(data);
                }
                break;
            case BluetoothState.REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    mBluePresent.enableBlue();
                }else{
                    Toast.makeText(this
                            , R.string.blue_not_enable
                            , Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    importFile(path);
                }
                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void initUI(){
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar)findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar)findViewById(R.id.seekBar4);
        seekBar5 = (SeekBar)findViewById(R.id.seekBar5);
        seekBar6 = (SeekBar)findViewById(R.id.seekBar6);
        seekBar7 = (SeekBar)findViewById(R.id.seekBar7);
        seekBar8 = (SeekBar)findViewById(R.id.seekBar8);
        seekBar9 = (SeekBar)findViewById(R.id.seekBar9);
        tvConsole = (TextView)findViewById(R.id.tvConsole);

        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnRgb = (Button)findViewById(R.id.btnRgb);
        btnRun = (Button)findViewById(R.id.btnRun);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnImport = (Button)findViewById(R.id.btnImport);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnDel = (Button)findViewById(R.id.btnDel);
        btnClean = (Button)findViewById(R.id.btnClean);

        etRed = (EditText)findViewById(R.id.etRed);
        etGreen = (EditText)findViewById(R.id.etGreen);
        etBlue = (EditText)findViewById(R.id.etBlue);
        etOrders = (EditText)findViewById(R.id.etOrders);

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar3.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar4.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar5.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar6.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar7.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar8.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
        seekBar9.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());

        btnConnect.setOnClickListener(this);
        btnRgb.setOnClickListener(this);
        btnRun.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnClean.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnImport.setOnClickListener(this);

        tv = (TextView)findViewById(R.id.tv);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);
        tv6 = (TextView)findViewById(R.id.tv6);
        tv7 = (TextView)findViewById(R.id.tv7);
        tv8 = (TextView)findViewById(R.id.tv8);
        tv9 = (TextView)findViewById(R.id.tv9);

        tv.setText("90");
        tv2.setText("90");
        tv3.setText("90");
        tv4.setText("90");
        tv5.setText("90");
        tv6.setText("90");
        tv7.setText("90");
        tv8.setText("90");
        tv9.setText("90");

        seekBar.setProgress(50);
        seekBar2.setProgress(50);
        seekBar3.setProgress(50);
        seekBar4.setProgress(50);
        seekBar5.setProgress(50);
        seekBar6.setProgress(50);
        seekBar7.setProgress(50);
        seekBar8.setProgress(50);
        seekBar9.setProgress(50);

        tvConsole.append("Hello, welcome!");

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnect:
                mBluePresent.findBlue(this);
                btnConnect.setEnabled(false);
                break;
            case R.id.btnRgb:
                String lightOrders = etRed.getText().toString()+","+etGreen.getText().toString()+","+etBlue.getText().toString();
                sendBluetoothData(lightOrders, 'B');
                break;
            case R.id.btnAdd:
                String newOrders = tv.getText().toString()+","+speed+","+tv2.getText().toString()+","+speed+","+
                        tv3.getText().toString()+","+speed+","+
                        tv4.getText().toString()+","+speed+","+
                        tv5.getText().toString()+","+speed+","+
                        tv6.getText().toString()+","+speed+","+
                        tv7.getText().toString()+","+speed+","+
                        tv8.getText().toString()+","+speed+","+
                        tv9.getText().toString()+","+speed;

                orders.add(newOrders);
                etOrders.setText(orders.toString());
                break;
            case R.id.btnDel:
                orders.remove(orders.size()-1);
                etOrders.setText(orders.toString());
                break;
            case R.id.btnClean:
                orders.clear();
                etOrders.setText("");
                break;

            case R.id.btnRun:
                Log.d(TAG, "Run!");
                if(btnConnect.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bRuning)
                    Toast.makeText(getApplicationContext(), "正在运行，请等待动作执行完毕！", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "开始运行，请等待动作执行完毕！", Toast.LENGTH_SHORT).show();
                RunThread th = new RunThread();
                th.run();
                break;
            case R.id.btnImport:
                showFileChooser();
                break;
            case R.id.btnSave:
                Log.d(TAG, "Before save file!");
                saveFile();
                break;
        }
    }

    private static int mProgress = 0;
    private class OnSeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener{

        //触发操作，拖动
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            mProgress = progress;
            switch (seekBar.getId()){
                case R.id.seekBar:
                    tv.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar2:
                    tv2.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar3:
                    tv3.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar4:
                    tv4.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar5:
                    tv5.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar6:
                    tv6.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar7:
                    tv7.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar8:
                    tv8.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                case R.id.seekBar9:
                    tv9.setText(String.valueOf((int)(1.8*mProgress)));
                    break;
                default:
                    break;
            }
        }

        //表示进度条刚开始拖动，开始拖动时候触发的操作
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        //停止拖动时候
        public void onStopTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
            String servo = "0";
            String data = String.valueOf((int)(1.8*mProgress));

            switch (seekBar.getId()){
                case R.id.seekBar:
                    tv.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "0";
                    break;
                case R.id.seekBar2:
                    tv2.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "1";
                    break;
                case R.id.seekBar3:
                    tv3.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "2";
                    break;
                case R.id.seekBar4:
                    tv4.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "3";
                    break;
                case R.id.seekBar5:
                    tv5.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "4";
                    break;
                case R.id.seekBar6:
                    tv6.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "5";
                    break;
                case R.id.seekBar7:
                    tv7.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "6";
                    break;
                case R.id.seekBar8:
                    tv8.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "7";
                    break;
                case R.id.seekBar9:
                    tv9.setText(String.valueOf((int)(1.8*mProgress)));
                    servo = "8";
                    break;

                default:
                    break;
            }
            String order = servo+","+data;
            sendBluetoothData(order, 'A');

        }
    }


    private void sendBluetoothData(String data, char flag) {
        if(btnConnect.isEnabled()){
            Toast.makeText(getApplicationContext(), "请先连接设备！", Toast.LENGTH_SHORT).show();
        }else {
            mBluePresent.sendData(flag + data);
        }
    }


    private void saveFile(){
        Log.d(TAG, "Start save file!");
        if(orders.size()<=0)
            return;
        File destDir = new File(getSDPath() +"/RobotController/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String fileName = formatter.format(curDate);
        try {
            FileOutputStream outStream = new FileOutputStream(getSDPath() +"/RobotController/"+ fileName + ".txt", true);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "gb2312");
            for(int i=0;i<orders.size();i++){
                writer.write(orders.get(i));
                writer.write("\n");
            }
            writer.flush();
            writer.close();
            Toast.makeText(getApplication(), "保存成功："+getSDPath() +"/RobotController/"+fileName + ".txt", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void importFile(String filePath){

        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = null;
            inputStreamReader = new InputStreamReader(inputStream, "gb2312");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            orders.clear();
            etOrders.setText("");
            while ((line = reader.readLine()) != null) {
                line.replace("\n", "");
                orders.add(line);
                Log.i(TAG, "steps->"+line);
                etOrders.append(line+"\n");
            }
            inputStream.close();
            inputStreamReader.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Import"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }


    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.toString();
        }
        Log.e(TAG, "SD卡不存在！") ;
        return null;


    }



    @Override
    public void onConnected(String name, String address) {
        tvConsole.append("\n已连接到"+address);
    }

    @Override
    public void onConnectFailed() {
        tvConsole.append("\n蓝牙连接失败！");
        btnConnect.setEnabled(true);
    }

    @Override
    public void onDisconnected() {
        tvConsole.append("\n蓝牙连接已断开");
        btnConnect.setEnabled(true);
    }

    @Override
    public void onRecvMsg(String data) {
        tvConsole.append("\n接收到消息："+data);
    }

    class RunThread extends Thread{
    @Override
    public void run() {
        super.run();
//        try {
//            Thread.sleep(1000);
//        }catch (Exception e){
//
//        }
        bRuning = true;
//        orders.clear();
//        orders.add("73,5,16,5,180,5,180,5,28,5,75,5,28,5,73,5,9,5");
//        orders.add("73,5,16,5,180,5,180,5,28,5,75,5,28,5,39,5,0,5");
//        orders.add("73,5,16,5,180,5,180,5,28,5,93,5,59,5,39,5,0,5");
//        orders.add("73,5,16,5,180,5,180,5,28,5,93,5,48,5,70,5,16,5");
//        orders.add("73,5,16,5,180,5,180,5,28,5,93,5,48,5,91,5,54,5");
//        orders.add("73,5,16,5,180,5,180,5,28,5,52,5,3,5,91,5,28,5");
//        orders.add("73,5,16,5,180,5,180,5,28,5,52,5,3,5,77,5,16,5");

        for(int i=0;i<orders.size();i++) {
            sendBluetoothData(orders.get(i).toString(), 'C');
            Log.d(TAG, orders.get(i).toString());
            try {
                Thread.sleep(2000);
            }catch (Exception e) {
                Log.e(TAG, "Sleep error!");
            }

            bRuning = false;
        }
    }
}



}
