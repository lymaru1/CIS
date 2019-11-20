package com.example.choiw.hometest;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class Outstream_Thread extends Thread  {
    Device_Order order;
    private String[] t;
    private String t2;
    private boolean s;
    String text;
    final static int port = 8008;
    String ip = "192.168.43.246";
    // String ip = "172.28.1.77";

    public Socket socket;
    int  bytesRead;
    byte[] buffer = new byte[8192];

    //public Outstream_Thread(Socket socket) {
   //     this.socket = socket;

   // }

    public void run() {

        Log.d("Outstream", "서버에게 보냅니다.");
        text = "";
            try {
                Socket socket = new Socket(ip,port);
                if(socket !=null) {
                    OutputStream out = socket.getOutputStream();
                    ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream(1024);
                    InputStream instream = socket.getInputStream();


                    for (int i = 0; i < t.length; i++) {
                        out.write(t[i].getBytes("utf-8"));
                        out.flush();
                        Log.d(i + "번째", t[i]);
                        Thread.sleep(400);
                    }
                    Log.d("Outstream_thread", "메시지를 다보냈습니다.");
                    out.write("$END".getBytes("utf-8"));
                    out.flush();
                    Thread.sleep(2000);
                    
                    while ((bytesRead = instream.read(buffer)) != -1) {
                        text += new String(buffer, 0, bytesRead, "utf-8");
                    }
                    Log.d("서버로부터온메시지", text);
                    out.close();
                    instream.close();
                    socket.close();
                }
                else Log.d ("소켓이 널","null");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    public void getString(String[] t) {
        this.t = t;
    }
    public String order(){
        return text;
    }

    public Device_Order getOrder() {
        return order;
    }
}

