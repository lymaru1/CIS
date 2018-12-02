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
DB =pymysql.connect(host='localhost',port=3306,user='root',passwd='arumaru',db='mydb')
GPIO.setmode(GPIO.BCM)
GPIO.setup(26,GPIO.OUT)
GPIO.setup(19,GPIO.OUT)

#---------------------DB Controller ---------------------------------------
def qurry_exe(sql):
    global DB
    cursor = DB.cursor()
    cursor.execute(sql)
    DB.commit()
    return cursor.fetchall()
 
def insert_db(table,qurry):
    sql = "INSERT INTO " + table + " VALUES(" + qurry + ")"
    qurry_exe(sql)

def select_table(table):
    sql = "SELECT * FROM mydb." + table
    return qurry_exe(sql)

def pwd_check(iddb,pwd):
    sql = "SELECT Password FROM mydb.User WHERE ID LIKE '" + iddb + "'"
    qurry = qurry_exe(sql)
    return value_check(pwd, qurry)

def sign_up(iddb,pwd,name):
    qurry = "'" + iddb + "', '" + pwd + "', '" + name + "', 'serial1'"
    try :
        insert_db("mydb.User", qurry)
    except :
        return False
    return True
    
def id_check(iddb) :
    sql = "SELECT ID FROM mydb.User WHERE ID LIKE '"+iddb+"'"
    qurry =qurry_exe(sql)
    return not(value_check(iddb,qurry))
               
def value_check(value, qurry) :
    if qurry == () :
        return False
    else :
        return value in qurry[0]
#--------------------DB Transaction--------------------------------

#--------------------Sever--------------------------------------------
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
    
def run_server(port=8008):
    host = ''
    data_list= []
    db_list=[]
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try :
        s.bind((host, port))
        s.listen(2)
       
##        t = threading.Thread(target=echo, args=(sock,))
##        t.daemon = True
##        t.start()
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
            if data_list[0] == '0':
                sock.sendall(state_data().encode())
            elif data_list[0] == '1' :
                sock.sendall(str(pwd_check(data_list[1],data_list[2])).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            elif data_list[0] == '2' :
                sock.sendall(sign_up(data_list[1],data_list[2],data_list[3]).encode())
                sock.sendall(",".encode())
                sock.sendall(state_data().encode())
            elif data_list[0] == '3' :
                led_light()
                sock.sendall(str(light_state).encode())
            elif data_list[0] == '4' :
                door_control()
                sock.sendall(str(door_state).encode())
            elif data_list[0] == '5' :
                db_Light_Sock(sock)
            elif data_list[0] == '6' :
                db_Door_Sock(sock)
            elif data_list[0] == '7' :
##                db_Temp_Sock(sock)
                pass
            elif data_list[0] == '8' :
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

def temp_time() :
    global temp, humidity
    humidity, temp=so = dht.read_retry(dht.DHT11, 4)
    thd =threading.Timer(10800,temp_time)
    qurry ='NOW(),' +str(temp) + ',' + str(humidity)
    insert_db("mydb.Tempdata",qurry)
    thd.daemon = True
    thd.start()
    
def state_data():
    global temp, humidity, light_state, door_state
    data = str(temp) + "," + str(humidity) + "," + str(door_state) + "," + str(light_state)
    return data

def db_Door_Sock(sock):
    i = 0
    dictdate = {}
    rows = select_table("Door")
    for data in rows :
        dictdate[i] = {'OpenT' : str(data[0]), 'CloseT' : str(data[1])}
        i += 1
    jstring = json.dumps(dictdate,ensure_ascii=False,indent="\t")
    sock.sendall(jstring.encode())

def db_Light_Sock(sock):
    i = 0
    dictdate = {}
    rows = select_table("Light")
    for data in rows :
        dictdate[i] = {'OnT':str(data[0]), 'OffT':str(data[2])}
        i += 1
    jstring = json.dumps(dictdate,ensure_ascii=False,indent="\t")
    sock.sendall(jstring.encode())

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
