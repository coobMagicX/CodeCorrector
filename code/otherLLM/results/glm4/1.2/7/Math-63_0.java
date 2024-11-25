public static boolean equals(double[] arr1, double[] arr2) {
    if ((arr1 == null) || (arr2 == null)) {
        return !((arr1 == null) ^ (arr2 == null));
    }
    if (arr1.length != arr2.length) {
        return false;
    }

    for (int i = 0; i < arr1.length; ++i) {
        if (!equals(arr1[i], arr2[i])) {
            return false;
        }
    }
    return true;
}

public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || x == y;
}