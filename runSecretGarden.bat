


@echo off
start runVpn
choice /t 10 /d n > nul
java -jar SecretGarden.jar
