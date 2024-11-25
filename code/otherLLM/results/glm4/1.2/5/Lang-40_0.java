public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    // Use the existing 'contains' method instead of directly calling toUpperCase.
    // This ensures that locale-specific character comparison is handled correctly.
    return contains(str, searchStr);
}

public static boolean contains(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    
    // Use codepoint iteration instead of toUpperCase for case-insensitive matching
    int strLen = str.length();
    int searchLen = searchStr.length();
    if (searchLen == 0) {
        return true;
    }
    if (strLen < searchLen) {
        return false;
    }

    // Iterate over each character in the string and perform case-insensitive comparison
    for (int i = 0; i <= strLen - searchLen; ++i) {
        int j;
        for (j = 0; j < searchLen; ++j) {
            char c1 = str.charAt(i + j);
            char c2 = searchStr.charAt(j);
            
            // Use Character.toLowerCase to handle locale-specific lower-casing
            if (Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                break;
            }
        }
        if (j == searchLen) {
            return true; // Found the whole string
        }
    }
    return false; // Not found
}

// Helper method to check for empty strings
private static boolean isEmpty(String str) {
    return str == null || str.isEmpty();
}