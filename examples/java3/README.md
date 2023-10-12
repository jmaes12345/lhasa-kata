# Example implementation 3
This is a start off setup for building a deep learning neural network based classification, following: https://www.baeldung.com/deeplearning4j
This will immediately provide a classification based upon the available training data.

However to make a good classification, it requires good mapping of suitable input values, good choice of settings and possible fine tuning.
Thus to make this into a model for our task, we need to identify important features from the svg - these will be based on the decision points in the workflow.
These need to be read out of the svgs and mapped into numerical values. Then written into csv format (comma separated values) as iris.txt is, just with the values from our test data.

The format should be: feature1,feature2,feature3,feature4,...,class  

Where class is 1, 2 or 3, matching our classification task.

Note that this will not be a perfect decision workflow, but is a quick way of categorising based on a known training data set.
The test data files in /test-data folder are labelled with their category in the filenames, -I, -II or -III so they can be split.
Thus this approach is really valuable when modelling a classification where the rules are not as well defined as in our example, or are not known at all. 
## To run the classification
* Requires Java 17
* Write the reading code (get code from other examples.
* Map features from the csv into numerical values, an example could be circle=1, square=2, etc...
* Think about how best to represent the significant features.
* Run this via the unit test or however you prefer.
* File input and output not yet implemented
