import threading
import socket
import pymysql
import RPi.GPIO as GPIO
import time
import Adafruit_DHT as dht
import json

light_state = False
door_state = False
temp = 0.0
humidity = 0.0
#-- 온습도, 도어락, LED 정보를 초기화
DB =pymysql.connect(host='localhost',port=3306,user='root',passwd='****',db='mydb')
#-- DB처리시 user는 기본으로 root계정으로 하였으므로 유저를 만들어서 변경해 주시기 바랍니다.
GPIO.setmode(GPIO.BCM)
GPIO.setup(26,GPIO.OUT)
GPIO.setup(19,GPIO.OUT)
#-- 라즈베리파이 하드웨어 제어를 위해서 26번 핀과 19번 핀으로 제어를 하였습니다.

#---------------------DB Controller ---------------------------------------
#-- funtion name: qurry_exe
#-- funtion parameter: str Type
#-- funtion contents: str형 쿼리문을 받아와 기능을 수행하고 select문으로 처리하면 데이터를 돌려준다
def qurry_exe(sql):
    global DB
    cursor = DB.cursor()
    cursor.execute(sql)
    DB.commit()
    return cursor.fetchall()

#-- funtion name: insert_db
#-- funtion parameter: str type, str type
#-- funtion contents: 데이터를 받아와 sql insert작업을 정리하였다.
def insert_db(table,qurry):
    sql = "INSERT INTO " + table + " VALUES(" + qurry + ")"
    qurry_exe(sql)
#-- funtion name: select_table
#-- funtion parameter: str type
#-- funtion contents: select 작업
def select_table(table):
    sql = "SELECT * FROM mydb." + table
    return qurry_exe(sql)
#-- funtion name: pwd_check 
#-- funtion parameter: str type, str type
#-- funtion contents: id, pwd를 받아와 쿼리에서 처리후 출력합니다. (보안상 코드 재사용시 새롭게 작성해주세요.)
def pwd_check(iddb,pwd):
    sql = "SELECT Password FROM mydb.User WHERE ID LIKE '" + iddb + "'"
    qurry = qurry_exe(sql)
    return value_check(pwd, qurry)

#-- funtion name:sign_up
#-- funtion contents: 회원가입
def sign_up(iddb,pwd,name):
    qurry = "'" + iddb + "', '" + pwd + "', '" + name + "', 'serial1'"
    try :
        insert_db("mydb.User", qurry)
    except :
        return False
    return True
#-- funtion name:id_check
#-- funtion contents:아이디 중복 체크
def id_check(iddb) :
    sql = "SELECT ID FROM mydb.User WHERE ID LIKE '"+iddb+"'"
    qurry =qurry_exe(sql)
    return not(value_check(iddb,qurry))
#-- funtion name:value_check
#-- funtion contents:쿼리에 있으면 출력해주고 없으면 false
def value_check(value, qurry) :
    if qurry == () :
        return False
    else :
        return value in qurry[0]

#--------------------Sever--------------------------------------------
#-- funtion name:echo
#-- funtion contents: 서버 쓰레드로 작동
def echo(sock):
    while True:
        data_list= []
        db_list=[]
       
        while True:
            data = sock.recv(1024)
            data_list.append(data.decode())     
            if '$END' == data.decode() : break
            elif not data : break
            
            time.sleep(1)
            print(data_list[0])
            if data_list[0] == '1' :
                sock.sendall(str(pwd_check(data_list[1],data_list[2])).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            elif data_list[0] == '2' :
                sock.sendall(sign_up(data_list[1],data_list[2],data_list[3]).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            elif data_list[0] == '3' :
                led_light()
                sock.sendall(state_data().encode())
            elif data_list[0] == '4' :
                door_control()
                sock.sendall(state_data().encode())
            elif data_list[0] == '5' :
                db_Light_Sock(sock)
            elif data_list[0] == '6' :
                db_Door_Sock(sock)
            elif data_list[0] == '7' :
                db_Temp_Sock(sock)
            elif data_list[0] == '8' :
                sock.sendall(str(id_check(data_list[1])).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            else :
                print("err")
            
        data_list.clear()
        db_list.clear()
    
    sock.close()
#-- funtion name:run_server
#-- funtion contents:서버 실행
def run_server(port=8008):
    host = ''
    data_list= []
    db_list=[]
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try :
        s.bind((host, port))
        s.listen(2)
       
        while True:
            global light_state, door_state
            
            sock,addr = s.accept()
            print("connect addr : ", addr)
            
            while True:
                data = sock.recv(1024)
                data_list.append(data.decode())        
                if '$END' == data.decode() : break
                elif not data : break

            time.sleep(1)
            print(data_list[0])
            if data_list[0] == '0': #처음 클라이언트에 데이터 줌
                sock.sendall(state_data().encode())
            elif data_list[0] == '1' :  #로그인
                sock.sendall(str(pwd_check(data_list[1],data_list[2])).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            elif data_list[0] == '2' :  #회원가입
                sock.sendall(sign_up(data_list[1],data_list[2],data_list[3]).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            elif data_list[0] == '3' :  #LED제어
                led_light()
                sock.sendall(str(light_state).encode())
            elif data_list[0] == '4' :  #doorlock제어
                door_control()
                sock.sendall(str(door_state).encode())
            elif data_list[0] == '5' :  #LED정보 호출
                db_Light_Sock(sock)
            elif data_list[0] == '6' :  #doorlock 정보 호출
                db_Door_Sock(sock)
            elif data_list[0] == '7' :  #dlc 추가 금액 후원하면 보임
##                db_Temp_Sock(sock)
                pass
            elif data_list[0] == '8' :  #아이디 중복 체크
                sock.sendall(str(id_check(data_list[1])).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            else :
                print("err")
                
            data_list.clear()
            db_list.clear() 
            sock.close()
    except :
        s.close()           
        
#----------------Controller------------------------------------------------------
#-- funtion name:led_light
#-- funtion contents:LED 제어 및 db에 데이터 저장
def led_light():
    global light_state
    if light_state == False :
        GPIO.output(26,True)
        light_state = True
        qurry = "NOW(),'1',NULL"
    else :
        GPIO.output(26,False)
        light_state = False
        qurry = "NOW(),'0',NOW()"
    insert_db("mydb.Light",qurry)
#-- funtion name:door_control
#-- funtion contents: 도어락 제어및 db에 데이터 저장
def door_control():
    global door_state
    GPIO.output(19,False)
    time.sleep(1)
    GPIO.output(19,True)
    if door_state == False :
        door_state = True
        qurry = "NOW(), NULL, 'serial1'"
    else:
        door_state = False
        qurry = "NOW(), NOW(), 'serial1'"
    insert_db("mydb.Door",qurry)
#-- funtion name: temp_time
#-- funtion contents: 온습도 데이터를 센서로 받아오고 3시간 마다 db에 저장 
def temp_time() :
    global temp, humidity
    humidity, temp=so = dht.read_retry(dht.DHT11, 4)
    thd =threading.Timer(10800,temp_time)
    qurry ='NOW(),' +str(temp) + ',' + str(humidity)
    insert_db("mydb.Tempdata",qurry)
    thd.daemon = True
    thd.start()
#-- funtion name:state_data
#-- funtion contents: 온도, 습도, 도어락, LED정보를 출력
def state_data():
    global temp, humidity, light_state, door_state
    data = str(temp) + "," + str(humidity) + "," + str(door_state) + "," + str(light_state)
    return data
#-- funtion name: db_Door_Sock
#-- funtion contents: 도어락 정보를 json으로 보냄
def db_Door_Sock(sock):
    i = 0
    dictdate = {}
    rows = select_table("Door")
    for data in rows :
        dictdate[i] = {'OpenT' : str(data[0]), 'CloseT' : str(data[1])}
        i += 1
    jstring = json.dumps(dictdate,ensure_ascii=False,indent="\t")
    sock.sendall(jstring.encode())
#-- funtion name: db_Light_Sock
#-- funtion contents: LED 정보를 json으로 보냄
def db_Light_Sock(sock):
    i = 0
    dictdate = {}
    rows = select_table("Light")
    for data in rows :
        dictdate[i] = {'OnT':str(data[0]), 'OffT':str(data[2])}
        i += 1
    jstring = json.dumps(dictdate,ensure_ascii=False,indent="\t")
    sock.sendall(jstring.encode())
#-- funtion name: db_Temp_Sock
#-- funtion contents: 온습도 정보를 json으로 보냄
def db_Temp_Sock(sock):
    i = 0
    dictdate = {}
    rows = select_table("Temp")
    for data in rows :
        dictdate[i] = {'Tday':str(data[0]), 'AvgD':str(data[1]), 'HtimeTe':str(data[2])}
        i += 1
    jstring = json.dumps(dictdate,ensure_ascii=False,indent="\t")
    sock.sendall(jstring.encode())
#__int__________________________________________________
temp_time()
time.sleep(5)
GPIO.output(19,True)

#__main________________________________________________

while True :
    run_server()

#__end clear_____________________________________________
GPIO.cleanup()
DB.close()
