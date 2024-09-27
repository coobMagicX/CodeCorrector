Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  Boolean isUnnamedFunction = false;
  if (name == null) {
    name = new Name();
    name.setIdentifier("");
    isUnnamedFunction = true;
  }
  // Check if the function node is unnamed and not within an expression
  if (isUnnamedFunction && !isWithinExpression(functionNode)) {
    throw new ParseError("Unnamed function statement outside of expression context");
  }
  Node node = newNode(Token.FUNCTION);
  Node newName = transform(name);
  if (isUnnamedFunction) {
    // Old Rhino tagged the empty name node with the line number of the
    // declaration.
    newName.setLineno(functionNode.getLineno());
    // TODO(bowdidge) Mark line number of paren correctly.
    // Same problem as below - the left paren might not be on the
    // same line as the function keyword.
    int lpColumn = functionNode.getAbsolutePosition() +
        functionNode.getLp();
    newName.setCharno(position2charno(lpColumn));
  }

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);
  // The left paren's complicated because it's not represented by an
  // AstNode, so there's nothing that has the actual line number that it
  // appeared on.  We know the paren has to appear on the same line as the
  // function name (or else a semicolon will be inserted.)  If there's no
  // function name, assume the paren was on the same line as the function.
  // TODO(bowdidge): Mark line number of paren correctly.
  Name fnName = functionNode.getFunctionName();
  if (fnName != null) {
    lp.setLineno(fnName.getLineno());
  } else {
    lp.setLineno(functionNode.getLineno());
  }
  int lparenCharno = functionNode.getLp() +
      functionNode.getAbsolutePosition();

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

// Helper method to check if the function node is within an expression
private boolean isWithinExpression(FunctionNode functionNode) {
  Node parent = functionNode.getParent();
  while (parent != null) {
    if (parent.getType() == Token.EXPR_RESULT) {
      return true;
    }
    parent = parent.getParent();
  }
  return false;
}