@echo off
echo ==========================================
echo      JAVATRO - BUILD AND RUN SCRIPT
echo ==========================================

cd ..\..

echo [1/3] Limpiando y creando carpeta bin...
if not exist bin mkdir bin

echo [2/3] Compilando codigo fuente...
javac --release 8 -d bin -sourcepath src src/javatro/app/Main.java
if %errorlevel% neq 0 (
    echo ERROR DE COMPILACION. Revisa los errores arriba.
    pause
    exit /b %errorlevel%
)

echo [3/3] Copiando recursos (imagenes y audio)...
xcopy /s /y /i "src\javatro\recursos" "bin\javatro\recursos" > nul

echo.
echo ==========================================
echo      EJECUTANDO JUEGO...
echo ==========================================
java -cp bin javatro.app.Main

pause
