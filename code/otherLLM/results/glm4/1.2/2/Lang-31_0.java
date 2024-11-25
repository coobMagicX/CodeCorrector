public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;

    // Check for supplementary characters in cs
    for (int i = 0; i < csLength; ) {
        char ch = cs.charAt(i);
        
        if (Character.isSupplementaryCodePoint(ch)) {
            // Get the actual code point value of the supplementary character
            int codePoint = Character.codePointAt(cs, i);
            
            // Convert the code point to a single char array element that can be compared
            char[] cpAsSingleChar = new char[1];
            cpAsSingleChar[0] = (char) codePoint;
            
            // Check if any character in searchChars matches the supplementary character
            for (int j = 0; j < searchLength; j++) {
                if (cpAsSingleChar[0] == searchChars[j]) {
                    return true;
                }
            }
            i += Character.charCount(codePoint); // Increment index by two to move past the supplementary character pair
        } else {
            // Check for standard characters directly
            for (int j = 0; j < searchLength; j++) {
                if (ch == searchChars[j]) {
                    return true;
                }
            }
            i++; // Increment index by one to move past the current character
        }
    }
    
    return false;
}

public static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}

// You would also need to provide ArrayUtils.isEmpty if it is not already defined:
public static boolean isEmpty(char[] array) {
    return array == null || array.length == 0;
}