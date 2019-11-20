package com.example.choiw.hometest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.R.*;

import java.io.Serializable;

public class main extends AppCompatActivity {
    Outstream_Thread output;
    Device_Order order;
    String subdo,ondo = "";
    Switch doorswitch;
    Switch ledswitch;
    ImageView lockimg,unlockimg,light_onimg,light_offimg;
    boolean doorlock,led;
    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        TextView tv_ondo = findViewById(R.id.tv_ondo);
        TextView tv_subdo = findViewById(R.id.tv_subdo);
        lockimg = findViewById(R.id.lock);
        unlockimg = findViewById(R.id.unlock);
        light_onimg = findViewById(R.id.lighton);
        light_offimg = findViewById(R.id.lightoff);
        ondo = intent.getStringExtra("ondo");
        subdo = intent.getStringExtra("subdo");
        doorlock = intent.getBooleanExtra("door",false);
        led = intent.getBooleanExtra("led",false);
        tv_ondo.setText(ondo + "℃");
        tv_subdo.setText(subdo+"%");
        unlockimg.setVisibility(View.INVISIBLE);
        light_onimg.setVisibility(View.INVISIBLE);
        doorswitch = findViewById(R.id.doorswitch);
        doorswitch.setChecked(doorlock);
        if(!doorlock){
            lockimg.setVisibility(View.VISIBLE);
            unlockimg.setVisibility(View.INVISIBLE);
        }
        else if(doorlock){
            lockimg.setVisibility(View.INVISIBLE);
            unlockimg.setVisibility(View.VISIBLE);
        }
        doorswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //도어락 스윗치
                if(isChecked){
                    output = new Outstream_Thread();
                    String[] t = new String[1];
                    t[0] = "4";
                    output.getString(t);
                    output.start();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order = new Device_Order();
                    order.device(output.order());
                    order.m(4);
                    doorlock = order.r_door();
                }
                else{
                    output = new Outstream_Thread();
                    String[] t = new String[1];
                    t[0] = "4";
                    output.getString(t);
                    output.start();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order = new Device_Order();
                    order.device(output.order());
                    order.m(4);
                    doorlock = order.r_door();
                }
                Log.d("door부분",doorlock+"");
                buttonView.setChecked(doorlock);
                if(!doorlock){
                   lockimg.setVisibility(View.VISIBLE);
                    unlockimg.setVisibility(View.INVISIBLE);
                }
                else if(doorlock){
                    lockimg.setVisibility(View.INVISIBLE);
                    unlockimg.setVisibility(View.VISIBLE);
                }
            }
        });
            ledswitch = findViewById(R.id.ledswitch);
            ledswitch.setChecked(led);
        if(!led){
            light_offimg.setVisibility(View.VISIBLE);
            light_onimg.setVisibility(View.INVISIBLE);
        }
        else if(led){
            light_offimg.setVisibility(View.INVISIBLE);
            light_onimg.setVisibility(View.VISIBLE);
        }
             ledswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // LED 스윗치
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    output = new Outstream_Thread();
                    String[] t = new String[1];
                    t[0] = "3";
                    output.getString(t);
                    output.start();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order = new Device_Order();
                    order.device(output.order());
                    order.m(3);
                    led = order.r_light();
                }
                else{
                    output = new Outstream_Thread();
                    String[] t = new String[1];
                    t[0] = "3";
                    output.getString(t);
                    output.start();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    order = new Device_Order();
                    order.device(output.order());
                    order.m(3);
                    led = order.r_light();
                }
                buttonView.setChecked(led);
                if(!led){
                    light_offimg.setVisibility(View.VISIBLE);
                    light_onimg.setVisibility(View.INVISIBLE);
                }
                else if(led){
                    light_offimg.setVisibility(View.INVISIBLE);
                    light_onimg.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void dmb(View view){//도어락
        output = new Outstream_Thread();
        order = new Device_Order();
        String[] t = new String[1];
        t[0]="6";
        output.getString(t);
        output.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // order.j_device(output.order());
        Intent intent = new Intent(main.this,door.class);
        intent.putExtra("text",output.order());
        startActivity(intent);
    }
    public void lmb(View view){
        output = new Outstream_Thread();
        order = new Device_Order();
        String[] t = new String[1];
        t[0]="5";
        output.getString(t);
        output.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // order.j_device(output.order());
        Intent intent = new Intent(main.this,led.class);
        intent.putExtra("text",output.order());
        startActivity(intent);
    }
}
