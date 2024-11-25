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
  if (fnName != null) {
    // If there's a function name, assign the line number of the function name.
    lp.setLineno(fnName.getLineno());
    int lparenCharno = fnName.getAbsolutePosition() + fnName.getLp();
    lp.setCharno(position2charno(lparenCharno));
  } else {
    // If no function name, use the line number and char number of the function node.
    lp.setLineno(functionNode.getLineno());
    int lparenCharno = functionNode.getAbsolutePosition() + functionNode.getLp();
    lp.setCharno(position2charno(lparenCharno));
  }

  for (AstNode param : functionNode.getParams()) {
    lp.addChildToBack(transform(param));
  }
  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);
 return node;
}