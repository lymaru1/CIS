package com.example.choiw.hometest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Device_Order {
    private boolean door;  int num = 0;
    private boolean light;
    private String[] light_info;
    private boolean pass = false;
    private String ondo;
    private String subdo;
    private String[] onsubdo_info;
    String[] order;
    JSONObject json;
    boolean ready = false;

    public boolean r_ready(){
        return ready;
    }
    public void device(String t) {
      order = t.split(",");
    }
    public void j_device(String t) {int i = 0;
       while(json ==null){
        try {
           json =  new JSONObject(t);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(json == null) t+= "}";
        i++;
     if(i == 10) break; // 무한반복 루트빠져나오기
       }

        Log.d("json 들어감","ㅇㅇ");
    }
    public void m(int a){
        //a = Integer.parseInt(order[0]);
        switch(a)
        {
            case 1:
                try {order[0].toLowerCase();
                    pass = Boolean.valueOf(order[0]);
                    ondo =order[1];
                    subdo = order[2];
                    door = Boolean.valueOf(order[3]);
                    light = Boolean.valueOf(order[4]);
                    Log.d("case:1",light+"");
                }catch(ArrayIndexOutOfBoundsException e){
                    Log.d("Device_Order","case 1 배열초과");
                }
                break;
            case 2:
                try {
                    pass = Boolean.valueOf(order[0]);
                    ondo = order[1];
                    subdo = order[2];
                    door = Boolean.valueOf(order[3]);
                    light = Boolean.valueOf(order[4]);
                }catch(ArrayIndexOutOfBoundsException e){
                    Log.d("Device_Order","case 2 배열초과");
                }
                break;
            case 3:
                light = Boolean.valueOf(order[0]);
                break;
            case 4:
                door = Boolean.valueOf(order[0]);
                break;
            case 5://led more
                num = 0;
                if(json == null) Log.d("5번","json 널");
                else{
                    num = json.length();
                }
                break;
            case 6://doorlock more
                num = 0;
                if(json == null) Log.d("6번","json 널");
                else{
                    num = json.length();
                }

                break;
            case 8:
                try {
                    pass = Boolean.valueOf(order[0]);
                    ondo = order[1];
                    subdo =order[2];
                    door = Boolean.valueOf(order[3]);
                    light = Boolean.valueOf(order[4]);
                }catch(ArrayIndexOutOfBoundsException e){
                    Log.d("Device_Order","case 8 배열초과");
                }
                break;
        }
    }
    public boolean r_pass(){
        return this.pass;
    }
    public boolean r_door(){
        return this.door;
    }
    public String r_ondo(){
        return this.ondo;
    }
    public String r_subdo(){
        return this.subdo;
    }
    public boolean r_light(){
        return this.light;
    }
    public JSONObject getJson() {
        return json;
    }
    public int getNum(){
        return num;
    }


}
