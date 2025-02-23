Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  Node node = newNode(Token.FUNCTION);
  if (name != null) {
    Node newName = transform(name);
    newName.setLineno(name.getLineno());
    int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
    newName.setCharno(position2charno(lpColumn));
    node.addChildToBack(newName);
  }

  Node lp = newNode(Token.LP);
  if (name != null) {
    lp.setLineno(name.getLineno());
  } else {
    // If there's no name, use the function node's line number.
    lp.setLineno(functionNode.getLineno());
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

int position2charno(int position) {
  // Conversion logic here, ensure it is correctly implemented
  return position;
}
