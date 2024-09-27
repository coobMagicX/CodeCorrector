private void printAndQuote(final Object object, final CharSequence value, final int offset, final int len,
        final Appendable out, final boolean newRecord) throws IOException {
    boolean quote = false;
    int start = offset;
    int pos = offset;
    final int end = offset + len;

    final char delimChar = getDelimiter();
    final char quoteChar = getQuoteCharacter().charValue();

    QuoteMode quoteModePolicy = getQuoteMode();
    if (quoteModePolicy == null) {
        quoteModePolicy = QuoteMode.MINIMAL;
    }
    switch (quoteModePolicy) {
        case ALL:
            quote = true;
            break;
        case NON_NUMERIC:
            quote = !(object instanceof Number);
            break;
        case NONE:
            // Use the existing escaping code
            printAndEscape(value, offset, len, out);
            return;
        case MINIMAL:
            if (len <= 0) {
                // always quote an empty token that is the first
                // on the line, as it may be the only thing on the
                // line. If it were not quoted in that case,
                // an empty line has no tokens.
                if (newRecord) {
                    quote = true;
                }
            } else {
                char c = value.charAt(pos);

                // RFC4180 (https://tools.ietf.org/html/rfc4180) TEXTDATA =  %x20-21 / %x23-2B / %x2D-7E
                if (newRecord && (c < '0' || c > '9' && c < 'A' || c > 'Z' && c < 'a' || c > 'z')) {
                    quote = true;
                } else if (c <= COMMENT) {
                    // Some other chars at the start of a value caused the parser to fail, so for now
                    // encapsulate if we start in anything less than '#'. We are being conservative
                    // by including the default comment char too.
                    quote = true;
                } else {
                    while (pos < end) {
                        c = value.charAt(pos);
                        if (c == LF || c == CR || c == quoteChar || c == delimChar) {
                            quote = true;
                            break;
                        }
                        pos++;
                    }

                    if (!quote) {
                        pos = end - 1;
                        c = value.charAt(pos);
                        // Some other chars at the end caused the parser to fail, so for now
                        // encapsulate if we end in anything less than ' '
                        if (c <= SP) {
                            quote = true;
                        }
                    }
                }
            }

            if (!quote) {
                // no encapsulation needed - write out the original value
                out.append(value, start, end);
                return;
            }
            break;
        default:
            throw new IllegalStateException("Unexpected Quote value: " + quoteModePolicy);
    }

    if (!quote) {
        // no encapsulation needed - write out the original value
        out.append(value, start, end);
        return;
    }

    // we hit something that needed encapsulation
    out.append(quoteChar);

    // Pick up where we left off: pos should be positioned on the first character that caused
    // the need for encapsulation.
    while (pos < end) {
        final char c = value.charAt(pos);
        if (c == quoteChar) {
            // write out the chunk up until this point

            // add 1 to the length to write out the encapsulator also
            out.append(value, start, pos + 1);
            // put the next starting position on the encapsulator so we will
            // write it out again with the next string (effectively doubling it)
            start = pos;
        }
        pos++;
    }

    // write the last segment
    out.append(value, start, pos);
    out.append(quoteChar);
}

private void printAndEscape(final CharSequence value, final int offset, final int len, final Appendable out)
        throws IOException {
    int start = offset;
    int pos = offset;
    final int end = offset + len;

    final char delim = getDelimiter();
    final char escape = getEscapeCharacter().charValue();

    while (pos < end) {
        char c = value.charAt(pos);
        if (c == CR || c == LF || c == delim || c == escape) {
            // write out segment up until this char
            if (pos > start) {
                out.append(value, start, pos);
            }
            if (c == LF) {
                c = 'n';
            } else if (c == CR) {
                c = 'r';
            }

            out.append(escape);
            out.append(c);

            start = pos + 1; // start on the current char after this one
        }

        pos++;
    }

    // write last segment
    if (pos > start) {
        out.append(value, start, pos);
    }
}