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
    // Ensures that the name node of an unnamed function is correctly attributed to the start of "function"
    newName.setLineno(functionNode.getLineno());
    newName.setCharno(functionNode.getAbsolutePosition());
  }

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);
  // Simplify the logic by always setting lpDetails based on functionNode
  lp.setLineno(functionNode.getLineno());
  int lparenCharno = functionNode.getAbsolutePosition() + functionNode.getLp();
  lp.setCharno(position2charno(lparenCharno));

  for (AstNode param : functionNode.getParams()) {
    lp.addChildToBack(transform(param));
    // Ensure parameters of anonymous function correctly receive their line numbers as well
    param.setLineno(functionNode.getLineno());
  }
  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);
  
  return node;
}
