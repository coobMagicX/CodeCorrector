Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    Boolean isUnnamedFunction = false;
    if (name == null) {
        name = new Name();
        name.setIdentifier("");
        isUnnamedFunction = true;
    }
    Node node = newNode(Token.FUNCTION);
    Node newName = transform(name);
    if (isUnnamedFunction) {
        // Assign the line number of the function declaration.
        newName.setLineno(functionNode.getLineno());
        // Mark the line number of the left parenthesis correctly.
        int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
        newName.setCharno(position2charno(lpColumn));
    }

    node.addChildToBack(newName);
    Node lp = newNode(Token.LP);
    // Assign the line number of the left parenthesis.
    if (isUnnamedFunction) {
        lp.setLineno(functionNode.getLineno());
    } else {
        lp.setLineno(name.getLineno()); // Use the line number of the function name.
    }
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();

    lp.setCharno(position2charno(lparenCharno));
    for (AstNode param : functionNode.getParams()) {
        Node transformedParam = transform(param);
        transformedParam.setLineno(lp.getLineno()); // Set line number to match left parenthesis.
        transformedParam.setCharno(position2charno(transformedParam.getAbsolutePosition()));
        lp.addChildToBack(transformedParam);
    }
    node.addChildToBack(lp);

    Node bodyNode = transform(functionNode.getBody());
    parseDirectives(bodyNode);
    bodyNode.setLineno(name.getLineno()); // Set line number of the body to match function name.
    node.addChildToBack(bodyNode);
    return node;
}