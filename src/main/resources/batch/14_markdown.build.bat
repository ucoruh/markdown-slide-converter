@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Delete any remaining mkdocs_ folders
for /r %%# in (mkdocs_* pandoc_* panppt_*) do (
    del "%%#"
    echo "%%~nx#" deleted.
)

echo All mkdocs_* files deleted, building again

java -jar markdown-slide-converter.jar --drawioexport --folder docs

java -jar markdown-slide-converter.jar --mergepages --folder docs --pandoc --mkdocs --overwrite --language en tr

echo All mkdocs_* files created again.

echo Delete any remaining mkdocs_ folders
for /r %%# in (*_slide.pdf *_slide.html *_slide.pptx *_word.docx *_word.pptx) do (
    del "%%#"
    echo "%%~nx#" deleted.
)

echo Delete any remaining tex2pdf folders
for /d %%i in (tex2pdf.*) do (
    echo deleting "%%~ni"...
    rd /s /q "%%i"
    echo "%%~ni" deleted.
)

for /r %%# in (*.en.md *.tr.md) do (
    set "file_name=%%~nx#"
    echo processing "%%~f#"...
    marp "%file_name%" --html --pdf -o "%file_name%_slide.pdf" --allow-local-files
    marp "%file_name%" --html -o "%file_name%_slide.html" --allow-local-files
    marp "%file_name%" --pptx -o "%file_name%_slide.pptx" --allow-local-files
    pandoc "%file_name%" --pdf-engine=xelatex -f markdown-implicit_figures -V colorlinks -V urlcolor=NavyBlue -V to--toc -N -o "pandoc_%file_name%_doc.pdf"
	pandoc -o "pandoc_%file_name%_word.docx" -f markdown -t docx "%file_name%"
	pandoc -o "panppt_%file_name%_word.pptx" -f markdown -t pptx "%file_name%"
)

pause
