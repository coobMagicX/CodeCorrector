public static boolean equals(double[] a, double[] b) {
    if (a == b) {
        return true; // Both pointing to the same array or both are null
    }
    if (a == null || b == null) {
        return false; // One of them is