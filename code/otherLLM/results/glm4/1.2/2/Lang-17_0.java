public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    int len = Character.codePointCount(input, 0, input.length());
    while (pos < len) {
        int codepoint = Character.codePointAt(input, pos);
        
        // Check for non-BMP characters and handle them specifically
        if (Character.isSurrogate(codepoint)) {
            out.write(String.valueOf(Character.toChars(codepoint)));
            pos += 2; // Skip the next character as it is a part of the surrogate pair
        } else if (codepoint > 0xFFFF) { // Non-BMP characters are greater than 0xFFFF
            // Use hex representation to handle characters like "\ud842\udfb7"
            out.write(hex(codepoint));
            pos += Character.charCount(codepoint);
        } else {
            // Handle BMP characters by escaping them if necessary
            char ch = (char) codepoint;
            switch (ch) {
                case '&':
                    out.write("&amp;");
                    break;
                case '<':
                    out.write("&lt;");
                    break;
                case '>':
                    out.write("&gt;");
                    break;
                case '"':
                    out.write("&quot;");
                    break;
                case '\'':
                    out.write("&apos;");
                    break;
                default:
                    if (ch < 0x20 || ch > 0x7E) {
                        // Handle non-printable BMP characters
                        out.write("&#x" + Integer.toHexString(ch) + ";");
                    } else {
                        // Write the character as is if it's already a safe XML character
                        out.write(ch);
                    }
            }
        }

        pos++;
    }
}

// Method to convert codepoint to hex string
public static String hex(int codepoint) {
    return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
}