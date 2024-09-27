// Method to convert a string to lowercase
public String toLowerCase(String str) {
    return str.toLowerCase();
}

// Method to convert a string to uppercase
public String toUpperCase(String str) {
    return str.toUpperCase();
}

// Method to check if a string contains another string (case-insensitive)
public boolean containsIgnoreCase(String str, String subStr) {
    return str.toLowerCase().contains(subStr.toLowerCase());
}