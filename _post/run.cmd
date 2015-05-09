@echo off
REM Windows - Post build script for flob projects
REM Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
REM All rights reserved.

set project=blackheart

echo #### Obfuscating %project%.jar
java -jar ../../../../lib/proguard/proguard.jar @%project%.pg
cd ../dist
echo #### Removing the build cruft
del %project%.jar
del README.TXT
rmdir /s /q "../build"
rename o_%project%.jar %project%.jar
echo #### Copying the documents
xcopy "../_docs" "docs" /S /I /Y /Q
echo #### Copying the native files
xcopy "../../../../lib/lwjgl2/native" "native" /S /I /Y /Q
echo #### Removing the native cruft
cd native
rmdir /s /q "solaris"
for /r %cd% %%f in (*jinput*) do (del  "%%f")
echo #### Done
pause
