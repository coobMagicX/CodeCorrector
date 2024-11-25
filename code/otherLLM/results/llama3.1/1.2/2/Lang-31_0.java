public static boolean containsAny(CharSequence cs, char[] searchChars) {
	if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
		return false;
	}
	int csLength = cs.length();
	int searchLength = searchChars.length;
	for (int i = 0; i < csLength; i++) {
		char ch = cs.charAt(i);
		// Check if the character is a supplementary character
		if ((ch & 0xFFFF0000) == 0x0000FFFF) { // If high bits are zero, it's in Basic Multilingual Plane
			for (int j = 0; j < searchLength; j++) {
				if (searchChars[j] == ch) {
					return true;
				}
			}
		} else { // Check for supplementary characters
			int codePoint = ch;
			for (int j = 0; j < searchLength; j++) {
				char searchChar = searchChars[j];
				if ((searchChar & 0xFFFF0000) == 0x0000FFFF) { // If high bits are zero, it's in Basic Multilingual Plane
					if (codePoint == searchChar) return true;
				} else if (codePoint == searchChar + 0x10000) return true; // Check for supplementary character match
			}
		}
	}
	return false;
}