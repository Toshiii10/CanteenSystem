@echo off
:: Force the script to look at your actual project directory instead of System32
cd /d "%~dp0"

:: Quick check to make sure the database driver is present
if not exist "lib\mysql-connector-j-8.4.0.jar" (
    echo Missing JDBC driver. Run download_mysql_connector_driver_windows.bat first.
    pause
    exit /b 1
)

:: Run the application with both the MySQL driver AND FlatLaf library included in the classpath
java -cp "dist\CanteenSalesInventorySystem.jar;lib\mysql-connector-j-8.4.0.jar;lib\*" com.csis.Main
if errorlevel 1 (
    echo.
    echo Application crashed or failed to start. 
    echo Make sure you ran build_windows.bat after adding FlatLaf to your lib folder!
    pause
)