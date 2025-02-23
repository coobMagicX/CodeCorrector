public static boolean equals(double[] a, double[] b) {
    if (a == b) {
        return true; // both pointers refer to the same array
    }
    if (a == null || b == null || a.length != b.length) {
        return false; // if one is null or lengths differ, they're not equal
    }
    for