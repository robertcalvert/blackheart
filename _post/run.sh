#!/bin/sh
# Linux - Post build script for openflob projects
# Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
# All rights reserved.

project="blackheart"

echo "#### Obfuscating $project.jar"
java -jar ../../../../lib/proguard/proguard.jar @$project.pg
cd ../dist
echo "#### Removing the build cruft"
rm $project.jar
rm README.TXT
rm -rf ../build
mv o_$project.jar $project.jar
echo "#### Copying the documents"
cp -a "../_docs" "docs"
echo "#### Copying the native files"
cp -a "../../../../lib/lwjgl2/native" "native"
echo "#### Removing the native cruft"
rm -rf ./native/solaris
for f in $(find ./native -name '*jinput*'); do rm $f; done 
echo "#### Done"
read -p "Press any key to continue . . . " nothing
