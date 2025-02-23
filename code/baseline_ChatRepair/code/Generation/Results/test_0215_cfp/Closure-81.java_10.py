Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    boolean isUnnamedFunction = (name == null);
    
    Node node = newNode(Token.FUNCTION);
    Node newName;
    
    if (!isUnnamedFunction) {
        newName = transform(name);
    } else {
        // If the function is unnamed, create an empty name node for the function
        newName = newNode(Token.NAME);
        newName.setString(""); // explicitly set an empty string for the name
        newName.setLineno(functionNode.getLineno());
        int nameCharno = functionNode.getAbsolutePosition();
        newName.setCharno(position2charno(nameCharno));
    }
    
    node.addChildToBack(newName);

    Node lp = newNode(Token.LP);
    if (!isUnnamedFunction && name != null) {
        lp.setLineno(name.getLineno());
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
