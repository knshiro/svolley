#!/bin/sh

cd volley
android update project -p .
ant jar
echo "Copy jar to lib folder"
cp bin/volley.jar ../lib
