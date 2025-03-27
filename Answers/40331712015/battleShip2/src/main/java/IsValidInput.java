public class IsValidInput {
    public static boolean isValidInput(String input, int size) {
        if (input == null || input.length() < 2 || input.length() > 3) {
            return false;
        }

        char columnChar = Character.toUpperCase(input.charAt(0));
        String rowString = input.substring(1);


        if (columnChar < 'A' || columnChar >= ('A' + size)) {
            return false;
        }


        for (char ch : rowString.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        int row = Integer.parseInt(rowString);
        if (row < 1 || row > size) {
            return false;
        }

        return true;
    }
}
