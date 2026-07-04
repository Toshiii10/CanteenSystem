#!/usr/bin/env bash
set -e
mkdir -p build/classes dist
find src -name '*.java' > sources.txt
javac -encoding UTF-8 -source 8 -target 8 -cp "lib/*" -d build/classes @sources.txt
jar cfm dist/CanteenSalesInventorySystem.jar manifest.mf -C build/classes .
jar cf dist/CanteenSalesInventorySystem-SourceCode.jar -C src .
