TESTFILE0 = NumberedItemTest0
TESTFILE = NumberedItemTest
JAVAFILE = NumberedItem

compileTest: $(JAVAFILE).class $(TESTFILE).class $(TESTFILE0).class

$(JAVAFILE).class: $(JAVAFILE).java
        javac $(JAVAFILE).java

$(TESTFILE).class: $(TESTFILE).java
        javac -classpath /usr/share/java/junit4.jar:.  $(TESTFILE).java

$(TESTFILE0).class: $(TESTFILE0).java
        javac -classpath /usr/share/java/junit4.jar:.  $(TESTFILE0).java

test0:
        java -classpath /usr/share/java/junit4.jar:.  org.junit.runner.JUnitCore  NumberedItemTest0

test:
        java -classpath /usr/share/java/junit4.jar:.  org.junit.runner.JUnitCore  NumberedItemTest

clean:
        rm *.class