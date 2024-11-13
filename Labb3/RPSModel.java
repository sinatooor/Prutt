import java.io.*;
import java.net.*;

public class RPSModel {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public RPSModel() {
        // Anslut till servern
        try {
            socket = new Socket("localhost", 4713);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Skicka ditt namn till servern
            out.println("Malte&Sina");
            out.flush();

            // Läs serverns hälsning
            String response = in.readLine();
            System.out.println("Servern: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Skicka spelarens drag till servern och få serverns drag
    public String getServerMove(String playerMove) {
        out.println(playerMove);
        out.flush();
        try {
            String serverMove = in.readLine();
            return serverMove;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Stäng anslutningen
    public void closeConnection() {
        try {
            out.println("");
            out.flush();
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Metod för att avgöra vinnaren
    public String[] determineWinner(String playerMove, String serverMove) {
        if (playerMove.equals(serverMove)) {
            return new String[] {"Oavgjort!", "Oavgjort!"};
        } else if ((playerMove.equals("STEN") && serverMove.equals("SAX")) ||
                   (playerMove.equals("SAX") && serverMove.equals("PASE")) ||
                   (playerMove.equals("PASE") && serverMove.equals("STEN"))) {
            return new String[] {"Du vann!", "Du förlorade!"};
        } else {
            return new String[] {"Du förlorade!", "Du vann!"};
        }
    }
}
