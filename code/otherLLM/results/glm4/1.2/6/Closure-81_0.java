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
    // Old Rhino tagged the empty name node with the line number of the
    // declaration.
    newName.setLineno(functionNode.getLineno());
    int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
    newName.setCharno(position2charno(lpColumn));
  }

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);

  // Update the line and character number for the left parenthesis '('
  lp.setLineno(functionNode.getLineno());
  int lparenCharno = functionNode.getAbsolutePosition() + functionNode.getLp();
  lp.setCharno(position2charno(lparenCharno));
  
  for (AstNode param : functionNode.getParams()) {
    Node transformedParam = transform(param);
    // Verify that the transform method correctly translates parameters
    if (transformedParam != null) {
      transformedParam.setLineno(param.getLineno());
      transformedParam.setCharno(param.getCharno());
    }
    lp.addChildToBack(transformedParam);
  }

  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  if (bodyNode != null) {
    // Ensure the line and character number of the body node is updated
    bodyNode.setLineno(functionNode.getLineno());
    bodyNode.setCharno(functionNode.getLp() + functionNode.getAbsolutePosition());
  }
  node.addChildToBack(bodyNode);
 return node;
}