private char getMappingCode(final String str, final int index) {
    // map() throws IllegalArgumentException
    final char mappedChar = this.map(str.charAt(index));
    // HW rule check
    if (index > 1 && mappedChar != '0') {
        final char hwChar = str.charAt(index - 1);
        if (('H' == hwChar || 'W' == hwChar) && (index > 2)) {
            final char preHWChar = str.charAt(index - 2);
            final char firstCode = this.map(preHWChar);
            if (firstCode == mappedChar || ('H' == preHWChar || 'W' == preHWChar && firstCode != '0')) {
                return 0;
            }
        }
    }
    return mappedChar;
}