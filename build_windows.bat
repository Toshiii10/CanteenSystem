@echo off
:: Force the script to look at your actual project directory instead of System32
cd /d "%~dp0"

if not exist build\classes mkdir build\classes

:: Delete old tracking file if it exists
if exist "%~dp0sources.txt" del "%~dp0sources.txt"

:: Loop through all .java files inside 'src' and write them as relative paths
for /R "%~dp0src" %%F in (*.java) do (
    set "ABS_PATH=%%F"
    setlocal enabledelayedexpansion
    set "REL_PATH=!ABS_PATH:%~dp0=!"
    echo !REL_PATH!>> "%~dp0sources.txt"
    endlocal
)

:: Compile using modern Java release properties (Removes obsolete warnings)
javac -encoding UTF-8 --release 17 -cp "lib\*" -d build\classes @"%~dp0sources.txt"
if errorlevel 1 (
    if exist "%~dp0sources.txt" del "%~dp0sources.txt"
    pause
    exit /b 1
)

:: Clean up the temporary file right after compilation completes
if exist "%~dp0sources.txt" del "%~dp0sources.txt"

:: Package the finalized build distributions
if not exist dist mkdir dist
jar cfm dist\CanteenSalesInventorySystem.jar manifest.mf -C build\classes .
jar cf dist\CanteenSalesInventorySystem-SourceCode.jar -C src .

echo Build successful.
pause