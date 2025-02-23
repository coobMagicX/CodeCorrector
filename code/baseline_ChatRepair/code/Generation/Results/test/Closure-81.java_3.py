Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    Boolean isUnnamedFunction = false;

    if (name == null) {
        name = new Name();
        name.setIdentifier("");
        isUnnamedFunction = true;
    }
    Node node = newNode(Token.FUNCTION);

    // If function names are required and it is an unnamed function,
    // throw a parser error or omit depending on requirements.
    if (isUnnamedFunction) {
        if (!allowUnnamedFunctions) {
            throw new ParseError("Unnamed function statements are not allowed.");
        } else {
            // This block should be executed in environments where unnamed functions  
            // are expected and handled for specific scenarios (e.g., as callbacks, IIFEs).
            name.setIdentifier("unnamed");  // Providing a default identifier.
            node.putProp(Node.ANONYMOUS_FN_NAME, "unnamed");
        }
    }

    Node newName = transform(name);
    node.addChildToBack(newName);
    Node lp = newNode(Token.LP);

    Name fnName = name; // Previously acquired; directly use.
    if (fnName != null && !fnName.getIdentifier().isEmpty()) {
        lp.setLineno(fnName.getLineno());
    } else {
        lp.setLineno(functionNode.getLineno());
    }
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
    lp.setCharno(position2charno(lparenCharno));

    for (AstNode param : functionNode.getParams()) {
        lp.addChildToBack(transform(param));
    }
    node.addChildToBack(lp);

    Node bodyNode = transform(functionNode.getBody());
    parseDirectives(bodyNode);
    node.addChildToBack(bodyNode);

    return node;
}
