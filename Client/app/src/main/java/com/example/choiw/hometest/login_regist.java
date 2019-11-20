package com.example.choiw.hometest;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class login_regist extends AppCompatActivity {
    Device_Order order;
   Outstream_Thread output;
   EditText id,pass,name,passCheck;
   TextView check;
   String ip = "112.170.118.171";
   String stra_check = "";
   boolean a_check,b_check,c_check; // a: 아이디 확인 b:비밀번호 확인 c: 이름 확인
        @Override
        protected void onCreate(Bundle savedInstaceState){
            super.onCreate(savedInstaceState);
            setContentView(R.layout.login_regist);
        id = findViewById(R.id.e_id);
        pass = findViewById(R.id.password);
        name = findViewById(R.id.name);
        passCheck = findViewById(R.id.password_check);
        check = findViewById(R.id.check);
        Intent intent = getIntent();
            //패스워드 실시간 체크
            passCheck.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String a = passCheck.getText().toString();
                    String b = pass.getText().toString();
                    if(a.equals(b)){
                        check.setText("0");
                        b_check = true;

                    }

                    else{
                        Log.d("비밀번호 비교",a);
                        Log.d("비밀번호2 비교",b);
                        check.setText("x");
                        b_check = false;}
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }



    public void registClick(View view) {
            //방어코드 세워야됨 아이디 중복체크 확인과 비밀번호 확인이 되었는지 확인후 보낼 수 있도록 한다.
        String[] t = new String[4];
        t[0] = "2";
        t[1] = id.getText().toString();
        t[2] = pass.getText().toString();
        t[3] = name.getText().toString();
            if(!t[3].isEmpty()) c_check = true;
            else c_check = false;
            if(t[1].equals(stra_check)) a_check = true;  // 중복체크후 아이디를 다시 입력했을경우 회원가입 불가
            else a_check = false;
            Log.d("이름 ",t[3]);
            Log.d("이름 상태",c_check+"");
            if(a_check && b_check && c_check) {
                output.getString(t);
                output.start();
                try {
                    Thread.sleep(2900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                order.device(output.order());
                if(order.r_pass()){
                    Log.d("login regist","생성완료");
                    try {
                        Thread.sleep(2900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.setMessage("회원가입이 완료되었습니다.");
                    alert.show();
                }
                else {
                    Log.d("login regis", "생성 완료되지않음");
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setMessage("한번더 시도해주세요");
                    alert.show();
                }
            }
            else {
                if(!a_check){
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setMessage("아이디 중복확인을 해주세요");
                    alert.show();
                }
                else if(!b_check){
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setMessage("비밀번호 확인을 해주세요");
                    alert.show();
                }
                else if(!c_check){
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setMessage("이름을 입력해주세요");
                    alert.show();
                }

            }
    }

    public void cancelClick(View view) {
            this.finish();
    }

    public void e_idClick(View view) {
            a_check = false;
            output = new Outstream_Thread();
            order = new Device_Order();
            String[] t = new String[2];
            t[0] = "8";
            t[1] = id.getText().toString();
            if(!t[1].isEmpty()) {
                output.getString(t);
                output.start();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                order.device(output.order());
                order.m(8);
                a_check = order.r_pass();
                if(a_check == false){
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setMessage("아이디가 중복입니다.");
                    alert.show();
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alert.setMessage("아이디를 사용하실수 있습니다.");
                    alert.show();
                    stra_check = t[1];
                }
            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setMessage("아이디를 입력해주세요");
                alert.show();
            }


    }

}
