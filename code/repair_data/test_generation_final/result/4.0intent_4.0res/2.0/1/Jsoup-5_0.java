private Attribute parseAttribute() {
    tq.consumeWhitespace();
    String key = tq.consumeAttributeKey();
    String value = "";
    tq.consumeWhitespace();
    if (tq.matchChomp("=")) {
        tq.consumeWhitespace();

        if (tq.matchChomp(SQ)) {
            value = tq.chompTo(SQ);
        } else if (tq.matchChomp(DQ)) {
            value = tq.chompTo(DQ);
        } else {
            StringBuilder valueAccum = new StringBuilder();
            // no ' or " to look for, so scan to end tag or space (or end of stream)
            while (!tq.matchesAny("<", "/>", ">") && !tq.matchesWhitespace() && !tq.isEmpty()) {
                valueAccum.append(tq.consume());
            }
            value = valueAccum.toString();
        }
        tq.consumeWhitespace();
    }
    // Handling malformed attributes: skip if key is empty or contains irregular characters
    if (key.length() != 0 && key.matches("[a-zA-Z0-9-_]+")) {
        return Attribute.createFromEncoded(key, value);
    } else {
        // Skip to next valid attribute or end of tag
        while (!tq.matchesAny(">", "/>") && !tq.isEmpty()) {
            tq.consume();
        }
        return null;
    }
}