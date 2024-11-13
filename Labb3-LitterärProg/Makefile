# Variables
SOURCE = RPSGame.nw
TEX_FILE = RPSGame.tex
PDF_FILE = RPSGame.pdf
JAVA_FILE1 = Server4713
JAVA_FILE2 = RPSSkel
JAVA_FILE3 = Gameboard
JAVA_FILE4 = RPSModel

pdf: $(PDF_FILE)

# Rule to generate LaTeX documentation from Noweb
$(TEX_FILE): $(SOURCE)
	noweave -latex $(SOURCE) > $(TEX_FILE)

# Rule to generate PDF from LaTeX
$(PDF_FILE): $(TEX_FILE)
	pdflatex $(TEX_FILE)

java: $(JAVA_FILE1).java $(JAVA_FILE2).java $(JAVA_FILE3).java $(JAVA_FILE4).java

# Rule to generate javafile
$(JAVA_FILE1).java: $(SOURCE)
	notangle -R$(JAVA_FILE1).java $(SOURCE) > $(JAVA_FILE1).java

$(JAVA_FILE2).java: $(SOURCE)
	notangle -R$(JAVA_FILE2).java $(SOURCE) > $(JAVA_FILE2).java

$(JAVA_FILE3).java: $(SOURCE)
	notangle -R$(JAVA_FILE3).java $(SOURCE) > $(JAVA_FILE3).java

$(JAVA_FILE4).java: $(SOURCE)
	notangle -R$(JAVA_FILE4).java $(SOURCE) > $(JAVA_FILE4).java

startServer:
	killall java
	javac $(JAVA_FILE1).java
	java $(JAVA_FILE1) &

# Rule to compile and run the Java program
run: 
	javac *.java
	java $(JAVA_FILE2)

# Clean up generated files
clean:
	rm -f $(TEX_FILE) $(PDF_FILE) *.aux *.log *.class *.java