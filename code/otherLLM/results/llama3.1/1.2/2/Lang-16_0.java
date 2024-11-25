public class NumberUtils {
    public static Number createNumber(String numberStr) throws InvalidNumberFormatException {
        try {
            if (isInteger(numberStr)) return Integer.parseInt(numberStr);
            else if (isLong(numberStr)) return Long.parseLong(numberStr);
            // Consider adding more type-specific parsing here...
            else throw new InvalidNumberFormatException("Not a valid integer or long.");
        } catch (NumberFormatException e) {
            // If not an integer, try other types
            try {
                return Float.parseFloat(numberStr);
            } catch (NumberFormatException ex) {
                try {
                    return Double.parseDouble(numberStr);
                } catch (NumberFormatException exc) {
                    throw new InvalidNumberFormatException("Not a valid float or double.");
                }
            }
        }
    }

    private static boolean isInteger(String numberStr) {
        // Simple check for integers
        return numberStr.matches("-?\\d+");
    }

    private static boolean isLong(String numberStr) {
        // Simple check for longs (similar to integers)
        return numberStr.matches("-?\\d+");
    }

    public static class InvalidNumberFormatException extends NumberFormatException {
        public InvalidNumberFormatException(String message) {
            super(message);
        }
    }
}