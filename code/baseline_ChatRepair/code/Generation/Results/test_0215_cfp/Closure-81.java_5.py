Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    if (name == null) {
        throw new IllegalArgumentException("Unnamed function statements are not allowed.");
    }

    Node node = newNode(Token.FUNCTION);
    Node newName = transform(name);
    node.addChildToBack(newName);
    Node lp = newNode(Token.LP);

    // Since the name is confirmed non-null and not to be an unnamed function
    lp.setLineno(name.getLineno());
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
    lp.setCharno(position2charno(lparenCharno));

    for (AstNode param : functionNode.getParams()) {
        lp.addChildToBack(transform(param));
    }
    node.addChildToBack(lp);

    Node bodyNode = transform(functionNode.getBody());
    parseDirectives(bodyNode);  // Assurance that parseDirectives is defined or handled elsewhere
    node.addChildToBack(bodyNode);

    return node;
}
