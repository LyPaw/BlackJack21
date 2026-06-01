@echo off
chcp 65001 >nul
echo === Compilando proyecto ===
call mvn clean package -q
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fallo la compilacion con Maven.
    pause
    exit /b 1
)
echo Compilacion exitosa.
echo.
echo === Iniciando Blackjack 21 ===
echo.
java --module-path "target\lib" --add-modules javafx.controls,javafx.media -cp "target\classes;target\lib\*" com.blackjack.app.Juego21App
pause
