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
    // Old Rhino tagged the empty name node with the line number of the declaration.
    newName.setLineno(functionNode.getLineno());
    // Set the charno for the empty name to reflect its position in the source code.
    int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
    newName.setCharno(position2charno(lpColumn));
  }

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);
  // If there is no name, we assume the line number of the left parenthesis is the same as the function declaration.
  if (isUnnamedFunction) {
    lp.setLineno(functionNode.getLineno());
  } else {
    lp.setLineno(newName.getLineno()); // Use the line number of the transformed name for named functions.
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