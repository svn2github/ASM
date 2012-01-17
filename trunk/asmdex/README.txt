This directory is the base directory of AsmDex.
It contains the following items:

- src : the sources files of Asmdex.
- test/case : dex files used for testing.
- test/conform : source files of the tests of conformity.
- test/perf : source files of the tests of performance.
- build.xml: main ant file to build AsmDex.
- lib : folder containing the libraries used by the AsmDex unit tests :
	- JUnit-4-8-2 (included).
	- Baksmali (NOT included).
- readme.txt : this file.
	
In order to execute AsmDex tests, one must :
- use src, test/conform and test/perf as source folders.
- add baksmali to the classpath. The version used for the development of AsmDex is baksmali-1.2.6.jar.
