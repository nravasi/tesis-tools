#!/bin/bash

while true;
do
	adb shell monkey -p $1 -v 5000
done