public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (o1 == o2) {
        return true; // This handles the scenario of comparing the same instances including arrays.
    } else {
        try {
            return o1.equals(o2); // Leveraging Object's equals method.
        } catch (RuntimeException e) {
            System.err.println("Exception encountered in equals method: " + e.getMessage());
            return false;
       