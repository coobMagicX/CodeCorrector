Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    Boolean isUnnamedFunction = false;

    if (name == null) {
        throw new ParseError("Unnamed function statements are not allowed.");
    }
    
    Node node = newNode(Token.FUNCTION);
    Node newName = transform(name);
    node.addChildToBack(newName);
    Node lp = newNode(Token.LP);

    // Function's name information is directly obtained without further checks needed
    lp.setLineno(name.getLineno());
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
    lp.setCharno(position2charno(lparenCharno));

    for (AstNode param : functionNode.getParams()) {
        lp.addChildToBack(transform(param));
    }
    node.addChildToBack(lp);

    Node bodyNode = transform(functionNode.getBody());
    parseDirectives(bodyNode); // Provided parseDirectives exists or is correctly handled elsewhere
    node.addChildToBack(bodyNode);

    return node;
}

/**
 * This helper function assumes presence of a placeholder for custom exceptions.
 */
private static class ParseError extends RuntimeException {
    ParseError(String message) {
        super(message);
    }
}
