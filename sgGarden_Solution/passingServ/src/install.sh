#! /bin/sh


#관련 라이브러리 실행
echo "@@Library install"
apt-get install mysql-server
apt-get install mysql-client
apt-get install openvpn
apt-get install openssl
apt-get install libssl-dev


#기본 키설정
echo "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
echo "VPN 설정 파일을 시스템에 복사 합니다"

cd /home/sgGarden_Solution/passingServ/src/ && sudo cp -R -f openvpn /etc/


#파일 컴파일
echo "@@수행될 서버 프로그램을 컴파일 합니다"
make

#네트워크 재시작
echo "@@VPN서비스를 등록합니다"
#/etc/init.d/openvpn restart




