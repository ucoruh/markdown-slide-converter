@echo off
@setlocal enableextensions
@cd /d "%~dp0"

REM Uninstall packages installed via Chocolatey
choco uninstall pandoc -y
choco uninstall rsvg-convert -y
choco uninstall python -y
choco uninstall miktex -y
choco uninstall curl -y
choco uninstall marp-cli -y
choco uninstall doxygen.install -y
choco uninstall graphviz -y

REM Uninstall Python packages
pip uninstall mkdocs -y
pip uninstall pymdown-extensions -y
pip uninstall mkdocs-material -y
pip uninstall mkdocs-material-extensions -y
pip uninstall mkdocs-simple-hooks -y
pip uninstall mkdocs-video -y
pip uninstall mkdocs-minify-plugin -y
pip uninstall mkdocs-git-revision-date-localized-plugin -y
pip uninstall mkdocs-minify-plugin -y
pip uninstall mkdocs-static-i18n -y
pip uninstall mkdocs-with-pdf -y
pip uninstall qrcode -y
pip uninstall mkdocs-awesome-pages-plugin -y
pip uninstall mkdocs-embed-external-markdown -y
pip uninstall mkdocs-include-markdown-plugin -y
pip uninstall mkdocs-ezlinks-plugin -y
pip uninstall mkdocs-git-authors-plugin -y
pip uninstall mkdocs-git-committers-plugin -y
pip uninstall mkdocs-exclude -y

REM Uninstall Scoop
powershell Remove-Item "$env:USERPROFILE\scoop" -Recurse

REM Uninstall Chocolatey
powershell Remove-Item "$env:ProgramData\Chocolatey" -Recurse

pause