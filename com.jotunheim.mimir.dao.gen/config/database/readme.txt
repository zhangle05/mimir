This directory contains database-specific fragments of the ant script for the airline application. 
Each of the files must define the following properties:

database.script.file       The path to the sql script that will create the database
database.driver.file       The path to the jar file containing the database's JDBC driver
database.driver.classpath  Usually ${database.driver.file}, but in some cases several jars (like for mckoi)
database.driver            The fully qualified name of the database driver class
database.url               The URL to the database
database.userid            The userid used to connect to the database
database.password          The password used to connect to the database
database.schema            The database schema from which the metadata should be read
database.catalog           The database catalog from which the metadata should be read

Edit the build.xml file to include the database-specific ant script fragment that matches your
database.
