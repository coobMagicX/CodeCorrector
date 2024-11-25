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
  Name fnName = functionNode.getFunctionName();
  int lpLine = (fnName != null) ? fnName.getLineno() : functionNode.getLineno();
  lp.setLineno(lpLine);
  
  lp.setCharno(position2charno(functionNode.getAbsolutePosition() + functionNode.getLp()));
  for (AstNode param : functionNode.getParams()) {
    lp.addChildToBack(transform(param));
  }
  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);
  
  // If the function is unnamed, add a semicolon
  if (isUnnamedFunction) {
    Node semicolon = newNode(Token.SEMI);
    semicolon.setLineno(lpLine);
    semicolon.setCharno(position2charno(functionNode.getAbsolutePosition() + functionNode.getLp()));
    node.addChildToBack(semicolon);
  }
  
  return node;
}