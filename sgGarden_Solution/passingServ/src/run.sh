
echo "Compile 소스 파일"
make

echo "VPN 실행"
sudo openvpn --config /etc/openvpn/openvpn.conf &

echo "메세징 서버 구동"
sudo ./passingServ 13000 &

