@echo off
rem StudentManagerSystem - compile and run (MVC refactor)
rem Uses UTF-8 console so Chinese input and output are handled correctly.
chcp 65001 >nul
cd /d "%~dp0"
setlocal enabledelayedexpansion

if not exist out mkdir out

rem Collect all source files into one command line (no @argfile,
rem because backslashes inside an argfile would be treated as escapes)
set SRCS=
for /f "delims=" %%f in ('dir /s /b src\*.java') do set SRCS=!SRCS! "%%f"

echo [1/2] compiling...
javac -encoding UTF-8 -d out !SRCS!
if errorlevel 1 (
    echo [FAIL] compile failed, see errors above.
    pause
    exit /b 1
)

echo [2/2] launching...
java -cp out com.jay.studentmanager.Main
