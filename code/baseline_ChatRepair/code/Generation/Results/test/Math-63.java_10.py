public static boolean equals(double[] a, double[] b) {
    if (a == b) {
        return true; // both pointers refer to the same array
    }
    if (a == null || b == null) {
        return false; // if one is null, definitely not equal
