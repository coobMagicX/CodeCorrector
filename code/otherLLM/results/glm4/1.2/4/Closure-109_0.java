private Node parseContextTypeExpression(JsDocToken token) {
    if (token != JsDocToken.STRING) {
        return reportGenericTypeSyntaxWarning();
    }

    String typeName = stream.getString();
    int lineno = stream.getLineno();
    int charno = stream.getCharno();
    while (match(JsDocToken.EOL) &&
           typeName.charAt(typeName.length() - 1) == '.') {
        skipEOLs();
        if (match(JsDocToken.STRING)) {
            next();
            typeName += stream.getString();
        }
    }

    Node typeNameNode = newStringNode(typeName, lineno, charno);
    
    // Check for constructor or nominal constructor syntax
    if (typeName.equals("constructor")) {
        // Handle constructor function syntax
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
        } else if (!typeName.equals("constructor")) {
            // Handle nominal constructor syntax
            Node classNode = new StringNode(typeName, lineno, charno);
            typeNameNode.addChildToFront(classNode);
        }
    }

    return typeNameNode;
}