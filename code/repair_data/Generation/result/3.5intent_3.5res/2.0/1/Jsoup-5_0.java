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
            boolean isInsideSingleQuotes = false;
            boolean isInsideDoubleQuotes = false;
            while (!tq.matchesAny("<", "/>", ">") && !tq.matchesWhitespace() && !tq.isEmpty()) {
                char c = tq.consume();
                if (c == SQ && !isInsideDoubleQuotes) {
                    isInsideSingleQuotes = !isInsideSingleQuotes;
                } else if (c == DQ && !isInsideSingleQuotes) {
                    isInsideDoubleQuotes = !isInsideDoubleQuotes;
                }
                valueAccum.append(c);
            }
            value = valueAccum.toString();
        }
        tq.consumeWhitespace();
    }
    if (key.length() != 0)
        return Attribute.createFromEncoded(key, value);
    else {
        tq.consume();
            
        return null;
    }
}