@echo off
REM Windows post build script
REM Copyright (c) 2013 Robert Calvert <http://robert.calvert.io>
REM
REM This program is free software; you can redistribute it and/or modify
REM it under the terms of the GNU General Public License as published by
REM the Free Software Foundation; either version 2 of the License, or
REM (at your option) any later version.
REM
REM This program is distributed in the hope that it will be useful,
REM but WITHOUT ANY WARRANTY; without even the implied warranty of
REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM GNU General Public License for more details.
REM
REM You should have received a copy of the GNU General Public License
REM along with this program.  If not, see <http://www.gnu.org/licenses/>

set project=blackheart

echo #### Obfuscating %project%.jar
java -jar ../../../../lib/proguard/proguard.jar @proguard.properties
cd ../dist
echo #### Removing the build cruft
del %project%.jar
del README.TXT
rmdir /s /q "../build"
rename o_%project%.jar %project%.jar
echo #### Copying the documents
mkdir docs
cd docs
copy "..\..\LICENSE.txt" %project%.txt
mkdir 3rdparty
cd 3rdparty
copy "..\..\..\src\io\flob\%project%\font\LICENSE.txt" fonts.txt
copy "..\..\..\..\..\..\lib\JOrbis\COPYING.LIB" JOrbis.txt
copy "..\..\..\..\..\..\lib\lwjgl2\doc\LICENSE" lwjgl2.txt
copy "..\..\..\..\..\..\lib\slick-util\LICENSE" slick.txt
echo #### Copying the native files
cd ../../lib
xcopy "../../../../../lib/lwjgl2/native" "native" /S /I /Y /Q
echo #### Removing the native cruft
cd native
rmdir /s /q "solaris"
for /r %cd% %%f in (*jinput*) do (del  "%%f")
echo #### Done
pause
