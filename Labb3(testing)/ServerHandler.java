public class ServerHandler {
    private GameClient client;

    public ServerHandler(GameClient client) {
        this.client = client;
    }

    // Process moves, manage scores, and provide results to UI
    public String processMove(String playerMove) {
        return client.sendMove(playerMove);  // Communicate with server
    }
}
