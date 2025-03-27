import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Board {
    private char[][] grid;
    private int size;
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    public Board(int size) {
        this.size = size;
        grid = new char[size][size];
        for (char[] row : grid) {
            Arrays.fill(row, '~');
        }
    }

    public void placeShipsInteractively(int[] shipSizes) {
        for (int shipSize : shipSizes) {
            int choice;
            do {
                System.out.println("\nChoose placement for a ship of size " + shipSize + ":");
                System.out.println("1. Random Placement");
                System.out.println("2. Manual Placement");
                System.out.print("Your choice: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input! Please enter 1 or 2.");
                    scanner.next();
                }
                choice = scanner.nextInt();
                scanner.nextLine();
            } while (choice != 1 && choice != 2);

            if (choice == 1) {
                placeShipRandom(shipSize);
            } else {
                placeShipManually(shipSize);
            }
            display(false);
        }
    }

    public boolean placeShip(Ship ship, int row, int col, boolean horizontal) {
        int shipSize = ship.getSize();

        if (horizontal && (col + shipSize > size)) {
            System.out.println("Error: Ship goes out of horizontal bounds!");
            return false;
        }
        if (!horizontal && (row + shipSize > size)) {
            System.out.println("Error: Ship goes out of vertical bounds!");
            return false;
        }

        for (int i = 0; i < shipSize; i++) {
            if (horizontal) {
                if (grid[row][col + i] != '~') {
                    System.out.println("Error: Space is already occupied!");
                    return false;
                }
            } else {
                if (grid[row + i][col] != '~') {
                    System.out.println("Error: Space is already occupied!");
                    return false;
                }
            }
        }

        for (int i = 0; i < shipSize; i++) {
            if (horizontal) {
                grid[row][col + i] = 'S';
            } else {
                grid[row + i][col] = 'S';
            }
        }

        return true;
    }

    private void placeShipRandom(int shipSize) {
        boolean placed = false;
        while (!placed) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            boolean horizontal = random.nextBoolean();

            placed = placeShip(new Ship(shipSize), row, col, horizontal);
        }
    }

    private void placeShipManually(int shipSize) {
        boolean placed = false;
        while (!placed) {
            System.out.print("\nEnter ship start position (e.g., A1): ");
            String input = scanner.nextLine().toUpperCase();

            if (isValidInput(input)) {
                int col = input.charAt(0) - 'A';
                int row = Integer.parseInt(input.substring(1)) - 1;

                System.out.print("Horizontal (H) or Vertical (V)? ");
                String direction;
                do {
                    direction = scanner.nextLine().trim().toUpperCase();
                    if (!direction.equals("H") && !direction.equals("V")) {
                        System.out.println("Invalid input! Please enter 'H' or 'V'.");
                    }
                } while (!direction.equals("H") && !direction.equals("V"));

                boolean horizontal = direction.equals("H");
                placed = placeShip(new Ship(shipSize), row, col, horizontal);

                if (!placed) System.out.println("Cannot place the ship there. Try again.");
            } else {
                System.out.println("Invalid input! Try again.");
            }
        }
    }

    public boolean attack(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            System.out.println("Error: Attack is out of bounds!");
            return false;
        }

        if (grid[row][col] == 'S') {
            grid[row][col] = 'X';
            System.out.println("Hit!");
            return true;
        } else if (grid[row][col] == '~') {
            grid[row][col] = 'O';
            System.out.println("Miss!");
        } else {
            System.out.println("This location has already been attacked!");
        }
        return false;
    }

    public boolean allShipsSunk() {
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == 'S') {
                    return false;
                }
            }
        }
        return true;
    }

    public void display(boolean hideShips) {
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.printf("%2d ", (i + 1));
            for (int j = 0; j < size; j++) {
                char cell = grid[i][j];
                if (hideShips && cell == 'S') {
                    System.out.print("~ ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }

    private boolean isValidInput(String input) {
        if (input.length() < 2) return false;
        char letter = input.charAt(0);
        String numberPart = input.substring(1);

        if (letter < 'A' || letter >= 'A' + size) return false;
        try {
            int number = Integer.parseInt(numberPart);
            return number >= 1 && number <= size;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getSize() {
        return size;
    }

    public char getCell(int row, int col) {
        return grid[row][col];
    }
}
