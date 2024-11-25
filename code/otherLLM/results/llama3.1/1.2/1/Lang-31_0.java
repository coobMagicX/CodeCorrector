public static boolean containsAny(CharSequence cs, char[] searchChars) {
	if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
		return false;
	}
	int csLength = cs.length();
	for (int i = 0; i < csLength; i++) {
		char ch = cs.charAt(i);
		if (Character.isSupplementaryCodePoint(ch)) {
			boolean inBasicMultilingualPlane = Character.isBMP(ch);
			for (char searchChar : searchChars) {
				if (searchChar == ch && (inBasicMultilingualPlane || searchChar == ch)) {
					return true;
				}
			}
		} else if (isAlphaSpace(String.valueOf(ch))) { // Check if the character is alphanumeric or space
			for (char searchChar : searchChars) {
				if (searchChar == ch) {
					return true;
				}
			}
		}
	}
	return false;
}