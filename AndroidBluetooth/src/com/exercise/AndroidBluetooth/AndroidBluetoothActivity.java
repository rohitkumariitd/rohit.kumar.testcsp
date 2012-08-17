package com.exercise.AndroidBluetooth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidBluetoothActivity extends Activity {
	String tag="Main";
	// mbluetoothadapter.getAdrress();
	String addr[]=new String[30];
	int n;
	int ptr;
	long timer[]=new long[30];

	String dev1;
	private static final int REQUEST_ENABLE_BT = 1;

	// AsyncTask ATTEMPT
	private class SendFiles extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			if (params.length != 2) {
				Log.v(tag, "params length error");
			}
			else {
				String dv1 = new String(params[0]);
				String dv2 = new String(params[1]);
                                  if((dv1 != null) &&(dv2 != null))
                                  { 
				try {
					Log.v(tag, "sending data...");
					//http://bluetoothlogs.gramvaani.org/insertentry.php?device1=14741134F50C&device2=0C607688088B
					/*	GET: /insertentry.php?device1=123&device2=132\r\n
					 *  Host: http://bluetoothlogs.gramvaani.org
					 *  \r\n
					 * */
					String host="bluetoothlogs.gramvaani.org";
					String url="/insertentry.php?device1=";
					String url2="&device2=";
					int port=80;
					Log.v(tag,"getting Inet");
					InetAddress addr = InetAddress.getByName(host);
					Log.v(tag,"Inetaddress "+addr);
					Socket socket = new Socket(addr, port);
					BufferedWriter wr_socket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
					String method="GET";
					wr_socket.write(method+" "+url+dv1+url2+dv2+" HTTP/1.1\r\n");
					wr_socket.write("HOST: "+host+"\r\n");
					wr_socket.write("\r\n");
					wr_socket.flush();
					Log.v(tag,"data sent for device2 = " + dv2);
					/*BufferedReader rd_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String line;
					while ((line = rd_socket.readLine()) != null) {
						Log.v(tag, line);
					}*/
					wr_socket.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.v(tag,"exception1");
				}
                                  }
			}
			return null;
		}
		protected void onProgressUpdate() {
			Log.v(tag, "sending data...");
		}

		protected void onPostExecute() {
			Log.v(tag, "sending data...");
		}


	}


	void sendData(String dv1, String dv2){
		try {
			//http://bluetoothlogs.gramvaani.org/insertentry.php?device1=14741134F50C&device2=0C607688088B
			/*	GET: /insertentry.php?device1=123&device2=132\r\n
			 *  Host: http://bluetoothlogs.gramvaani.org
			 *  \r\n
			 * */
			String host="bluetoothlogs.gramvaani.org";
			String url="/insertentry.php?device1=";
			String url2="&device2=";
			int port=80;
			//Log.v(tag,"getting Inet");
			InetAddress addr = InetAddress.getByName(host);
			//Log.v(tag,"Inetaddress "+addr);
			Socket socket = new Socket(addr, port);
			BufferedWriter wr_socket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
			String method="GET";
			wr_socket.write(method+" "+url+dv1+url2+dv2+" HTTP/1.1\r\n");
			wr_socket.write("HOST: "+host+"\r\n");
			wr_socket.write("\r\n");
			wr_socket.flush();
			Log.v(tag,"data sent");
			/*BufferedReader rd_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while ((line = rd_socket.readLine()) != null) {
				Log.v(tag, line);
			}
			wr_socket.close();*/
			wr_socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.v(tag,"exception1");
		}

	}

	ListView listDevicesFound;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;

	ArrayAdapter<String> btArrayAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ptr=0;
		for (int i=0;i<30;i++) {
			addr[i] = "0";
		}
		btnScanDevice = (Button)findViewById(R.id.scandevice);
		stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		listDevicesFound = (ListView)findViewById(R.id.devicesfound);
		btArrayAdapter = new ArrayAdapter<String>(AndroidBluetoothActivity.this, android.R.layout.simple_list_item_1);
		listDevicesFound.setAdapter(btArrayAdapter);

		CheckBlueToothState();

		btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

		//		registerReceiver(ActionFoundReceiver, 
		//			new IntentFilter(BluetoothDevice.ACTION_FOUND));
		// COPIED FROM ON CLICK
		/*
		n=0;
		dev1=colonTrimmer(bluetoothAdapter.getAddress());
		Log.v(tag,"My device: " + dev1);
		btArrayAdapter.clear();
		registerReceiver(ActionFoundReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
		new Thread(new find_blue()).start();

		// bluetoothAdapter.startDiscovery();
		//while(bluetoothAdapter.isDiscovering()){
			/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		//}
		Log.v(tag,"discovered");
		//unregisterReceiver(ActionFoundReceiver);
		Log.v(tag,"value of n before sending "+n);*/
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(ActionFoundReceiver);
	}

	private void CheckBlueToothState(){
		if (bluetoothAdapter == null){
			stateBluetooth.setText("Bluetooth NOT support");
		}else{
			if (bluetoothAdapter.isEnabled()){
				if(bluetoothAdapter.isDiscovering()){
					stateBluetooth.setText("Bluetooth is currently in device discovery process.");
				}else{
					stateBluetooth.setText("Bluetooth is Enabled.");
					btnScanDevice.setEnabled(true);
				}
			}else{
				stateBluetooth.setText("Bluetooth is NOT Enabled!");
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	private String colonTrimmer(String s) {
		String fin = new String();
		int i = 0, j = 0;
		for (i=0,j=0; i < s.length(); i++, j++) {
			if (s.charAt(i) != ':') 
			{
				fin += s.charAt(i);
			}
		}
		return fin;

	}

	private Button.OnClickListener btnScanDeviceOnClickListener
	= new Button.OnClickListener(){

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			n=0;
			dev1=colonTrimmer(bluetoothAdapter.getAddress());
			Log.v(tag,"My device: " + dev1);
			btArrayAdapter.clear();
			registerReceiver(ActionFoundReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
			new Thread(new find_blue()).start();

			// bluetoothAdapter.startDiscovery();
			//while(bluetoothAdapter.isDiscovering()){
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}*/
			//}
			Log.v(tag,"discovered");
			//unregisterReceiver(ActionFoundReceiver);
			Log.v(tag,"value of n before sending "+n);
			/*			for(int i=0;i<n;i++){
				sendData(colonTrimmer(dev1),colonTrimmer(addr[n]));
				Log.v(tag,colonTrimmer(dev1)+" "+colonTrimmer(addr[n]));
			}*/
			//Log.v(tag,"data sent");
		}};

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			if(requestCode == REQUEST_ENABLE_BT){
				CheckBlueToothState();
			}
		}

		private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
				if(BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					String found = device.getAddress();
					//addr[n]=colonTrimmer(found);
					int put=0, tosend=0;
					int l=ptr;
					do {
						if (addr[l].equals(found)) {
							if(System.currentTimeMillis()>timer[l] && System.currentTimeMillis() < timer[l] + 300000)
							{
								// do nothing already found
								put=1;
								break;
							}
							else
							{
								//send to server
								timer[l] = System.currentTimeMillis();
								//send
								tosend=1;
								put=1;
								break;
							}
						}
						l = (l+1) % 30;
					} while (l != ptr);
					if (l==ptr && put==0)
					{
						Log.v(tag, "duplicate not found");
						addr[l] = found;
						timer[l] = System.currentTimeMillis();
						tosend=1;
					}
					ptr = (ptr + 1)% 30;
					if (tosend==1) {
						Log.v(tag,"Sending: " + addr[l] + " " + found +" " + device.getName() + " at time " + timer[l]);
                                                if(colonTrimmer(found)!=null)
                                               {
						sendData(dev1,colonTrimmer(found));
						btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
						btArrayAdapter.notifyDataSetChanged();
                                               }
					}
				}
			}};

}