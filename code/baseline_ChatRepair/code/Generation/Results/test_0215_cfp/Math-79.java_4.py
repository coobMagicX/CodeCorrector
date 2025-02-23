public static double distance(int[] p1, int[] p2) {
    // Check if either input array is null
    if (p1 == null || p2 == null) {
        throw new IllegalArgumentException("Input point arrays cannot be null.");
    }

    // Check if the lengths of the arrays are the same
    if (p1.length != p2.length) {
        throw new IllegalArgumentException("Input point arrays must be of the same length.");
    }

    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final