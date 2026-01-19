@echo off
chcp 65001 >nul
setlocal

set "PSQL=C:\Program Files\PostgreSQL\16\bin\psql.exe"
set "PGHOST=localhost"
set "PGPORT=5432"
set "PGUSER=postgres"
set "PGCLIENTENCODING=UTF8"

if not exist "%PSQL%" (
  echo psql.exe not found: "%PSQL%"
  pause
  exit /b 1
)

"%PSQL%" -h "%PGHOST%" -p "%PGPORT%" -U "%PGUSER%" -d postgres -v ON_ERROR_STOP=1 -f "%~dp0bookstore_init.sql"

if errorlevel 1 (
  echo FAILED
  pause
  exit /b 1
)

echo OK
pause
exit /b 0
