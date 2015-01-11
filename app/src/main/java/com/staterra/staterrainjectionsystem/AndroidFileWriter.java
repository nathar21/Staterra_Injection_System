package com.staterra.staterrainjectionsystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class AndroidFileWriter {

	private FileWriter f;
	File file;
	
	public AndroidFileWriter(){
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File (sdCard.getAbsolutePath() + "/BTtestData/");
		dir.mkdirs();
		file = new File(dir, "testdata.txt");
	}
	
	
	//Checks to see if external storage is readable and writable
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public void write(String x){
		try {
			f = new FileWriter(file, true);
			f.write(x + "\n");
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
