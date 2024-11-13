public class RPSController {
    private RPSModel model;
    private RPSSkel view;
    private int counter = 0; // För att räkna ETT... TVÅ... TRE

    public RPSController(RPSModel model) {
        this.model = model;
        this.view = new RPSSkel(this); // Skapa vyn med denna controller
    }

    // Hantera spelarens knapptryckningar
    public void handlePlayerMove(String playerMove) {
        counter++;
        if (counter == 1) {
            view.myboard.resetColor();
            view.computersboard.resetColor();
            view.myboard.setLower("ETT...");
            view.computersboard.setLower("ETT...");
        } else if (counter == 2) {
            view.myboard.setLower("TVÅ...");
            view.computersboard.setLower("TVÅ...");
        } else if (counter == 3) {
            // Få serverns drag via modellen
            String serverMove = model.getServerMove(playerMove);

            // Avgör vinnaren
            String result = model.determineWinner(playerMove, serverMove);

            // Uppdatera vyn
            view.updateView(playerMove, serverMove, result);

            counter = 0; // Återställ räknaren
        }
    }

    // Hantera avslutning av anslutningen
    public void closeConnection() {
        model.closeConnection();
    }

    public static void main(String[] args) {
        RPSModel model = new RPSModel();
        new RPSController(model);
    }
}
