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
  
  // Correct handling of unnamed function statements outside of blocks
  if (isUnnamedFunction && !(functionNode instanceof Block)) {
    newName.setLineno(functionNode.getLineno());
    int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
    newName.setCharno(position2charno(lpColumn));
  }

  node.addChildToBack(newName);
  
  // Correctly identify and handle unnamed function statements within blocks
  Node lp = newNode(Token.LP);
  if (!(functionNode instanceof Block)) {
    Name fnName = functionNode.getFunctionName();
    if (fnName != null) {
      lp.setLineno(fnName.getLineno());
    } else {
      lp.setLineno(functionNode.getLineno());
    }
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
    lp.setCharno(position2charno(lparenCharno));
  } else { // if the function is within a block
    lp.setLineno(newName.getLineno()); // set the line number of the left paren to be the same as the name node
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
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