package com.staterra.staterrainjectionsystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import static com.staterra.staterrainjectionsystem.WelcomeScreen.*;

public class MyBlueTooth extends Service {
    private final IBinder mBinder = new LocalBinder();
    GlobalData globalData;
    AndroidFileWriter writer = new AndroidFileWriter();
	private static final int REQUEST_ENABLE_BT = 1;
    private int uploadProgress = 0;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mmDevice;
	OutputStream mmOutputStream;
	InputStream mmInputStream;
	BluetoothSocket mmSocket;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
    String data;
	volatile boolean stopWorker;
    public boolean isConnected = false;
	volatile boolean isWriting = false;
    volatile boolean getSystemStatus = false;
    private int systemStatusCount = 0;
    final private int DATAMAX = 4;
    //0  =  tamper
    //1  =  battery life
    //2  =  nutrient used
    //3  =  nutrient left
    private String systemStatusArr[] = new String[DATAMAX];

    public class LocalBinder extends Binder {
        MyBlueTooth getService() {
            return MyBlueTooth.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        findBT();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void findBT()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {

		}else{
		    enableBT();
		}
	}

	public void enableBT(){
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		}else{
			setPaired();
		}
	}

	public void setPaired(){
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
	    if(pairedDevices.size() > 0)
	    {
	        for(BluetoothDevice device : pairedDevices)
	        {
	            if(device.getName().equals("DL921600K1")){
                    mmDevice = device;
                    openBT();
                    break;
                }
	        }
	    }
	}

	public void openBT(){
	    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        try{
	    	mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
		    mmSocket.connect();
		    mmOutputStream = mmSocket.getOutputStream();
		    mmInputStream = mmSocket.getInputStream();
            Notification n  = new Notification.Builder(this)
                    .setContentTitle("Staterra BlueTooth")
                    .setContentText("Connected")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher).build();
            notificationManager.notify(0, n);
            listenForData();
            isConnected = true;
            getSystemStatus();
	    }catch(Exception IOException){
            Notification n  = new Notification.Builder(this)
                    .setContentTitle("Staterra BlueTooth")
                    .setContentText("Not Connected")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .addAction(R.drawable.ic_launcher, "Retry", pIntent).build();
            notificationManager.notify(0, n);
	    }

    }
	
	public void listenForData()
	{
	    final Handler handler = new Handler(); 
	    final byte delimiter = 10; //This is the ASCII code for a newline character

	    stopWorker = false;
	    readBufferPosition = 0;
	    readBuffer = new byte[1024];
	    workerThread = new Thread(new Runnable()
	    {
	        public void run()
	        {                
	           while(!Thread.currentThread().isInterrupted() && !stopWorker)
	           {
	                try 
	                {
	                    int bytesAvailable = mmInputStream.available();                        
	                    if(bytesAvailable > 0)
	                    {
	                        byte[] packetBytes = new byte[bytesAvailable];
	                        mmInputStream.read(packetBytes);
	                        for(int i=0;i<bytesAvailable;i++)
	                        {
	                            byte b = packetBytes[i];
	                            if(b == delimiter)
	                            {
								     byte[] encodedBytes = new byte[readBufferPosition];
								     System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
								     data = new String(encodedBytes, "US-ASCII");
								     readBufferPosition = 0;

	                                handler.post(new Runnable()
	                                {
	                                    public void run()
	                                    {
                                            //System.out.println(data);
                                            if(uploadProgress < 99){
                                                uploadProgress++;
                                            }
                                            if(data.length() > 2){
                                                String temp = "";
                                                temp+= data.charAt(0);
                                                temp+= data.charAt(1);
                                                temp+= data.charAt(2);
                                                if(temp.equalsIgnoreCase("EOF")){
                                                    uploadProgress = 100;
                                                    System.out.println("Progress = 100");
                                                    writer.write("End File");
                                                    isWriting = false;
                                                }
                                            }
                                            if(isWriting){
                                                writer.write(data);
	                                    	}else if(getSystemStatus){
                                                systemStatusArr[systemStatusCount++] = data;
                                                if(systemStatusCount == DATAMAX){
                                                    getSystemStatus = false;
                                                    systemStatusCount = 0;
                                                }else{

                                                }
	                                    	}
	                                    }
	                                });
	                            }
	                            else
	                            {
                                    readBuffer[readBufferPosition++] = b;
	                            }
	                        }
	                    }
	                } 
	                catch (IOException ex) 
	                {
	                    stopWorker = true;
	                }
	           }
	        }
	    });

	    workerThread.start();
	}
	
	public void sendData(String x) throws IOException
	{
		if(x.equals("info")){
			mmOutputStream.write(1);
		}else if(x.equals("read")){
			mmOutputStream.write(2);
		}else if(x.equals("delete")){
            mmOutputStream.write(3);
		}else if(x.equals("collect")){
            mmOutputStream.write(4);
		}else if(x.equals("stop")){
            mmOutputStream.write(5);
		}
        mmOutputStream.flush();
	}

    public void getSystemStatus() throws IOException{
        getSystemStatus = true;
        mmOutputStream.write(20);
    }

    public void getDataFile() throws IOException{
        isWriting = true;
        mmOutputStream.write(10);
    }

    public int getProgress(){
        return uploadProgress;
    }

	public void stopBT() throws IOException
	{
	    stopWorker = true;
	    mmOutputStream.close();
	    mmInputStream.close();
	    mmSocket.close();
        isConnected = false;
        //Toast.makeText(mainActivity.getApplicationContext(), "BlueTooth Closed", Toast.LENGTH_SHORT).show();
	}

    public void restartProgress(){
        uploadProgress = 0;
    }

    public boolean isConnected(){
        return isConnected;
    }


    //0  =  tamper
    //1  =  battery life
    //2  =  nutrient used
    //3  =  nutrient left
    public String getTamper(){
        if(systemStatusArr[0] != null){
            return systemStatusArr[0];
        }else{
            return "No Data";
        }
    }

    public String getBatteryLife(){
        if(systemStatusArr[1] != null){
            return systemStatusArr[1];
        }else{
            return "No Data";
        }
    }

    public String getNutrUsed(){
        if(systemStatusArr[2] != null){
            return systemStatusArr[2];
        }else{
            return "No Data";
        }
    }

    public String getNutrLeft(){
        if(systemStatusArr[3] != null){
            return systemStatusArr[3];
        }else{
            return "No Data";
        }
    }

}
