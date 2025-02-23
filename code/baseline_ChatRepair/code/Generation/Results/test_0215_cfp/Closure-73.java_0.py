default:
  // If we're given an outputCharsetEncoder, then check if the
  // character can be represented in this character set.
  if (outputCharsetEncoder != null) {
    if (outputCharsetEncoder.canEncode(c)) {
      sb.append(c);
    } else {
      // Unicode-escape the character
      appendHexJavaScriptRepresentation(sb, c);
    }
  } else {
    // No charsetEncoder provided - pass straight latin characters
    // through, and escape the rest. Doing the explicit character
    // check is measurably faster than using the CharsetEncoder.
    if (c > 0x1f && c < 0x7f) { // Updated condition to exclude 0x7f
      sb.append(c);
    } else {
      // Other characters can be misinterpreted by some js parsers,
      // or perhaps mangled by proxies along the way,
      // so we play it safe and unicode escape them.
      appendHexJavaScriptRepresentation(sb, c);
    }
  }
