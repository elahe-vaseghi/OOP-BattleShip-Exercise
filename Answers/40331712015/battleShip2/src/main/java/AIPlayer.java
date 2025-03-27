import java.util.Random;

public class AIPlayer extends Player {

    public AIPlayer(String name, int boardSize) {
        super(name, boardSize, true);
    }

    @Override
    protected Coordinate makeAIMove() {

        Random rand = new Random();
        int row = rand.nextInt(getBoard().getSize());
        int col = rand.nextInt(getBoard().getSize());
        System.out.println("AI makes a move at: " + (char)('A' + row) + (col + 1));
        return new Coordinate(row, col);
    }
}
