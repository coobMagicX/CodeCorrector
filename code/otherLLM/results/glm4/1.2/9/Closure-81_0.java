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
    // Set line and character number for the parentheses correctly.
    int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
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
  if (fnName != null) {
    lp.setLineno(fnName.getLineno());
  } else {
    lp.setLineno(functionNode.getLineno());
  }
  int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();

  lp.setCharno(position2charno(lparenCharno));
  
  // Check if the params are present and transform them.
  for (AstNode param : functionNode.getParams()) {
    Node transformedParam = transform(param);
    lp.addChildToBack(transformedParam);
  }
  
  node.addChildToBack(lp);

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);
 return node;
}