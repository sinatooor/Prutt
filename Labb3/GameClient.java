import java.io.*;
import java.net.Socket;

public class GameClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;

    public GameClient(String serverAddress, int port, String playerName) {
        this.playerName = playerName;
        connectToServer(serverAddress, port);
    }

    // Connects to the server
    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(playerName); // Send player's name to the server
        } catch (IOException e) {
            System.err.println("Unable to connect to server.");
            e.printStackTrace();
        }
    }

    // Sends a move to the server and receives a response
    public String sendMove(String move) {
        out.println(move);  // Send move (e.g., "STEN", "SAX", or "PÅSE")
        try {
            return in.readLine();  // Server's response with its move
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Closes the connection to the server
    public void closeConnection() {
        try {
            out.println("");  // Send empty string to signal end
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameClient client = new GameClient("localhost", 4713, "PlayerName");
        // Initialize Gameboard and GUI here
    }
}
