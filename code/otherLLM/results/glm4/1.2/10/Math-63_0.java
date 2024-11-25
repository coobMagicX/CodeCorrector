public static boolean equals(double[] array1, double[] array2) {
    // Check if both arrays are null
    if (array1 == null && array2 == null) {
        return true;
    }
    // If one is null and the other isn't, or they're of different lengths, return false
    if (array1 == null || array2 == null || array1.length != array2.length) {
        return false;
    }

    // Compare each element in both arrays for equality, taking NaN values into account
    for (int i = 0; i < array1.length; i++) {
        if (Double.compare(array1[i], array2[i]) != 0) {
            return false;
        }
    }

    // If all elements are equal, the arrays are considered equal
    return true;
}