static String unescape(String string) {
    if (!string.contains("&"))
        return string;

    Matcher m = unescapePattern.matcher(string); // &(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+);?
    StringBuffer accum = new StringBuffer(string.length()); // pity matcher can't use stringbuilder, avoid syncs
    // todo: replace m.appendReplacement with own impl, so StringBuilder and quoteReplacement not required

    while (m.find()) {
        int charval = -1;
        String num = m.group(3);
        if (num != null) {
            try {
                int base = m.group(2) != null ? 16 : 10; // 2 is hex indicator
                charval = Integer.parseInt(num, base);
            } catch (NumberFormatException e) {
                continue;
            }
        } else {
            String name = m.group(1);
            if (full.containsKey(name))
                charval = full.get(name);
        }

        if (charval != -1 && charval > 0xFFFF) {
            String c = Character.toString((char) charval);
            m.appendReplacement(accum, c);
        } else {
            m.appendReplacement(accum, m.group(0));
        }
    }
    m.appendTail(accum);
    return accum.toString();
}

// Added full map for testing purposes
private static final Map<String, Integer> full = new HashMap<>();