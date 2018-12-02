import sys
from PyQt5.QtWidgets import *
import socket
import time
import json

class MyWindow(QMainWindow):
    light_state = 'False'
    door_state = 'False'
    temp = '0.0'
    humidity = '0.0'
    
    def __init__(self):
        super().__init__()
        state = self.data_recv(0)
        self.state_set(state)
        self.setWindowTitle("Homebutler")
        self.setGeometry(300, 300, 300, 400)
        
        btn_led = QPushButton("LED Control", self)
        btn_led.resize(150,40)
        btn_led.move(10, 0)
        btn_led.clicked.connect(self.btn_led_clicked)
        label_led = QLabel("LED",self)
        label_led.resize(60,40)
        label_led.move(170,0)
        self.state_led = QLabel(self.light_state,self)
        self.state_led.resize(40,40)
        self.state_led.move(250,0)
        
        btn_door = QPushButton("DoorLock Control", self)
        btn_door.resize(150,40)
        btn_door.move(10, 50)
        btn_door.clicked.connect(self.btn_door_clicked)
        label_door = QLabel("Door",self)
        label_door.resize(60,40)
        label_door.move(170,50)
        self.state_door = QLabel(self.door_state,self)
        self.state_door.resize(40,40)
        self.state_door.move(250,50)
        
        label_temp = QLabel("temp",self)
        label_temp.resize(60,40)
        label_temp.move(10,100)
        self.state_temp = QLabel(self.temp,self)
        self.state_temp.resize(40,40)
        self.state_temp.move(70,100)
        
        label_humidity = QLabel("humidity",self)
        label_humidity.resize(70,40)
        label_humidity.move(170,100)
        self.state_humidity = QLabel(self.humidity,self)
        self.state_humidity.resize(40,40)
        self.state_humidity.move(250,100)       
        
        btn_door_info = QPushButton("door_info", self)
        btn_door_info.resize(150,40)
        btn_door_info.move(10, 220)
        btn_door_info.clicked.connect(self.btn_door_info_clicked)
        
    def data_recv(self,num):
        sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        sock.connect(('192.168.43.264',8008))
        sock.sendall(str(num).encode())
        time.sleep(0.1)
        sock.sendall('$END'.encode())
        s = ''
        while True:
            data = sock.recv(1024)
            s += data.decode()
            if not data : break
        sock.close()
        return s
    
    def state_set(self,state):
        self.temp = state.split(',')[0]
        self.humidity = state.split(',')[1]
        self.light_state = state.split(',')[2]
        self.door_state = state.split(',')[3]
        
    def state_update(self):
        self.state_led.setText(self.light_state)
        self.state_door.setText(self.door_state)
        self.state_temp.setText(self.temp)
        self.state_humidity.setText(self.humidity)
    
    def btn_led_clicked(self):
        state = self.data_recv(3)
        self.light_state = state
        self.state_update()
                
    def btn_door_clicked(self):
        state = self.data_recv(4)
        self.door_state = state
        self.state_update()
        
    def btn_door_info_clicked(self):
        s = self.data_recv(6)
        door_dlg = door_text_dialog(None,s)
        door_dlg.exec_()
        
class door_text_dialog(QDialog):
    def __init__(self,parent,s):
        super(door_text_dialog,self).__init__(parent)
        self.setWindowTitle("Door Info")
        self.setGeometry(300, 300, 300, 400)
        self.door_text = QTextEdit(self)
        self.door_text.resize(300,400)
        
        jdata = json.loads(s)
        
        for i in range(1,len(jdata)):
            for k,v in jdata.items():
                if k == str(i):
                    if v['CloseT'] == 'None' :
                        self.door_text.append("Open :"+v['OpenT'])
                    else :
                        self.door_text.append("Close :" +v['CloseT'])
                else :
                    pass
    
if __name__ == "__main__":
    app = QApplication(sys.argv)
    myWindow = MyWindow()
    myWindow.show()
    app.exec_()
