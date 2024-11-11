all: GameClient

GameClient: GameClient.java Gameboard.java RPSSkel.java ServerHandler.java
	javac GameClient.java Gameboard.java RPSSkel.java ServerHandler.java

run: GameClient
	java GameClient

clean:
	rm *.class
