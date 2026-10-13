@echo off
REM Compila y ejecuta la demo (Windows)
cd /d "%~dp0"
chcp 65001 > nul
if exist out rmdir /s /q out
dir /s /b src\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
if errorlevel 1 (
  del fuentes.txt
  echo.
  echo Hubo un error compilando. Revisar JDK 17 o superior.
  pause
  exit /b 1
)
del fuentes.txt
java -Dstdout.encoding=UTF-8 -cp out app.Main
pause
