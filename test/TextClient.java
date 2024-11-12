import java.io.*;
import java.net.*;

public class TextClient {
    public static void main(String[] args) {
        try {
            // Connect to the server running on localhost at port 4713
            Socket socket = new Socket("localhost", 4713);

            // Set up input and output streams for communication
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())); //för att ta emot byte stream från server 
            PrintWriter ut = new PrintWriter(socket.getOutputStream(), true);// if you have auto flush so you dont need ut.flush()

            // Send the client's name to the server
            ut.println("Charlotta"); // send the message to server
            ut.flush(); // Ensure the message is sent immediately

            // Read and print the server's response
            System.out.println("Server: " + in.readLine());

            // Send some moves to the server and read responses
            String[] moves = {"STEN", "SAX", "PASE"}; // STEN = ROCK, SAX = SCISSORS, PASE = PAPER

            for (String move : moves) {
                ut.println(move); // Send the move to the server
                ut.flush();

                // Read and print the server's response
                String serverMove = in.readLine();
                System.out.println("Server played: " + serverMove);

                // Optionally, you can determine the result here
                // For simplicity, we're just printing the server's move
            }

            // Send an empty string to signal the end of communication
            ut.println("");
            ut.flush();

            // Close the socket and streams
            in.close();
            ut.close();
            socket.close();

            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
