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

echo Refreshing the environment variables...
RefreshEnv