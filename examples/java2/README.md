# Example implementation 2
This is a partially implemented approach that models the decision tree within a graph database.
This has a more difficult entry barrier, but provides a convenient visualisation. 
## Requirements
OrientDB studio running standalone, on the default location of localhost:2424
* Download the community edition of OrientDB and unzip into directory. https://orientdb.org/download
* Open the /bin folder and run server.bat
* Make note of the root password you set
* Create a new database called "kata1", if you use the default root user, use the password you set when first starting orientDB studio
* Build the database decision tree via 
> org.katas.graph.DbServiceTest.buildSvgDecisionTree

Default GUI is at http://localhost:2480/studio/index.html
Using the Graph Editor view gives you  the visual representation of the graph.
You need to change some settings to get human readable properties (for example name) showing on the visualisation.
> select * from start
## To run the test application
* Requires Java 17
* If you do not have it, copy the input and output directories somewhere that does to run

Create folders 
> C:/kata-svg/input
> 
and 
> C:/kata-svg/output
> 
Copy the test data folder onto C:/kata-svg/input
Run:
> java -jar svgGraph-1.0-SNAPSHOT.jar
> 
Alternatively, pass the absolute path to a directory into the jar command:
> java -jar svgGraph-1.0-SNAPSHOT.jar "C:/myLocation/else where"
>