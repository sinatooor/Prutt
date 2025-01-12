TESTFILE = SparseVecTestB
INTERFACE = SparseVec
JAVAFILE = SparseVecB

compile: $(JAVAFILE).class $(TESTFILE).class $(INTERFACE).class

$(INTERFACE).class: $(INTERFACE).java
        javac $(INTERFACE).java

$(JAVAFILE).class: $(JAVAFILE).java
        javac $(JAVAFILE).java

$(TESTFILE).class: $(TESTFILE).java
        javac -classpath /usr/share/java/junit4.jar:.  $(TESTFILE).java

test:
        java -classpath /usr/share/java/junit4.jar:.  org.junit.runner.JUnitCore  $(TESTFILE)

clean:
        rm *.class