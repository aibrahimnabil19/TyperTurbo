@echo off
setlocal

set "ROOT=%~dp0"
set "CP=%ROOT%out\production\Typing Test;%ROOT%lib\*"

java -cp "%CP%" HomePage

if errorlevel 1 (
    echo.
    echo Typer Turbo could not be started.
    pause
)

endlocal
