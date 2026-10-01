@echo off

if /I "%~1"=="sloth" goto sloth_upload
if /I "%~1"=="fast" goto sloth_upload

if /I "%~1"=="teamcode" goto teamcode_upload
if /I "%~1"=="normal" goto teamcode_upload
if /I "%~1"=="slow" goto teamcode_upload

:sloth_upload
echo Starting sloth upload...
echo.
gradlew deploySloth
echo Upload finished successfully
goto :eof

:teamcode_upload
adb connect 192.168.43.1:5555
adb -s 192.168.43.1:5555 get-state >nul 2>&1
if %errorlevel% neq 0 (
	echo.
	echo [ERROR] ADB is not connecting
	exit /b %errorlevel%
)
echo Connected to control hub
echo.
echo Starting teamcode upload...
echo.
gradlew assembleDebug
if %errorlevel% neq 0 (
	echo [ERROR] Teamcode upload failed
	exit /b %errorlevel%
)
adb disconnect
echo Upload finished successfully
goto :eof