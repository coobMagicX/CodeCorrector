Node processFunctionNode(FunctionNode functionNode) {
  Node node = newNode(Token.FUNCTION);
  
  Name name = functionNode.getFunctionName();
  if (name != null) {
    Node newName = transform(name);
    newName.setLineno(name.getLineno());
    int nameColumn = functionNode.getAbsolutePosition() + functionNode.getNameRelativePosition(); 
    newName.setCharno(position2charno(nameColumn));
    node.addChildToBack(newName);
  }
  
  Node lp = newNode(Token.LP);
  if (name != null) {
    lp.setLineno(name.getLineno());
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
    lp.setCharno(position2charno(lparenCharno));
  } else {
    // Assume the paren is on the same line as the 'function' keyword
    lp.setLineno(functionNode.getLineno());
    int lparenCharno = functionNode.getFunctionKeywordPosition() + functionNode.getLp();
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

int position2charno(int position) {
  // Implement conversion of absolute position to character number if needed
  return position;
}
