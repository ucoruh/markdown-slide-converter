set GRAPHVIZ_DOT="C:\Program Files\Graphviz\bin\dot.exe"
set DOXYGEN_EXE="C:\Program Files\doxygen\bin\doxygen.exe"
set SOURCE_DIR=..\src
set DOC_IMG_PATH_UML=..\doxygen\Resources
set DOC_IMG_PATH=../doxygen/Resources
set MAIN_PAGE="..\README.md"
rem SET STRIP_PATH="C:/Users/ugur.coruh/Desktop/"
IF NOT EXIST plantuml.jar ( curl -o plantuml.jar "https://github.com/plantuml/plantuml/releases/download/v1.2021.14/plantuml-1.2021.14.jar" )
java -jar "plantuml.jar" -v -o "%DOC_IMG_PATH_UML%" "%SOURCE_DIR%/**.(c|cpp|doc|h|puml)"
doxygen Doxyfile
pause
