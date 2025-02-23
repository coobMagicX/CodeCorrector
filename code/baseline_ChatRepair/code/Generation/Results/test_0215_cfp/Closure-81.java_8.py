Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  boolean isUnnamedFunction = false;
  if (name == null) {
    name = new Name();
    name.setIdentifier("");
    isUnnamedFunction = true;
  }
  Node node = newNode(Token.FUNCTION);
  Node newName = transform(name);
  
  if (isUnnamedFunction) {
    newName.setLineno(functionNode.getLineno());
    // If newName.setCharno is not valid, we could handle column in a custom way (not shown) 
    // or ensure position tracking is maintained elsewhere in the code, or add that method if modifying original source.
  }

  node.addChildToBack(newName);
  Node lp = newNode(Token.LP);
  lp.setLineno(functionNode.getLineno());
  node.addChildToBack(lp);

  for (AstNode param : functionNode.getParams()) {
    Node paramNode = transform(param);
    paramNode.setLineno(functionNode.getLineno());
    lp.addChildToBack(paramNode);
  }

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);

  return node;
}
