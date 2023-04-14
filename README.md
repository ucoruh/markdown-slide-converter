# markdown-slide-converter

Remove Markdown Slide/NewPage Notations and Merge Slide Pages,  Generate Single Markdown Page for Mkdocs and Pandocs







## Export Draw.IO Images

Following script export svg,png,jpeg images to /assets/ folder for markdown

```bash
java -jar markdown-slide-converter.jar --drawioexport --folder "ce100-algorithms-and-programming-II\docs"
```

## Build Plantuml Scripts (with script)

Following scripts search folder for *.puml files and generate SVG and PNG images for markdown files

### SVG Outputs

```bash
@echo off
setlocal

REM Set the URL to download the latest version of PlantUML
set PLANTUML_URL=https://github.com/plantuml/plantuml/releases/download/v1.2021.14/plantuml-1.2021.14.jar

REM Check if the plantuml.jar file exists. If it doesn't, download it from the URL.
if not exist plantuml.jar (
    echo Downloading PlantUML...
    curl -L %PLANTUML_URL% -o plantuml.jar
)

REM Run PlantUML on all .puml files in the current directory and its subdirectories, and output files to the plantuml folder
java -Dplantuml.include.path="./plantuml/" -jar plantuml.jar -charset UTF-8 -svg -v -r "./**.puml"

pause

```

### PNG Outputs

```bash
@echo off
setlocal

REM Set the URL to download the latest version of PlantUML
set PLANTUML_URL=https://github.com/plantuml/plantuml/releases/download/v1.2021.14/plantuml-1.2021.14.jar

REM Check if the plantuml.jar file exists. If it doesn't, download it from the URL.
if not exist plantuml.jar (
    echo Downloading PlantUML...
    curl -L %PLANTUML_URL% -o plantuml.jar
)

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
