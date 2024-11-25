private String getRemainingJSDocLine() {
    StringBuilder result = new StringBuilder();
    boolean inJSDocComment = false;

    while (stream.hasNext()) {
        JsDocToken token = stream.next();

        if (!inJSDocComment && token == JsDocToken.DOCCOMMENT) {
            inJSDocComment = true;
        } else if (inJSDocComment && token != JsDocToken.DOCCOMMENT) {
            if (token == JsDocToken.EOL || token == JsDocToken.BLOCKEND) {
                break;
            }
            result.append(token.getString());
        }

        // If we have reached a DOCCOMMENT without an EOL or BLOCKEND, just continue to the next token
    }

    return inJSDocComment ? result.toString() : "";
}