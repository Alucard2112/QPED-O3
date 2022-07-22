QPED - Feedback tools for Java
========================

## Checkers
Certain parts of this project are referred to as "checkers".
A checker is a module that can be configured individually and has a specific analytical function for the submitted code.
A "Syntax Checker" is for instance used to analyze the syntax of submitted code and generate feedback in that specific regard.
Below you may find a list of currently implemented checkers and a sort description of each one.

### Syntax Checker
Syntax checker has the ability to compile java code and report the encountered errors in readable form for students with extra information that helps the check system.

### Style Checker
Style checker is a checker that checks and then reports common formatting, code quality or efficiency  violations.
The report also contains examples for fixing the violations.
It is also possible to base the report on one of the three knowledge levels specified in the [configuration section below](#Syntax-Checker-Configuration).

### Semantics Checker
Semantics Checker analyzes the meaning of the code and gives feedback on the methodology used within a given method.
The checker can give feedback on whether a learner is supposed to use a certain type of loop or recursion, as well as the correct return type.

### Design Checker
Design Checker analyzes a given class or project for class design by using well-known class design metrics.
This checker generates feedback when the given class or method violates one of these metrics' set boundaries.

### Class Checker
Class Checker compares the given solution to the expected design decisions, such as correctly  setting field or method visibilities.
This includes checking if the solution correctly uses the expected class type for either the current class, or the inherited classes. 
The checker generates feedback if any discrepancies occur and provides starting points on how to resolve the issues.

### Test Checker
To be added: Short description

Configuration
------------
The configuration uses JSON-syntax.
Each configuration is located in a separate JSON-object.
For use with Quarterfall, use a code-action and add the specific JSON-objects to the qf object.
Please refer to the tables below how to configure specific options within the feedback tools.
An example for use with Quarterfall will be provided at the end of this section.

### General
With the general configuration it is possible to set overall aspects of the feedback generation.
For instance, the level of a learner (beginner, intermediate, expert) or the language that the feedback should be generated in.

Object: mainSettings

| Option Name       | Possible Values                           | 
|-------------------|-------------------------------------------|
| preferredLanguage | Google language codes (e.g. en, de, etc)  |


### Syntax Checker Configuration
The syntax checker has not many configs because its goal just to check the correctness of the code.
Object: mainSettings

| Option Name | Possible Values                           |
|-------------|-------------------------------------------|
| level       | String (BEGINNER, INTERMEDIATE, ADVANCED) | 

### Style Checker Configuration

In syntax checker can many configs be modified. Classes, methods and variables name pattern.
Level of the student in naming conventions, efficiency, and styling.
Length of the class, method and variables names.
The max allowed number of Fields, methods and lines.
If the number is below 0, the option is ignored and the learner may use as many as they like.

Object: styleSettings

| Option Name      | Possible Values                           |
|------------------|-------------------------------------------|
| mainLevel        | String (BEGINNER, INTERMEDIATE, ADVANCED) |
| compLevel        | String (BEGINNER, INTERMEDIATE, ADVANCED) |
| namesLevel       | String (BEGINNER, INTERMEDIATE, ADVANCED) |
| basisLevel       | String (BEGINNER, INTERMEDIATE, ADVANCED) |
| classLength      | numeric [-1, 0, 1, *]                     |
| methodLength     | numeric [-1, 0, 1, *]                     |
| cycloComplexity  | numeric [-1, 0, 1, *]                     |
| fieldsCount      | numeric [-1, 0, 1, *]                     |
| varName          | String (Regular Expression)               |
| methodName       | String (Regular Expression)               |
| className        | String (Regular Expression)               |


### Semantics Checker Configuration
Semantic checker has the ability to check the code according to criteria specified by the teacher 
e.g. number of loops or number of ```if``` expressions allowed and if it is required to solve the task with recursion.
Setting a negative number ignores the option and lets the learner use as many as they like.


Object: semSettings

| Option Name      | Possible Values        |
|------------------|------------------------|
| methodName       | String                 |
| recursionAllowed | TRUE, FALSE            |
| whileLoop        | numeric [-1, 0, 1, *]  |
| forLoop          | numeric [-1, 0, 1, *]  |
| forEachLoop      | numeric [-1, 0, 1, *]  |
| ifElseStmt       | numeric [-1, 0, 1, *]  |
| doWhileLoop      | numeric [-1, 0, 1, *]  |
| returnType       | String                 |

### Design Checker Configuration WIP
The design checker is able to check one or more given classes for well-known class design metrics.
The used metrics are e.g. from the **Chidamber Kemerer**, **quality oriented extension to C&K**, **QMOOD** metrics suites, 

| Option Name | Possible Values          |
|-------------|--------------------------|
| amc         | numeric array (min, max) |
| ca          | numeric array (min, max) |
| cbo         | numeric array (min, max) |
| cc          | numeric array (min, max) |
| ce          | numeric array (min, max) |
| dam         | numeric array (min, max) |
| dit         | numeric array (min, max) |
| ic          | numeric array (min, max) |
| lcom        | numeric array (min, max) |
| lcom3       | numeric array (min, max) |
| loc         | numeric array (min, max) |
| mfa         | numeric array (min, max) |
| moa         | numeric array (min, max) |
| noc         | numeric array (min, max) |
| npm         | numeric array (min, max) |
| rfc         | numeric array (min, max) |
| wmc         | numeric array (min, max) |

### Class Checker Configuration WIP
The class checker provides ways for the teacher to specify on what to expect from a given class.
The teacher can specify, for example, what the expected class type and name should be, which
classes the current class should inherit and what each field or method in the class should look like.

Object: designSettings

| Option Name    | Possible Values  |
|----------------|------------------|
| classTypeName  | String           |
| inheritsFrom   | String Array     |
| fieldKeywords  | String Array     |
| methodKeywords | String Array     |


### Test Checker Configuration WIP
To be added: short description on what can be configured

Object: 

| Option Name | Possible Values |
|-------------| --------------- |


### Coverage Checker WIP
Coverage Checker maps not covered constructor, methods and branching statements like

- if 
- else if
- else
- for 
- foreach
- while 
- case
- default

to default or custom feedback. Additional there is also the possibility to generate feedback for failed *JUNIT5* test.

#### Example Assignment:
For a given program description analyse all constraints of the program and test them. 

``` 
# Pseudocode-PROGRAM                            # Feedback-CUSTOM

if (setConstraint(true)) {                          
    if (subSetAConstraint(false)) {             // are you sure you tested all constraints. Maybe read passage X more carefully. 
        ... 
    }
    if (subSetBConstraint(true)) {
        if (constraintB1(true)) {               
            ...
        }
        if (constraintB2(false) {               // you overlooked one constraint for case B.
            ...
        }
    }
}
```
[coverage-doc]: checkers/coverage/doc.md
#### Feedback:
There are two types of feedback default and custom.
The default feedback is only available in english and german. 
If needed we are happy to support your language.
Custom feedback can be provided directly over the quarterfall-platform.
For more information over feedback and settings see [doc] [coverage-doc].


Object:

| Option Name                      | Possible Values                                                                                                            | 
|----------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| qf.qfCovSetting.feedback         | Array< String >:<br> classname : TEST, COVERGA <br> classname : TEST, COVERGA, CUSTOM : line index, method name : feedback |
| qf.qfCovSetting.excludeByTypeSet | Array< String >: GET, SET, PRIVATE, PROTECTED                                                                              |
| qf.qfCovSetting.excludeByNameSet | Array< String >: class or method name                                                                                      |
| qf.qfCovSetting.format           | String: content of a vm file  or empty                                                                                     |
| qf.additional                    | Zip file with additional resources                                                                                         |                                                                                                                                             |

```
qf.checkerClass="eu.qped.umr...CoverageChecker";
qf.file = {
    "id: "",
	"label":"",
	"extension: "",
	"path":"",
	"mimetype":"",
	"url":""
};
qf.additional = {
    "id: "",
	"label":"",
	"extension: "",
	"path":"",
	"mimetype":"",
	"url":""
};
qf.covSetting = {
   "excludeByTypeSet" : ["SET", "GET", "PRIVATE"],
   "excludeBYNameSet" : [],
   "format" :  #foreach($r in $summary.stmtFeedback())
               <p>Branch $r.type() failed at index $r.start()</p>
               #end",
   "feedback" : [":test", "Checker:COVERAGE"] 
};

```


### Example Configuration with Quarterfall
```
qf.checkerClass = "eu.qped.java.checkers.mass.Mass";

qf.mainSettings = {
    "syntaxLevel":"BEGINNER",
    "preferredLanguage":"en",
    "semanticNeeded":"true", 
    "styleNeeded":"true"  
};

qf.styleSettings= {
    "mainLevel":"ADVANCED",
    "compLevel":"BEGINNER"  , 
    "namesLevel":"ADVANCED"  , 
    "basisLevel":"ADVANCED" ,
    "classLength":"100" ,
    "methodLength":"10" , 
    "cycloComplexity":"10" , 
    "fieldsCount":"-1" ,
    "varName":"undefined" , 
    "methodName":"[a-z][a-zA-Z0-9]*",
    "className":"undefined"
};
 
qf.semSettings = {
    "methodName":"getMinimum" ,
    "recursionAllowed":"false",
    "whileLoop":"0",
    "forLoop":"0",
    "forEachLoop":"0",
    "ifElseStmt":"1",
    "doWhileLoop":"0",
    "returnType":"float"
};

qf.designSettings = {
    "classInfos": [
        {
            "classTypeName":"class Card",
            "inheritsFrom":["interface Comparable"],
            "fieldKeywords": [
                "private String name",
                "private String year"
            ],
            "methodKeywords": [
                "public int compareTo"
            ]
        }
    ]
    
};
```