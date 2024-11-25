private Node parseContextTypeExpression(JsDocToken token) {
    if (token != JsDocToken.STRING) {
        return reportGenericTypeSyntaxWarning();
    }

    String typeName = stream.getString();
    int lineno = stream.getLineno();
    int charno = stream.getCharno();

    // Modify the loop to check for constructor-like types
    boolean isConstructorLike = false;
    while (match(JsDocToken.EOL) &&
           typeName.charAt(typeName.length() - 1) == '.') {
        skipEOLs();
        if (match(JsDocToken.STRING)) {
            next();
            typeName += stream.getString();
            // Check for constructor-like type names
            isConstructorLike = typeName.endsWith(".new") || typeName.contains("Constructor");
        }
    }

    Node typeNameNode = newStringNode(typeName, lineno, charno);

    if (isConstructorLike) {
        // Handle constructor like types differently
        skipEOLs();
        if (!match(JsDocToken.LT)) { // Expecting '<' after constructor-like type name
            return reportTypeSyntaxWarning("msg.jsdoc.missing.lt");
        }
        next();

        // Parse the constructor arguments (if any)
        Node memberType = parseTypeExpressionList(next());
        if (memberType != null) {
            typeNameNode.addChildToFront(memberType);
        }

        skipEOLs();
        if (!match(JsDocToken.GT)) { // Expecting '>' after constructor arguments
            return reportTypeSyntaxWarning("msg.jsdoc.missing.gt");
        }
        next();
    } else {
        if (match(JsDocToken.LT)) {
            next();
            skipEOLs();
            Node memberType = parseTypeExpressionList(next());
            if (memberType != null) {
                typeNameNode.addChildToFront(memberType);

                skipEOLs();
                if (!match(JsDocToken.GT)) {
                    return reportTypeSyntaxWarning("msg.jsdoc.missing.gt");
                }

                next();
            }
        }
    }

    // Mark the node as a constructor if it is a constructor-like type
    typeNameNode.setIsConstructor(isConstructorLike);
    return typeNameNode;
}