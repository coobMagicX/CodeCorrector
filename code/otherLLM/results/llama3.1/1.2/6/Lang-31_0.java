public static boolean containsAny(CharSequence cs, char[] searchChars) {
	if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
		return false;
	}
	int csLength = cs.length();
	for (int i = 0; i < csLength; i++) {
		char ch = cs.charAt(i);
		if (isSupplementaryChar(ch)) { // check if the character is supplementary
			if (isInBasicMultilingualPlane(ch)) { // check if the supplementary character is in Basic Multilingual Plane
				for (char searchChar : searchChars) {
					if (searchChar == ch) {
						return true;
					}
				}
			} else {
				for (int j = 0; j < searchChars.length; j++) { // loop through the search characters
					if (searchChars[j] == ch) {
						return true;
					}
				}
			}
		} else {
			for (char searchChar : searchChars) {
				if (searchChar == ch) {
					return true;
				}
			}
		}
	}
	return false;
}

private static boolean isSupplementaryChar(char c) {
    return Character.MIN_SUPPLEMENTARY_CODE_POINT < c && c <= Character.MAX_SUPPLEMENTARY_CODE_POINT;
}

private static boolean isInBasicMultilingualPlane(char c) {
    return 0x0000 <= c && c <= 0xFFFF;
}