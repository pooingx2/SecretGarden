#! /bin/sh

#기본 키설정
echo "@@VPN 서버 및 클라이언트를 위한 키를 생성합니다"

#파일 컴파일
echo "@@수행될 서버 프로그램을 컴파일 합니다"
make

#네트워크 재시작
echo "@@VPN서버를 구동합니다"
/etc/init.d/openvpn restart

#메시징 서버 시작
echo "@@메세징 서버를 구동합니다"
./passingServ 12600
