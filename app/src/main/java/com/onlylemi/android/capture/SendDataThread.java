package com.onlylemi.android.capture;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendDataThread extends Thread {

    public final static String TAG = "SendDataThread:";

    private byte byteBuffer[] = new byte[1024];
    private OutputStream outsocket;
    private ByteArrayOutputStream myoutputstream;
    private String ipname;

    public SendDataThread(ByteArrayOutputStream myoutputstream, String ipname) {
        this.myoutputstream = myoutputstream;
        this.ipname = ipname;
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
                Socket tempSocket = new Socket(ipname, 6000);
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