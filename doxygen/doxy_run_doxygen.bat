@echo off

:: Set the source directory
set "SOURCE_DIR=..\src"

:: Set the output directory
set "OUTPUT_DIR=docs"

:: Set the log file
set "LOG_FILE=doxygen_warning.log"

:: Set the path to the directory where PlantUML images will be generated
set "DOC_IMG_PATH_UML=..\doxygen\assets"

:: Set the path to the directory where other images will be copied
set "DOC_IMG_PATH=../doxygen/assets"

:: Set the path to the main page of the documentation
:: for mainpage add following command to begining of README.md file add to doxygen config
:: 
:: Main Page {#mainpage}
:: =========
:: 
set "MAIN_PAGE=..\README.md"

echo Generate PlantUML diagrams and save them to the specified directory
java -jar "plantuml.jar" -v -o "%DOC_IMG_PATH_UML%" "%SOURCE_DIR%/**.(c|cpp|doc|h|java|cs|puml)"

echo Plantuml Execution Completed

echo Run Doxygen with the specified configuration file
doxygen Doxyfile

echo Doxygen Execution Completed
pause

