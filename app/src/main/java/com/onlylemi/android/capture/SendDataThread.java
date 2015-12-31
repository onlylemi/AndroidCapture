package com.onlylemi.android.capture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendDataThread extends Thread {

    public final static String TAG = "SendDataThread:";

    private byte[] byteBuffer = new byte[1024];
    private OutputStream outsocket;
    private ByteArrayOutputStream myoutputstream;
    private String ipname;
    private int port;

    public SendDataThread(ByteArrayOutputStream myoutputstream, String ipname, int port) {
        this.myoutputstream = myoutputstream;
        this.ipname = ipname;
        this.port = port;

        try {
            myoutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // send image data by socket
            if (!"".equals(ipname) && null != ipname) {
                Socket tempSocket = new Socket(ipname, port);
                outsocket = tempSocket.getOutputStream();
                ByteArrayInputStream inputstream = new ByteArrayInputStream(
                        myoutputstream.toByteArray());
                int amount;
                while ((amount = inputstream.read(byteBuffer)) != -1) {
                    outsocket.write(byteBuffer, 0, amount);
                }
                myoutputstream.flush();
                myoutputstream.close();
                tempSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}