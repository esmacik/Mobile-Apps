package com.techexchange.mobileapps.assignment3;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

//    public static final int PORT_NUMBER = 7232;
//    public static String DEVICE_IP_ADDRESS;
//    public Button serverButton, clientButton;
//    public TextView ipAddress;
//    public EditText ipAddressField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this, false));

//        serverButton = findViewById(R.id.server_button);
//        clientButton = findViewById(R.id.client_button);
//        ipAddress = findViewById(R.id.ip_address_text);
//        ipAddressField = findViewById(R.id.text_edit);
//
//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//        DEVICE_IP_ADDRESS = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//        ipAddress.setText("This device's IP Address: "+DEVICE_IP_ADDRESS);
//
//        serverButton.setOnClickListener(v -> {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    new ServerThread().run();
//                    return null;
//                }
//            }.execute();
//        });
//
//        clientButton.setOnClickListener(v -> {
//            SERVER_IP_ADDRESS = ipAddressField.getText().toString();
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    new ClientThread().run();
//                    return null;
//                }
//            }.execute();
//        });
    }

//    //When a device is the server device.
//    private class ServerThread implements Runnable{
//        private static final String TAG = "ServerThread";
//
//        @Override
//        public void run() {
//            Socket clientSocket = null;
//            try{
//                Log.d(TAG, "run: Begin sever socket thread");
//                //Server socket is accepting connection, saving reference to client socket it finds,
//                //and reading objects from that socket's output stream;
//                ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
//                clientSocket = serverSocket.accept();
//                Log.d(TAG, "run: CONNECTED TO CLIENT DEVICE");
//
//                setContentView(new GameView(getApplicationContext(), false));
//
//                InputStream iStream = clientSocket.getInputStream();
//                ObjectInputStream objectInputStream = new ObjectInputStream(iStream);
//                DataPacket readObject = (DataPacket) objectInputStream.readObject();
//
//                //Server socket is writing objects to the client's
//                OutputStream oStream = clientSocket.getOutputStream();
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(oStream);
//                objectOutputStream.writeObject(new DataPacket());
//            } catch (IOException e) {
//                Log.e(TAG, "Exception!", e);
//            }
//        }
//    }

//    private static String SERVER_IP_ADDRESS;

    //When a device is the client device
//    private class ClientThread implements Runnable{
//        private static final String TAG = "ClientThread";
//
//        @Override
//        public void run() {
//            try {
//                Log.d(TAG, "run: Begin client socket thread");
//                new Socket(InetAddress.getByName(SERVER_IP_ADDRESS), PORT_NUMBER);
//                Log.d(TAG, "run: CONNECTED TO SERVER DEVICE");
//                setContentView(new GameView(getApplicationContext(), true));
//            } catch (IOException e) {
//                Log.e(TAG, "Exception!", e);
//            }
//        }
//    }
//
//    private class DataPacket implements Serializable{
//        int swipeDirection;
//    }
}