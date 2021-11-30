@echo off
SET BEFORE_HTTPFLOW_CHANGE=%CD%
cd /D %~dp0\..\work

java -jar ..\lib\httpflow-console-0.0.1.jar %*

cd /D %BEFORE_HTTPFLOW_CHANGE%