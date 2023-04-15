Main Page {#mainpage}
=========

# markdown-slide-converter

Remove Markdown Slide/NewPage Notations and Merge Slide Pages,  Generate Single Markdown Page for Mkdocs and Pandocs

## Export Draw.IO Images

Following script export svg,png,jpeg images to /assets/ folder for markdown

```bash
java -jar markdown-slide-converter.jar --drawioexport --folder "ce100-algorithms-and-programming-II\docs"
```

## Build Plantuml Scripts (with script)

Following scripts search folder for *.puml files and generate SVG and PNG images for markdown files

### Install Chocolatey and CURL

Following utilities required for package installation

```bash
echo Check if Chocolatey is installed, if not install it using PowerShell...
if not exist "%ProgramData%\Chocolatey\bin\choco.exe" (
    echo Installing Chocolatey...
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))"
)

echo Chocolatey Setup Passed

echo Check if curl is installed, if not install it using Chocolatey
choco install curl --force --force-dependencies -y
echo curl Setup Passed
```

### Download and Install Graphiz with CURL

```bash
echo Check if Graphviz is installed, if not install it using Chocolatey
choco install graphviz --force --force-dependencies -y
echo Graphviz Setup Passed
```

### Download and Install Doxygen (DEVELOPMENT PURPOSE)

```bash
echo Check if Graphviz is installed, if not install it using Chocolatey
choco install graphviz --force --force-dependencies -y
echo Graphviz Setup Passed
```

### Set PATH variable (RECOMMENDED MANUAL UPDATE)

* **Bug**: Strip the PATH variable.

```bash
@echo off

:: Define the installation directories
set "DOXYGEN_INSTALL_DIR=C:\Program Files\doxygen"
set "GRAPHVIZ_INSTALL_DIR=C:\Program Files\Graphviz"

:: Set the path to Graphviz's dot executable
set "GRAPHVIZ_DOT=%GRAPHVIZ_INSTALL_DIR%\bin\dot.exe"

:: Set the path to Doxygen's executable
set "DOXYGEN_EXE=%DOXYGEN_INSTALL_DIR%\bin\doxygen.exe"

Add the Graphviz and Doxygen installation directory to the PATH environment variable
setx PATH "%DOXYGEN_INSTALL_DIR%\bin;%GRAPHVIZ_INSTALL_DIR%\bin;%PATH%" /M

echo Refresh the environment variables
RefreshEnv
```

### Download Plantuml with Script

```bash
@echo off
setlocal enabledelayedexpansion

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

pause

```

Call SVG and PNG Output Scripts

### SVG Outputs

```bash
@echo off
setlocal

REM Run PlantUML on all .puml files in the current directory and its subdirectories, and output files to the plantuml folder
java -Dplantuml.include.path="./plantuml/" -jar plantuml.jar -charset UTF-8 -svg -v -r "./**.puml"

pause
```

### PNG Outputs

```bash
@echo off
setlocal

REM Run PlantUML on all .puml files in the current directory and its subdirectories, and output files to the plantuml folder
java -Dplantuml.include.path="./plantuml/" -jar plantuml.jar -charset UTF-8 -v -r "./**.puml"

pause
```

## Merge Pages

Following script read marp formatted markdown files and remove page seperators. If there is a similar title then remove them. Update marp formatted image links for pandoc and prepare presentation, docx and pdf enable markdown files for build phase, if build or rebuild flag present then build operation executed

```bash
java -jar markdown-slide-converter.jar --mergepages --folder "ce100-algorithms-and-programming-II\docs" --pandoc --mkdocs --overwrite --language en tr --rebuild --build
```

## Build Pages

Search files with prefix, postfix and extension and according to prefix,postfix and extension generate html, pdf, docx,pptx files with marp and pandoc. If merge option has --build or --rebuild flag then build task executed.

```bash
java -jar markdown-slide-converter.jar --buildpages --folder "ce100-algorithms-and-programming-II\docs" --pandoc --mkdocs --overwrite --language en tr --rebuild --build
```
