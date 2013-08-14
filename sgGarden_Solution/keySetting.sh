#!/bin/bash

#1. 키 생성
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && rm -rf keys
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && mkdir keys
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && source ./vars
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && ./clean-all
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && ./build-ca
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && ./build-key-server server
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && ./build-key authModule
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && ./build-key sgClient
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && ./build-dh

#2. 각 모듈로 배포 
cd /home/sgGarden_Solution/authModule/openvpn/easy-rsa/2.0/ && rm -rf keys
cd /home/sgGarden_Solution/authModule/openvpn/easy-rsa/2.0/ && mkdir keys
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0/keys && cp ca.crt /home/sgGarden_Solution/authModule/openvpn/easy-rsa/2.0/keys/
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0/keys && cp authModule.crt /home/sgGarden_Solution/authModule/openvpn/easy-rsa/2.0/keys/
cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0/keys && cp authModule.key /home/sgGarden_Solution/authModule/openvpn/easy-rsa/2.0/keys/

#cd /home/sgGarden_Solution/passingServ/src/openvpn/easy-rsa/2.0 && source ./vars2. 각 모듈로 배포

