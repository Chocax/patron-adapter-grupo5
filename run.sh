#!/usr/bin/env bash
# Compila y ejecuta la demo (Mac / Linux / Git Bash en Windows)
set -e
cd "$(dirname "$0")"
rm -rf out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -Dstdout.encoding=UTF-8 -cp out app.Main
