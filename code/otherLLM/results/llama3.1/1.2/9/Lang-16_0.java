public static Number createNumber(String str) throws NumberFormatException {
    // ... (unchanged code)
    if (str.startsWith("0x") || str.startsWith("-0x")) {
        try {
            return createInteger(str);
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
            throw new NumberFormatException(str + " is not a valid number.");
        }
    }
    // ... (unchanged code)
}