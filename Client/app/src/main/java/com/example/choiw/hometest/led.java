package com.example.choiw.hometest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class led extends AppCompatActivity {
    LinearLayout llon,lloff;
    Device_Order order;
    boolean swit = true;
    String text;
    JSONObject json;
    TextView tv,total;
    int jtotal = 0;
    double num = 0;
    String oncalc = null,offcalc = null;
    private final int VIEW_ID = 0X8500;
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.led);
        llon = findViewById(R.id.LLlighton);
        lloff = findViewById(R.id.LLlightoff);
        total = findViewById(R.id.total_elec);
        order = new Device_Order();
        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        order.j_device(text);
        json = order.getJson();
        order.m(5);
        Log.d("json최대 숫자",order.getNum() +"");
        jtotal = order.getNum();
        json = order.getJson();
        try {
            String jnum = json.getString(Integer.toString(jtotal-1));
            JSONObject jsontxt = new JSONObject(jnum);
            if(jsontxt.getString("OffT").equals("None")){
                swit = true;
            }
            else swit = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
            Log.d("swit상태",swit+"");
        for(int i = jtotal-1; i >= 0; i--){
            tv = new TextView(this);
            tv.setId(VIEW_ID+i);
            try {
                String jnum = json.getString(Integer.toString(i));
                JSONObject jsontxt = new JSONObject(jnum);
                if(jsontxt.getString("OffT").equals("None")&&swit){
                    tv.setText(jsontxt.getString("OnT"));
                    tv.setTextSize(20);
                    llon.addView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    swit = false;
                    oncalc =  jsontxt.getString("OnT");
                    if(offcalc != null){
                        oncalc = oncalc.replaceAll("-","");
                        oncalc = oncalc.replaceAll(" ","");
                        oncalc = oncalc.replaceAll(":","");
                        oncalc = oncalc.substring(6,12);
                        offcalc = offcalc.replaceAll("-","");
                        offcalc = offcalc.replaceAll(" ","");
                        offcalc = offcalc.replaceAll(":","");
                        offcalc = offcalc.substring(6,12);
                        Log.d("onCalc",oncalc);
                        Log.d("offcalc",offcalc);
                        num += Integer.parseInt(offcalc) - Integer.parseInt(oncalc);
                        offcalc = null;
                    }
                }
                else if(!(jsontxt.getString("OffT").equals("None"))&&!swit){
                    tv.setText(jsontxt.getString("OffT"));
                    tv.setTextSize(20);
                    lloff.addView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    swit = true;
                    offcalc = jsontxt.getString("OffT");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.d("사용시간",Math.floor(num/60*100)/100.0+"");
        num = Math.floor(num/60*100)/100.0;
        num = 0.5*num;
        num = Math.floor(num/60*1000)/1000.0;
        total.setText(num + " Kwh");
       }
}
