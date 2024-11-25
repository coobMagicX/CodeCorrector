public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        // Check if both objects are arrays and have custom equals implementation
        if (isArray(o2) && o1 instanceof Cloneable && o2 instanceof Cloneable) {
            try {
                Object clonedO1 = o1.getClass().getMethod("clone").invoke(o1);
                return clonedO1.equals(o2);
            } catch (Exception e) {
                // Handle exception if cloning fails
            }
        }
        
        // Compare arrays of different lengths or with mismatched elements
        return areArraysEqual(o1, o2);
    } else {
        // Enhance type checking to accurately identify incompatible types
        if (o1 instanceof Integer && o2 instanceof Double) {
            throw new IllegalArgumentException("Incompatible types: integer and double");
        }
        
        // Compare objects of compatible types using their own equals method
        return o1.equals(o2);
    }
}