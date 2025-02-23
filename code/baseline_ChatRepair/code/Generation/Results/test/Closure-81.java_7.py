Node processFunctionNode(FunctionNode functionNode) {
  Name name = functionNode.getFunctionName();
  Boolean isUnnamedFunction = (name == null);
  if (name == null) {
    name = new Name();  // Create a dummy name object for unnamed functions
    name.setIdentifier("");
    name.setLineno(functionNode.getLineno());
    name.setCharno(functionNode.getAbsolutePosition());
    isUnnamedFunction = true;
  }
  Node node = newNode(Token.FUNCTION);
  Node newName = transform(name);

  // If the function is unnamed, it's crucial the new 'name' node has suitable line and character numbers set
  if (isUnnamedFunction) {
    newName.setLineno(functionNode.getLineno());
    newName.setCharno(functionNode.getAbsolutePosition());
  }
  node.addChildToBack(newName);

  Node lp = newNode(Token.LP);
  lp.setLineno(functionNode.getLineno());
  int lparenCharno = functionNode.getAbsolutePosition() + functionNode.getLp();
  lp.setCharno(position2charno(lparenCharno));
  node.addChildToBack(lp);

  for (AstNode param : functionNode.getParams()) {
    Node paramNode = transform(param);
    paramNode.setLineno(functionNode.getLineno());  // properly set line numbers for function parameters too
    paramNode.setCharno(position2charno(functionNode.getLp() + functionNode.getAbsolutePosition()));
    lp.addChildToBack(paramNode);
  }

  Node bodyNode = transform(functionNode.getBody());
  parseDirectives(bodyNode);
  node.addChildToBack(bodyNode);

  return node;
}
