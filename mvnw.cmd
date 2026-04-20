@echo off
setlocal
set "WRAPPER_DIR=%~dp0.mvn\wrapper"
set "PROPS_FILE=%WRAPPER_DIR%\maven-wrapper.properties"
for /f "tokens=2 delims==" %%A in ('findstr /b "distributionUrl=" "%PROPS_FILE%"') do set "DIST_URL=%%A"
if "%DIST_URL%"=="" (
  echo Unable to resolve Maven distribution URL.
  exit /b 1
)
set "MAVEN_DIR=%~dp0.mvn\apache-maven"
set "MAVEN_HOME="
for /d %%D in ("%MAVEN_DIR%\apache-maven-*") do set "MAVEN_HOME=%%~fD"
if not defined MAVEN_HOME (
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ErrorActionPreference='Stop';" ^
    "$ProgressPreference='SilentlyContinue';" ^
    "[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12;" ^
    "$url='%DIST_URL%';" ^
    "$zip=Join-Path '%WRAPPER_DIR%' 'apache-maven.zip';" ^
    "$dest='%MAVEN_DIR%';" ^
    "New-Item -ItemType Directory -Force -Path $dest | Out-Null;" ^
    "Invoke-WebRequest -Uri $url -OutFile $zip;" ^
    "Expand-Archive -Path $zip -DestinationPath $dest -Force;"
  if errorlevel 1 exit /b %errorlevel%
  for /d %%D in ("%MAVEN_DIR%\apache-maven-*") do set "MAVEN_HOME=%%~fD"
)
if not defined MAVEN_HOME (
  echo Maven bootstrap failed.
  exit /b 1
)
"%MAVEN_HOME%\bin\mvn.cmd" %*
