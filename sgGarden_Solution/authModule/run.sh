#! /bin/sh

#RUN VPN
echo "Run VPN"
openvpn --config /home/ilhwanjeong/workspace/SecretGa restart


#RUN Server
cd authServ && make
cd ..
cd dirServ && make
cd ..

#chsh

sh runAuth.sh &
sh runDir.sh &
#sh runHDFS.sh 


#cd authServ && ./authServ  12700 &   
#cd .. 
#cd dirServ && ./dirServ 12700 &                                                                                
#cd ..

wait
#/etc/init.d/openvpn stop
#cd openvpn && openvpn --config client.conf
                            
