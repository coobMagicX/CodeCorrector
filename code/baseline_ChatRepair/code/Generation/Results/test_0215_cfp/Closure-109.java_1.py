private Node parseContextTypeExpression(JsDocToken token) {
    if (token == JsDocToken.LC) { // Start of a record type
        return parseRecordType();
    } else {
        return parseTypeName(token);
    }
}
