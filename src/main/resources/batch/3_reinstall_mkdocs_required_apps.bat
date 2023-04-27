@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Checking if Chocolatey is installed...
if exist "%ProgramData%\Chocolatey\bin\choco.exe" (
    echo Chocolatey is already installed. Uninstalling...
    choco uninstall chocolatey -y
) else (
    echo Chocolatey is not installed.
)

echo Checking if Scoop is installed...
where scoop >nul 2>&1
if %errorlevel%==0 (
    echo Scoop is already installed. Uninstalling...
    scoop uninstall scoop -f
) else (
    echo Scoop is not installed.
)

echo Re-installing Chocolatey...
powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))"

echo Re-installing Scoop...
powershell Invoke-Expression (New-Object System.Net.WebClient).DownloadString('https://get.scoop.sh')
powershell Set-ExecutionPolicy RemoteSigned -scope CurrentUser

echo Re-installing Pandoc...
choco install pandoc --force --force-dependencies -y

echo Re-installing rsvg-convert...
choco install rsvg-convert --force --force-dependencies -y

echo Re-installing Python...
choco install python --force --force-dependencies -y

echo Re-installing Miktex...
choco install miktex --force --force-dependencies -y

echo Re-installing CuRL...
choco install curl --force --force-dependencies -y

echo Re-installing MARP-CLI...
choco install marp-cli --force --force-dependencies -y

echo Re-installing Doxygen...
choco install doxygen.install --force --force-dependencies -y

echo Re-installing Graphviz...
choco install graphviz --force --force-dependencies -y

echo Re-installing Python packages...
pip install --upgrade --force-reinstall mkdocs
pip install --upgrade --force-reinstall pymdown-extensions
pip install --upgrade --force-reinstall mkdocs-material
pip install --upgrade --force-reinstall mkdocs-material-extensions
pip install --upgrade --force-reinstall mkdocs-simple-hooks
pip install --upgrade --force-reinstall mkdocs-video
pip install --upgrade --force-reinstall mkdocs-minify-plugin
pip install --upgrade --force-reinstall mkdocs-git-revision-date-localized-plugin
pip install --upgrade --force-reinstall mkdocs-minify-plugin
pip install --upgrade --force-reinstall mkdocs-static-i18n
pip install --upgrade --force-reinstall mkdocs-with-pdf
pip install --upgrade --force-reinstall qrcode
pip install --upgrade --force-reinstall mkdocs-awesome-pages-plugin
pip install --upgrade --force-reinstall mkdocs-embed-external-markdown
pip install --upgrade --force-reinstall mkdocs-include-markdown-plugin
pip install --upgrade --force-reinstall mkdocs-ezlinks-plugin
pip install --upgrade --force-reinstall mkdocs-git-authors-plugin
pip install --upgrade --force-reinstall mkdocs-git-committers-plugin
pip install --upgrade --force-reinstall mkdocs-exclude
pip install --upgrade --force-reinstall pptx2md

pause
