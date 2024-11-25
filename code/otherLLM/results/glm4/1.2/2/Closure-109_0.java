private Node parseContextTypeExpression(JsDocToken token) {
    // Determine if the current token is for a structural constructor by checking if it's a keyword 'new'
    if ("new".equals(token.getText())) {
        // If it is, proceed to parse and record as a structural constructor node
        return parseAndRecordTypeNameNode(token, token.getLineNumber(), token.getStartCharPositionInLine(), true);
    } else {
        // Otherwise, use the existing method to parse the type name
        return parseTypeName(token);
    }
}