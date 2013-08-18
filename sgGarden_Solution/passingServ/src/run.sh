

server_ip=""
mysql_password=""

echo "Compile 소스 파일"
make



#echo "VPN 실행"
#sudo openvpn --config /etc/openvpn/openvpn.conf &

echo -n "Mysql 비밀번호 입력 : "
read -s mysql_password
echo ""
echo -n "서버 IP 입력 : "
read -s server_ip
echo ""



#echo "메세징 서버 구동"
#sudo ./passingServ 13000 &

