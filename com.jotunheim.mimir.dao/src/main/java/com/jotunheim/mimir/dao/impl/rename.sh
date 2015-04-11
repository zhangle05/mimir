#!/bin/sh
#*Home.java to *DaoImpl.java

sed -i "" "s/Home/DaoImpl/" `grep Home -l *.java`

for file in *Home.java
do
	new=${file/Home/DaoImpl}
	mv $file $new
done
