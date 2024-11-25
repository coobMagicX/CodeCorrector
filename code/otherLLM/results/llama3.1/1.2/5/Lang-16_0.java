public static Number createNumber(String str) throws NumberFormatException {
    // ... rest of the code remains the same ...
    if (str.startsWith("0x") || str.startsWith("-0x")) {
        // ... rest of the code remains the same ...
    } else {
        char lastChar = str.charAt(str.length() - 1);
        String numeric = str.substring(0, str.length());
        boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch (lastChar) {
            // ... rest of the code remains the same ...
            default :
                if (str.startsWith("X") || str.startsWith("-X")) {
                    numeric = str.substring(1, str.length());
                }
                try {
                    return createInteger(numeric);
                } catch (NumberFormatException nfe) { 
                    // ignore the bad number
                }
                // ... rest of the code remains the same ...
        }
    }
}