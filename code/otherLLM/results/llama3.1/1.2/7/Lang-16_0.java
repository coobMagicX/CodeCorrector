public static BigInteger createBigInteger(String str) throws NumberFormatException {
    if (str.startsWith("0x") || str.startsWith("-0x")) {
        String hexStr = str.substring(2);
        return new BigInteger(hexStr, 16); // Using Integer.parseInt() with radix 16 to parse "0Xfade" as a hexadecimal number
    } else {
        return super.createBigInteger(str);
    }
}