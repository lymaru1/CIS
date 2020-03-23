# CIS
homebutler - project raspberry Pi -led, doorlock, temp data control sever and simple gui client

작업 환경은 -raspberry pi 3b+ 기기에 라즈베리안 환경에서 작성 하였으며 작업 툴은 vim, python IDLE에서 작업 하였습니다.

라즈베리파이에서 LED 제어, 도어락 제어, 온습도 정보를 받아와 db에 저장하는 기능을 가지고 있습니다.

client는 기본적으로 led 제어 기능, doorlock 제어 기능, 온습도 정보를 서버에서 받아오는 기능을 가지고 있으며
데이터는 json으로 받아오며 소켓통신을 통해 데이터를 주고 받습니다.

hombutler는 도어락 상태 표시, LED 상태 표시, 온습도 정보 표시등 각각의 정보를 갖고 있습니다.
도어락및 LED는 제어를 하여 상태를 바꾸어 주고 온습도 데이터는 기기 온습도 센서를 통해서 데이터를 받아와 출력 해줍니다.
제어와 3시간 마다 온습도 센서를 통해 데이터를 DB에 저장하고 출력하는 문장도 있습니다.

기본적인 처리는 서버에서 정보를 받아 처리 후 클라이언트에 제공해주며 처리를 위해 escape 문을 주어($END) 처리하여
도어락, LED, 온습도 정보를 받아와 출력 해주고 제어및 정보를 저장하는 기능을 갖고 있습니다.

DB정보는 사용자에 맞게 다시 세팅하여 사용해야합니다.
데이터를 암호화 및 복호화를 못하여 login 및 sign up 기능을 다시 생성해야하며 
tempdata 테이블을 작성시 기본키를 현재시간을 받아 처리하여 이 코드를 사용 하시려면 DB 소스를 참고하여 사용하십시오.

<div>
  <img width="300" height="400" src="https://github.com/lymaru1/CIS/blob/master/img/door.png">
  <img width="300" height="400" src="https://github.com/lymaru1/CIS/blob/master/img/light.png">
  <img width="300" height="400" src="https://github.com/lymaru1/CIS/blob/master/img/history.png">
</div>
