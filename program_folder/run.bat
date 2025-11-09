@echo off
setlocal
set "JAVA_EXE=java"
"%JAVA_EXE%" -jar "%~dp0program.jar"
echo.
echo Exit code: %ERRORLEVEL%
pause
endlocal