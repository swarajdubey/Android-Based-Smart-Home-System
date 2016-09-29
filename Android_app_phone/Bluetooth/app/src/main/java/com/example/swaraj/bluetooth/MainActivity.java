package com.example.swaraj.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends Activity implements SensorEventListener {

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID for bluetooth
    private static final int MESSAGE_READ = 0;
    private Button b;

    public static final int SUCCESS_CONNECT = 0; //integer variable for successfull connection

    private BluetoothAdapter bluetoothAdapter; //bluetooth adapter to start discovering devices
    ArrayAdapter<String> adapter;
    Set<BluetoothDevice> set;
    BluetoothDevice selectedDevice; //stores the Edison in here

    ArrayList<String> pairedDevices;
    ArrayList<String> devices=new ArrayList<String>();
    IntentFilter intentFilter;
    BroadcastReceiver broadcastReceiver;

    Handler mHandler;

    private boolean ConnectThreadstatus=false;
    private boolean ConnectedThreadstatus=false;
    private ConnectedThread c;
    private boolean sent_once=false;
    private boolean edison_found=false;

    //non bluetooth stuff
    private final int Speech_recognition_code=100;
    private TextToSpeech textToSpeech;
    private boolean internet_connection_status;
    private String mac_address;
    private ListView lv;
    private SeekBar lightseekbar,fanseekbar;
    private TextView test,fantest;

    //sensor stuff
    private SensorManager mSensorManager;
    private String fan_command,light_command;

    //Home command widgets
    private Button auto_button,manual_button,b0_fan,b1_fan,b2_fan,b3_fan,b4_fan,b0_light,b1_light,b2_light,b3_light,b4_light;
    private boolean allow_auto_mode=false,allow_manual_mode=true;
    private int Light_values_array[]={0,60,120,180,255};
    private int Fan_values_array[]={0,60,120,180,255};

    private TextView light_text,tv3;
    private TextView step_counter;
    private TextView tempTextView,lightIntensityTextView,fanSpeedTextView;

    //---------walking variables--------------
    private double steps_walked_so_far;
    private boolean activityRunning;

    //------------resting and sleeping variables------------
    private double totalGforce;
    Handler restHandler;
    Runnable restRunnable;
    private int counttillrest=0;

    //URL's for API's on the cloud
    private String updatestepcounterurl="http://www.eeeonlinecourse.com/FYP_15-16/update_step_counter.php";
    private String insertstepcounterRow="http://www.eeeonlinecourse.com/FYP_15-16/receive_stepcounter_data.php";
    private String getStepsInfo="http://www.eeeonlinecourse.com/FYP_15-16/step_counter_AI.php"; //gets number of steps info
    private String getTempInfo="http://www.eeeonlinecourse.com/FYP_15-16/get_temperature_info.php"; //gets the temperate info
    private String getemergencyStatus="http://www.eeeonlinecourse.com/FYP_15-16/get_emergency_info.php";
    private String emergencyapi="http://www.eeeonlinecourse.com/FYP_15-16/upload_emergency_message.php";

    //class object for internet connectivity
    JSONParser jsonParser=new JSONParser();

    private boolean start_app_counter=false;

    private boolean step_counter_row_updated=false;

    //-----walking textviews----
    private TextView walkStatus;
    private boolean displayStepCounterAI=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final RelativeLayout r = (RelativeLayout)findViewById(R.id.relativelayout);
        checkInternetConnection(); //FIRST STEP IS TO CHECK INTERNET CONNECITON!!

        if(android.os.Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mSensorManager = ((SensorManager)this.getSystemService(SENSOR_SERVICE)); //get sensor services


        //step_counter=(TextView)findViewById(R.id.textView6);
        //walkStatus=(TextView)findViewById(R.id.textView2);

        ///intialize widgets ID here
        b=(Button) findViewById(R.id.button);
        light_text=(TextView)findViewById(R.id.textView);
        tv3=(TextView)findViewById(R.id.textView3);
        test=(TextView)findViewById(R.id.textView2);
        //fantest=(TextView)findViewById(R.id.textView4);
        lightseekbar=(SeekBar)findViewById(R.id.seekBar);
        auto_button=(Button)findViewById(R.id.button2);
        manual_button=(Button)findViewById(R.id.button3);
        tempTextView=(TextView)findViewById(R.id.textView4);
        lightIntensityTextView=(TextView)findViewById(R.id.textView6);
        fanSpeedTextView=(TextView)findViewById(R.id.textView7);





        //allow auto mode here
        auto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allow_auto_mode=true; //sliders and small button will not take effect here
                allow_manual_mode=false;
                //r.setBackgroundColor(Color.parseColor("#4d5ec1"));

                auto_button.setTextColor(Color.parseColor("#5ec14d"));
                manual_button.setTextColor(Color.BLACK);

                light_text.setVisibility(View.INVISIBLE);
                tv3.setVisibility(View.INVISIBLE);



                lightseekbar.setVisibility(View.INVISIBLE);
                fanseekbar.setVisibility(View.INVISIBLE);

                //set the number 256 for edison sensor to take over
                //String start_sensor="256";
                if(edison_found==true) //-----ENABLE AUTO MODE------------
                {
                    String light_auto="{\"light\":256}";
                    String fan_auto="{\"fan\":256}";

                    sendCommandViaBluetooth(fan_auto);


                    sendCommandViaBluetooth(light_auto);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Edison is not connected yet",Toast.LENGTH_LONG).show();
                }

            }
        });

        manual_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allow_manual_mode=true;//sliders and small buttons will take place here
                allow_auto_mode=false;

                manual_button.setTextColor(Color.parseColor("#c14d5e"));
                auto_button.setTextColor(Color.BLACK);

                light_text.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);



                lightseekbar.setVisibility(View.VISIBLE);
                fanseekbar.setVisibility(View.VISIBLE);
            }
        });

        lightseekbar.setMax(255);
        lightseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                light_command="{\"light\":"+progress+"}";
                double d_progress=(double)progress;
                double percentage=(d_progress/255.0)*100;
                lightIntensityTextView.setText("");
                lightIntensityTextView.setText(String.format( "Intensity: %.2f  ", percentage ));
                lightIntensityTextView.append("%");
                if(edison_found==true)
                {
                    if(allow_manual_mode==true)
                    {
                        sendCommandViaBluetooth(light_command);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        fanseekbar=(SeekBar)findViewById(R.id.seekBar2);
        fanseekbar.setMax(255);
        fanseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fan_command="{\"fan\":"+progress+"}";

                double d_progress=(double)progress;
                double percentage=(d_progress/255.0)*100;
                fanSpeedTextView.setText("");
                fanSpeedTextView.setText(String.format( "Speed: %.2f  ", percentage ));
                fanSpeedTextView.append("%");

                if(edison_found==true)
                {
                    if(allow_manual_mode==true)
                    {
                        sendCommandViaBluetooth(fan_command); //send json string via bluetooth upon slider change
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });


        //lv=(ListView)findViewById(R.id.listView);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        initializeBluetooth();

        //adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,0);
        //lv.setAdapter(adapter);

        if(bluetoothAdapter==null)
        {
            Toast.makeText(getApplicationContext(),"no bluetooth",Toast.LENGTH_LONG).show();
        }
        else{
            if(!bluetoothAdapter.isEnabled()) //if bluetooth has not been enabled then enable is now
            {
                bluetoothAdapter.enable();
                turnonBT();
            }
            getPairedDevices();
            initializeBluetooth();

        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internet_connection_status==true)
                {
                    getSpeech(); //only if internet is available then enable speech recoognition
                }
                else if(internet_connection_status==false)
                {
                    Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_LONG).show();
                }

            }
        });



        restHandler = new Handler();
        restRunnable = new Runnable() {
            @Override
            public void run() {
                try{
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); //convert time in this format
                    String formattedDate = df.format(c.getTime());

                    int hours = new Time(System.currentTimeMillis()).getHours();
                    int minutes= new Time(System.currentTimeMillis()).getMinutes();
                    //Toast.makeText(getApplicationContext(),"time is: "+hours+":"+minutes,Toast.LENGTH_LONG).show();
                    if(hours==0 && minutes==1) //insert new row at 12:01 am
                    {
                        //just before midnight, insert a new row into the step counter table
                        if(step_counter_row_updated==false)
                        {
                            //update new row for step counter table at this time
                            Toast.makeText(getApplicationContext(),"Row Updated",Toast.LENGTH_LONG).show();
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("stepkey",""+0));
                            jsonParser.makeHttpRequest(insertstepcounterRow,"POST",params);
                            step_counter_row_updated=true;
                        }


                    }

                    if(hours==0 && minutes==22)
                    {
                        step_counter_row_updated=false; //reset this boolean variable for it to update next row on the next day
                    }

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    JSONObject emergencyObject=jsonParser.makeHttpRequest(getemergencyStatus,"POST",params);
                    if(emergencyObject.optString("stat").contentEquals("help"))
                    {
                        //update the database
                        JSONParser jsonParser= new JSONParser();
                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("help_msg","no"));
                        jsonParser.makeHttpRequest(emergencyapi,"POST",params);
                        params.clear();


                        //call the person here
                        Intent my_callIntent = new Intent(Intent.ACTION_CALL);
                        my_callIntent.setData(Uri.parse("tel:"+"0103612855"));//phone number
                        startActivity(my_callIntent);
                    }

                    restHandler.postDelayed(restRunnable,4000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        restHandler.postDelayed(restRunnable,0);//4 seconds


        mSensorManager.registerListener((android.hardware.SensorEventListener) this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),3);
        mSensorManager.registerListener((android.hardware.SensorEventListener) this,mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE),3); //activate heart rate sensor
        mSensorManager.registerListener((android.hardware.SensorEventListener) this,mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),3);
    }

    private void checkInternetConnection()
    {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()==null)
        {
            Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_LONG).show();
            internet_connection_status=false; //use this variable to conduct offline activities
        }
        else{
            //Toast.makeText(getApplicationContext(),"Internet connection found",Toast.LENGTH_LONG).show();
            WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo=wifiManager.getConnectionInfo();
            mac_address=wifiInfo.getMacAddress();
            internet_connection_status=true; //use this variable to conduct on line activities
        }
    }

    private void getSpeech()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        try{
            startActivityForResult(intent,Speech_recognition_code);
        }catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),"Speech recognition not enabled",Toast.LENGTH_LONG).show();
        }
    }

    private void turnonBT() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }

    private void initializeBluetooth()
    {
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
        pairedDevices=new ArrayList<String>();
        intentFilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);

        //Toast.makeText(getApplicationContext(),"in here",Toast.LENGTH_LONG).show();
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    String action = intent.getAction();
                    Bundle b = intent.getExtras();

                    if (BluetoothDevice.ACTION_FOUND.equals(action)) //if a device appears
                    {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        //Bundle bb = intent.getExtras();

                        devices.contains(device.getName());
                        if (devices.contains(device.getName()) == false)//if it does not contain
                        {
                            devices.add(device.getName());
                            if (device.getName().contains("Edison")) {
                                selectedDevice = device;
                                edison_found = true;
                                Toast.makeText(getApplicationContext(), "Edison found", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //Toast.makeText(getApplicationContext(),"duplicate found",Toast.LENGTH_LONG).show();
                        }

                    }
                    else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){}
                    else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){}
                    else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
                    {
                        if(bluetoothAdapter.getState()==bluetoothAdapter.STATE_OFF)
                        {
                            turnonBT();
                        }
                    }
                }catch (NullPointerException n){
                    Log.d("NULLPOINTER"," on receive exception");
                }

            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(broadcastReceiver,intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver,intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver,intentFilter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(broadcastReceiver!=null)
        {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver=null;
        }


        /*try {
            mmSocket.close();
        } catch (IOException e) {
            Log.d("CLOSE_SOCKET"," socket closing exception");
        }*/
    }

    private void getPairedDevices()
    {
        set=bluetoothAdapter.getBondedDevices();
        if(set.size()>0)
        {
            for(BluetoothDevice device:set)
            {
                pairedDevices.add(device.getName()+"\n"+device.getAddress());
            }
        }
    }

    //-------------------------------GET SPEECH-----------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Speech_recognition_code:

                if(resultCode==RESULT_OK && data!=null)
                {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    speech_to_text_processing(result.get(0));//start text processing
                }

                break;
        }
    }

    //---------------------UNDERSTANDING SPEECH AND FIND APPROPRIATE------------------
    private void speech_to_text_processing(String sentence)
    {
        Speech_to_text_algorithm speech_to_text_algorithm = new Speech_to_text_algorithm();
        final String returned_string=speech_to_text_algorithm.speech_to_text_processing(sentence);
        textToSpeech=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.speak(returned_string,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        if(edison_found==true)
        {
            if(returned_string.contains("light") && returned_string.contains("on"))
            {
                String homeCommand="{\"light\":\"on\"}";
                sendCommandViaBluetooth(homeCommand);
            }

            if(returned_string.contains("light") && returned_string.contains("off"))
            {
                String homeCommand="{\"light\":\"off\"}";
                sendCommandViaBluetooth(homeCommand);
            }

            if(returned_string.contains("fan") && returned_string.contains("on"))
            {
                String homeCommand="{\"fan\":\"on\"}";
                sendCommandViaBluetooth(homeCommand);
            }

            if(returned_string.contains("fan") && returned_string.contains("off"))
            {
                String homeCommand="{\"fan\":\"off\"}";
                sendCommandViaBluetooth(homeCommand);
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Edison is not connected",Toast.LENGTH_LONG).show();
        }
    }

    //------------------------SEND BYTES VIA BLUETOOTH------------------------
    private void sendCommandViaBluetooth(final String command)
    {
        if(ConnectThreadstatus==false) //establish socket connection only once when the app runs
        {
            final ConnectThread connect = new ConnectThread(selectedDevice); //establish socket connection
            connect.start(); //MAKE SURE connect only once
            ConnectThreadstatus=true;
        }


        if(sent_once==true) //can send as mnay times after the first try
        {
            c.write(command.getBytes());
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SUCCESS_CONNECT) {
                    sent_once=true;
                    if(ConnectedThreadstatus==false) //establish streams only once when connecting
                    {
                        c = new ConnectedThread((BluetoothSocket) msg.obj);
                        ConnectedThreadstatus=true;
                    }

                    c.write(command.getBytes());


                } else if (msg.what == MESSAGE_READ) {
                    Toast.makeText(getApplicationContext(), "READ", Toast.LENGTH_LONG).show(); //reading in data from edison
                }
            }
        };
    }


    int no_of_times=0;
    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();


        List<NameValuePair> params = new ArrayList<NameValuePair>();

        JSONObject stepCounterObject=jsonParser.makeHttpRequest(getStepsInfo,"POST",params);

        String advice=stepCounterObject.optString("step_message").toString();
        double no_of_steps=stepCounterObject.optDouble("no_of_steps");
        int int_no_of_steps=(int)no_of_steps;
        test.setText("Average steps for last 5 days: "+int_no_of_steps+" "+advice);



        params = new ArrayList<NameValuePair>();
        JSONObject tempObject=jsonParser.makeHttpRequest(getTempInfo,"POST",params);
        double temp=tempObject.optDouble("avg_temp");
        tempTextView.setText("Average tmp. for last 2 days is: "+temp);

    }


    //-------------------------COMMUNICATION VIA BLUETOOTH--------------------------------

    private class ConnectThread extends Thread {

        private final BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID); //establishes rfcomm socket

            } catch (IOException e) {
                Log.d("SOCKET","rfcomm coket exception");
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            bluetoothAdapter.cancelDiscovery();
            try {

                mmSocket.connect();//establish the socket connection

            } catch (IOException connectException) {
                try {
                    //try reestablishing the connected here
                    mmSocket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                    mmSocket.connect();
                } catch (IOException closeException) {

                }
                catch (InvocationTargetException e) {
                    Log.d("INVO","invocation exception");
                } catch (NoSuchMethodException e) {
                    Log.d("NOMETHOD","method exception");
                } catch (IllegalAccessException e) {
                    Log.d("ILLLEE","illegeal exception");
                }

                //return;
            }

            mHandler.obtainMessage(SUCCESS_CONNECT,mmSocket).sendToTarget(); //socket connection successfull

            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);


        }

        private void manageConnectedSocket(BluetoothSocket mmSocket) {

        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket=socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();//get the streams from the socket
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.d("STREAMMMMMM","stream error");
            }

            //initialize the streams
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;   // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    buffer = new byte[1024];
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    Log.d("MESSAGEFAILURE","cant send message");
                    break;
                }
            }
        }

        //this function sends data to the target bluetooth device
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes); //send data to the target bluetooth device
            } catch (IOException e) {
                Log.d("BYTEFailure","cannot write bytes");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }



}
