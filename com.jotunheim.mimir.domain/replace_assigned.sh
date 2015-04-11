#!/bin/bash
##将hibernate自动生成的hbm.xml文件中的"assigned"替换成"identity", 否则自动创建的mysql表不会有auto-increment主键

cd src/main/java/com/jotunheim/mimir/domain
sed -i "" "s/assigned/identity/" `grep assigned -l *.hbm.xml`
