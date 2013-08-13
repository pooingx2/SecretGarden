#! /bin/sh

#Library Setup  - Mysql, OpenVPN
echo "@@Library install"
apt-get install mysql-server
apt-get install mysql-client
apt-get install openvpn
apt-get install openssl
apt-get install libssl-dev

echo "@@JDK Install"
#add-apt-repository ppa:upubuntu-com/java
#apt-get update
#apt-get install oracle-java7-installer
echo "@@Create DB"



#Septu VPN - restart VPN
echo "@@VPN Client Setup And Restart"
#/etc/init.d/openvpn stop
#cd openvpn && openvpn --config client.conf
#openvpn --config home//client.conf

#Makefile - Compile Client
