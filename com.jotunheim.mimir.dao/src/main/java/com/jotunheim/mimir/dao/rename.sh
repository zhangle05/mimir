#!/bin/sh
#*Home.java to *DaoImpl.java

sed -i "" "s/DaoImpl/Dao/" `grep DaoImpl -l *.java`
sed -i "" "s/dao\.impl/dao/" `grep dao.impl -l *.java`
sed -i "" "s/public class/public interface/" `grep 'public class' -l *.java`

for file in *Impl.java
do
	new=${file/DaoImpl/Dao}
	mv $file $new
done
