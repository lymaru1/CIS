package com.example.choiw.hometest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

//모든 버튼들 버튼 누른후 재 누름을 방지하기위해 블록을 해야한다. 스윗치도 꼭 해줘야한다.
public class MainActivity extends AppCompatActivity {
    Outstream_Thread output;
    Device_Order order;
    String[] t = new String[3];
    EditText id,password;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final EditText id = findViewById(R.id.email_ed);
       final EditText password = findViewById(R.id.password_ed);
        b1 = findViewById(R.id.loginbutton);
      //  socket = new Socket_Thread();
        //socket.start();
        //while(true){
          //  if(socket.r_socket() != null)
            //    break;
       // }
       b1.setOnClickListener(new Button.OnClickListener(){

           @Override
           public void onClick(View v) {
               output = new Outstream_Thread();
               order = new Device_Order();
               t[0] = "1";
               t[1] = id.getText().toString();
               t[2] = password.getText().toString();
               output.getString(t);
               output.start();
               try {
                   output.join();
                   output.sleep(5000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               order.device(output.order());
               order.m(1);
                Log.d("dd",order.r_ondo() + "");
               if(order.r_pass()){
                   Intent intent = new Intent(MainActivity.this,main.class);

                   intent.putExtra("ondo",order.r_ondo());
                   intent.putExtra("subdo",order.r_subdo());
                   intent.putExtra("door",order.r_door());
                   intent.putExtra("led",order.r_light());
                   startActivity(intent);

               }
               else if(order.r_ondo().isEmpty()){
                   AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                   alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                       }
                   });
                   alert.setMessage("통신이 원활하지 않습니다.");
                   alert.show();

               }
               else {
                   AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                   alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                       }
                   });
                   alert.setMessage("아이디 비밀번호가 일치 하지 않습니다.");
                   alert.show();

               }

           }
       });

    }


    public void register(View view) {
        Intent intent = new Intent(MainActivity.this,login_regist.class);

        startActivity(intent);
    }
}







