@echo off
if not exist lib mkdir lib
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar' -OutFile 'lib\mysql-connector-j-8.4.0.jar'"
echo MySQL Connector/J downloaded to lib folder.
pause
