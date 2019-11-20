package com.example.choiw.hometest;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Socket_Thread extends Thread implements Parcelable {
    final static int port = 8008;
    String ip = "112.170.118.171";
    Socket socket;

    protected Socket_Thread(Parcel in) {
        ip = in.readString();
    }
    Socket_Thread(){}
    public static final Creator<Socket_Thread> CREATOR = new Creator<Socket_Thread>() {
        @Override
        public Socket_Thread createFromParcel(Parcel in) {
            return new Socket_Thread(in);
        }

        @Override
        public Socket_Thread[] newArray(int size) {
            return new Socket_Thread[size];
        }
    };

    public void run(){
        if(socket  == null){
        try {
            socket = new Socket(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
        }}

    }
    public Socket r_socket(){
        return socket;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ip);
    }
}
