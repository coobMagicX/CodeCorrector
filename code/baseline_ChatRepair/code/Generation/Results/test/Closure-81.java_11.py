Node processFunctionNode(FunctionNode functionNode) {
    Node node = newNode(Token.FUNCTION);
    Node newName;
    
    // Check if the function is unnamed
    if (functionNode.getFunctionName() == null || functionNode.getFunctionName().getIdentifier().isEmpty()) {
        // Create an anonymous node for the function name
        newName = newNode(Token.NAME, "");
    } else {
        // Regular named function
        newName = transform(functionNode.getFunctionName());
    }
    
    node.addChildToBack(newName);
    newName.setLineno(functionNode.getLineno());
    newName.setCharno(position2charno(functionNode.getAbsolutePosition()));

    // Handling for function parameters
    Node lp = newNode(Token.LP);
    lp.setLineno(functionNode.getLineno());
    lp.setCharno(position2charno(
        functionNode.getAbsolutePosition() + functionNode.getLp()
    ));

    for (AstNode param : functionNode.getParams()) {
        lp.addChildToBack(transform(param));
    }
    node.addChildToBack(lp);

    // Handling for function body
    Node bodyNode = transform(functionNode.getBody());
    parseDirectives(bodyNode);
    node.addChildToBack(bodyNode);

    return node;
}
