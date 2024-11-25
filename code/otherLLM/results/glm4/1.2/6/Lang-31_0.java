public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        if (!Character.isSurrogate(ch)) { // Check if it's not a supplementary character
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        } else {
            // Handle the supplementary characters
            int highSurrogate = Character.highSurrogate(ch);
            int lowSurrogate = Character.lowSurrogate(ch);

            for (int j = 0; j < searchLength; j++) {
                char c = searchChars[j];
                if (Character.isSurrogate(c)) { // Ensure the searchChar is also a supplementary character
                    int highSearch = Character.highSurrogate(c);
                    int lowSearch = Character.lowSurrogate(c);

                    // Check for match of both surrogates or the full code point
                    if ((highSurrogate == highSearch && lowSurrogate == lowSearch) ||
                        (Character.codePointAt(new char[]{highSurrogate, lowSearch}) ==
                         Character.codePointAt(new char[]{c, c}))) {
                        return true;
                    }
                } else {
                    // If the searchChar is not a surrogate, it's a BMP character
                    if (c == ch) {
                        return true;
                    }
                }
            }
        }
    }
    return false;
}

public static boolean isEmpty(CharSequence cs) {
    return cs == null || cs.length() == 0;
}

// You should include ArrayUtils.isEmpty method as it is not part of the standard Java library.
// This is just a placeholder to indicate that this method exists and should be used:
public static boolean isEmpty(char[] array) {
    return array == null || array.length == 0;
}