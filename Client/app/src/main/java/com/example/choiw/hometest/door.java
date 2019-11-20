package com.example.choiw.hometest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.Set;

public class door extends AppCompatActivity {
    LinearLayout llclose,llopen;
    Device_Order order;
    boolean swit = true;
    String text;
    JSONObject json;
    TextView tv;
    private final int VIEW_ID = 0X8000;

    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.door);
        llopen = findViewById(R.id.LLopendor);
        llclose = findViewById(R.id.LLclosedor);
        int jtotal = 0;
        order = new Device_Order();
        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        order.j_device(text);
        order.m(6);
        Log.d("json최대 숫자",order.getNum() +"");
        json = order.getJson();
        jtotal = order.getNum();
        try {
            String jnum = json.getString(Integer.toString(jtotal -1));
            JSONObject jsontxt = new JSONObject(jnum);
            if(jsontxt.getString("CloseT").equals("None")){
                swit = true;
            }
            else swit = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = jtotal-1; i >= 0; i--){
            tv = new TextView(this);
            tv.setId(VIEW_ID+i);
            try {
                String jnum = json.getString(Integer.toString(i));
                JSONObject jsontxt = new JSONObject(jnum);
                if(jsontxt.getString("CloseT").equals("None")&&swit){
                    tv.setText(jsontxt.getString("OpenT"));
                    tv.setTextSize(20);
                    llopen.addView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    swit = false;
                }
                else if(!(jsontxt.getString("CloseT").equals("None"))&&!swit){
                    tv.setText(jsontxt.getString("CloseT"));
                    tv.setTextSize(20);
                    llclose.addView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    swit = true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
