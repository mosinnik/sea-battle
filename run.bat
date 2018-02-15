@echo off
title game
:start
echo.
java -Xmx100m -Xms100m -cp ./SeaBattle.jar core.SeaBattle
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo.
goto start
:error
echo.
echo.
:end
echo.
echo.
pause
