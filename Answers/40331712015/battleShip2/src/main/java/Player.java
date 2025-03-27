import java.util.Random;
import java.util.Scanner;

public class Player {
    private String name;
    private Board board;
    private boolean isAI;

    public Player(String name, int boardSize, boolean isAI) {
        this.name = name;
        this.board = new Board(boardSize);
        this.isAI = isAI;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isAI() {
        return isAI;
    }


    public Coordinate[] makeMove(Scanner scanner) {
        if (isAI) {
            return new Coordinate[]{makeAIMove()};
        } else {
            return new Coordinate[]{makeHumanMove(scanner)};
        }
    }


    protected Coordinate makeAIMove() {
         Random rand = new Random();
        int row = rand.nextInt(board.getSize());
        int col = rand.nextInt(board.getSize());
        return new Coordinate(row, col);
    }


    protected Coordinate makeHumanMove(Scanner scanner) {
        while (true) {
            System.out.print(name + ", enter your move (e.g., B7): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (IsValidInput.isValidInput(input, board.getSize())) {
                int row = input.charAt(0) - 'A';
                int col = Integer.parseInt(input.substring(1)) - 1;
                return new Coordinate(row, col);
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

}
