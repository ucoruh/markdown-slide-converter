@echo off
setlocal enableextensions
cd /d "%~dp0"

rem Delete any remaining mkdocs_ folders
for /r %%# in (mkdocs_* pandoc_* panppt_*) do (
    del "%%#"
    echo "%%~nx#" deleted.
)

echo All mkdocs_* files deleted, building again

rem Delete any remaining mkdocs_ folders
for /r %%# in (*_slide.pdf *_slide.html *_slide.pptx *_word.pptx) do (
    del "%%#"
    echo "%%~nx#" deleted.
)

rem Delete any remaining tex2pdf folders
for /d %%i in (tex2pdf.*) do (
    echo deleting "%%~ni"...
    rd /s /q "%%i"
    echo "%%~ni" deleted.
)

echo All tex2pdf.* folders deleted.

echo Generating PLANTUML Images

java -DPLANTUML_LIMIT_SIZE=8192 -jar "plantuml.jar" -v "./**.(puml)"

java -DPLANTUML_LIMIT_SIZE=8192 -jar "plantuml.jar" -svg -v "./**.(puml)"

echo PLANTUML Images Exported as PNG and SVG

echo Exporting Images

java -jar markdown-slide-converter.jar --drawioexport --folder "docs"

echo Merge & Build Files
java -jar markdown-slide-converter.jar --mergepages --folder "docs" --pandoc --mkdocs --overwrite --language en tr --rebuild --build

echo Operation Completed
pause
