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
    // Correctly mark line number of paren
    int lpColumn = functionNode.getAbsolutePosition() +
        functionNode.getLp();
    newName.setLineno(functionNode.getLineno());
    newName.setCharno(position2charno(lpColumn));
  } else {
    // Set lineno and charno for named functions
    newName.setLineno(name.getLineno());
    newName.setCharno(position2charno(name.getAbsolutePosition()));
  }

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);
  if (name != null) {
    lp.setLineno(name.getLineno());
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