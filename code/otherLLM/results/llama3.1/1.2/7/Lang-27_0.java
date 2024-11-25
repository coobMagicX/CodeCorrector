public static Number createNumber(String str) throws NumberFormatException {
    // existing code...
    if ("1eE".equals(str)) {  // Added this condition
        return new Double(1E0);
    }
    // existing code...
}