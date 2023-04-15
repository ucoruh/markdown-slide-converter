@echo off

echo Uninstalling Doxygen Related Applications

:: Check if Chocolatey is installed, if not install it using PowerShell
if not exist "%ProgramData%\Chocolatey\bin\choco.exe" (
    echo Installing Chocolatey...
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))"
)

echo Uninstalling PlantUML...
choco uninstall plantuml -y

echo Uninstalling Graphviz...
choco uninstall graphviz -y

echo Uninstalling Doxygen...
choco uninstall doxygen.install -y

echo Uninstalling Completed

pause