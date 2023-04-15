@echo off

:: Define the installation directories
set "DOXYGEN_INSTALL_DIR=C:\Program Files\doxygen"
set "GRAPHVIZ_INSTALL_DIR=C:\Program Files\Graphviz"

:: Set the path to Graphviz's dot executable
set "GRAPHVIZ_DOT=%GRAPHVIZ_INSTALL_DIR%\bin\dot.exe"

:: Set the path to Doxygen's executable
set "DOXYGEN_EXE=%DOXYGEN_INSTALL_DIR%\bin\doxygen.exe"

:: Check if the directories are already in the PATH variable, and add them if they are not
set "PATH_TO_ADD=%DOXYGEN_INSTALL_DIR%\bin;%GRAPHVIZ_INSTALL_DIR%\bin"

echo Adding directories to PATH variable...
setx PATH "%PATH_TO_ADD%;%PATH%" /M

echo Check if Chocolatey is installed, if not install it using PowerShell...
if not exist "%ProgramData%\Chocolatey\bin\choco.exe" (
    echo Installing Chocolatey...
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))"
)

echo Chocolatey Setup Passed

echo Check if curl is installed, if not install it using Chocolatey
choco install curl --force --force-dependencies -y
echo curl Setup Passed

echo Check if Doxygen is installed, if not install it using Chocolatey
choco install doxygen.install --force --force-dependencies -y
echo Doxygen Setup Passed

echo Check if Graphviz is installed, if not install it using Chocolatey
choco install graphviz --force --force-dependencies -y
echo Graphviz Setup Passed

::echo Check if Plantuml is installed, if not install it using Chocolatey
::choco install plantuml --force --force-dependencies -y
::echo PlantUML Setup Passed

echo Refreshing the environment variables...
RefreshEnv
