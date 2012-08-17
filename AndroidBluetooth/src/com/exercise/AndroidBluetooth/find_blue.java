package com.exercise.AndroidBluetooth;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;


public class find_blue implements Runnable{

	BluetoothAdapter bluetoothAdapter;

	public void run()
	{
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		while(true)
		{

			
			//writeToFile("\nDiscovery sent\n");
			bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			bluetoothAdapter.cancelDiscovery();
			bluetoothAdapter.startDiscovery();
			Log.v("Main", "find blue running");

			try {
				while(bluetoothAdapter.isDiscovering()){
					Log.v("Main", "find blue is discovering");
					Thread.sleep(5000);
				}
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}



	}


}
