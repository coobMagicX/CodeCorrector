private static boolean looksLikeUtf8(byte[] input) {
    int i = 0;
    // BOM:
    if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
        && (input[1] & 0xFF) == 0xBB && (input[2] & 0xFF) == 0xBF) {
        i = 3;
    }

    int end;
    for (int j = input.length; i < j; ++i) {
        int o = input[i];
        if ((o & 0x80) == 0) {
            continue; // ASCII
        }

        // UTF-8 leading:
        if ((o & 0xE0) == 0xC0) {
            end = i + 1;
        } else if ((o & 0xF0) == 0xE0) {
            end = i + 2;
        } else if ((o & 0xF8) == 0xF0) {
            end = i + 3;
        } else {
            return false;
        }

        while (i < end) {
            i++;
            o = input[i];
            if ((o & 0xC0) != 0x80) {
                return false;
            }
        }
    }
    return true;
}

private static String fixHeaderEncoding(String val) {
    try {
        byte[] bytes = val.getBytes("ISO-8859-1");
        if (!looksLikeUtf8(bytes))
            return val;
        return new String(bytes, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        // shouldn't happen as these both always exist
        return val;
    }
}