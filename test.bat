@echo off
rem StudentManagerSystem - compile and run tests (no try anywhere)
chcp 65001 >nul
cd /d "%~dp0"
setlocal enabledelayedexpansion

if not exist out mkdir out

rem Collect all source and test files into one command line (no @argfile,
rem because backslashes inside an argfile would be treated as escapes)
set SRCS=
for /f "delims=" %%f in ('dir /s /b src\*.java') do set SRCS=!SRCS! "%%f"
for /f "delims=" %%f in ('dir /s /b test\*.java') do set SRCS=!SRCS! "%%f"

echo compiling...
javac -encoding UTF-8 -d out !SRCS!
if errorlevel 1 (
    echo [FAIL] compile failed.
    pause
    exit /b 1
)

echo ================ SmokeTest ================
java -cp out com.jay.studentmanager.SmokeTest
echo.
pause
