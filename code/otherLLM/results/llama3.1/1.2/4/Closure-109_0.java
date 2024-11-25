private Node parseContextTypeExpression(JsDocToken token) {
    // Check if the token indicates a function constructor (i.e., 'new')
    if (token == JsDocToken.NEW) {
        next(); // Consume the 'new' keyword
        skipEOLs();
        
        // Since we have consumed the 'new' keyword, we expect a type name after it
        Node typeNameNode = parseTypeName(token);
        
        // Check if the parser has successfully parsed a type name
        if (typeNameNode == null) {
            return reportSyntaxWarning("msg.jsdoc.missing.type.name");
        }
        
        // Now that we have the type name, create a constructor node and add it to the AST
        Node constructorNode = new Node(NodeType.CONSTRUCTOR);
        constructorNode.addChildToFront(typeNameNode);
        
        return constructorNode;
    } else {
        // If the token is not 'new', simply parse the type name as usual
        return parseTypeName(token);
    }
}