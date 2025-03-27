import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Scanner scanner = new Scanner(System.in);
    private Player player1;
    private Player player2;
    private boolean gameOver;
    private static final int BOARD_SIZE = 10;
    private static final int[] SHIP_SIZES = {5, 4, 3, 3, 2};

    public static void main(String[] args) {
        System.out.println("Welcome to the Battleship Game!");
        Game game = new Game();
        game.start();
    }

    public Game() {
        String name1 = getValidName(" Enter Player 1 name: ");
        String name2 = getValidName(" Enter Player 2 name (or type 'AI' for computer opponent): ");
        boolean isAI = name2.equalsIgnoreCase("AI");

        player1 = new Player(name1, BOARD_SIZE, false);
        if (isAI) {
            player2 = new AIPlayer(name2, BOARD_SIZE);
        } else {
            player2 = new Player(name2, BOARD_SIZE, false);
        }

        gameOver = false;
    }

    public void start() {
        System.out.println("\n Placing ships...");
        placeShips(player1);
        placeShips(player2);

        boolean playAgain;
        do {
            playGame();
            playAgain = askReplay();
        } while (playAgain);

        scanner.close();
    }

    private String getValidName(String prompt) {
        String name;
        do {
            System.out.print(prompt);
            name = scanner.nextLine().trim();
        } while (name.isEmpty());
        return name;
    }

    private void placeShips(Player player) {
        Board board = player.getBoard();
        Random rand = new Random();

        System.out.println("\n " + player.getName() + ", choose how to place ships:");
        System.out.print("1. Random Placement ");
        System.out.println("2. Manual Placement");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        while (choice != 1 && choice != 2) {
            System.out.print("Invalid choice! Please choose 1 for Random or 2 for Manual: ");
            choice = scanner.nextInt();
        }

        if (choice == 1) { // Random placement
            for (int size : SHIP_SIZES) {
                boolean placed = false;
                int attempts = 0;
                while (!placed && attempts < 100) {
                    int row = rand.nextInt(BOARD_SIZE);
                    int col = rand.nextInt(BOARD_SIZE);
                    boolean horizontal = rand.nextBoolean();

                    placed = board.placeShip(new Ship(size), row, col, horizontal);
                    attempts++;
                }
                if (!placed) {
                    System.out.println(" Failed to place ship of size " + size);
                }
            }
        } else {
            for (int size : SHIP_SIZES) {
                System.out.println("Place a ship of size " + size + ":");
                boolean placed = false;
                while (!placed) {
                    int row = getRow();
                    int col = getCol();
                    boolean horizontal = getOrientation();

                    placed = board.placeShip(new Ship(size), row, col, horizontal);
                    if (!placed) {
                        System.out.println(" Invalid placement. Try again.");
                    }
                }
            }
        }
    }

    private int getRow() {
        System.out.print("Enter row (A-J): ");
        String row = scanner.next().toUpperCase();
        return row.charAt(0) - 'A';
    }

    private int getCol() {
        System.out.print("Enter column (1-10): ");
        return scanner.nextInt() - 1;
    }

    private boolean getOrientation() {
        System.out.print("Horizontal or Vertical? (h/v): ");
        char orientation = scanner.next().toLowerCase().charAt(0);
        return orientation == 'h';
    }

    private boolean askReplay() {
        System.out.print("Play again? (yes/no): ");
        return scanner.next().equalsIgnoreCase("yes");
    }

    private void playGame() {
        System.out.println("\nGame starting...");
        while (!gameOver) {
            takeTurn(player1, player2);
            if (checkGameOver()) break;
            takeTurn(player2, player1);
            if (checkGameOver()) break;
        }
        System.out.println("\nGame Over!");
    }

    private void takeTurn(Player current, Player opponent) {
        System.out.println("\n" + current.getName() + "'s turn!");
        opponent.getBoard().display(true);

        Coordinate[] moves = current.makeMove(scanner);
        for (Coordinate move : moves) {
            boolean hit = opponent.getBoard().attack(move.getRow(), move.getCol());
            System.out.println(" " + (char) ('A' + move.getRow()) + " " + (move.getCol() + 1) + ": " + (hit ? "= Hit!" : "= Miss!"));
        }
    }

    private boolean checkGameOver() {
        if (player1.getBoard().allShipsSunk()) {
            System.out.println("\n= " + player2.getName() + " wins!");
            gameOver = true;
            return true;
        } else if (player2.getBoard().allShipsSunk()) {
            System.out.println("\n= " + player1.getName() + " wins!");
            gameOver = true;
            return true;
        }
        return false;
    }
}
