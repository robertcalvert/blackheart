#!/bin/sh
# Linux post build script
# Copyright (c) 2013 Robert Calvert <http://robert.calvert.io>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>

project="blackheart"

echo "#### Obfuscating $project.jar"
java -jar ../../../../lib/proguard/proguard.jar @proguard.properties
cd ../dist
echo "#### Removing the build cruft"
rm $project.jar
rm README.TXT
rm -rf ../build
mv o_$project.jar $project.jar
echo "#### Copying the documents"
mkdir docs
cd docs
cp "../../LICENSE.txt" $project".txt"
mkdir 3rdparty
cd 3rdparty
cp "../../../src/io/flob/$project/font/LICENSE.txt" "fonts.txt"
cp "../../../../../../lib/JOrbis/COPYING.LIB" "JOrbis.txt"
cp "../../../../../../lib/lwjgl2/doc/LICENSE" "lwjgl2.txt"
cp "../../../../../../lib/slick-util/LICENSE" "slick.txt"
echo "#### Copying the native files"
cd ../../lib/
cp -a "../../../../../lib/lwjgl2/native" "native"
echo "#### Removing the native cruft"
rm -rf ./native/solaris
rm -rf ./native/freebsd
rm -rf ./native/openbsd
for f in $(find ./native -name '*jinput*'); do rm $f; done 
echo "#### Done"
read -p "Press any key to continue . . . " nothing
