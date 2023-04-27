@echo off
setlocal enabledelayedexpansion

set url=http://beta.plantuml.net/plantuml-jlatexmath.zip

echo Setting current working directory...
CD /D "%~dp0"

echo Deleting PlantUML JLatexMath jar files...
del batik-all-1.7.jar
del jlatexmath-minimal-1.0.3.jar
del jlm_cyrillic.jar
del jlm_greek.jar

echo Downloading zip file from %url%...
curl -L -o "plantuml-jlatexmath.zip" "%url%"

echo Extracting contents to current directory...
tar -xf "plantuml-jlatexmath.zip" 

echo Cleaning up...
del "plantuml-jlatexmath.zip"

echo Deleting PlantUML jar file...
del plantuml.jar

echo Download and install jq
curl -sL -o jq.exe https://github.com/stedolan/jq/releases/download/jq-1.6/jq-win64.exe

echo Extract download URL that ends with "plantuml.jar" from JSON response using jq
for /f "delims=" %%a in ('curl -s https://api.github.com/repos/plantuml/plantuml/releases/latest ^| jq -r ".assets[] | select(.name | endswith(\"plantuml.jar\")) | .browser_download_url"') do (
    set download_url=%%a
)

echo Download plantuml.jar
curl -sL -o plantuml.jar "%download_url%"

echo Download URL: %download_url%
echo PlantUML downloaded successfully!

echo Deleting PlantUML jar file...
del jq.exe

echo Done!
pause
