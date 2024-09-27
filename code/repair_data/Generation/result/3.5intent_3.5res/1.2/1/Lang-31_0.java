public static boolean containsAny(CharSequence cs, char[] searchChars) {
	if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
		return false;
	}
	int csLength = cs.length();
	int searchLength = searchChars.length;
	for (int i = 0; i < csLength; i++) {
		char ch = cs.charAt(i);
		if (Character.isHighSurrogate(ch) && i < csLength - 1) {
			// Combine the high and low surrogates to form the complete character.
			ch = Character.toCodePoint(ch, cs.charAt(i + 1));
		}
		for (int j = 0; j < searchLength; j++) {
			if (searchChars[j] == ch) {
				// ch is in the Basic Multilingual Plane or is a supplementary character
				return true;
			}
		}
	}
	return false;
}