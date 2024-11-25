public static boolean containsAny(CharSequence cs, char[] searchChars) {
	if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
		return false;
	}
	int csLength = cs.length();
	int searchLength = searchChars.length;
	for (int i = 0; i < csLength; i++) {
		char ch = cs.charAt(i);
		if (isSupplementaryChar(ch)) {
			for (char searchChar : searchChars) {
				if (searchChar == ch && isInBasicMultilingualPlane(searchChar)) {
					return true;
				}
			}
		} else {
			for (int j = 0; j < searchLength; j++) {
				if (searchChars[j] == ch) {
					return true;
				}
			}
		}
	}
	return false;
}

public static boolean isSupplementaryChar(char ch) {
    return Character.isHighSurrogate(ch) || Character.isLowSurrogate(ch);
}

public static boolean isInBasicMultilingualPlane(char ch) {
    return (int)ch < 0x10000;
}