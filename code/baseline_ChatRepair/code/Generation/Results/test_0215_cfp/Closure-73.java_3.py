default:
    if (outputCharsetEncoder != null) {
        if (outputCharsetEncoder.canEncode(c)) {
            sb.append(c);
        } else {
            // Unicode-escape the character if it cannot be encoded.
            appendHexJavaScriptRepresentation(sb, c);
        }
    } else {
        // No charsetEncoder provided - Unicode escape all non-ASCII characters, as well as control characters.
        if (c >= 0x20 && c <= 0x7e) { // ASCII printable characters from space to ~
            sb.append(c);
        } else {
            // Other characters outside the range are escaped.
            appendHexJavaScriptRepresentation(sb, c);
        }
    }
