TESTFILE = SparseVecTestA
INTERFACE = SparseVec
JAVAFILE = SparseVecA

compile: $(JAVAFILE).class $(TESTFILE).class $(INTERFACE).class

$(JAVAFILE).class: $(JAVAFILE).java
        javac $(JAVAFILE).java

$(TESTFILE).class: $(TESTFILE).java
        javac -classpath /usr/share/java/junit4.jar:.  $(TESTFILE).java

$(INTERFACE).class: $(INTERFACE).java
        javac $(INTERFACE).java

test:
        java -classpath /usr/share/java/junit4.jar:.  org.junit.runner.JUnitCore  $(TESTFILE)

clean:
        rm *.class