public class MockObject implements Boardgame{

    public boolean move(int x, int y) {
        return true;
    }

    public String getStatus(int x, int y) {
        return "#";
    }
    public String getMessage() {
        return "I'm doing nothing. Having fun?";
    }
    
}
