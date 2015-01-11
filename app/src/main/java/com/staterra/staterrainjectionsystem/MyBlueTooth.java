package com.staterra.staterrainjectionsystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class MyBlueTooth {
    MainActivity mainActivity;
    AndroidFileWriter writer = new AndroidFileWriter();
	private static final int REQUEST_ENABLE_BT = 1;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mmDevice;
	OutputStream mmOutputStream;
	InputStream mmInputStream;
	BluetoothSocket mmSocket;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	boolean isWriting = false;
	
	public MyBlueTooth(MainActivity mainActivity){
		this.mainActivity = mainActivity;
	}
	
	public void findBT()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
            Toast.makeText(mainActivity.getApplicationContext(), "Device is not BT Capable", Toast.LENGTH_SHORT).show();
		}else{
		    enableBT();
		}
	}
	//checks to see if BT is enabled, if it isn't, requests permission
	//**************COMPLETE*****************
	public void enableBT(){
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    mainActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}else{
			setPaired();
		}
	}
	
	//set the Paired device
	//***************COMPLETE*******************
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
	//Opens the bluetooth IO connection
	//****************COMPLETE********************
	public void openBT(){
	    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
	    try{
	    	mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
		    mmSocket.connect();
		    mmOutputStream = mmSocket.getOutputStream();
		    mmInputStream = mmSocket.getInputStream();
            Toast.makeText(mainActivity.getApplicationContext(), "Connected to Controller", Toast.LENGTH_SHORT).show();
            listenForData();
	    }catch(Exception IOException){
            Toast.makeText(mainActivity.getApplicationContext(), "Could Not Connect to Controller", Toast.LENGTH_SHORT).show();
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
								     final String data = new String(encodedBytes, "US-ASCII");
								     readBufferPosition = 0;

	                                handler.post(new Runnable()
	                                {
	                                    public void run()
	                                    {
	                                    	if(isWriting){
		                                        writer.write(data);
	                                    	}else{
		                                        //myLabel.append("\n" + data);
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
        byte y;
		if(x.equals("info")){
            y = 0x31;
			mmOutputStream.write(1);
		}else if(x.equals("read")){
            y = 0x32;
			mmOutputStream.write(2);
		}else if(x.equals("delete")){
            y = 0x33;
            mmOutputStream.write(3);
		}else if(x.equals("collect")){
            y = 0x34;
            mmOutputStream.write(4);
		}else if(x.equals("stop")){
            y = 0x35;
            mmOutputStream.write(5);
		}
        mmOutputStream.flush();
	}
	void stopBT() throws IOException
	{
	    stopWorker = true;
	    mmOutputStream.close();
	    mmInputStream.close();
	    mmSocket.close();
        Toast.makeText(mainActivity.getApplicationContext(), "BlueTooth Closed", Toast.LENGTH_SHORT).show();
        //logger.isStreamOpen = false;
	    //logger.setStreamOpenButtons();
	}
}