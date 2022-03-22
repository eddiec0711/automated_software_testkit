# crispy-invention
An automation tools to create Junit test case

# Group member:
 - Ken Lok Chan
    - klchan2@sheffield.ac.uk
    ##### contribution:
    - Basic Test Generation
    - Test Generation
    - Class Object Generate
    - Test Output
    - README.md
    
 
 - Eddie Chieng Siew Kin 
    - eskchieng1@sheffield.ac.uk
    ##### contribution
     - AutoTester
     - Branch Coverage
     - Condition Coverage
     - Correlated MCDC
     - Logger
     - Method Modifier
     - Setup
     - README.md
   
## Installation 

Download project jar file and add to the same directory as the file to be tested.
Compiled jar file can be found in target directory with filename "project-1.0-SNAPSHOT-jar-with-dependencies.jar".

#### Optional
To clone the project, run following command:

```bash
git clone https://github.com/aca18klc/crispy-invention.git
```

To build project, run following command:

```bash
mvn clean assembly:assembly 
```

## Running 

The project consists of two main stages. First stage involves running the jar file  with parameters supplied in order to generate a test class. 
The test class will be a cloned version of the target class to be tested, except with the target method instrumented. 
If there's any constructor in the class, it will also be renamed to the test class name provided

To run the jar file, use the following command:

```bash
java -jar project-1.0-SNAPSHOT-jar-with-dependencies.jar [targetFile] [testFile] [targetMethod]
```
Where each argument will be of type string, e.g. need to be enclosed in quotes("")
- [targetFile] is the name of target file to be tested, e.g. "BMICalculator.java"
- [testFile] is name of the test file that user want to output to, e.g. "Test.java".
   File will be automatically generated if file has not already existed
- [targetMethod] is the name of target method to be tested, e.g. "calculate"

After the test file is generated, the second stage involves user declaring an entry point (main method) in the test file.
Alternatively user can import the test file and create entry point elsewhere.

**Note: autoTester can be renamed based on user preference, but user will need to modify all statement in the generated test file 
where the instance has been declared/called**  

User can call for various methods from AutoTester class (see next section). Attach project jar file to the class path during compiling/running:

```bash
javac -cp project-1.0-SNAPSHOT-jar-with-dependencies.jar [file] 

java -cp ".:project-1.0-SNAPSHOT-jar-with-dependencies.jar" [class]
```
- [file] and [class] represent the java file/class autoTester has been initialised

**Note: run command is documented based on macOS, syntax of attaching jar file to class path might be different on different os**

## AutoTester

The AutoTester class contains three methods which compute different coverage criteria.
In order to get the coverage, user will have to first run the instrumented test method using test data generated.
The following methods will print uncovered requirements to console:

```java
autoTester.getConditionCoverage()

autoTester.getBranchCoverage()

autoTester.getMCDCoverage()
```

The class also comes with two logger method, which collect the trace of method when being run. 
The loggers will be automatically inserted in stage 1, but user can also self-define their own logger.
Note that only the first branch logger in a then statement will be registered 

```java
if (autoTester.logCondition([id], [condition])) {...}

autoTester.logBranch([id]);
```

- [id] is the unique id for branch and condition, e.g. logBranch(0)
- [condition] is the original condition statement, e.g. logCondition(0, bmi < 18.5)

## Test Generation 
By initialising the class
```java
TestGeneration t = new TestGeneration(3, new Object[]{1,2,3}, new Object[][] {{0,180},null,null});
```
#### Parameter:
 1. the number of input parameter
 2. the object arraylist of the example e.g. {1,0.1, 1.381418}
 3. range of the input parameter, the lower bound and upper bound value.(optional)
    - Int : {int lowerBound, int upperBound}
    - Double : {double lowerBound, double upperBound}
    - Long : {long lowerBound, long upperBound}
    - Float : {float lowerBound, float upperBound}
    - Char : { char lowerBound, char upperBound}
    
    **Note: no combination of lowercase and uppsercase, must be either one for both argument**
    - String: { Boolean isUpperCase, String length}
      **Notes: Boolean is false for lowercase while true is uppercase.**  
 4. the iteration, how many test data that the user needed. Default is 100. (optional)

**Exception would be putting an example of enums into the input parameter/ the second option,
will not expect any enums value in it** 

To generate test data, insert the following method:

```java
t.generateRandomTestData();
```

This will return a 2D arraylist of object. 
After the object is being generated, the user need to cast the variable type in order to use the data in the method. 
To compute coverage, the user can loop through the data generated and obtained the coverage using autoTester methods as mentioned above

## Test Generation Object
This is the method that enable it to retrieve the class field. By initialising the class first.

```java
TestClass test = new TestClass();
ClassObjectGenerate t = new ClassObjectGenerate();
Field[] fields = test.getClass().getFields();
t.getRandomValueForField(fields);
```

This method would return random data by class field type.
- [TestClass] is the classname that user want to test.

**Note that if there is no variable in the class like BMICalculator, nothing would be shown**

## Basic Test Generation
This class enable the user to use any of the method here which consist of 
simple data generation. Insert the statement as followed to use the method:

```java
BasicTestGeneration test = new BasicTestGeneration();
test.randomData();
```

#### method
 - randomData(), output would be random int data
 - randomDataLong(), output would be random long data
 - rangeDataInt(lowerbound, upperbound), output is base on the range that the user insert
 - randomChar(boolean), return random char, boolean input true would be lowercase while false is uppercase
 - randomCharRange(lower char, upper char), return random char within range, only apply with it corresponding cases,
   e.g. 'a' to 'z' , 'A' to 'V'. 'a' to 'C' can't be input to it.  
 - randomString(String.length, boolean), return a random string, string length indicate how longthe user want the string, boolean true input for lowercase
 while false for uppercase.
 - randomDataFloat(), output would be a random Float
 - randomDataDouble(), output would be a random Double
 - randomDataBoolean(), output would be a random Boolean
 - rangeDataFloat(lower, upper), output is base on the range that the user insert with the type float
 - rangeDataDouble(lower, upper), output is base on the range that the user insert with the type double
 - rangeDataLong(lower, upper), output is base on the range that the user insert with the type long
 
## Test Output

```java
TestOutput test = new TestOutput[ExpectedResult], [ClassName], [MethodName]);
test.generateTestOutput();

assertTrue(classname.methodname("parameter") == result)
```

This method will return an assertion method of type String as shown above: 
- [ExpectedResult] is an arrayList that contain the result which user expects when running the method
  with condition provided e.g.(TYPE.NORMAL, true and etc.)
- [Classname] is the class name that user want to test, input of type String
- [Methodname] is the name of the name that user want to test, input is of type String
  
This class allow the user to get a string that contain the Junit pseudo code for it.
It only contain assertTrue and assertFalse method. User need to replace "parameter" with their own data

## Example Test
There are some files that could show example to it which is in the src.main.java.Examples packages, which contain 
files that would enable the user to test it.

   ## Grading:
   - Analysis of method
   - Test Requirement Generation
   - Instrumentation
   - Test Data Generation
   - Coverage Level Computation
   - Test Suite Output
   - Github repo and README.md  
