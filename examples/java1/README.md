# Example implementation 1
This is a partially implemented straightforward approach simply using if-else statements.
Relatively quick to initially write.
Note how this quickly gets hard to understand, hard to know where you are in the decision tree, without lots of logging or comments.  
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
> java -jar svgIfElse-1.0-SNAPSHOT.jar
> 
Alternatively, pass the absolute path to a directory into the jar command:
> java -jar svgIfElse-1.0-SNAPSHOT.jar "C:/myLocation/else where"
>